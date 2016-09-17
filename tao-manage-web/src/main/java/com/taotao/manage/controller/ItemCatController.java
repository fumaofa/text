package com.taotao.manage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.bean.ItemCatResult;
import com.taotao.manage.pojo.ItemCat;
import com.taotao.manage.service.ItemCatService;

@RequestMapping("item/cat")
@Controller
public class ItemCatController {

	@Autowired
	private ItemCatService itemCatService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<ItemCat>> queryItemCatByParentId(
			@RequestParam(value = "id", defaultValue = "0") Long parentId) {
		try {
			List<ItemCat> list = itemCatService.queryItemCatByParentId(parentId);
			System.out.println(list);
			return ResponseEntity.status(HttpStatus.OK).body(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	/**
	 * http://manage.taotao.com/rest/item/cat/all菜单显示全部
	 * @return
	 */
	@RequestMapping(value="all",method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ItemCatResult> queryAll(){
		try {
			ItemCatResult itemCatResult=itemCatService.queryAllToTree();
			return ResponseEntity.status(HttpStatus.OK).body(itemCatResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
}
