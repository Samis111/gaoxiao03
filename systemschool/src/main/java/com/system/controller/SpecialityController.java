package com.system.controller;

import com.system.domain.Result;
import com.system.domain.Speciality;
import com.system.domain.University;
import com.system.service.SpecialityService;
import com.system.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/speciality")
public class SpecialityController {



    @Autowired
    private SpecialityService universityService;

    @GetMapping("list")
    public Result list() {
        List<Speciality> list = universityService.list();
        return Result.ok(list);
    }


    @PostMapping("save")
    public Result save(@RequestBody Speciality university) {
        boolean save = universityService.save(university);
        return Result.ok();
    }

    @PostMapping("update")
    public Result update(@RequestBody Speciality university) {
        boolean save = universityService.updateById(university);
        return Result.ok();
    }


    @PostMapping("delete/{id}")
    public Result delete(@PathVariable("id")Integer id) {
        boolean save = universityService.removeById(id);
        return Result.ok();
    }

}
