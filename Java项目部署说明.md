# Java 正式部署环境说明

> 本文档汇总 newbee-mall-api（Spring Boot 2.7.5 单体应用）的**正式环境部署**全流程，覆盖从环境准备、打包、配置、进程托管、反向代理、数据加固、一键部署、日志监控到安全与回滚的完整闭环。
>
> 开发环境启动方式见 [README.md](./README.md) 与 [开发需求文档.md](./开发需求文档.md)，本文重点说明与本地开发**不同的生产配置与运维手段**。

- [一、部署架构总览](#一部署架构总览)
- [二、环境要求](#二环境要求)
- [三、打包 JAR](#三打包-jar)
- [四、生产配置文件](#四生产配置文件)
- [五、systemd 服务托管（开机自启/崩溃重启）](#五systemd-服务托管开机自启崩溃重启)
- [六、Nginx 反向代理 + HTTPS](#六nginx-反向代理--https)
- [七、数据库与 Redis 生产加固](#七数据库与-redis-生产加固)
- [八、部署步骤（一键脚本）](#八部署步骤一键脚本)
- [九、日志与监控](#九日志与监控)
- [十、安全加固清单](#十安全加固清单)
- [十一、回滚与版本管理](#十一回滚与版本管理)
- [十二、与本地开发环境差异对照](#十二与本地开发环境差异对照)

---

## 一、部署架构总览

### 1.1 组件与拓扑

newbee-mall-api 是一个**前后端分离的单体应用**（非微服务），正式环境采用单机部署：

```
                        公网
                         │ 80 / 443
                    ┌────▼─────┐
                    │  Nginx   │  反向代理 + HTTPS 终结
                    └────┬─────┘
                         │ proxy_pass http://127.0.0.1:28099
                    ┌────▼───────────────┐
                    │  Spring Boot JAR   │  端口 28099
                    │  newbee-mall-api   │  systemd 托管
                    └────┬───────────┬───┘
                         │           │
              ┌──────────▼──┐   ┌────▼──────┐
              │   MySQL 8   │   │  Redis 7  │
              │  3306 内网  │   │  6379 内网 │
              └─────────────┘   └───────────┘
```

- **入口**：Nginx 只暴露 80/443 到公网；28099 / 3306 / 6379 全部绑定内网或回环地址，不对外暴露。
- **应用**：单一 JAR 包由 systemd 托管，开机自启、崩溃自动重启。
- **数据**：MySQL 存业务数据与登录 token，Redis 存验证码（5 分钟过期，key 前缀 `captcha:`）。

### 1.2 应用内模块

| 模块 | API 前缀 | 服务对象 | 端口 |
|---|---|---|---|
| 商城前端 | `/api/v1` | H5 商城（用户/商品/购物车/订单/地址） | 28099 |
| 管理后台 | `/manage-api/v1` | Vue3 管理后台（管理员/商品/分类/轮播图/首页配置/上传） | 28099 |
| 验证码 | `/api/v1/captcha`、`/manage-api/v1/captcha` | 登录验证码（Redis 存储，绑定账号） | 28099 |
| Swagger | `/swagger-ui/index.html` | 接口文档（**生产环境建议关闭**） | 28099 |

### 1.3 请求链路

```
前端 → Nginx(443 TLS) → Spring Boot(28099)
     → Controller 层（api/）→ Service 层 → DAO 层（MyBatis → MySQL）
     → 验证码/Redis → 登录 token（MySQL，48 小时过期）
```

### 1.4 关键设计点（影响部署）

- **上传目录**：文件上传路径硬编码在 `Constants.FILE_UPLOAD_DIC`，当前默认 `/tmp/tempupload`（重启即丢）。**生产环境必须改为持久化目录**（如 `/opt/newbee/upload`）并重新打包，否则重启后图片丢失。
- **静态资源**：项目内 `classpath:/file-test/`、`classpath:/static/` 走 `/resources/**` 路径。
- **端口与地址**：`server.port=28099`、`server.address=0.0.0.0`（生产建议由 Nginx 反代后改绑 `127.0.0.1`）。


## 二、环境要求

### 2.1 服务器

| 项 | 要求 |
|---|---|
| 操作系统 | Linux x86_64（CentOS 7+/Ubuntu 20.04+ 均可，本文示例以 Ubuntu 系命令为主） |
| 内存 | ≥ 2 GB（建议 4 GB，Hikari 连接池 + JVM 堆） |
| 磁盘 | ≥ 20 GB（应用 500 MB + 数据库 + 上传文件 + 日志） |
| 架构 | 单体应用，单节点即可支撑中小流量 |

### 2.2 运行依赖

| 软件 | 版本 | 用途 | 部署机是否必需 |
|---|---|---|---|
| JDK | 1.8（Java 8） | 运行 JAR | 是 |
| MySQL | 8.0 | 业务数据库 | 是 |
| Redis | 7.x（本项目用 redis:7-alpine） | 验证码存储 | 是 |
| Nginx | 1.20+ | 反向代理 + HTTPS | 是 |
| Maven | 3.6+ | 仅打包阶段使用 | 否（构建机用） |

> 项目 `pom.xml` 声明 `java.version=1.8`，请勿在服务器安装更高版本 JDK 直接运行（存在兼容风险），或确认 JDK 版本与构建时一致。

### 2.3 端口规划

| 端口 | 服务 | 对外暴露 |
|---|---|---|
| 80 / 443 | Nginx | 是（公网） |
| 28099 | Spring Boot | 否（仅本机/内网） |
| 3306 | MySQL | 否（仅内网） |
| 6379 | Redis | 否（仅本机） |

---

## 三、打包 JAR

### 3.1 打包命令（构建机执行）

```bash
# 项目根目录
mvn clean package -DskipTests
```

产物：`target/newbee-mall-api-3.0.0-SNAPSHOT.jar`

- 通过 `spring-boot-maven-plugin` 打成可执行 fat jar（内嵌 Tomcat）。
- `-DskipTests` 跳过测试；如需先跑测试去掉该参数。
- 当前版本号在 `pom.xml` 的 `<version>3.0.0-SNAPSHOT</version>`，发布时建议去掉 `-SNAPSHOT` 并打 Git tag（见[十一、回滚与版本管理](#十一回滚与版本管理)）。

### 3.2 本地快速验证产物

```bash
# 先用 dev profile 冒烟验证，确认能正常启动再上生产
java -jar target/newbee-mall-api-3.0.0-SNAPSHOT.jar --spring.profiles.active=dev

# 另开终端验证接口
curl http://localhost:28099/api/v1/captcha?userName=admin
```

启动日志出现 `Started NewBeeMallAPIApplication` 即成功。

### 3.3 上传目录修改提示（重要）

`src/main/java/ltd/newbee/mall/common/Constants.java:11`：

```java
public final static String FILE_UPLOAD_DIC = "/tmp/tempupload"; // 默认，重启丢失
```

生产环境改为持久化目录后**需重新编译打包**：

```java
public final static String FILE_UPLOAD_DIC = "/opt/newbee/upload";
```

```bash
mkdir -p /opt/newbee/upload && chown -R newbee:newbee /opt/newbee/upload
```

---

## 四、生产配置文件

### 4.1 Profile 机制

项目通过 `application.properties`（公共配置）+ `application-{profile}.properties`（环境差异）分层：

| 文件 | 作用 |
|---|---|
| `src/main/resources/application.properties` | 公共配置（端口、连接池、MyBatis 等） |
| `src/main/resources/application-dev.properties` | 开发环境（Docker MySQL 33060、Redis 63790） |
| `src/main/resources/application-prod.properties` | **生产环境（已存在，需按真实环境修改）** |
| `src/main/resources/application.yml` | 占位/备用，数据源支持环境变量覆盖 |

生产启动必须显式指定：

```bash
java -jar newbee-mall-api-3.0.0-SNAPSHOT.jar --spring.profiles.active=prod
```

### 4.2 生产必须修改的配置项

对照现有 `application-prod.properties`，正式上线前按下表调整（**敏感信息不要明文提交 Git**，用环境变量或部署时注入）：

| 配置项 | 开发默认值 | 生产建议 |
|---|---|---|
| `spring.datasource.url` | `localhost:3306/newbee_mall_db_v2`，`useSSL=false` | 内网地址 + `useSSL=true` |
| `spring.datasource.username/password` | `root / nyh123` | 专用低权限账号 + 强密码（见[七、数据库加固](#七数据库与-redis-生产加固)） |
| `spring.redis.host/port/password` | `localhost:63790 / redis123` | 内网地址 + 强密码 |
| `server.address` | `0.0.0.0` | `127.0.0.1`（仅经 Nginx 反代） |
| 上传目录 | `/tmp/tempupload` | `/opt/newbee/upload`（改 Constants 后重新打包） |
| Swagger | 开启 | 生产关闭（见[十、安全加固](#十安全加固清单)） |

### 4.3 生产配置模板（application-prod.properties 参考）

```properties
# ===== 服务 =====
server.address=127.0.0.1
server.port=28099

# ===== 数据源 =====
spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/newbee_mall_db_v2?useUnicode=true&serverTimezone=Asia/Shanghai&characterEncoding=utf8&autoReconnect=true&useSSL=true&allowMultiQueries=true
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}

# Hikari 连接池
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.maximum-pool-size=15
spring.datasource.hikari.auto-commit=true
spring.datasource.hikari.idle-timeout=30000
spring.datasource.hikari.pool-name=hikariCP
spring.datasource.hikari.max-lifetime=600000
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.connection-test-query=SELECT 1

# ===== MyBatis =====
mybatis.mapper-locations=classpath:mapper/*Mapper.xml

# ===== Redis（验证码） =====
spring.redis.host=127.0.0.1
spring.redis.port=6379
spring.redis.password=${REDIS_PASSWORD}
spring.redis.database=0
spring.redis.timeout=3000ms

# ===== 文件上传 =====
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# ===== 日志 =====
logging.file.name=/opt/newbee/logs/application.log
```

> 说明：也可以不改 `application-prod.properties`，而是通过启动时 JVM 参数覆盖（如 `--spring.datasource.password=xxx` 或 `-DDB_PASSWORD=xxx`），避免配置带密码进 Git。

---

## 五、systemd 服务托管（开机自启/崩溃重启）

### 5.1 创建服务文件

```bash
sudo vim /etc/systemd/system/newbee-mall.service
```

```ini
[Unit]
Description=NEWBEE Mall API (Spring Boot)
After=network.target mysql.service redis.service
Wants=mysql.service redis.service

[Service]
Type=simple
# 使用专用运行用户（需提前创建：useradd -m -s /sbin/nologin newbee）
User=newbee
Group=newbee
WorkingDirectory=/opt/newbee/app
Environment=SPRING_PROFILES_ACTIVE=prod
Environment=DB_USER=newbee
Environment=DB_PASSWORD=<你的强密码>
Environment=REDIS_PASSWORD=<你的强密码>
ExecStart=/usr/bin/java -Xms256m -Xmx1024m -jar /opt/newbee/app/newbee-mall-api-3.0.0-SNAPSHOT.jar
# 崩溃自动重启
Restart=always
RestartSec=5
# 平滑停止：等待进程退出再 kill
TimeoutStopSec=30
KillSignal=SIGTERM
# 日志统一走 journald（也可配合文件日志，见第九章）
StandardOutput=journal
StandardError=journal

[Install]
WantedBy=multi-user.target
```

> `-Xms/-Xmx` 按服务器内存调整；Hikari 最大 15 连接 + Tomcat 默认线程，1~2 GB 堆通常够用。

### 5.2 启用与常用命令

```bash
sudo systemctl daemon-reload
sudo systemctl enable --now newbee-mall     # 开机自启 + 立即启动

sudo systemctl status newbee-mall           # 查看状态
sudo systemctl restart newbee-mall          # 重启（部署时用）
sudo systemctl stop newbee-mall             # 停止
sudo journalctl -u newbee-mall -f           # 实时查看日志
sudo journalctl -u newbee-mall -n 200       # 最近 200 行
```

### 5.3 自检

```bash
# 进程与端口
sudo ss -lntp | grep 28099

# 接口冒烟（Nginx 未就绪时先走本机）
curl -s http://127.0.0.1:28099/api/v1/captcha?userName=admin
```

---

## 六、Nginx 反向代理 + HTTPS

### 6.1 站点配置

```bash
sudo vim /etc/nginx/conf.d/newbee-mall.conf
```

```nginx
# HTTP → HTTPS 跳转
server {
    listen 80;
    server_name api.example.com;            # 改成真实域名
    return 301 https://$host$request_uri;
}

server {
    listen 443 ssl;
    http2 on;
    server_name api.example.com;

    ssl_certificate     /etc/nginx/ssl/api.example.com.pem;
    ssl_certificate_key /etc/nginx/ssl/api.example.com.key;
    ssl_protocols       TLSv1.2 TLSv1.3;
    ssl_ciphers         HIGH:!aNULL:!MD5;

    # 上传文件目录（对应用户可访问），图片访问路径如 /upload/xxx.png
    location /upload/ {
        alias /opt/newbee/upload/;
        expires 7d;
    }

    # 反向代理到 Spring Boot
    location / {
        proxy_pass http://127.0.0.1:28099;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_connect_timeout 10s;
        proxy_read_timeout 60s;
    }
}
```

```bash
sudo nginx -t && sudo systemctl reload nginx
```

> 上传图片返回的 URL 前缀与上传目录路径相关，若后端返回的是 `/upload/xxx`，需保证与上述 `location /upload/` 一致（后端 UploadController 中拼接的前缀需与部署路径匹配）。

### 6.2 HTTPS 证书（Let's Encrypt / certbot）

```bash
sudo apt install -y certbot python3-certbot-nginx   # Ubuntu
sudo certbot --nginx -d api.example.com             # 自动申请并改写 Nginx 配置
sudo certbot renew --dry-run                        # 验证自动续期
```

### 6.3 验证

```bash
curl -I https://api.example.com/api/v1/captcha?userName=admin
```

应返回 `200 OK`（或验证码接口正常响应）。

---

## 七、数据库与 Redis 生产加固

### 7.1 MySQL 初始化与导入

项目 schema 文件：`src/main/resources/newbee_mall_db_v2_schema.sql`（含建库建表 + 初始数据）。

```bash
# 导入（含库结构），库名 newbee_mall_db_v2
mysql -h127.0.0.1 -uroot -p < /path/to/newbee_mall_db_v2_schema.sql

# 或仅建表语句：先建库再导入
mysql -h127.0.0.1 -uroot -p -e "CREATE DATABASE IF NOT EXISTS newbee_mall_db_v2 DEFAULT CHARACTER SET utf8mb4;"
mysql -h127.0.0.1 -uroot -p newbee_mall_db_v2 < /path/to/newbee_mall_db_v2_schema.sql
```

### 7.2 MySQL 加固清单

```sql
-- 1. 修改 root 密码
ALTER USER 'root'@'localhost' IDENTIFIED BY '<强密码>';

-- 2. 创建应用专用低权限账号（只授权业务库）
CREATE USER 'newbee_app'@'127.0.0.1' IDENTIFIED BY '<强密码>';
GRANT SELECT, INSERT, UPDATE, DELETE ON newbee_mall_db_v2.* TO 'newbee_app'@'127.0.0.1';
FLUSH PRIVILEGES;
```

`my.cnf` 关键项：

```ini
[mysqld]
bind-address = 127.0.0.1        # 仅本机访问，禁止远程直连
character-set-server = utf8mb4
collation-server = utf8mb4_unicode_ci
skip-name-resolve
```

> 若必须允许内网其他机器直连 MySQL，bind 内网 IP 并限制防火墙，**切勿用 root + 默认密码对外**。

### 7.3 MySQL 定时备份

```bash
sudo crontab -e
# 每天凌晨 2 点全量备份，保留 7 天
0 2 * * * mysqldump -h127.0.0.1 -unewbee_app -p'<密码>' --single-transaction --routines newbee_mall_db_v2 | gzip > /opt/newbee/backup/db_$(date +\%F).sql.gz && find /opt/newbee/backup -name 'db_*.sql.gz' -mtime +7 -delete
```

### 7.4 Redis 加固

```bash
# Redis 仅本机使用，绑定 127.0.0.1 并启用密码认证
# 修改 redis.conf（或 docker-compose 中的启动命令）
bind 127.0.0.1
protected-mode yes
requirepass <强密码>
appendonly yes
```

若用 Docker（本项目 `docker-mysql/.env`），修改 `REDIS_PASSWORD` 后重启：

```bash
cd docker-mysql
docker-compose -f docker-compose-redis.yml up -d
```

> 项目验证码 Redis key 设计：`captcha:{uuid} → "code|userName"`，TTL 300 秒，一次性使用（校验后删除），无需额外清理。

---

## 八、部署步骤（一键脚本）

### 8.1 目录规范

```
/opt/newbee/
├── app/                      # 当前 JAR（运行目录）
├── upload/                   # 上传文件（持久化，需改 Constants 后打包）
├── logs/                     # 应用日志
├── releases/                 # 历史版本归档（用于回滚）
└── backup/                   # 数据库备份
```

```bash
sudo mkdir -p /opt/newbee/{app,upload,logs,releases,backup}
sudo useradd -m -s /sbin/nologin newbee
sudo chown -R newbee:newbee /opt/newbee
```

### 8.2 一键部署脚本（构建机/服务器执行）

```bash
sudo vim /opt/newbee/deploy.sh && sudo chmod +x /opt/newbee/deploy.sh
```

```bash
#!/usr/bin/env bash
# 用法: ./deploy.sh <jar文件>
set -euo pipefail

JAR="$1"
APP_DIR="/opt/newbee/app"
RELEASES_DIR="/opt/newbee/releases"
SERVICE="newbee-mall"
TIMESTAMP="$(date +%Y%m%d%H%M%S)"
VERSION="${JAR##*/}"            # 如 newbee-mall-api-3.0.0-SNAPSHOT.jar

# 1. 归档旧版本（回滚用）
if [ -f "$APP_DIR/$VERSION" ]; then
    cp "$APP_DIR/$VERSION" "$RELEASES_DIR/${VERSION%.jar}-${TIMESTAMP}.jar"
    echo "[1/5] 旧版本已归档到 $RELEASES_DIR"
fi

# 2. 停止服务
sudo systemctl stop "$SERVICE"
echo "[2/5] 服务已停止"

# 3. 替换 JAR
cp "$JAR" "$APP_DIR/$VERSION"
sudo chown newbee:newbee "$APP_DIR/$VERSION"
echo "[3/5] JAR 已更新: $APP_DIR/$VERSION"

# 4. 启动服务
sudo systemctl start "$SERVICE"
echo "[4/5] 服务已启动"

# 5. 健康检查（最多等 60 秒）
for i in $(seq 1 60); do
    if curl -sf http://127.0.0.1:28099/api/v1/captcha?userName=admin >/dev/null; then
        echo "[5/5] 部署成功，服务健康"
        exit 0
    fi
    sleep 1
done

echo "[FAIL] 健康检查超时，正在回滚..."
sudo systemctl stop "$SERVICE"
# 回滚到最近归档
LATEST="$(ls -t "$RELEASES_DIR"/*.jar 2>/dev/null | head -1)"
if [ -n "$LATEST" ]; then
    cp "$LATEST" "$APP_DIR/$VERSION"
    sudo systemctl start "$SERVICE"
fi
exit 1
```

执行：

```bash
sudo /opt/newbee/deploy.sh target/newbee-mall-api-3.0.0-SNAPSHOT.jar
```

> 脚本依赖 systemd 服务名 `newbee-mall`（见[五、systemd 服务托管](#五systemd-服务托管开机自启崩溃重启)）。

---

## 九、日志与监控

### 9.1 日志配置

生产配置中加入文件日志（见[四、生产配置](#四生产配置文件)）：

```properties
logging.file.name=/opt/newbee/logs/application.log
```

查看方式：

```bash
# systemd journal（实时）
sudo journalctl -u newbee-mall -f

# 应用日志文件
tail -f /opt/newbee/logs/application.log
```

> 开启 DAO 层 SQL 日志排查用（生产建议关闭，避免刷屏）：`logging.level.ltd.newbee.mall.dao=debug`。

### 9.2 日志切割（logrotate）

```bash
sudo vim /etc/logrotate.d/newbee-mall
```

```text
/opt/newbee/logs/*.log {
    daily
    rotate 14
    compress
    missingok
    notifempty
    copytruncate
}
```

### 9.3 监控项与手段

项目**未引入 Spring Boot Actuator**，生产用 OS 级监控即可：

| 监控项 | 手段 |
|---|---|
| 进程/端口存活 | systemd（Restart=always）+ 定时 `curl` 健康检查（见部署脚本） |
| JVM / 内存 / CPU | `jcmd`、`jstat`、`top`；可挂 Node Exporter + Prometheus 采集 |
| 磁盘 | 上传目录 + 日志 + 数据库备份目录容量 |
| 数据库 | MySQL 慢查询日志、连接数 |
| 外部告警 | 结合健康检查脚本 + 邮件/企业微信/钉钉 webhook |

示例（cron 每 5 分钟健康检查 + 告警占位）：

```bash
*/5 * * * * curl -sf http://127.0.0.1:28099/api/v1/captcha?userName=admin || echo "[$(date)] app down" >> /opt/newbee/logs/health.log
```

---

## 十、安全加固清单

上线前逐项核对：

- [ ] **修改所有默认密码**：MySQL root/`nyh123`、应用账号 `newbee`/`newbee123`、Redis `redis123`（仓库内 `.env.example`、`application-dev.properties`、`application-prod.properties` 中均有默认值，生产全部替换）
- [ ] **敏感信息不入 Git**：数据库/Redis 密码用环境变量或启动参数注入（`${DB_PASSWORD}` 等），密码文件加入 `.gitignore`
- [ ] **防火墙只开 80/443**：`28099 / 3306 / 6379` 不对外暴露；Nginx 反代后 `server.address=127.0.0.1`
- [ ] **MySQL**：专用低权限账号、`useSSL=true`、`bind-address` 内网/本机、开启定时备份
- [ ] **Redis**：`requirepass` 强密码、`bind 127.0.0.1`、`protected-mode yes`、开启 AOF 持久化
- [ ] **HTTPS**：Nginx 配置 TLS 1.2+，HTTP 301 跳转 HTTPS，证书自动续期
- [ ] **上传目录**：`/tmp/tempupload` → `/opt/newbee/upload`（改 `Constants.java` 重新打包），目录属主为运行用户，限制可写
- [ ] **生产关闭 Swagger**：`springfox` 未显式开关时默认开启，生产建议通过配置/注解关闭接口文档暴露
- [ ] **应用运行用户**：使用专用低权限用户 `newbee`（`/sbin/nologin`），勿用 root 运行 JAR
- [ ] **更新 JVM/依赖漏洞**：关注 Spring Boot 2.7.x 安全补丁版本

---

## 十一、回滚与版本管理

### 11.1 版本管理

- 发布前更新 `pom.xml` 版本号（去掉 `-SNAPSHOT`），如 `3.0.0` → `3.0.1`。
- 每次发布打 Git tag 并记录变更：`git tag -a v3.0.1 -m "release v3.0.1"`。
- 构建产物命名带版本，便于区分与追溯：`newbee-mall-api-3.0.1.jar`。

### 11.2 回滚策略

部署脚本（见[八、一键脚本](#八部署步骤一键脚本)）已内置"旧版本归档 + 健康检查失败自动回滚"。

手动回滚：

```bash
# 1. 查看可回滚版本
ls -lt /opt/newbee/releases/

# 2. 停服 → 还原 → 重启
sudo systemctl stop newbee-mall
sudo cp /opt/newbee/releases/newbee-mall-api-3.0.1-20260101120000.jar /opt/newbee/app/newbee-mall-api-3.0.1.jar
sudo systemctl start newbee-mall

# 3. 健康检查
curl -sf http://127.0.0.1:28099/api/v1/captcha?userName=admin
```

### 11.3 数据库回滚注意事项

- 升级前**必须**先做数据库备份（见[七、MySQL 备份](#七数据库与-redis-生产加固)）。
- 若本次发布涉及表结构变更，建议：
  - 变更脚本与代码**同版本、向后兼容**（先加列、后删列）；
  - 需要回滚时，用备份恢复或执行对应的逆向迁移脚本，再回退 JAR。
- 回滚顺序：先回退 JAR → 再处理数据库，避免新代码配旧库、旧代码配新库。

---

## 十二、与本地开发环境差异对照

| 维度 | 本地开发（dev） | 正式生产（prod） |
|---|---|---|
| Profile | `application-dev.properties` | `application-prod.properties` |
| 启动命令 | `mvn spring-boot:run -Dspring-boot.run.profiles=dev` | `systemctl start newbee-mall`（`SPRING_PROFILES_ACTIVE=prod`） |
| 监听地址 | `0.0.0.0`（局域网可访问） | `127.0.0.1`（仅经 Nginx） |
| 对外端口 | 28099 直连 | 仅 80/443（Nginx 反代） |
| MySQL | Docker（`localhost:33060`，root/nyh123） | 服务器本机/内网 `3306`，专用账号 + 强密码，`useSSL=true` |
| Redis | Docker（`localhost:63790`，redis123） | 服务器本机 `6379`，强密码，`bind 127.0.0.1` |
| 上传目录 | `/tmp/tempupload`（重启丢失） | `/opt/newbee/upload`（持久化，需改 Constants 重新打包） |
| Swagger | 开启，方便联调 | 建议关闭 |
| SQL 日志 | 可按需开启 `dao=debug` | 关闭，避免刷屏 |
| 进程托管 | 前台/IDE 内运行 | systemd 托管（开机自启、崩溃重启） |
| 日志 | 终端/IDE 控制台 | `/opt/newbee/logs/application.log` + journald + logrotate |
| 反向代理 | 无 | Nginx + HTTPS |
| 数据库备份 | 无（可随时重置） | 每日定时备份，保留 7 天 |
| 回滚 | 无（本地代码可随时改） | 归档版本 + 一键回滚脚本 |
| 数据安全 | 默认弱密码，仅本机 | 全部强密码、防火墙收敛、TLS 加密 |

### 差异背后的原因

- **端口/地址**：生产由 Nginx 统一入口，应用收敛到回环地址可减少攻击面。
- **数据库**：开发用 Docker 便捷重置；生产需持久化、备份、最低权限与加密连接。
- **上传目录**：`/tmp` 重启即清，生产必须落到持久化磁盘。
- **进程与日志**：生产要求"挂了能自动拉起、日志能长期排查"，所以用 systemd + 文件日志 + logrotate。
