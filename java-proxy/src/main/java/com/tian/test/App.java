package com.tian.test;

import com.tian.test.cglib.CglibProxyFactory;

public class App {

	/**
	 * 静态代码 代理对象也要实现和目标对象同样的接口
	 */
	public void staticProxy(){
		IUserDao userDao = new UserDao();
		IUserDao userDaoProxy = new UserDaoProxy(userDao);
		userDaoProxy.addUser();
	}

	/**
	 * 动态代理
	 */
	public void dynamicProxy(){
		IUserDao userDao = new UserDao();
		IUserDao proxy = (IUserDao) new ProxyFactory(userDao).getProxyInstance();
		proxy.addUser();
		proxy.deleteUser();
	}

	public void cglibProxy(){
		com.tian.test.cglib.UserDao userDao = new com.tian.test.cglib.UserDao();
		com.tian.test.cglib.UserDao proxy = (com.tian.test.cglib.UserDao) new CglibProxyFactory(userDao).getProxyInstance();
		proxy.addUser();
		proxy.deleteUser();
	}
	public static void main(String[] args) {
		App app = new App();
		app.cglibProxy();
	}

}
