package com.taotao.manage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.abel533.entity.Example;
import com.github.abel533.mapper.Mapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

public abstract class BaseService<T> {
	
	@Autowired
	public Mapper<T> mapper;
	
	/**
	 * 根据ID查询返回一个对象
	 * @param id
	 * @return
	 */
	public T queryById(Long id){
		return mapper.selectByPrimaryKey(id);
	}
	
	/**
	 * 查询所有数据
	 * @return
	 */
	public List<T> queryAll(){
		return mapper.select(null);
	}
	
	/**
	 * 条件查询集合
	 */
	public List<T> queryByWhere(T t){
		return mapper.select(t);
	}
	
	/**
	 * 查询记录数
	 */
	public Integer queryCount(T t){
		return mapper.selectCount(t);
	}
	
	/**
	 * 条件分页查询
	 * @param t
	 * @param page
	 * @param rows
	 * @return
	 */
	public PageInfo<T> queryByPageWhere(T t,Integer page,Integer rows){
		PageHelper.startPage(page,rows);
		List<T> queryByWhere = queryByWhere(t);
		PageInfo<T> pageInfo=new PageInfo<>(queryByWhere);
		return pageInfo;
	}
	
	/**
	 * 查询一条记录
	 * @param t
	 * @return
	 */
	public T queryOne(T t){
		return mapper.selectOne(t);
	}
	
	/**
	 * 新添数据
	 */
	public Integer save(T t){
		return mapper.insert(t);
	}
	
	/**
	 * 添加非空字段
	 */
	public Integer saveSelective(T t){
		return mapper.insertSelective(t);
	}
	
	/**
	 * 根据ID更新记录
	 */
	public Integer update(T t){
		return mapper.updateByPrimaryKey(t);
	}
	
	/**
	 * 根据ID更新记录
	 */
	public Integer updateSelective(T t){
		return mapper.updateByPrimaryKeySelective(t);
	}
	
	/**
	 * 根据id删除单个字段
	 * @param id
	 * @return
	 */
	public Integer delete(Long id){
		return mapper.deleteByPrimaryKey(id);
	}
	/**
	 * 批量删除数据
	 * @param clazz
	 * @param list
	 * @return
	 */
	public Integer deleteByIds(Class<T> clazz,List<Object> list){
		Example example=new Example(clazz);
		example.createCriteria().andIn("id", list);
		return mapper.deleteByExample(example);
	}
}
