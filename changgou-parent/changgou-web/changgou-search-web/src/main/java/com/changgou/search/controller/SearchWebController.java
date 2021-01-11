package com.changgou.search.controller;

import com.changgou.search.feign.SkuFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/search")
public class SearchWebController {
    @Autowired
    private SkuFeign skuFeign;


    @GetMapping("/list")
    public String list(@RequestParam Map<String,String> searchMap, Model model){
        Map dataMap = skuFeign.search(searchMap);
        model.addAttribute("result",dataMap);
        model.addAttribute("searchMap",searchMap);
        return "search";
    }
}
