# docker-mysql

#### 介绍

Docker MySQL 本地开发环境。

连接信息


项目	值
Host	localhost 或 127.0.0.1
Port	33060
root 密码	nyh123
普通用户	newbee
普通用户密码	newbee123
数据库	newbee_mall_db_v2

本地连接：直接使用roor，密码nyh123，端口：33060

#### 目录结构

```
docker-mysql/
├── .env               # 环境变量配置（镜像、端口、密码等）
├── .gitignore          # 忽略 data/ 目录
├── docker-compose.yml  # Docker Compose 编排
├── README.md           # 本文件
├── init/               # SQL 初始化脚本（首次启动时自动执行）
│   └── *.sql           # 例如 schema.sql、seed-data.sql
└── data/               # MySQL 数据持久化目录（已 gitignore）
    └── db/mysql/
```

#### 使用说明

**首次启动：**

```bash
docker-compose up -d
```

**重启：**

```bash
docker-compose restart
```

**完全重置（清空所有数据后重新初始化）：**

```bash
docker-compose down -v
rm -rf data/
docker-compose up -d
```

#### 环境变量

编辑 `.env` 文件可配置：

- 数据库密码
- 宿主机端口（默认 `33060` → 容器 `3306`）
- 时区、字符集
- 数据持久化路径

#### 初始化 SQL

将 DDL/DML 脚本放入 `init/` 目录（如 `init.sql`），首次启动容器时 MySQL 会自动按文件名顺序执行。注意：**仅首次启动时执行**，如果已有 `data/` 目录存在则不会重复执行。

#### 代码同步注意事项

- `init/` 目录下的 SQL 脚本应当提交到 Git，方便他人拉取后直接 `docker-compose up -d` 即可运行
- `data/` 目录**不要提交到 Git**（已在 `.gitignore` 中忽略），属于本地运行时数据
- 如果需要干净的数据库环境，执行上面的「完全重置」步骤即可