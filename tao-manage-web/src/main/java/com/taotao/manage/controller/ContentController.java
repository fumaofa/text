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

import com.github.pagehelper.PageInfo;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.manage.pojo.Content;
import com.taotao.manage.service.ContentService;

@RequestMapping("content")
@Controller
public class ContentController {

	@Autowired
	private ContentService contentService;
	
	/**
	 * 查询指定目录下的广告位
	 * @param categoryId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<EasyUIResult> queryByContent(
			@RequestParam("categoryId") Long categoryId,
			@RequestParam("page") Integer page,
			@RequestParam("rows") Integer rows) {
		try{
			PageInfo<Content> pageInfo=contentService.queryByContent(categoryId,page,rows);
			return ResponseEntity.status(HttpStatus.OK).body(new EasyUIResult(pageInfo.getTotal(), pageInfo.getList()));
		}catch(Exception e){
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	/**
	 * 添加广告位
	 */
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Void> savaContent(Content content){
		try{
			contentService.savaContent(content);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}catch(Exception e){
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	/**
	 * 修改广告内容
	 */
	@RequestMapping(value="edit",method=RequestMethod.POST)
	public ResponseEntity<Void> updateContent(Content content){
		try{
			contentService.updateContent(content);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}catch(Exception e){
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	/**
	 * 删除广告
	 */
	@RequestMapping(value="delete",method=RequestMethod.POST)
	public ResponseEntity<Void> deleteContent(@RequestParam("ids")List<Object> ids){
		try{
			contentService.deleteByIds(Content.class, ids);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}catch(Exception e){
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
}
