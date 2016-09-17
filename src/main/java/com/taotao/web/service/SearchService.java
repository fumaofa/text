package com.taotao.web.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.bean.EasyUIResult;
import com.taotao.common.service.ApiService;
import com.taotao.common.service.RedisService;
import com.taotao.web.pojo.Item;

@Service
public class SearchService {
	@Autowired
	private ApiService apiService;

	public static final Integer ROWS = 36;
	
	@Autowired
	private RedisService redisService;
	
	@Value("${SEARCH_TAOTAO_URL}")
	private String SEARCH_TAOTAO_URL;

	/**
	 * 调用搜索系统接口
	 * 
	 * @param query
	 * @param page
	 * @return
	 */
	public EasyUIResult search(String query, Integer page) {
		String url = SEARCH_TAOTAO_URL + "/item/search/";
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("keyWords", query);
		param.put("page", page);
		param.put("rows", ROWS);
		try {
			String jsonData = apiService.doGet(url, param);
			return EasyUIResult.formatToList(jsonData, Item.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
