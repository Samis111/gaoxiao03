package com.system.controller;

import com.system.domain.Result;
import com.system.domain.User;
import com.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RequestMapping("/api")
@RestController()
public class UserController {


    @Autowired
    private UserService userService;

    @GetMapping("list")
    public Result list() {

        return Result.ok(userService.list());

    }

    @GetMapping("updatePassword/{id}")
    public Result updatePassword(@PathVariable("id")Integer id) {

        User user =  new User();
        user.setId(id);
        user.setPassword("123123");
        boolean update = userService.updateById(user);

        return Result.ok();
    }

    @GetMapping("profile/{id}")
    public Result profile(@PathVariable("id") Integer id) {

        User byId = userService.getById(id);
        return Result.ok(byId);
    }

    @PostMapping("updateById")
    public Result updateById(@RequestBody User user) {

        boolean update = userService.updateById(user);
        return Result.ok();

    }


}
