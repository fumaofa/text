package com.taotao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.web.service.ItemService;

@RequestMapping("item")
@Controller
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	//http://manage.taotao.com/rest/item/1258720
	@RequestMapping(value="{itemId}",method=RequestMethod.GET)
	public ModelAndView getItemByItemId(@PathVariable("itemId")Long itemId){
		ModelAndView mv = new ModelAndView("item");
		mv.addObject("item", itemService.getItemByItemId(itemId));
		mv.addObject("itemDesc", itemService.getItemDescByItemId(itemId));
		String itemParamHtml = itemService.getItemParamHtml(itemId);
		mv.addObject("itemParam", itemParamHtml);
		return mv;
	}
}
