package com.taotao.web.mq.handle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.RedisService;
import com.taotao.web.service.ItemService;

@Component
public class ItemMQHandle {
	
	private static final ObjectMapper MAPPER=new ObjectMapper();
	
	@Autowired
	private RedisService redisService;
	/**
	 * 接收消息并删除redis缓存
	 * @param msg
	 */
	private void excute(String msg){
		try{
			JsonNode jsonNode=MAPPER.readTree(msg);
			redisService.del(ItemService.TAOTAO_WEB_ITEM_+jsonNode.get("itemId").asText());
			redisService.del(ItemService.TAOTAO_WEB_ITEM_DESC_+jsonNode.get("itemId").asText());
			redisService.del(ItemService.TAOTAO_WEB_ITEM_PARAM_+jsonNode.get("itemId").asText());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
