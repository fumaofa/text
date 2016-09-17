package com.taotao.cart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.cart.pojo.Cart;
import com.taotao.cart.service.CartService;

@RequestMapping("cart")
@Controller
public class CartController {
	@Autowired
	private CartService cartService;

	/**
	 * 保存购物车
	 * 
	 * @param cart
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> saveItemToCart(Cart cart) {
		try {
			Boolean bool = cartService.saveItemToCart(cart);
			if (bool) {
				return ResponseEntity.status(HttpStatus.CREATED).build();
			} else {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	/**
	 * 根据用户id查询购物车
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "{userId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Cart>> queryCartListByUserId(
			@PathVariable("userId") Long userId) {
		try {
			List<Cart> carts = cartService.queryCartListByUserId(userId);
			return ResponseEntity.status(HttpStatus.OK).body(carts);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
				null);
	}

	/**
	 * 更新购物车
	 * 
	 * @param userId
	 * @param itemId
	 * @param num
	 * @return
	 */
	@RequestMapping(value = "{userId}/{itemId}/{num}", method = RequestMethod.PUT)
	public ResponseEntity<Void> updateCart(@PathVariable("userId") Long userId,
			@PathVariable("itemId") Long itemId,
			@PathVariable("num") Integer num) {
		try {
			Boolean bool = cartService.updateCart(userId, itemId, num);
			if (bool) {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	/**
	 * 删除购物车物品
	 * @param userId
	 * @param itemId
	 * @return
	 */
	@RequestMapping(value = "{userId}/{itemId}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteCart(@PathVariable("userId") Long userId,
			@PathVariable("itemId") Long itemId) {	
		try{
			cartService.deleteCart(userId,itemId);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}catch(Exception e){
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
}
