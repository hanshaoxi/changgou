package com.changgou.goods.controller;

import com.changgou.goods.pojo.Category;
import com.changgou.goods.service.CategoryService;
import entity.Result;
import entity.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/{id}")
    public Result<Category> findById(@PathVariable("id") Integer id){

        return ResultUtil.findSuccess(categoryService.findById(id));

    }

    @GetMapping("/list/{pid}")
    public Result<List<Category>> findByParentId(@PathVariable("pid") Integer pid){

        return ResultUtil.findSuccess(categoryService.findByParentId(pid));
    }



}
