package com.changgou.goods.service;

import com.changgou.goods.pojo.Album;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 相册管理的业务接口
 */
public interface AlbumService {
    /**
     * 查询所有
     * @return
     */
    public List<Album> findAll();

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public Album findById(Long id);

    /**
     * 分页查询
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<Album> findPage(Integer pageNum , Integer pageSize);

    /**
     * 根据自定义条件分页查询
     * @param album
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<Album> findPage(Album album , Integer pageNum ,Integer pageSize);

    /**
     * 添加相册
     * @param album
     */
    public void add(Album album);

    /**
     * 删除相片
     * @param album
     */
    public void delete(Album album);

    /**
     * 修改相册
     * @param album
     */
    public void update(Album album);

    /**
     * 根据id删除
     * @param id
     */
    public void delete(Long id);
}
