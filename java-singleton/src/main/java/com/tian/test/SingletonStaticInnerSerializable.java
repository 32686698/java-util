package com.tian.test;

import java.io.Serializable;

/**
 * 序列化问题
 * 单例模式虽然能保证线程安全，但在序列化和反序列化的情况下会出现生成多个对象的情况
 * 加入readResolve方法后，能保存序列化－反序列化后是同一个对象
 * 
 * readResolve()方法到底是何方神圣，其实当JVM从内存中反序列化地”组装”一个新对象时，
 * 就会自动调用这个 readResolve方法来返回我们指定好的对象了, 
 * 单例规则也就得到了保证。readResolve()的出现允许程序员自行控制通过反序列化得到的对象。
 * @author tian
 *
 */
public class SingletonStaticInnerSerializable implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SingletonStaticInnerSerializable(){
		
	}
	private static class SingletonInner{
		private static SingletonStaticInnerSerializable singletonStaticInnerSerializable = new SingletonStaticInnerSerializable();
	}
	
	public static SingletonStaticInnerSerializable getInstance(){
		return SingletonInner.singletonStaticInnerSerializable;
	}
	
	/**
	 * 保证序列化－反序列化后，是同一个对象
	 * @return
	 */
	protected Object readResolve(){
		System.out.println("调用了readResolve");
		return SingletonInner.singletonStaticInnerSerializable;
	}

}
