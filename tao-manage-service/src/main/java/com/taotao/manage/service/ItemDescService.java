package com.taotao.manage.service;

import org.springframework.stereotype.Service;

import com.taotao.manage.pojo.ItemDesc;

@Service
public class ItemDescService extends BaseService<ItemDesc>{
	
	/**
	 * 根据ID查询商品描述信息
	 * @param itemId
	 * @return
	 */
	public ItemDesc queryItemDescByItemId(Long itemId) {
		return queryById(itemId);
	}
	
}
