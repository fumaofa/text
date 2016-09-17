package com.taotao.manage.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.bean.ItemCatData;
import com.taotao.common.bean.ItemCatResult;
import com.taotao.common.service.RedisService;
import com.taotao.manage.pojo.ItemCat;

@Service
public class ItemCatService extends BaseService<ItemCat> {

	private static final String REDIS_KEY = "TAOTAO_MANAGE_ITEM_CAT";

	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	@Autowired
	private RedisService redisService;

	/**
	 * 根据parentId查询商品分类
	 * 
	 * @param parentId
	 * @return
	 */
	public List<ItemCat> queryItemCatByParentId(Long id) {
		ItemCat itemCat = new ItemCat();
		itemCat.setParentId(id);
		return this.queryByWhere(itemCat);
	}

	/**
	 * http://manage.taotao.com/rest/item/cat/all菜单显示全部
	 * 
	 * @return
	 */
	public ItemCatResult queryAllToTree() {
		//查询是否存在于缓存服务器
		try{
			String jsonData = redisService.get(REDIS_KEY);
			if(StringUtils.isNotBlank(jsonData)){
				//返回命中对象
				return MAPPER.readValue(jsonData, ItemCatResult.class);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		ItemCatResult result =new ItemCatResult();
		//查出所有目录数据
		List<ItemCat> cats  = queryAll();
		//创建一个map对象
		Map<Long, List<ItemCat>> itemCatMap=new HashMap<>();
		//循环目录数据
		for (ItemCat itemCat : cats) {
			//筛选出不重复的同级目录
			if(!itemCatMap.containsKey(itemCat.getParentId())){
				//对所有同一级目录创建一个list集合来存放
				itemCatMap.put(itemCat.getParentId(), new ArrayList<ItemCat>());
			}
			//把所有同一级目录数据存放进去
			itemCatMap.get(itemCat.getParentId()).add(itemCat);
		}
		
		/**封装一级目录*/
		List<ItemCat> itemCatList1  = itemCatMap.get(0L);
		//把1级目录数据拿出来修改成封装的对象
		for (ItemCat itemCat : itemCatList1) {
			//需要封装成需要的json对象
			ItemCatData itemCatData=new ItemCatData();
			/**
			 * json需要封装成
			 * cts/1.html",
            "n": "<a href='/products/1.html'>图书、音像、电子书刊</a>",
            "i": [
                {
                    "u": "/products/2.html",
                    "n": "电子书刊",
                    "i": [
                        "/products/3.html|电子书",
                    ]
			 */
			//封装/products/1.html
			itemCatData.setUrl("/products/"+itemCat.getId()+".html");
			//封装<a href='/products/1.html'>
			itemCatData.setNname("<a href='"+itemCatData.getUrl()+"'>"+itemCat.getName()+"</a>");
			//添加进itemCat集合
			result.getItemCats().add(itemCatData);
			//判断是否为父类，是就继续，不是就结束
			if(!itemCat.getIsParent()){
				continue;
			}
			/**
			 * 封装二级目录
			 */
			List<ItemCat> itemCatList2=itemCatMap.get(itemCat.getId());
			//在创建个新的集合存放2级目录
			List<ItemCatData> itemCatData2 =new ArrayList<>();
			//把二级目录存放在一级目录集合下
			itemCatData.setItems(itemCatData2);
			//重新封装2级目录
			for (ItemCat itemCat2 : itemCatList2) {
				ItemCatData id2=new ItemCatData();
				id2.setUrl("/products/"+itemCat2.getId()+".html");
				id2.setNname(itemCat2.getName());
				itemCatData2.add(id2);
				/**
				 * 封装三级目录
				 * 判断是否为子目录
				 */
				if(itemCat2.getIsParent()){
					List<ItemCat> itemCatList3=itemCatMap.get(itemCat2.getId());
					//最高三层目录存放string类型
					List<String> itemCatData3 = new ArrayList<String>();
					//把三级目录存在二级目录下
					id2.setItems(itemCatData3);
					//重组三级目录
					for (ItemCat itemCat3 : itemCatList3) {
						itemCatData3.add("/products/" + itemCat3.getId() + ".html|"+itemCat3.getName());
					}
				}
			}
			//最多只能存放14条数据
			if(result.getItemCats().size()>=14){
				break;
			}
		}
		
		//加入缓存
		try{
			redisService.set(REDIS_KEY, MAPPER.writeValueAsString(result), 60*60*24*30);
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
}
