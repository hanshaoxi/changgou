package com.changgou.goods.controller;

import com.changgou.goods.pojo.Template;
import com.changgou.goods.service.TemplateService;
import com.github.pagehelper.PageInfo;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/template")
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    @GetMapping
    public Result<Template> findAll(){
        List<Template> templateList = templateService.findList(null);
        return new Result(true, StatusCode.OK,"查询成功",templateList);

    }

    @GetMapping("/{id}")
    public Result findById(@PathVariable("id") Integer id){
        return new Result(true,StatusCode.OK,"查询成功",templateService.findById(id));
    }

    @GetMapping("/search/{pageNum}/{pageSize}")
    public Result<Template> findPage(@PathVariable("pageNum") Integer pageNum ,
                                     @PathVariable("pageSize") Integer pageSize){
        PageInfo<Template> page = templateService.findByPage(pageNum,pageSize);
        return new Result<>(true,StatusCode.OK,"查询成功",page);
    }

    @PostMapping("/search/{pageNum}/{pageSize}")
    public Result findPage(@PathVariable("pageNum") Integer pageNum ,@PathVariable("pageSize") Integer pageSize,
                           @RequestBody(required = false) Template template){
        PageInfo<Template> page = templateService.findPage(template,pageNum,pageSize);
        return new Result(true,StatusCode.OK,"查询成功",page);
    }
    @PutMapping("/{id}")
    public Result update(@PathVariable("id") Integer id,Template template){
        template.setId(id);

        templateService.update(template);
        return new Result(true,StatusCode.OK,"更新成功");
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") Integer id){
        templateService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    @PostMapping
    public Result add(@RequestBody Template template){
        templateService.add(template);
        return new Result(true,StatusCode.OK,"添加成功");
    }
}
