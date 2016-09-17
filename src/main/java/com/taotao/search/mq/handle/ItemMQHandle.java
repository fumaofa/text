package com.taotao.search.mq.handle;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.search.pojo.Item;
import com.taotao.search.service.ItemService;

@Component
public class ItemMQHandle {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	@Autowired
	private HttpSolrServer httpSolrServer;
	
	@Autowired
	private ItemService itemService;
	/**
	 * 接收消息并删除redis缓存
	 * 
	 * @param msg
	 */
	private void excute(String msg) {
		try {
			JsonNode readTree = MAPPER.readTree(msg);
			Long itemId = readTree.get("itemId").asLong();
			String type = readTree.get("type").asText();
			
			if(StringUtils.equals(type, "delete")){
				
				httpSolrServer.deleteById(itemId+"");
				
			}else if(StringUtils.equals(type, "sava")||StringUtils.equals(type, "update")){
				Item item = itemService.queryItemByItemId(itemId);
				httpSolrServer.addBean(item);
			}
			
			httpSolrServer.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
