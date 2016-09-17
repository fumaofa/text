package com.taotao.web.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.utils.CookieUtils;
import com.taotao.web.pojo.Cart;
import com.taotao.web.pojo.Item;

@Service
public class CookieCartService {
	private static final String CART_COOKIE_NAME = "CART_COOKIE";

	private static final ObjectMapper MAPPER = new ObjectMapper();

	@Autowired
	private ItemService itemService;

	/**
	 * 公用取出cookie值转换成集合
	 * 
	 * @param request
	 * @return
	 */
	public List<Cart> queryCartList(HttpServletRequest request) {
		String cookieValue = CookieUtils.getCookieValue(request,
				CART_COOKIE_NAME, true);
		List<Cart> carts = new ArrayList<Cart>();
		try {
			carts = MAPPER.readValue(cookieValue, MAPPER.getTypeFactory()
					.constructCollectionType(List.class, Cart.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return carts;
	}

	/**
	 * 没登入时修改cookie值
	 * 
	 * @param itemId
	 * @param request
	 */
	public void addItemToCart(Long itemId, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			List<Cart> cookies = queryCartList(request);
			// 从cookie中找出cart对象
			Cart cart = null;
			for (Cart c : cookies) {
				if (c.getItemId().intValue() == itemId.intValue()) {
					cart = c;
					break;
				}
			}

			if (cart == null) {
				Item item = itemService.getItemByItemId(itemId);
				cart = new Cart();
				cart.setCreated(new Date());
				cart.setUpdated(cart.getCreated());
				cart.setItemId(itemId);
				cart.setItemTitle(item.getTitle());
				cart.setItemPrice(item.getPrice());
				String[] images = item.getImages();
				if (images == null) {
					cart.setItemImage("");
				} else {
					cart.setItemImage(images[0]);
				}
				cart.setNum(1);
				// 把当前的商品追加到购物车carts集合中
				cookies.add(cart);
			} else {
				// 商品在Cookie中存在，数量相加
				cart.setNum(cart.getNum() + 1);
				cart.setUpdated(new Date());
			}

			CookieUtils.setCookie(request, response, CART_COOKIE_NAME,
					MAPPER.writeValueAsString(cookies), 60 * 60, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 没登入修改cookie
	 * 
	 * @param itemId
	 * @param num
	 * @param request
	 * @param response
	 */
	public void updateCartItemNum(Long itemId, Integer num,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			List<Cart> cartList = queryCartList(request);
			for (Cart c : cartList) {
				if (c.getItemId().intValue() == itemId.intValue()) {
					c.setUpdated(new Date());
					c.setNum(num);
					break;
				}
			}
			CookieUtils.setCookie(request, response, CART_COOKIE_NAME,
					MAPPER.writeValueAsString(cartList), 60 * 60, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 没登入修改cookie
	 * 
	 * @param itemId
	 * @param request
	 * @param response
	 */
	public void deleteFromCart(Long itemId, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			List<Cart> carts = queryCartList(request);
			for (Cart c : carts) {
				if (c.getItemId().intValue() == itemId.intValue()) {
					carts.remove(c);
					break;
				}
			}
			
			CookieUtils.setCookie(request, response, CART_COOKIE_NAME,
					MAPPER.writeValueAsString(carts),60*60, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
