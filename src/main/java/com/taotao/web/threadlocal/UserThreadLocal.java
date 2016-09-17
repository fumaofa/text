package com.taotao.web.threadlocal;

import com.taotao.web.pojo.User;

public class UserThreadLocal {

    //存放User对象在线程中
    private static ThreadLocal<User> USER = new ThreadLocal<User>();
    
    /**
     * 装User对象放入ThreadLocal中
     */
    public static void set(User user){
        USER.set(user);
    }
    
     /**
      * 从ThreadLocal中取出User对象
      */
    public static User get(){
        return USER.get();
    }
}
