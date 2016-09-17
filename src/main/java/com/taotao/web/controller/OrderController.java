package com.taotao.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.web.pojo.Order;
import com.taotao.web.pojo.User;
import com.taotao.web.service.CartService;
import com.taotao.web.service.ItemService;
import com.taotao.web.service.OrderService;
import com.taotao.web.threadlocal.UserThreadLocal;

@RequestMapping("order")
@Controller
public class OrderController {
	
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private CartService cartService;
	/**
	 * 跳转下单确认页
	 * @param itemId
	 * @return
	 */
	@RequestMapping(value="{itemId}",method=RequestMethod.GET)
	public ModelAndView toOrder(@PathVariable("itemId")Long itemId){
		ModelAndView mv=new ModelAndView("order");
		mv.addObject("item",itemService.getItemByItemId(itemId));
		return mv;
	}
	/**
	 * 提交订单
	 * @param order
	 * @param ticket
	 * @return
	 */
	 @RequestMapping(value = "submit", method = RequestMethod.POST)
	    @ResponseBody
	    public Map<String, Object> submit(Order order,@CookieValue(UserController.TT_TICKET) String ticket) {
	        //设置买家信息
	        User user = UserThreadLocal.get();
	        order.setUserId(user.getId());
	        order.setBuyerNick(user.getUsername());
	        
	        String orderNumber = this.orderService.submit(order);
	        Map<String, Object> result  = new HashMap<String, Object>();
	        if (orderNumber == null) {
	            // 创建订单失败
	            result.put("status", "500");
	        }else{
	         // 创建订单成功
	            result.put("status", "200");
	            result.put("data", orderNumber);
	        }
	        
	        return result;
	    }
	
	/**
	 * 跳转成功页
	 * @param orderId
	 * @return
	 */
    @RequestMapping(value="success",method=RequestMethod.GET)
    public ModelAndView success(@RequestParam("id") String orderId){
        ModelAndView mv = new ModelAndView("success");
        mv.addObject("order", orderService.queryOrderByOrderId(orderId));
        mv.addObject("date", new DateTime().plusDays(2).toString("MM月dd日"));
        return mv;
    }
    
    /**
     * 下单确认页
     */
    @RequestMapping(value="create",method=RequestMethod.GET)
    public ModelAndView toCartOrder(){
        ModelAndView mv = new ModelAndView("order-cart");
        mv.addObject("carts",cartService.queryCartList(UserThreadLocal.get()));
        return mv;
    }
}
