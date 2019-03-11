package com.tian.test;

/**
 * ��̬����
 * @author tian
 *
 */
public class UserDaoProxy implements IUserDao{
	
	private IUserDao target;
	
	public UserDaoProxy(IUserDao target){
		this.target = target;
	}

	@Override
	public void addUser() {
		System.out.println("--��ʼ�����û�");
		target.addUser();
		System.out.println("--���������û�");
	}

	@Override
	public void deleteUser() {
		
	}
	
	

}
