package com.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.system.domain.Result;
import com.system.domain.Topicfordiscussion;
import com.system.service.TopicfordiscussionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
public class TopicfordiscussionController {


    @Autowired
    private TopicfordiscussionService topicfordiscussionService;


    @GetMapping("/projects")
    public Result list() {
        return Result.ok(topicfordiscussionService.list());
    }





    @PostMapping("/projects")
    public Result save(@RequestBody Topicfordiscussion topicfordiscussion) {
        boolean save = topicfordiscussionService.save(topicfordiscussion);
        return Result.ok();
    }

    @PutMapping("/projects/{id}")
    public Result put(@PathVariable("id") Integer id, @RequestBody Topicfordiscussion topicfordiscussion) {
        topicfordiscussion.setId(id);
        boolean update = topicfordiscussionService.updateById(topicfordiscussion);
        return Result.ok();
    }

    @DeleteMapping("/projects/{id}")
    public Result delete(@PathVariable("id") Integer id) {
        boolean remove = topicfordiscussionService.removeById(id);
        return Result.ok();
    }

}
