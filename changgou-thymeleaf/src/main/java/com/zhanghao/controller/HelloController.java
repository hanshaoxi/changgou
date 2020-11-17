package com.zhanghao.controller;

import com.zhanghao.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequestMapping("/test")
public class HelloController {

    @GetMapping("/hello")
    public String hello(Model model){
        model.addAttribute("message","hello world");
        User user1 = new User(new Long(1),"zhangSan");
        User user2 = new User(new Long(2),"liSi");
        User user3 = new User(new Long(3),"wangWu");
        List<User> userList = new ArrayList<User>();
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        model.addAttribute("userList",userList);
        model.addAttribute("now",new Date());
        model.addAttribute("age",19);

        Map<String,Object> dataMap = new HashMap<String,Object>();
        dataMap.put("user","zhangSan");
        model.addAttribute("dataMap",dataMap);
        return "hello";
    }
}
