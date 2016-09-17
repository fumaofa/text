package com.taotao.manage.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.manage.pojo.Content;

@Service
public class ContentService extends BaseService<Content> {
	
	/**
	 * 保存广告内容
	 * @param parentId
	 * @param content
	 */
	public void savaContent(Long categoryId, Content content) {
		System.out.println(content);
		System.out.println(categoryId);
	}
	
	/**
	 * 查询指定目录下的广告位
	 * @param categoryId
	 * @param page
	 * @param rows
	 * @return
	 */
	public PageInfo<Content> queryByContent(Long categoryId, Integer page,
			Integer rows) {
		PageHelper.startPage(page, rows);
		Content content=new Content();
		content.setCategoryId(categoryId);
		List<Content> list = queryByWhere(content);
		PageInfo<Content> pageInfo=new PageInfo<Content>(list);
		return pageInfo;
	}
	
	/**
	 * 添加广告位
	 * @param content
	 */
	public void savaContent(Content content) {
		content.setCreated(new Date());
		content.setUpdated(content.getCreated());
		saveSelective(content);
	}
	
	/**
	 * 更新广告位
	 * @param content
	 */
	public void updateContent(Content content) {
		content.setUpdated(new Date());
		update(content);
	}
	
	
}
