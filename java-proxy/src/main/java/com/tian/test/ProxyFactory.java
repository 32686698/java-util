package com.tian.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 动态代理
 * @author tian
 *
 */
public class ProxyFactory {
	
	private Object target;
	public ProxyFactory(Object target) {
		this.target = target;
	}
	
	/**
	 * 给目标对象生成代理对象
	 * @return
	 */
	public Object getProxyInstance(){
		return Proxy.newProxyInstance(
				target.getClass().getClassLoader(), 
				target.getClass().getInterfaces(), 
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						System.out.println("--开始保存用户");
						//执行目标对象方法
						Object returnValue = method.invoke(target, args);
						System.out.println("--结束保存用户");
						return returnValue;
					}
				});
	}
	
	

}
