项目说明<br/>
1.整体采用spring-cloud搭建，版本号：Finchley.RELEASE<br/>
2.整合了spring-cloud-gateway,spring-cloud-starter-netflix-hystrix等中间件<br/>
3.gateway路由这块，采用编码形式路由。如有兴趣，也可以采用注册中心路由，配合yml文件即可。<br/>
4.重写了项目异常，针对自身异常和下游项目异常，均有处理<br/>
5.其他cloud中间件系列，例如feign，bus，config,zk等，看具体业务情况引入<br/>
