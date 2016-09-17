package com.taotao.manage.controller;

import java.util.List;

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
import com.taotao.manage.pojo.ItemParam;
import com.taotao.manage.service.ItemParamService;

@RequestMapping("item/param")
@Controller
public class ItemParamController {

	@Autowired
	private ItemParamService itemParamService;

	/**
	 * 分页参数规格
	 * 
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<EasyUIResult> queryItemParamList(
			@RequestParam("page") Integer page,
			@RequestParam("rows") Integer rows) {
		try {

			PageInfo<ItemParam> pageInfo = itemParamService.queryItemParamList(
					page, rows);

			return ResponseEntity.status(HttpStatus.OK).body(
					new EasyUIResult(pageInfo.getTotal(), pageInfo.getList()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
				null);
	}

	/**
	 * 根据id查询商品规格参数列表
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value = "{itemId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ItemParam> queryItemParamByItemCatId(
			@PathVariable("itemId") Long itemCatId) {
		try {
			ItemParam itemParam = itemParamService
					.queryItemParamByItemCatId(itemCatId);
			return ResponseEntity.ok(itemParam);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
				null);
	}

	/**
	 * 新增模板
	 */
	@RequestMapping(value = "{itemCatId}", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Void> saveItemParam(
			@PathVariable("itemCatId") Long itemCatId,
			@RequestParam("paramData") String paramData) {
		try{
			itemParamService.saveItemParam(itemCatId,paramData);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	/**
	 * 删除模板
	 */
	@RequestMapping(value="delete",method=RequestMethod.DELETE)
	public ResponseEntity<Void> deleteItemParam(@RequestParam("ids")List<Object> ids){
		try{
			itemParamService.deleteByIds(ItemParam.class, ids);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}catch(Exception e){
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
}
