# Springboot-REST-Login

This is a Simple application by Springboot which show you how Springboot REST application to have User authorization.

## Data Layer 

I use Mysql for data persistence to save the information for users ,  and use Redis for cache to control the authorization .


## How to use ? 

1. fulfill your configuration in application.porperties 
 
for example

```
#MySQL
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/login?useUnicode=true&autoReconnect=true&failOverReadOnly=false&zeroDateTimeBehavior=round&autoReconnect=true
spring.datasource.username=root
spring.datasource.password=123456

#Redis
spring.redis.host=127.0.0.1
#spring.redis.password=123
spring.redis.port=6379

```

2. send `username` and password to `POST /tokens/login` and you will get a token.

For each Http request you want to contorl , add  `Authorization` in HTTP header with token value , and also you should add `@Authorization` to your REST handler and add `@CurrentUser` in Param.

Obviously `logout` is a handler with authorization control , so it is a good example to show how to use it.


```

@RequestMapping(method =  RequestMethod.DELETE) 
     @Authorization 
     public ResponseEntity<ResultModel> logout(@CurrentUser User user ){ 
         if(user == null ){ 
             System.out.println("Here"); 
         } 
         tokenManager.deleteToken(user.getId()); 
         return new ResponseEntity<ResultModel>(ResultModel.ok(),HttpStatus.OK); 
     } 

```
