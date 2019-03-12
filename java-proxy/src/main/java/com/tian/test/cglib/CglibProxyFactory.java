package com.tian.test.cglib;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * Cglib���������
 * ��UserDao���ڴ��ж�̬����һ���������
 * @author tian
 *
 */
public class CglibProxyFactory implements MethodInterceptor{
	/*Ŀ�����*/
	private Object target;
	
	public CglibProxyFactory(Object target) {
		this.target = target;
	}
	
	/**
	 * ��Ŀ����󴴽�һ���������
	 * @return
	 */
	public Object getProxyInstance(){
		//1.������
		Enhancer en = new Enhancer();
		 //2.���ø���
		en.setSuperclass(target.getClass());
		//3.���ûص�����
		en.setCallback(this);
		//4.��������(�������)
		return en.create();
	}
	
	@Override
	public Object intercept(Object arg0, Method arg1, Object[] arg2, MethodProxy arg3) throws Throwable {
		System.out.println("��ʼִ�з���������");
		Object returnValue = arg1.invoke(target, arg2);
		System.out.println("����ִ�з���������");
		return returnValue;
	}
	

}
