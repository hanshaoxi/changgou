package com.changgou.goods.controller;

import com.changgou.goods.pojo.Category;
import com.changgou.goods.pojo.Spec;
import com.changgou.goods.service.CategoryService;
import com.changgou.goods.service.SpecService;
import entity.Result;
import entity.ResultUtil;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/spec")
public class SpecController {
    @Autowired
    private SpecService specService;
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public Result add(@RequestBody Spec spec){
        specService.add(spec);
        return  new Result(true, StatusCode.OK,"添加成功");
    }

    @GetMapping("/{id}")
    public Result<Spec> findById(@PathVariable(value="id") Integer id){

        Spec spec = specService.findById(id);
        return new Result(true,StatusCode.OK,"查询成功",spec);
    }

    /**
     * 分页查询规格信息
     * @param pageNum
     * @param pageSize
     * @return
     */
    @PostMapping("/{pageNum}/{pageSize}")
    public Result<List<Spec>> findPage(@PathVariable(value="pageNum") Integer pageNum ,
                                       @PathVariable(value="pageSize") Integer pageSize){
        List<Spec> specList = specService.findPage(pageSize,pageNum);

        return new Result(true,StatusCode.OK,"查询成功",specList);

    }

    /**
     * 根据条件自定义查询
     * @param spec
     * @param pageNum
     * @param pageSize
     * @return
     */
    @PostMapping("/search/{pageNum}/{pageSize}")
    public Result<List<Spec>> findPage(@RequestBody Spec spec,
                                       @PathVariable(value="pageNum") Integer pageNum,
                                       @PathVariable(value="pageSize") Integer pageSize){
        List<Spec> specList = specService.findPage(spec,pageNum,pageSize);


        return new Result(true,StatusCode.OK,"查询成功",specList);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") long id){

        specService.delete(id);
        return  new Result(true,StatusCode.OK,"删除成功");
    }
    @PutMapping("/{id}")
    public Result update(@PathVariable("id") Integer id,@RequestBody Spec spec){

        spec.setId(id);
        specService.update(spec);
        return  new Result(true,StatusCode.OK,"修改成功");
    }

    @GetMapping("/category/{id}")
    public Result<List<Spec>> findByCategoryId(@PathVariable("id") Integer categoryId){
        return ResultUtil.findSuccess(specService.findByCategoryId(categoryId));
    }
}
