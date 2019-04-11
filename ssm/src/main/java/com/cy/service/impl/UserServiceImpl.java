package com.cy.service.impl;

import com.cy.dao.UserDao;
import com.cy.pojo.User;
import com.cy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: SSM
 * @description: UserService 实现类
 * @author: cy
 * @create: 2019-04-08 17:42
 **/

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public void save(User user) {
        log.info("进入 UserServiceImpl");
        userDao.save(user);

    }
}
