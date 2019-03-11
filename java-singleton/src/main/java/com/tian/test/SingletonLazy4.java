package com.tian.test;

/**
 * ��������������
 * �̲߳���ȫ
 * Ϊʲô�أ����Ǽ����������߳�A��Bͬʱ�ߵ��ˡ�����1������Ϊ��ʱ�����ǿյģ����Զ��ܽ����������棬
 * �߳�A�����������������˶����ͷ������߳�B�õ�����Ҳ���ߵ�������2����Ҳ������һ��������˶��̻߳����¾Ͳ��ܱ�֤�����ˡ�
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
