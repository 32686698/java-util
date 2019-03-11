package com.tian.test;

/**
 * ��������������
 * �̰߳�ȫ
 * ͨ������ͬ������飬ʹ��DCL˫���������
 * ʹ��˫��������Ƴɹ��Ľ���˵���ģʽ������ʵ�ֵ��̲߳���ȫ�����Ч������
 * DCL Ҳ�Ǵ�������߳̽�ϵ���ģʽʹ�õĽ������
 * @author tian
 *
 */
public class SingletonLazy5 {
	
	private static SingletonLazy5 singletonLazy5 ;
	
	private SingletonLazy5(){
		
	}
	
	public static SingletonLazy5 getInstance(){
		if(null==singletonLazy5){
			try{
				Thread.sleep(1000);
			}catch (Exception e) {
			}
			synchronized (SingletonLazy5.class) {
				if(null==singletonLazy5){
					singletonLazy5 = new SingletonLazy5();
				}
			}
		}
		return singletonLazy5;
	}

}
