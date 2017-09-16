package com.yisa.REST.Login.authorization.manager.Impl;

import com.yisa.REST.Login.authorization.manager.TokenManager;
import com.yisa.REST.Login.authorization.model.TokenModel;
import com.yisa.REST.Login.config.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RedisTokenManager implements TokenManager{

    private RedisTemplate<Long,String> redis;


    @Autowired
    public void setRedis(RedisTemplate<Long, String> redis) {
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

    //TODO

    @Override
    public boolean checkToken(TokenModel model) {
        return false;
    }

    @Override
    public TokenModel getToken(String authentication) {
        return null;
    }

    @Override
    public void deleteToken(long userId) {

    }
}
