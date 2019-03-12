package com.tian.test;

/**
 * 第五种懒汉单例
 * 线程安全
 * 通过设置同步代码块，使用DCL双检查锁机制
 * 使用双检查锁机制成功的解决了单例模式的懒汉实现的线程不安全问题和效率问题
 * DCL 也是大多数多线程结合单例模式使用的解决方案
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
