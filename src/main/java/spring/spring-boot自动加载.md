#流程
* ![](img/spring-boot自动装配.image)
* 所有 Spring Boot Starter 下的META-INF/spring.factories都会被读取到。
* spring.factories配置不会都加载，根据@condition按需加载