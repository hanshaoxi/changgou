package com.changgou.goods.service.impl;

import com.changgou.goods.mapper.AlbumMapper;
import com.changgou.goods.pojo.Album;
import com.changgou.goods.service.AlbumService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 相册管理
 */
@Service
public class AlbumServiceImpl implements AlbumService {

    @Autowired
    private AlbumMapper albumMapper;


    @Override
    public List<Album> findAll() {
        return albumMapper.selectAll();
    }

    @Override
    public Album findById(Long id) {
        return albumMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo<Album> findPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return new PageInfo<Album>(albumMapper.selectAll());
    }

    @Override
    public PageInfo<Album> findPage(Album album, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return new PageInfo<Album>(albumMapper.select(album));
    }

    @Override
    public void add(Album album) {
        albumMapper.insertSelective(album);
    }

    @Override
    public void delete(Album album) {
        albumMapper.delete(album);
    }

    @Override
    public void update(Album album) {
        albumMapper.updateByPrimaryKeySelective(album);
    }

    @Override
    public void delete(Long id) {
        albumMapper.deleteByPrimaryKey(id);
    }
}
