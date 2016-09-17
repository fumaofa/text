package com.taotao.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.bean.EasyUIResult;
import com.taotao.search.service.ItemSearchService;

@RequestMapping("item/search")
@Controller
public class ItemSearchController {

	@Autowired
	private ItemSearchService itemSearchService;
	
	/**
	 * 搜索系统的接口
	 * @param keyWords
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<EasyUIResult> search(
			@RequestParam("keyWords") String keyWords,
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "rows", defaultValue = "30") Integer rows) {
		try {
			keyWords=new String(keyWords.getBytes("ISO-8859-1"), "UTF-8");
			EasyUIResult easyUIResult=itemSearchService.search(keyWords,page,rows);
			return ResponseEntity.status(HttpStatus.OK).body(easyUIResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
				null);
	}
}
