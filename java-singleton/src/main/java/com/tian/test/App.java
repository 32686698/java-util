package com.tian.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * ����
 * �������̰߳�ȫ���˷���Դ
 * ����1���̲߳���ȫ����ʡ��Դ
 * ����2���̰߳�ȫ�����ܲ�
 * ����3������2�ı��֣��̰߳�ȫ�����ܲ�
 * ����4���̲߳���ȫ
 * ����5���̰߳�ȫ����������õ��������һ�μ���ʱ��Ӧ���죬����java�ڴ�ģ��һЩԭ��ż��ʧ��
 * ����6���̰߳�ȫ��������5����volatile�ؼ��֣��Ƽ�ʹ��
 * ��̬�ڲ��ࣺ�̰߳�ȫ����ʡ��Դ�����ܺã��Ƽ�ʹ��
 * ö�٣����ְ�ȫ����ʡ��Դ�����ܺã����л���Ҳ�ܱ��ֵ������Ƽ�
 * @author tian
 *
 */
public class App {
	
	/**
	 * ������������
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
			//���л�
			FileOutputStream fo = new FileOutputStream("tem");
			ObjectOutputStream oo = new ObjectOutputStream(fo);
			oo.writeObject(ssis);
			oo.close();
			fo.close();
			//�����л�
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
 * �����������̲߳���
 * �̰߳�ȫ
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
 * ��һ��������������
 * �̲߳���ȫ
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
 * �ڶ���������������
 * �̰߳�ȫ
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
 * ������������������ �͵ڶ���һ����Ч��
 * �̰߳�ȫ
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
 * ������������������
 * �̲߳���ȫ
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
 * ������������������
 * �̰߳�ȫ
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
 * ������������������
 * �̰߳�ȫ
 * ��Ϊ��ȫ�ɿ����������ط�ʽ
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
 * ��̬�ڲ���
 * �̰߳�ȫ
 * �������Ž�
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
 * ö��
 * �̰߳�ȫ
 * @author tian
 *
 */
class ThreadEnum extends Thread{
	@Override
	public void run() {
		System.out.println(SingletonEnum.INSTANCE.hashCode());
	}
}
