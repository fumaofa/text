package com.taotao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.web.service.IndexService;



@Controller
public class IndexController {
	@Autowired
	private IndexService indexService;
	
	/**
	 * 主页信息
	 * @return
	 */
	@RequestMapping(value="index",method=RequestMethod.GET)
	public ModelAndView index(){
		ModelAndView andView=new ModelAndView("index");
		andView.addObject("indexAd1", indexService.getIndexAd1());
		return andView;
	}
}
