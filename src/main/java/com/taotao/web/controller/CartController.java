package com.taotao.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.web.pojo.Cart;
import com.taotao.web.pojo.User;
import com.taotao.web.service.CartService;
import com.taotao.web.service.CookieCartService;
import com.taotao.web.threadlocal.UserThreadLocal;

@RequestMapping("cart")
@Controller
public class CartController {

	@Autowired
	private CartService cartService;

	@Autowired
	private CookieCartService cookieCartService;

	/**
	 * 添加到购物车
	 * 
	 * @param itemId
	 * @param cookie
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "add/{itemId}", method = RequestMethod.GET)
	public String addItemToCart(@PathVariable("itemId") Long itemId,
			HttpServletRequest request, HttpServletResponse response) {
		// 查询是否存在用户
		User user = UserThreadLocal.get();
		if (user == null) {
			// 没登入修改cookie
			cookieCartService.addItemToCart(itemId, request, response);
		} else {
			// 已登入
			cartService.addItemToCart(itemId, user.getId());
		}
		return "redirect:/cart/show.html";
	}

	/**
	 * 查看购物车
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "show", method = RequestMethod.GET)
	public ModelAndView show(HttpServletRequest request) {
		User user = UserThreadLocal.get();
		List<Cart> carts = null;
		if (user == null) {
			// 没登入修改cookie
			carts = cookieCartService.queryCartList(request);
		} else {
			// 登入读取数据库
			carts = cartService.queryCartList(user);
		}
		ModelAndView mv = new ModelAndView("cart");
		mv.addObject("cartList", carts);
		return mv;
	}

	/**
	 * 修改购物车数量
	 * 
	 * @param itemId
	 * @param num
	 * @return
	 */
	@RequestMapping(value = "update/num/{itemId}/{num}")
	public ResponseEntity<Void> updateCartItemNum(
			@PathVariable("itemId") Long itemId,
			@PathVariable("num") Integer num, HttpServletResponse response,
			HttpServletRequest request) {
		try {
			User user = UserThreadLocal.get();
			if (user == null) {
				// 没登入修改cookie
				cookieCartService.updateCartItemNum(itemId, num, request,
						response);
			} else {
				// 登入读取数据库
				cartService.updateCartItemNum(user.getId(), itemId, num);
			}
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	/**
	 * 删除购物车
	 * 
	 * @param itemId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "delete/{itemId}", method = RequestMethod.GET)
	public String deleteFromCart(@PathVariable("itemId") Long itemId,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			User user = UserThreadLocal.get();
			if (user == null) {
				// 没登入修改cookie
				cookieCartService.deleteFromCart(itemId,request,response);
			} else {
				// 登入读取数据库
				cartService.deleteFromCart(itemId, user.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/cart/show.html";
	}
}
