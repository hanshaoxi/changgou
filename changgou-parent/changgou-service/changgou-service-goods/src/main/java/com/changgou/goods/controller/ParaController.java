package com.changgou.goods.controller;

import com.changgou.goods.pojo.Para;
import com.changgou.goods.service.ParaService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/para")
public class ParaController {

    @Autowired
    private ParaService paraService;


    @GetMapping("/{id}")
    public Result<Para> findById(@PathVariable("id") Integer id){

        Para para = paraService.findById(id);
        return  new Result(true, StatusCode.OK,"查询成功",para);
    }

    @PostMapping
    public Result add(@RequestBody Para para){
        paraService.add(para);
        return  new Result(true,StatusCode.OK,"添加成功");
    }
    @PutMapping("/{id}")
    public Result update(@PathVariable("id") Integer id , @RequestBody Para para){

        para.setId(id);
        paraService.update(para);
        return  new Result(true,StatusCode.OK,"修改成功");
    }

    @GetMapping("/{pageNum}/{pageSize}")
    public Result<List<Para>> findPage(@PathVariable("pageNum") Integer pageNum,//分页查询
                                       @PathVariable("pageSize") Integer pageSize){

        List<Para> paraList = paraService.findPage(pageSize,pageNum);
        return new Result<List<Para>>(true,StatusCode.OK,"分页查询成功",paraList);
    }

    @PostMapping("/search/{pageNum}/{pageSize}")
    public Result<List<Para>> findPage(@RequestBody Para para ,@PathVariable("pageNum") Integer pageNum,//分页查询
                                        @PathVariable("pageSize") Integer pageSize ){

        List<Para> paraList = paraService.findPage(pageSize,pageNum,para);

        return new Result(true,StatusCode.OK,"分页查询成功",paraList);

    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") Integer id){
        paraService.delete(id);

        return new Result(true, StatusCode.OK,"删除成功");
    }



}
