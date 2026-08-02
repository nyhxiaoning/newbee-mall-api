# docker-mysql
## docker启动后，配置删除内容，如果有配置修改
```
docker启动，重启问题：是否可以使用数据库
1.如果不想要动数据库，那么restart，不会重新操作数据库
cd docker-mysql
docker-compose restart

2.指定配置文件启动
cd docker-mysql

# 单独启动 Redis
docker-compose -f docker-compose-redis.yml up -d

# 同时启动 MySQL + Redis
docker-compose -f docker-compose.yml -f docker-compose-redis.yml up -d

3.完全重置数据库
cd docker-mysql
sudo docker-compose down
sudo rm -rf data/
sudo docker-compose up -d
# 指定配置文件docker-compose.yml启动
sudo docker-compose -f docker-compose-redis.yml up -d


```
#### 介绍

Docker MySQL + Redis 本地开发环境。

#### 连接信息

**MySQL**

| 项目 | 值 |
|---|---|
| Host | localhost |
| Port | 33060 |
| root 密码 | nyh123 |
| 普通用户 | newbee |
| 普通用户密码 | newbee123 |
| 数据库 | newbee_mall_db_v2 |

**Redis**

| 项目 | 值 |
|---|---|
| Host | localhost |
| Port | 63790 |
| 密码 | redis123 |

#### 目录结构

```
docker-mysql/
├── .env                        # 环境变量配置
├── .gitignore                   # 忽略 data/ 目录
├── docker-compose.yml           # MySQL 编排
├── docker-compose-redis.yml     # Redis 编排
├── README.md                    # 本文件
├── init/                        # SQL 初始化脚本
│   └── *.sql
└── data/                        # 数据持久化目录（已 gitignore）
    ├── db/mysql/
    └── redis/
```

#### 使用说明

**启动 MySQL：**

```bash
docker-compose up -d
```

**启动 Redis：**

```bash
docker-compose -f docker-compose-redis.yml up -d
```

**同时启动 MySQL + Redis：**

```bash
docker-compose -f docker-compose.yml -f docker-compose-redis.yml up -d
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

编辑 `.env` 文件可配置 MySQL 和 Redis 的全部参数（密码、端口、数据路径等）。

#### 初始化 SQL

将 DDL/DML 脚本放入 `init/` 目录，首次启动 MySQL 时自动按文件名顺序执行。注意：**仅首次启动时执行**，如果已有 `data/` 目录存在则不会重复执行。

#### 代码同步注意事项

- `init/` 目录下的 SQL 脚本应当提交到 Git
- `data/` 目录**不要提交到 Git**（已在 `.gitignore` 中忽略），属于本地运行时数据
- 如果需要干净的数据库环境，执行上面的「完全重置」步骤即可