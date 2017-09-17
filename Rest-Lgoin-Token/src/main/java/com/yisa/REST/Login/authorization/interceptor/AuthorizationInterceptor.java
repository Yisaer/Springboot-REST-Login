package com.yisa.REST.Login.authorization.interceptor;

import com.yisa.REST.Login.authorization.annotation.Authorization;
import com.yisa.REST.Login.authorization.config.Constants;
import com.yisa.REST.Login.authorization.manager.TokenManager;
import com.yisa.REST.Login.authorization.model.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;


@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private TokenManager manager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /**
         * 如果不是映射到方法的则直接通过
         */
        if( ! ( handler instanceof HandlerMethod)){
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        /**
         * 从header中得到token
         */
        String authorization = request.getHeader(Constants.AUTHORIZATION);
        /**
         * 验证token
         */
        TokenModel model = manager.getToken(authorization);
        if( manager.checkToken(model)){
            /**
             * 验证成功，将token对应的用户id存在request中，便于之后注入
             */
            request.setAttribute(Constants.CURRENT_USER_ID,model.getUserId());
            return true;
        }
        /**
         * 验证失败
         */
        if( method.getAnnotation(Authorization.class) != null ){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }
}
