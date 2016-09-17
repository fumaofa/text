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

import com.taotao.manage.pojo.ContentCategory;
import com.taotao.manage.service.ContentCategoryService;

@RequestMapping("content/category")
@Controller
public class ContentCategoryController {

	@Autowired
	private ContentCategoryService contentCategoryService;

	/**
	 * 广告系统分页
	 * 
	 * @param parentId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<ContentCategory>> queryListByParentId(
			@RequestParam(value = "id", defaultValue = "0") Long parentId) {
		try {
			List<ContentCategory> list = contentCategoryService
					.queryListByParentId(parentId);
			return ResponseEntity.status(HttpStatus.OK).body(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
				null);
	}

	/**
	 * 新增广告类
	 * 
	 * @param parentId
	 * @param node
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<ContentCategory> savaContentCategory(
			@RequestParam("parentId") Long parentId,
			@RequestParam("name") String name) {
		try {
			ContentCategory contentCategory = contentCategoryService
					.savaContentCategory(parentId, name);
			return ResponseEntity.status(HttpStatus.CREATED).body(
					contentCategory);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
				null);
	}

	/**
	 * 重命名
	 */
	@RequestMapping(method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<ContentCategory> updateContentCategory(
			@RequestParam("id") Long id, @RequestParam("name") String name) {
		try {
			ContentCategory contentCategory=contentCategoryService.updateContentCategory(id,name);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(contentCategory);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	/**
	 * 批量删除(级联)--递归
	 */
	@RequestMapping(method=RequestMethod.DELETE)
	public ResponseEntity<Void> deleteContentCategory(ContentCategory contentCategory){
		try{
			contentCategoryService.deleteContentCategory(contentCategory);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}catch(Exception e){
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
}
