package com.tian.test;

/**
 * ��һ����������
 * �̲߳���ȫ�ĵ���
 * @author tian
 *
 */
public class SingletonLazy1 {
	
	private static SingletonLazy1 singletonLazy1 = null;
	
	private SingletonLazy1(){
	}
	
	public static SingletonLazy1 getInstance(){
		if(null == singletonLazy1){
			try{
				Thread.sleep(1000);
			}catch (Exception e) {
			}
			singletonLazy1 = new SingletonLazy1();
		}
		return singletonLazy1;
	}

}
