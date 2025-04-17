package com.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.system.domain.Result;
import com.system.domain.User;
import com.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RequestMapping("api")
@RestController
public class LoginController {

    @Autowired
    private UserService userService;


    @PostMapping("login")
    public Result login(@RequestBody User user, HttpSession httpSession) {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        queryWrapper.eq("password", user.getPassword());

        List<User> list = userService.list(queryWrapper);
        if (list.size() >= 1) {

            httpSession.setAttribute("info", list.get(0));

            return Result.ok(list.get(0));
        }
        return Result.fail();
    }

    @PostMapping("register")
    public Result register(@RequestBody User user) {

        boolean save = userService.save(user);

        return Result.ok();
    }
}
