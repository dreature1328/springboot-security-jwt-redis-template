# 基于 Spring Boot + Security + JWT + Redis 的入门级安全认证与授权模板

本项目是针对安全认证（Authentication）与授权（Authorization）场景的模板工程，基于 Spring Security 安全框架，结合 JWT 令牌 + Redis 缓存，提供注册、登录流程及权限控制的解决方案，便于初学者入门实践。

## 架构设计

项目采用 MVC 分层架构

- 控制层（`controller`）采用三泛型设计（实体类型 `T` + 主键类型 `ID` + 服务类 `S`），负责处理 HTTP 请求与响应，便于接口测试
- 服务层（`service`）采用三泛型设计（实体类型 `T` + 主键类型 `ID` + 映射器 `M`），接口与实现分离，服务实现类继承通用基类
- 持久层（`mapper`）采用双泛型设计（实体类型 `T` + 主键类型 `ID`），映射接口继承通用基接口，基于原生 MyBatis 实现 SQL 映射（暂不考虑 MyBatis-Plus）

## 安全集成

项目采用 **基于角色的访问控制（RBAC）** 模型，通过五表结构实现“用户-角色-权限”三级关系：用户表（`user`）、角色表（`role`）、权限表（`permission`）、用户角色关联表（`user_role`）、角色权限关联表（`role_permission`）。

用户登录时通过 Spring Security 认证管理器进行**凭证验证**，认证成功后返回 JWT Token，以此在后续请求中标识用户身份，并由 Spring Security 自动根据授权规则进行**权限校验**。

## 启动流程

1. **数据源配置**
   - 编辑 `application.properties` 文件，配置数据源参数
   - 执行 `rbac.sql` 脚本，初始化数据库表结构
2. **注入配置**：按需调整 `common.config` 中涉及安全认证的配置
3. **项目启动**：运行 `Application.java` 主类，启动 Spring Boot 应用
4. **接口测试**：发起请求调用控制层接口，执行增删改查操作（`<RBAC>Controller.java`）或认证操作（`AuthController.java`）

## 相关脚本

脚本位于 `script/` 目录

- 数据表结构定义脚本：`rbac.sql` 
- 随机用户数据生成脚本：`generate_mock_user.py`
