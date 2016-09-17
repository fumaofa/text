package com.taotao.manage.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.taotao.manage.pojo.ContentCategory;

@Service
public class ContentCategoryService extends BaseService<ContentCategory> {
	
	/**
	 * 根据parentId查询分页
	 * @param parentId
	 * @return
	 */
	public List<ContentCategory> queryListByParentId(Long parentId) {
		ContentCategory contentCategory=new ContentCategory();
		contentCategory.setParentId(parentId);
		return queryByWhere(contentCategory);
	}
	
	/**
	 * 新增分类
	 * @param parentId
	 * @param name
	 * @return
	 */
	public ContentCategory savaContentCategory(Long parentId, String name) {
		ContentCategory contentCategory=new ContentCategory();
		contentCategory.setParentId(parentId);
		contentCategory.setName(name);
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(contentCategory.getCreated());
		contentCategory.setStatus(1);
		contentCategory.setSortOrder(1);
		contentCategory.setIsParent(false);
		
		saveSelective(contentCategory);
		
		//判断是否为父类
		ContentCategory queryContentCategory = queryById(parentId);
		if(!queryContentCategory.getIsParent()){
			queryContentCategory.setIsParent(true);
			queryContentCategory.setUpdated(new Date());
			updateSelective(queryContentCategory);
		}
		return contentCategory;
	}
	
	/**
	 * 重命名
	 * @param id
	 * @param name
	 * @return
	 */
	public ContentCategory updateContentCategory(Long id, String name) {
		ContentCategory contentCategory=new ContentCategory();
		contentCategory.setId(id);
		contentCategory.setUpdated(new Date());
		contentCategory.setName(name);
		//更新对象
		updateSelective(contentCategory);
		return contentCategory;
	}
	
	/**
	 * 级联删除
	 * @param contentCategory
	 */
	public void deleteContentCategory(ContentCategory contentCategory) {
		List<Object> deleteIds =new ArrayList<Object>();
		deleteIds.add(contentCategory.getId());
		//进入递归删除
		findAllSubNode(deleteIds,contentCategory.getId());
		//批量删除
		deleteByIds(ContentCategory.class, deleteIds);
		
		ContentCategory parent =new ContentCategory();
		parent.setParentId(contentCategory.getParentId());
		List<ContentCategory> contentCategories = queryByWhere(parent);
		//判断他是否有子目录，没有就把自己isParent设置为false
		if(contentCategories.isEmpty()){
			ContentCategory contentCategory2=new ContentCategory();
			contentCategory2.setIsParent(false);
			contentCategory2.setUpdated(new Date());
			contentCategory2.setId(contentCategory.getParentId());
			updateSelective(contentCategory2);
		}
	}
	/**
	 * 递归删除
	 * @param deleteIds
	 * @param id
	 */
	private void findAllSubNode(List<Object> deleteIds, Long parentId) {
		ContentCategory parent =new ContentCategory();
		parent.setParentId(parentId);
		//查出同父类下的所有子合集
		List<ContentCategory> contentCategories=queryByWhere(parent);
		for (ContentCategory contentCategory : contentCategories) {
			//把父级目录下的子目录id都存入删除集合
			deleteIds.add(contentCategory.getId());
			//递归
			findAllSubNode(deleteIds, contentCategory.getId());
		}
	}

}
