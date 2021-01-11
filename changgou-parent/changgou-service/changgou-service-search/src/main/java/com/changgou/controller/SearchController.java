package com.changgou.controller;

import com.changgou.service.SkuService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/search")
@CrossOrigin
public class SearchController {

    @Autowired
    private SkuService skuService;

    @GetMapping("/import/data")
    public Result importData(){
        skuService.importSku();
        return new Result(true, StatusCode.OK,"导入成功");

    }
    @GetMapping
    public Map search(@RequestParam(required = false) Map<String,String> map){
        Map resultMap = skuService.search(map);
        System.out.println(resultMap);
        return resultMap;
    }

}
