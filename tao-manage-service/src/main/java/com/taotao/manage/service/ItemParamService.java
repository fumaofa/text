package com.taotao.manage.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.manage.pojo.ItemParam;

@Service
public class ItemParamService extends BaseService<ItemParam>{
	
	/**
	 * 分页查询规格参数
	 * @param page
	 * @param rows
	 * @return
	 */
	public PageInfo<ItemParam> queryItemParamList(Integer page, Integer rows) {
		PageHelper.startPage(page, rows);
		List<ItemParam> queryAll = queryAll();
		PageInfo<ItemParam> pageInfo=new PageInfo<ItemParam>(queryAll);
		return pageInfo;
	}
	/**
	 * 根据类目id查询模板信息
	 * @param itemCatId
	 * @return
	 */
	public ItemParam queryItemParamByItemCatId(Long itemCatId) {
		ItemParam itemParam=new ItemParam();
		itemParam.setItemCatId(itemCatId);
		ItemParam queryOne=queryOne(itemParam);
		return queryOne;
	}
	/**
	 * 新增模板
	 * @param itemCatId
	 * @param paramData
	 */
	public void saveItemParam(Long itemCatId, String paramData) {
		ItemParam itemParam=new ItemParam();
		itemParam.setItemCatId(itemCatId);
		itemParam.setParamData(paramData);
		itemParam.setCreated(new Date());
		itemParam.setUpdated(itemParam.getCreated());
		
		saveSelective(itemParam);
	}

}
