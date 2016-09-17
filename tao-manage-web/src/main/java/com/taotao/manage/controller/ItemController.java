package com.taotao.manage.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.service.ItemService;

@RequestMapping("item")
@Controller
public class ItemController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ItemController.class);

	@Autowired
	private ItemService itemService;

	/**
	 * 新增商品
	 * 
	 * @param item
	 * @param desc
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Void> savaItem(Item item,
			@RequestParam("desc") String desc,@RequestParam("itemParams") String itemParams) {
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("执行新增商品，item ={} ,desc ={} ", item, desc);
			}
			itemService.saveItem(item, desc,itemParams);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("新增商品成功，item ={} ,desc ={} ", item.getId(), desc);
			}
			return ResponseEntity.status(HttpStatus.CREATED).body(null);
		} catch (Exception e) {
			LOGGER.error("新增商品失败！itemId=" + item.getId() + e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
				null);
	}

	/**
	 * 查询商品
	 * 
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<EasyUIResult> queryItemListByPage(
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "rows") Integer rows) {
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("查询商品，page ={} ,rows ={} ", page, rows);
			}

			PageInfo<Item> pageInfo = itemService.queryItemListByPage(page,
					rows);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("查询商品成功，item ={} ,desc ={} ", pageInfo.getTotal());
			}

			return ResponseEntity.status(HttpStatus.OK).body(
					new EasyUIResult(pageInfo.getTotal(), pageInfo.getList()));
		} catch (Exception e) {
			LOGGER.error("新增商品失败!" + e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
				null);
	}

	/**
	 * 更新商品
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<Void> updateItemDesc(Item item,
			@RequestParam("desc") String desc,@RequestParam("itemParams") String itemParams, @RequestParam("itemParamId") Long itemParamId) {
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("执行更新商品 ，item ={} ,desc ={} ", item, desc);
			}
			itemService.updateItemDesc(item, desc,itemParams, itemParamId);

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("更新商品成功，item ={} ,desc ={} ", item.getId(), desc);
			}
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		} catch (Exception e) {
			LOGGER.error("更新商品 ！itemId=" + item.getId() + e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
				null);
	}

	/**
	 * 更改状态
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="/status",method = RequestMethod.POST)
	public ResponseEntity<Void> deleteItem(@RequestParam("ids") Long[] ids,
			@RequestParam("status") Integer status) {
		try {
			itemService.deleteItems(ids,status);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	/**
	 * 写一个接口，根据itemId查询商品
	 */
	@RequestMapping(value="{itemId}",method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Item> queryItemByItemId(@PathVariable("itemId")Long itemId){
		try{
			Item item=itemService.queryItemByItemId(itemId);
			return ResponseEntity.status(HttpStatus.OK).body(item);
		}catch(Exception e){
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
}
