package com.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.system.domain.Process;
import com.system.domain.Result;
import com.system.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/Process")
@RestController()
public class ProcessController {


    @Autowired
    private ProcessService processService;



    @GetMapping("/{rid}/{uid}")
    public Result By(@PathVariable("rid") Integer rid, @PathVariable("uid") Integer uid) {
        QueryWrapper<Process> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("rid", rid);
        queryWrapper.eq("uid", uid);
        List<Process> list = processService.list(queryWrapper);

        return Result.ok(list);
    }

}
