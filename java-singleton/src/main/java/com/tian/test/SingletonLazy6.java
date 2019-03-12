package com.tian.test;

/**
 * 第六种懒汉单例
 * 线程安全
 * 双重校验 加了volatile
 * 
 * 假设没有关键字volatile的情况下，两个线程A、B，都是第一次调用该单例方法，
 * 线程A先执行 person = new Person()，该构造方法是一个非原子操作，编译后生成多条字节码指令，
 * 由于JAVA的指令重排序，可能会先执行 person 的赋值操作，该操作实际只是在内存中开辟一片存储对象的区域后直接返回内存的引用，
 * 之后 person 便不为空了，但是实际的初始化操作却还没有执行，如果就在此时线程B进入，
 * 就会看到一个不为空的但是不完整 （没有完成初始化）的 Person对象，所以需要加入volatile关键字，
 * 禁止指令重排序优化，从而安全的实现单例。
 * @author tian
 *
 */
public class SingletonLazy6 {
	
	private static volatile SingletonLazy6 singletonLazy6;
	
	private SingletonLazy6(){
		
	}
	
	public static SingletonLazy6 getInstance(){
		if(null==singletonLazy6){
			synchronized (SingletonLazy6.class) {
				if(null==singletonLazy6){
					singletonLazy6 = new SingletonLazy6();
				}
			}
		}
		return singletonLazy6;
	}

}
