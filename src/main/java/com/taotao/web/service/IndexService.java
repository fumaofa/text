package com.taotao.web.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.common.service.ApiService;
import com.taotao.common.service.RedisService;
import com.taotao.web.pojo.Content;

@Service
public class IndexService {
	@Autowired
	private ApiService apiService;
	
	@Autowired
	private RedisService redisService;
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	//存在于缓存服务器的key
	private static final String REDIS_INDEX_AD1="TAOTAO_WEB_INDEX_AD1";

	@Value("${MANAGE_TAOTAO_URL}")
	private String MANAGE_TAOTAO_URL;
	@Value("${INDEX_AD1_URL}")
	private String INDEX_AD1_URL;

	public String getIndexAd1() {
		//从二级缓存中查询
		try{
			String redisId = redisService.get(REDIS_INDEX_AD1);
			if(StringUtils.isNotBlank(redisId)){
				return redisId;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		String url = MANAGE_TAOTAO_URL + INDEX_AD1_URL;
		try {
			String jsonData =apiService.doGet(url, null);
			EasyUIResult easyUIResult=new EasyUIResult().formatToList(jsonData, Content.class);
			List<Content> contents=(List<Content>) easyUIResult.getRows();
			
			List<Map<String, Object>> list=new ArrayList<>();
			for (Content content: contents) {
				/**
				 *  "srcB": "http://image.taotao.com/images/2015/03/03/2015030304360302109345.jpg",
			        "height": 240,
			        "alt": "",
			        "width": 670,
			        "src": "http://image.taotao.com/images/2015/03/03/2015030304360302109345.jpg",
			        "widthB": 550,
			        "href": "http://sale.jd.com/act/e0FMkuDhJz35CNt.html?cpdad=1DLSUE",
			        "heightB": 240
				 */
				Map<String, Object> map=new HashMap<>();
				map.put("srcB", content.getPic());
				map.put("height", 240);
				map.put("alt", content.getTitle());
				map.put("width", 670);
				map.put("src", content.getPic());
				map.put("widthB",550);
				map.put("href", content.getUrl());
				map.put("heightB", 240);
				list.add(map);
			}
			String valueAsString = MAPPER.writeValueAsString(list);
			
			//不存在就放入缓存服务器
			try{
				redisService.set(REDIS_INDEX_AD1,valueAsString, 60*60*24);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			return valueAsString;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
