package com.tian.test;

/**
 * 静态内部类
 * 线程安全
 * 加载一个类时，其内部类不会同时被加载。一个类被加载，当且仅当其某个静态成员（静态域、构造器、静态方法等）被调用时发生
 * 这种方式是实现单例模式的最优解
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


