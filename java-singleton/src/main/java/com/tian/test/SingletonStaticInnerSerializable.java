package com.tian.test;

import java.io.Serializable;

/**
 * ���л�����
 * ����ģʽ��Ȼ�ܱ�֤�̰߳�ȫ���������л��ͷ����л�������»�������ɶ����������
 * ����readResolve�������ܱ������л��������л�����ͬһ������
 * 
 * readResolve()���������Ǻη���ʥ����ʵ��JVM���ڴ��з����л��ء���װ��һ���¶���ʱ��
 * �ͻ��Զ�������� readResolve��������������ָ���õĶ�����, 
 * ��������Ҳ�͵õ��˱�֤��readResolve()�ĳ����������Ա���п���ͨ�������л��õ��Ķ���
 * @author tian
 *
 */
public class SingletonStaticInnerSerializable implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SingletonStaticInnerSerializable(){
		
	}
	private static class SingletonInner{
		private static SingletonStaticInnerSerializable singletonStaticInnerSerializable = new SingletonStaticInnerSerializable();
	}
	
	public static SingletonStaticInnerSerializable getInstance(){
		return SingletonInner.singletonStaticInnerSerializable;
	}
	
	/**
	 * ��֤���л��������л�����ͬһ������
	 * @return
	 */
	protected Object readResolve(){
		System.out.println("������readResolve");
		return SingletonInner.singletonStaticInnerSerializable;
	}

}
