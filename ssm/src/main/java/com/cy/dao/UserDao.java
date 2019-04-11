package com.cy.dao;

import com.cy.pojo.User;
import org.springframework.stereotype.Repository;

/**
 * @program: SSM
 * @description: user dao
 * @author: cy
 * @create: 2019-04-08 17:43
 **/

public interface UserDao {
    public void save(User user);
}
