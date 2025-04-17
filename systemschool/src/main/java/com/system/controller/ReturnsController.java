package com.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.system.domain.Process;
import com.system.domain.Result;
import com.system.domain.Returns;
import com.system.domain.Topicfordiscussion;
import com.system.service.ReturnsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api")
@RestController
public class ReturnsController {

    @Autowired
    private ReturnsService returnsService;

    @GetMapping("/Returns/list/{id}")
    public Result ReturnsList(@PathVariable("id") Integer id) {
        QueryWrapper<Returns> userid = new QueryWrapper<Returns>().eq("userid", id);
        return Result.ok(returnsService.list(userid));
    }

    @GetMapping("/Returns/{pid}")
    public Result find(@PathVariable("pid") Integer pid) {

        Returns byId = returnsService.getById(pid);
        return Result.ok(byId);
    }

    @GetMapping("/Returns")
    public Result list() {
        QueryWrapper<Returns> thistime = new QueryWrapper<Returns>().orderByDesc("thistime");
        return Result.ok(returnsService.list(thistime));
    }

    @PostMapping("/Returns/save")
    public Result save(@RequestBody Returns returns) {

        boolean save = returnsService.save(returns);
        return Result.ok();
    }


    @PostMapping("/Returns/withdraw")
    public Result withdraw(@RequestBody Returns returns) {

        boolean update = returnsService.updateById(returns);
        return Result.ok();
    }

    @PostMapping("/Returns/review")
    public Result review(@RequestBody Returns returns) {

        boolean update = returnsService.updateById(returns);
        return Result.ok();
    }

}
