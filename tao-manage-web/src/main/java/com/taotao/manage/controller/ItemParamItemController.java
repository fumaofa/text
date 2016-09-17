package com.taotao.manage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.manage.pojo.ItemParamItem;
import com.taotao.manage.service.ItemParamItemService;

@RequestMapping("item/param/item")
@Controller
public class ItemParamItemController {
	@Autowired
	private ItemParamItemService itemParamItemService;
	
	@RequestMapping(value="{itemId}",method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ItemParamItem> queryByItemParamId(@PathVariable("itemId")Long itemId){
		try{
			ItemParamItem itemParamItem=new ItemParamItem();
			itemParamItem.setItemId(itemId);
			return ResponseEntity.status(HttpStatus.OK).body(itemParamItemService.queryOne(itemParamItem));
		}catch(Exception e){
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
}
