package com.tian.test;

/**
 * ��̬�ڲ���
 * �̰߳�ȫ
 * ����һ����ʱ�����ڲ��಻��ͬʱ�����ء�һ���౻���أ����ҽ�����ĳ����̬��Ա����̬�򡢹���������̬�����ȣ�������ʱ����
 * ���ַ�ʽ��ʵ�ֵ���ģʽ�����Ž�
 * @author tian
 *
 */
public class SingletonStaticInner {
	private SingletonStaticInner(){
		
	}
	
	private static class SingletonInner{
		private static SingletonStaticInner singletonStaticInner = new SingletonStaticInner();
	}
	
	public static SingletonStaticInner getInstance(){
		try{
			Thread.sleep(1000);
		}catch (Exception e) {
		}
		return SingletonInner.singletonStaticInner;
	}

}


