package com.taotao.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.bean.HttpResult;
import com.taotao.common.service.ApiService;
import com.taotao.web.pojo.Cart;
import com.taotao.web.pojo.Item;
import com.taotao.web.pojo.User;

@Service
public class CartService {

	@Autowired
	private ApiService apiService;

	@Autowired
	private ItemService itemService;

	@Value("${CART_TAOTAO_URL}")
	private String CART_TAOTAO_URL;

	private static final ObjectMapper MAPPER = new ObjectMapper();

	/**
	 * 添加到购物车
	 * 
	 * @param itemId
	 * @param id
	 */
	public Boolean addItemToCart(Long itemId, Long id) {
		String url = CART_TAOTAO_URL + "/rest/cart";
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId", id);
		param.put("itemId", itemId);
		Item item = itemService.getItemByItemId(itemId);
		param.put("itemTitle", item.getTitle());
		String[] images = item.getImages();
		if (images == null) {
			param.put("itemImage", "");
		} else {
			param.put("itemImage", images[0]);
		}
		param.put("itemPrice", item.getPrice());
		param.put("num", 1);
		try {
			HttpResult doPost = apiService.doPost(url, param);
			if (doPost.getCode() == 201 || doPost.getCode() == 204) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 查询购物车
	 * @return
	 */
	public List<Cart> queryCartList(User user) {
		String url = CART_TAOTAO_URL + "/rest/cart/" + user.getId();
		try {
			String doGet = apiService.doGet(url, null);
			if (doGet == null) {
				return null;
			}
			return MAPPER.readValue(doGet, MAPPER.getTypeFactory()
					.constructCollectionType(List.class, Cart.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 修改购物车
	 * 
	 * @param id
	 * @param itemId
	 * @param num
	 */
	public Boolean updateCartItemNum(Long id, Long itemId, Integer num) {
		String url = CART_TAOTAO_URL + "/rest/cart/" + id + "/" + itemId + "/"
				+ num;
		try {
			HttpResult doPut = apiService.doPut(url, null);
			if (doPut.getCode() == 204) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 删除购物车
	 * 
	 * @param itemId
	 * @param id
	 */
	public Boolean deleteFromCart(Long itemId, Long id) {
		String url = CART_TAOTAO_URL + "/rest/cart/" + id + "/" + itemId;
		try {
			HttpResult doDelete = apiService.doDelete(url, null);
			if (doDelete.getCode() == 204) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
