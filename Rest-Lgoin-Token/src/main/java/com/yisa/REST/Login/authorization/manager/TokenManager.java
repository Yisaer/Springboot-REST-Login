package com.yisa.REST.Login.authorization.manager;

import com.yisa.REST.Login.authorization.model.TokenModel;

public interface TokenManager {

    /**
     * 创建Token关联上指定用户
     * @param userId
     * @return
     */
    TokenModel createToken(long userId);

    /**
     * 检查token是否有效
     * @param model
     * @return
     */
    boolean checkToken(TokenModel model );

    /**
     *  从字符串中解析token
     * @param authentication
     * @return
     */
    TokenModel getToken(String authentication);

    /**
     * 清除token
     * @param userId
     */
    void deleteToken(long userId);

}
