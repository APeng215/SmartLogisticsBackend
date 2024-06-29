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

### ~~如何认证？~~

~~进行任何 API 调用都需要在请求头中添加 **Basic Auth** 认证信息，方式如下：~~

~~1. 根据 Basic Auth 规则生成密钥，如 `Basic YWRtaW46MTIzNDU2`~~
~~2. 在请求头添加 `Authorization: Basic YWRtaW46MTIzNDU2`~~

> [!TIP]
> ~~后端内置了管理员账号，测试时可以选用此账号生成 Basic Auth :~~
> 
> ~~账号: Admin~~
> 
> ~~密码: 123456~~

> [!NOTE]
> 此功能暂时关闭

## API 文档 *(WIP)*

#### 创建订单 - 

```json
{
  
}
```





