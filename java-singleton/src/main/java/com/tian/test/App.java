package com.tian.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 单例
 * 饿汉：线程安全，浪费资源
 * 懒汉1：线程不安全，节省资源
 * 懒汉2：线程安全，性能差
 * 懒汉3：懒汉2的变种，线程安全，性能差
 * 懒汉4：线程不安全
 * 懒汉5：线程安全，性能问题得到解决，第一次加载时反应不快，由于java内存模型一些原因偶尔失败
 * 懒汉6：线程安全，比懒汉5多了volatile关键字，推荐使用
 * 静态内部类：线程安全，节省资源，性能好，推荐使用
 * 枚举：纯种安全，节省资源，性能好，序列化后也能保持单例，推荐
 * @author tian
 *
 */
public class App {
	
	/**
	 * 饿汉单例测试
	 */
	public static void hungary(){
		
	}
	
	public static void main(String[] args) {
		for(int i = 0;i < 10 ; i ++){
			Thread th = new ThreadEnum();
			th.start();
		}
		//App.singletonSerialize();
	}
	
	public static void singletonSerialize(){
		try{
			SingletonStaticInnerSerializable ssis = SingletonStaticInnerSerializable.getInstance();
			System.out.println(ssis.hashCode());
			//序列化
			FileOutputStream fo = new FileOutputStream("tem");
			ObjectOutputStream oo = new ObjectOutputStream(fo);
			oo.writeObject(ssis);
			oo.close();
			fo.close();
			//反序列化
			FileInputStream fi = new FileInputStream("tem");
			ObjectInputStream oi = new ObjectInputStream(fi);
			SingletonStaticInnerSerializable ssis2 = (SingletonStaticInnerSerializable) oi.readObject();
			oi.close();
			fi.close();
			System.out.println(ssis2.hashCode());
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
}
/**
 * 饿汉单例多线程测试
 * 线程安全
 * @author tian
 *
 */
class ThreadHungary extends Thread{
	@Override
	public void run() {
		System.out.println(SingletonHungary.getInstance().hashCode());
	}
}
/**
 * 第一种懒汉单例测试
 * 线程不安全
 * @author tian
 *
 */
class ThreadLazy1 extends Thread{
	@Override
	public void run() {
		System.out.println(SingletonLazy1.getInstance().hashCode());
	}
}
/**
 * 第二种懒汉单例测试
 * 线程安全
 * @author tian
 *
 */
class ThreadLazy2 extends Thread{
	@Override
	public void run() {
		System.out.println(SingletonLazy2.getInstance().hashCode());
	}
}
/**
 * 第三种懒汉单例测试 和第二种一样的效率
 * 线程安全
 * @author tian
 *
 */
class ThreadLazy3 extends Thread{
	@Override
	public void run() {
		System.out.println(SingletonLazy3.getInstance().hashCode());
	}
}
/**
 * 第四种懒汉单例测试
 * 线程不安全
 * @author tian
 *
 */
class ThreadLazy4 extends Thread{
	@Override
	public void run() {
		System.out.println(SingletonLazy4.getInstance().hashCode());
	}
}
/**
 * 第五种懒汉单例测试
 * 线程安全
 * @author tian
 *
 */
class ThreadLazy5 extends Thread{
	@Override
	public void run() {
		System.out.println(SingletonLazy5.getInstance().hashCode());
	}
}
/**
 * 第六种懒汉单例测试
 * 线程安全
 * 最为安全可靠的懒汉加载方式
 * @author tian
 *
 */
class ThreadLazy6 extends Thread{
	@Override
	public void run() {
		System.out.println(SingletonLazy6.getInstance().hashCode());
	}
}
/**
 * 静态内部类
 * 线程安全
 * 单例最优解
 * @author tian
 *
 */
class ThreadStatic extends Thread{
	@Override
	public void run() {
		System.out.println(SingletonStaticInner.getInstance().hashCode());
	}
}
/**
 * 枚举
 * 线程安全
 * @author tian
 *
 */
class ThreadEnum extends Thread{
	@Override
	public void run() {
		System.out.println(SingletonEnum.INSTANCE.hashCode());
	}
}
