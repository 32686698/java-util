package com.tian.test;

/**
 * 第四种懒汉单例
 * 线程不安全
 * 为什么呢？我们假设有两个线程A和B同时走到了‘代码1’，因为此时对象还是空的，所以都能进到方法里面，
 * 线程A首先抢到锁，创建了对象。释放锁后线程B拿到了锁也会走到‘代码2’，也创建了一个对象，因此多线程环境下就不能保证单例了。
 * @author tian
 *
 */
public class SingletonLazy4 {

	private static SingletonLazy4 singletonLazy4;
	
	private SingletonLazy4(){
	}
	
	public static SingletonLazy4 getInstance(){
		if(null==singletonLazy4){
			try{
				Thread.sleep(1000);
			}catch (Exception e) {
			}
			synchronized (SingletonLazy4.class) {
				singletonLazy4 = new SingletonLazy4();
			}
		}
		return singletonLazy4;
	}
}
