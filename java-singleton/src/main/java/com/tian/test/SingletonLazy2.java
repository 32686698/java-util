package com.tian.test;

/**
 * �ڶ���������������
 * �̰߳�ȫ
 * ��Ч��̫�ͣ���ͬ�����еģ��¸��߳���Ҫȡ�ö��󣬾ͱ���Ҫ����һ���߳��ͷţ��ſ��Լ���ִ��
 * @author tian
 *
 */
public class SingletonLazy2 {
	
	private static SingletonLazy2 singletonLazy2  = null;
	
	private SingletonLazy2(){
	}
	
	public synchronized static SingletonLazy2 getInstance(){
		if(null==singletonLazy2){
			try{
				Thread.sleep(1000);
			}catch (Exception e) {
			}
			singletonLazy2 = new SingletonLazy2();
		}
		return singletonLazy2;
	}

}
