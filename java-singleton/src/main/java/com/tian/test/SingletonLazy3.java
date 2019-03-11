package com.tian.test;

/**
 * 第三种懒汉单例测试
 * 和第二种一样
 * 线程安全
 * 但效率太低，是同步运行的，下个线程想要取得对象，就必须要等上一个线程释放，才可以继续执行
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
