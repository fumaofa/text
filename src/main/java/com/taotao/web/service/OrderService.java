package com.taotao.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.bean.HttpResult;
import com.taotao.common.service.ApiService;
import com.taotao.web.pojo.Order;

@Service
public class OrderService {

	@Autowired
	private ApiService apiService;

	@Value("${ORDER_TAOTAO_URL}")
	private String ORDER_TAOTAO_URL;

	private static final ObjectMapper MAPPER = new ObjectMapper();

	/**
	 * 提交订单功能
	 * 
	 * @param order
	 * @return
	 */
	public String submit(Order order) {
		String url = ORDER_TAOTAO_URL + "/order/create";
		try {
            HttpResult httpResult = this.apiService.doPostJson(url, MAPPER.writeValueAsString(order));
            if (httpResult.getCode() == 200 ) {
                //响应成功
                String jsonData = httpResult.getBody();
                JsonNode jsonNode = MAPPER.readTree(jsonData);
                if (jsonNode.get("status").intValue() == 200) {
                    //创建订单成功
                    return jsonNode.get("data").asText();
                }
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 跳转成功页
	 * @param orderId
	 * @return
	 */
	public Object queryOrderByOrderId(String orderId) {
		String url = ORDER_TAOTAO_URL + "/order/query/" + orderId;
		try {
			String jsondata = this.apiService.doGet(url,null);
			if (jsondata == null) {
				return null;
			}
			return MAPPER.readValue(jsondata, Order.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
