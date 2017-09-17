package com.yisa.REST.Login.controller;


import com.yisa.REST.Login.authorization.annotation.Authorization;
import com.yisa.REST.Login.authorization.annotation.CurrentUser;
import com.yisa.REST.Login.authorization.config.ResultStatus;
import com.yisa.REST.Login.authorization.manager.TokenManager;
import com.yisa.REST.Login.authorization.model.TokenModel;
import com.yisa.REST.Login.domain.User;
import com.yisa.REST.Login.model.ResultModel;
import com.yisa.REST.Login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/tokens")
public class TokenController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenManager tokenManager;


    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ResultModel> login(@RequestParam String username , @RequestParam String password ){
        Assert.notNull(username,"username can not be empty");
        Assert.notNull(password,"password can not be empty");

        User user = userRepository.findByUsername(username);
        if( user == null ||
                !user.getPassword().equals(password)){
            /**
             * 用户名或密码错误
             */
            return new ResponseEntity<ResultModel>(ResultModel.error(
                    ResultStatus.USERNAME_OR_PASSWORD_ERROR),
                    HttpStatus.NOT_FOUND
            );
        }
        TokenModel token = tokenManager.createToken(user.getId());
        return new ResponseEntity<ResultModel>(
                ResultModel.ok(token),
                HttpStatus.OK
        );
    }

    @RequestMapping(method =  RequestMethod.DELETE)
    @Authorization
    public ResponseEntity<ResultModel> logout(@CurrentUser User user ){
        if(user == null ){
            System.out.println("Here");
        }
        tokenManager.deleteToken(user.getId());
        return new ResponseEntity<ResultModel>(ResultModel.ok(),HttpStatus.OK);
    }


}
