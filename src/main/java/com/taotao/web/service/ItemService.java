package com.taotao.web.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.ApiService;
import com.taotao.common.service.RedisService;
import com.taotao.web.pojo.Item;
import com.taotao.web.pojo.ItemDesc;
import com.taotao.web.pojo.ItemParamItem;

@Service
public class ItemService {

	@Autowired
	private ApiService apiService;

	private static final ObjectMapper MAPPER = new ObjectMapper();

	public static final String TAOTAO_WEB_ITEM_ = "TAOTAO_WEB_ITEM_";
	// 存放商品描述信息
	public static final String TAOTAO_WEB_ITEM_DESC_ = "TAOTAO_WEB_ITEM_DESC";
	// 存放商品规格参数
	public static final String TAOTAO_WEB_ITEM_PARAM_ = "TAOTAO_WEB_ITEM_PARAM";

	@Autowired
	private RedisService redisService;

	@Value("${MANAGE_TAOTAO_URL}")
	private String MANAGE_TAOTAO_URL;

	/**
	 * 根据id查询商品详情
	 * 
	 * @param itemId
	 * @return
	 */
	public Item getItemByItemId(Long itemId) {
		// 查询缓存是否存在
		try {
			String item = redisService.get(TAOTAO_WEB_ITEM_+itemId);
			if (StringUtils.isNotBlank(item)) {
				return MAPPER.readValue(item, Item.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		String url = MANAGE_TAOTAO_URL + "/rest/item/" + itemId;
		try {
			String jsonData = apiService.doGet(url, null);
			if(jsonData==null){
				return null;
			}
			// 不存在放入缓存服务器
			try {
				redisService.set(TAOTAO_WEB_ITEM_+itemId, jsonData, 3600);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return MAPPER.readValue(jsonData, Item.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据id查询商品描述信息
	 */
	public Object getItemDescByItemId(Long itemId) {
		// 查询是否存在缓存服务器
		try {
			String itemDesc = redisService.get(TAOTAO_WEB_ITEM_DESC_+itemId);
			if (StringUtils.isNotBlank(itemDesc)) {
				return MAPPER.readValue(itemDesc, ItemDesc.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String url = MANAGE_TAOTAO_URL + "/rest/item/desc/" + itemId;
		try {
			String doGet = apiService.doGet(url, null);
				
			if(doGet==null){
				return null;
			}
			// 不存在就放入缓存服务器
			try {
				redisService
						.set(TAOTAO_WEB_ITEM_DESC_+itemId, doGet, 60 * 60 * 24 * 30);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return MAPPER.readValue(doGet, ItemDesc.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据id查询商品规格并封装成html
	 */
	public String getItemParamHtml(Long itemId) {
		// 查询是否存在缓存服务器
		try {
			String itemParam = redisService.get(TAOTAO_WEB_ITEM_PARAM_+itemId);
			if (StringUtils.isNotBlank(itemParam)) {
				return itemParam;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String url = MANAGE_TAOTAO_URL + "/rest/item/param/item/" + itemId;
		try {
			String doGet = apiService.doGet(url, null);
			if (!StringUtils.isNotBlank(doGet)) {
				return null;
			}
			ItemParamItem paramItem = MAPPER.readValue(doGet,ItemParamItem.class);
			// 解析json
			String paramData = paramItem.getParamData();
			JsonNode jsonNode = MAPPER.readTree(paramData);

			// 定义一个存放重组代码
			StringBuilder sb = new StringBuilder();
			sb.append("<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"0\" class=\"Ptable\"><tbody>");
			for (JsonNode node : jsonNode) {
				sb.append("<tr><th class=\"tdTitle\" colspan=\"2\">"
						+ node.get("group").asText() + "</th></tr>");
				JsonNode jsonNode2 = node.get("params");
				for (JsonNode node2 : jsonNode2) {
					sb.append("<tr><td class=\"tdTitle\">"
							+ node2.get("k").asText() + "</td><td>"
							+ node2.get("v") + "</td></tr>");
				}
			}
			sb.append("</tbody></table>");
			
			// 放入缓存服务器
			try {
				redisService.set(TAOTAO_WEB_ITEM_PARAM_+itemId, sb.toString(), 60*60*24*30);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
