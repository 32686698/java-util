package com.tian.test;

/**
 * ��������������
 * �̰߳�ȫ
 * ˫��У�� ����volatile
 * 
 * ����û�йؼ���volatile������£������߳�A��B�����ǵ�һ�ε��øõ���������
 * �߳�A��ִ�� person = new Person()���ù��췽����һ����ԭ�Ӳ�������������ɶ����ֽ���ָ�
 * ����JAVA��ָ�������򣬿��ܻ���ִ�� person �ĸ�ֵ�������ò���ʵ��ֻ�����ڴ��п���һƬ�洢����������ֱ�ӷ����ڴ�����ã�
 * ֮�� person �㲻Ϊ���ˣ�����ʵ�ʵĳ�ʼ������ȴ��û��ִ�У�������ڴ�ʱ�߳�B���룬
 * �ͻῴ��һ����Ϊ�յĵ��ǲ����� ��û����ɳ�ʼ������ Person����������Ҫ����volatile�ؼ��֣�
 * ��ָֹ���������Ż����Ӷ���ȫ��ʵ�ֵ�����
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
