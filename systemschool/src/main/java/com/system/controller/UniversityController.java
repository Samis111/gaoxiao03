package com.system.controller;

import com.system.domain.Result;
import com.system.domain.University;
import com.system.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/university")
@RestController
public class UniversityController {

    @Autowired
    private UniversityService universityService;

    @GetMapping("list")
    public Result list() {
        List<University> list = universityService.list();
        return Result.ok(list);
    }


    @PostMapping("save")
    public Result save(@RequestBody University university) {
        boolean save = universityService.save(university);
        return Result.ok();
    }

    @PostMapping("update")
    public Result update(@RequestBody University university) {
        boolean save = universityService.updateById(university);
        return Result.ok();
    }


    @PostMapping("delete/{id}")
    public Result delete(@PathVariable("id")Integer id) {
        boolean save = universityService.removeById(id);
        return Result.ok();
    }

}
