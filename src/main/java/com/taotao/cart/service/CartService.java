package com.taotao.cart.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.abel533.entity.Example;
import com.taotao.cart.mapper.CartMapper;
import com.taotao.cart.pojo.Cart;

@Service
public class CartService {
	
	@Autowired
	private CartMapper cartMapper;
	/**
	 * 添加购物车
	 * @param cart
	 * @return
	 */
	public Boolean saveItemToCart(Cart cart) {
		cart.setCreated(new Date());
		cart.setUpdated(cart.getCreated());
		cart.setId(null);
		
		Cart cart2=new Cart();
		cart2.setUserId(cart2.getUserId());
		cart2.setItemId(cart.getItemId());
		Cart selectOne = cartMapper.selectOne(cart2);
		
		if(selectOne==null){
			//如果购物车为空就添加
			cartMapper.insertSelective(cart);
			return true;
		}else{
			//如果购物车不为空就重新更新数量
			selectOne.setNum(cart.getNum());
			selectOne.setUpdated(cart.getCreated());
			cartMapper.updateByPrimaryKey(selectOne);
			return false;
		}
	}
	
	/**
	 * 根据userId用户id查询购物车
	 * @param 
	 * @return
	 */
	public List<Cart> queryCartListByUserId(Long userId) {
		Cart cart=new Cart();
		cart.setUserId(userId);
		return cartMapper.select(cart);
	}
	
	/**
	 * 更新购物车
	 * @param userId
	 * @param itemId
	 * @param num
	 * @return
	 */
	public Boolean updateCart(Long userId, Long itemId, Integer num) {
		Cart cart=new Cart();
		cart.setUserId(userId);
		cart.setItemId(itemId);
		Cart selectOne = cartMapper.selectOne(cart);
		if(selectOne==null){
			return false;
		}
		selectOne.setNum(num);
		selectOne.setUpdated(new Date());
		cartMapper.updateByPrimaryKeySelective(selectOne);
		return true;
	}
	
	/**
	 * 删除购物车物品
	 * @param userId
	 * @param itemId
	 * @return
	 */
	public void deleteCart(Long userId, Long itemId) {
		Example example=new Example(Cart.class);
		example.createCriteria().andEqualTo("userId", userId).andEqualTo("itemId", itemId);
		cartMapper.deleteByExample(example);
	}

}
