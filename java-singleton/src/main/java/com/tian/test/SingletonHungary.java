package com.tian.test;

/**
 * 饿汉单例 
 * 占用资源
 * 这种方式适合占用资源少，在初始化的时候就会被用到的类
 * @author tian
 *
 */
public class SingletonHungary {
	private static SingletonHungary singletonHungary = new SingletonHungary();
	
	/**
	 * 将构造器设置为private禁止通过new进行实例化
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
