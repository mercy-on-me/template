package com.cy.controller;

import com.alibaba.fastjson.JSONObject;
import com.cy.pojo.User;
import com.cy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * @program: SSM
 * @description: 用户 Controller
 * @author: cy
 * @create: 2019-04-08 17:40
 **/

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/save.action")
    public void save(){
        log.info("进入 controller");
        User user = new User();
        user.setUsername("爱谁谁");
        user.setSex("男");
        user.setAddress("爱哪哪");
        user.setBirthday("2017/1/1");
        user.setCat_id(1);
        userService.save(user);
    }


    @RequestMapping("/test/{id}")
    public void test(@PathVariable()int id){
        log.info("====================> "+ id);
    }


    /**
     * 对象转 json
     * @param args
     */
    public static void main(String[] args) {
        User user = new User();
        user.setUsername("爱谁谁");
        user.setSex("男");
        user.setAddress("爱哪哪");
        user.setBirthday("2017/1/1");
        user.setCat_id(1);
        User user2 = new User();
        user2.setUsername("爱谁谁1");
        user2.setSex("男");
        user2.setAddress("爱哪哪1");
        user2.setBirthday("2019/1/1");
        user2.setCat_id(2);

        List<User> list = Arrays.asList(user, user2);

        System.out.println(JSONObject.toJSON(list));
    }
}
