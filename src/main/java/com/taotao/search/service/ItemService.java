package com.taotao.search.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.ApiService;
import com.taotao.search.pojo.Item;

@Service
public class ItemService {
	@Autowired
	private ApiService apiService;
	
	@Value("${MANAGE_TAOTAO_URL}")
    private String MANAGE_TAOTAO_URL;
    
    private static final ObjectMapper MAPPER = new ObjectMapper(); 
    
    public Item queryItemByItemId(Long itemId){
    	String url = MANAGE_TAOTAO_URL+"/rest/item/"+itemId;
    	try{
    		String jsonDate = apiService.doGet(url, null);
    		Item item = MAPPER.readValue(jsonDate, Item.class);
    		return item;
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return null;
    }
}
