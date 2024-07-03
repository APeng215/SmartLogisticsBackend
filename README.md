# 智慧物流-后端部分

这是智慧物流项目的后端部分。

## 开发者须知

### 如何配置数据库？

在 `application.properties` 文件中配置以下四项以连接你自己的数据库:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/smart_logistics
spring.datasource.username=root
spring.datasource.password=123456
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

> [!NOTE]
> 后端会在启动时自动为数据库创建数据库表，**但是**数据库需要你手动创建并配置。
> 
> 具体参看 `application.properties` 中 `spring.jpa.hibernate.ddl-auto=xxx` 这一配置项。

### 如何登录/认证？

用 *form-data* 形式请求接口 `POST /login`，*form-data* 包含两组键值对:

```
username: admin
password: 123456
```
如果后端返回 `Code 2xx` 则说明登录成功，此后可以自由访问资源 API。

> [!TIP]
> 后端内置了管理员账号用于测试：
> 
> 账号: admin
> 
> 密码: 123456

## API 文档

在后端运行时查看
http://localhost:8081/swagger-ui/index.html





