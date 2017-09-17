package com.yisa.REST.Login.authorization.manager.Impl;

import com.yisa.REST.Login.authorization.manager.TokenManager;
import com.yisa.REST.Login.authorization.model.TokenModel;
import com.yisa.REST.Login.authorization.config.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Component
public class RedisTokenManager implements TokenManager{

    private RedisTemplate<Long,String> redis;


    @Autowired
    public void setRedis(RedisTemplate redis) {
        this.redis = redis;
        redis.setKeySerializer(new JdkSerializationRedisSerializer());
    }



    @Override
    public TokenModel createToken(long userId) {
        String token = UUID.randomUUID().toString().replace("-","");
        TokenModel model = new TokenModel(userId,token);
        redis.boundValueOps(userId).set(token, Constants.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
        return model;
    }

    @Override
    public boolean checkToken(TokenModel model) {
        if(model == null ){
            return false;
        }
        String token = redis.boundValueOps(model.getUserId()).get();
        if( token == null  || ! token.equals(model.getToken())){
            return  false;
        }

        /**
         * 验证成功，进行了一次有效操作，延长token的过期时间
         */
        redis.boundValueOps(model.getUserId()).expire(Constants.TOKEN_EXPIRES_HOUR,TimeUnit.HOURS);
        return true;
    }

    @Override
    public TokenModel getToken(String authentication) {
        if( authentication == null || authentication.length() == 0 ){
            return null;
        }
        String[] param = authentication.split("_");
        if(param.length!= 2 ){
            return null;
        }
        /**
         * 使用userId和源token简单拼接成的token，
         */
        long userId = Long.parseLong(param[0]);
        String token = param[1];
        return new TokenModel(userId,token);
    }

    @Override
    public void deleteToken(long userId) {
        redis.delete(userId);
    }
}
