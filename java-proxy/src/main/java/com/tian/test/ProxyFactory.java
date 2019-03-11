package com.tian.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * ��̬����
 * @author tian
 *
 */
public class ProxyFactory {
	
	private Object target;
	public ProxyFactory(Object target) {
		this.target = target;
	}
	
	/**
	 * ��Ŀ��������ɴ������
	 * @return
	 */
	public Object getProxyInstance(){
		return Proxy.newProxyInstance(
				target.getClass().getClassLoader(), 
				target.getClass().getInterfaces(), 
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						System.out.println("--��ʼ�����û�");
						//ִ��Ŀ����󷽷�
						Object returnValue = method.invoke(target, args);
						System.out.println("--���������û�");
						return returnValue;
					}
				});
	}
	
	

}
