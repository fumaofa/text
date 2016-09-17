package com.taotao.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.bean.EasyUIResult;
import com.taotao.web.pojo.Item;
import com.taotao.web.service.SearchService;

@Controller
public class SearchController {
	@Autowired
	private SearchService searchService;
	
	@RequestMapping(method=RequestMethod.GET,params="q")
	public ModelAndView search(@RequestParam("q") String query,
			@RequestParam(value = "page", defaultValue = "1") Integer page) {
		try{
			query=new String(query.getBytes("iso-8859-1"),"utf-8");
		}catch(Exception e){
			e.printStackTrace();
		}
		ModelAndView mv=new ModelAndView("search");
		
		EasyUIResult easyUIResult=searchService.search(query,page);
		mv.addObject("query",query);
		
		List<Item> items=(List<Item>)easyUIResult.getRows();
		
		mv.addObject("itemList",items);
		mv.addObject("page",page);
		//总页数
		Integer total=easyUIResult.getTotal();
		mv.addObject("pages",(total+SearchService.ROWS-1)/SearchService.ROWS);
		return mv;
	}
}
