package com.tian.test;

/**
 * ������������������
 * �͵ڶ���һ��
 * �̰߳�ȫ
 * ��Ч��̫�ͣ���ͬ�����еģ��¸��߳���Ҫȡ�ö��󣬾ͱ���Ҫ����һ���߳��ͷţ��ſ��Լ���ִ��
 * @author tian
 *
 */
public class SingletonLazy3 {

	private static SingletonLazy3 singletonLazy3 = null;
	
	private SingletonLazy3(){
		
	}
	
	public static SingletonLazy3 getInstance(){
		synchronized (SingletonLazy3.class) {
			if(null==singletonLazy3){
				try{
					Thread.sleep(1000);
				}catch (Exception e) {
				}
				singletonLazy3 = new SingletonLazy3();
			}
			return singletonLazy3;
		}
	}
}
