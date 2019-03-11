package com.tian.test.cglib;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * Cglib子类代理工厂
 * 对UserDao在内存中动态构建一个子类对象
 * @author tian
 *
 */
public class CglibProxyFactory implements MethodInterceptor{
	/*目标对象*/
	private Object target;
	
	public CglibProxyFactory(Object target) {
		this.target = target;
	}
	
	/**
	 * 给目标对象创建一个代理对象
	 * @return
	 */
	public Object getProxyInstance(){
		//1.工具类
		Enhancer en = new Enhancer();
		 //2.设置父类
		en.setSuperclass(target.getClass());
		//3.设置回调函数
		en.setCallback(this);
		//4.创建子类(代理对象)
		return en.create();
	}
	
	@Override
	public Object intercept(Object arg0, Method arg1, Object[] arg2, MethodProxy arg3) throws Throwable {
		System.out.println("开始执行方法！！！");
		Object returnValue = arg1.invoke(target, arg2);
		System.out.println("结束执行方法！！！");
		return returnValue;
	}
	

}
