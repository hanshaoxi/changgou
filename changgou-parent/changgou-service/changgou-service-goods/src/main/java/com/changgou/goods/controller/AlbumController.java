package com.changgou.goods.controller;

import com.changgou.goods.pojo.Album;
import com.changgou.goods.service.AlbumService;
import com.github.pagehelper.PageInfo;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/album")
public class AlbumController {
    @Autowired
    private AlbumService albumService;

    @GetMapping("/{id}")
    public Result<Album> findById(@PathVariable("id") Long id){
        Album album = albumService.findById(id);
        return  new Result<Album>(true, StatusCode.OK,"查询成功",album);
    }
    @GetMapping
    public Result<Album> findAll(){
        List<Album> albumList = albumService.findAll();
        return new Result<Album>(true,StatusCode.OK,"查询成功",albumList);

    }

    @PostMapping
    public Result add(@RequestBody Album album){
        albumService.add(album);
        return new Result(true,StatusCode.OK,"新增成功");
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable("id") Long id,Album album){
        album.setId(id);
        albumService.update(album);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") Long id){
        albumService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }
    @GetMapping("/search/{pageNum}/{pageSize}")
    public Result<Album> findPage(@PathVariable("pageNum") Integer pageNum,@PathVariable("pageSize") Integer pageSize){

        PageInfo<Album> albumList = albumService.findPage(pageNum,pageSize);
        return new Result(true,StatusCode.OK,"查询成功",albumList);

    }
    @PostMapping("/search/{pageNum}/{pageSize}")
    public Result<Album> findPage(@PathVariable("pageNum") Integer pageNum,@PathVariable("pageSize") Integer pageSize
                                    ,@RequestBody(required = false) Album album){

        PageInfo<Album> page = albumService.findPage(album, pageNum, pageSize);
        return  new Result(true,StatusCode.OK,"查询成功",page);

    }



}
