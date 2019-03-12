package com.tian.test;

/**
 * 第二种懒汉单例测试
 * 线程安全
 * 但效率太低，是同步运行的，下个线程想要取得对象，就必须要等上一个线程释放，才可以继续执行
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
