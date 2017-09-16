package com.yisa.REST.Login.repository;

import com.yisa.REST.Login.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository  extends CrudRepository<User,Long> {
    User findByUsername(String username);
}
