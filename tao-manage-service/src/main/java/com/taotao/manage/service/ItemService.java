package com.taotao.manage.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.manage.mapper.ItemDescMapper;
import com.taotao.manage.mapper.ItemMapper;
import com.taotao.manage.mapper.ItemParamItemMapper;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.pojo.ItemParamItem;

@Service
public class ItemService extends BaseService<Item> {
	private static final ObjectMapper MAPPER = new ObjectMapper();

	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private ItemDescMapper itemDescMapper;
	@Autowired
	private ItemParamItemMapper paramItemMapper;

	@Autowired
	private RabbitTemplate template;

	/**
	 * 添加商品
	 * 
	 * @param item
	 */
	public void saveItem(Item item, String desc, String itemParams) {
		item.setCreated(new Date());
		item.setUpdated(item.getCreated());
		item.setStatus(1);
		save(item);

		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(itemDesc.getCreated());
		itemDesc.setItemId(item.getId());
		itemDesc.setItemDesc(desc);
		itemDescMapper.insert(itemDesc);

		// 保存规格参数数据
		ItemParamItem itemParamItem = new ItemParamItem();
		itemParamItem.setId(null);
		itemParamItem.setCreated(new Date());
		itemParamItem.setUpdated(itemParamItem.getCreated());
		itemParamItem.setParamData(itemParams);
		itemParamItem.setItemId(item.getId());
		paramItemMapper.insertSelective(itemParamItem);

		// 发送消息通知
		sentMQ("sava", item.getId());
	}

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param rows
	 * @return
	 */
	public PageInfo<Item> queryItemListByPage(Integer page, Integer rows) {
		PageHelper.startPage(page, rows);
		Example example = new Example(Item.class);
		example.setOrderByClause(" updated DESC ");
		List<Item> list = itemMapper.selectByExample(example);
		PageInfo<Item> pageInfo = new PageInfo<Item>(list);
		return pageInfo;
	}

	/**
	 * 更新商品成功
	 * @param item
	 * @param desc
	 */
	public void updateItemDesc(Item item, String desc, String itemParams,
			Long itemParamId) {
		item.setUpdated(new Date());
		updateSelective(item);

		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(item.getId());
		itemDesc.setUpdated(new Date());
		itemDesc.setItemDesc(desc);
		itemDescMapper.updateByPrimaryKeySelective(itemDesc);

		// 更新规格参数数据
		ItemParamItem itemParamItem = new ItemParamItem();
		itemParamItem.setId(itemParamId);
		itemParamItem.setParamData(itemParams);
		itemParamItem.setUpdated(new Date());
		paramItemMapper.updateByPrimaryKeySelective(itemParamItem);

		// 发送消息通知
		sentMQ("update", item.getId());
	}

	/**
	 * 根据主键查询商品信息
	 * 
	 * @param itemId
	 * @return
	 */
	public Item queryItemByItemId(Long itemId) {
		Item item = super.queryById(itemId);
		return item;
	}

	/**
	 * 删除商品
	 * 
	 * @param ids
	 */
	public void deleteItems(Long[] ids, Integer status) {
		for (int i = 0; i < ids.length; i++) {
			Item item = queryById(ids[i]);
			item.setStatus(status);
			updateSelective(item);
			
			//发送消息通知
			sentMQ("delete",ids[i]);
		}
	}

	/**
	 * 定义发送MQ消息 type 进行的操作方法 item.* itemId 要操作的对象id
	 */
	private void sentMQ(String type, Long itemId) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("type", type);
			map.put("itemId", itemId);
			template.convertAndSend("item." + type,
					MAPPER.writeValueAsString(map));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
