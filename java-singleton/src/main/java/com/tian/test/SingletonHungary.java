package com.tian.test;

/**
 * �������� 
 * ռ����Դ
 * ���ַ�ʽ�ʺ�ռ����Դ�٣��ڳ�ʼ����ʱ��ͻᱻ�õ�����
 * @author tian
 *
 */
public class SingletonHungary {
	private static SingletonHungary singletonHungary = new SingletonHungary();
	
	/**
	 * ������������Ϊprivate��ֹͨ��new����ʵ����
	 */
	private SingletonHungary(){
	}
	
	public static SingletonHungary getInstance(){
		try{
			Thread.sleep(1000);
		}catch (Exception e) {
		}
		return singletonHungary;
	}

}
