package com.tian.test;

/**
 * 静态代理
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
		System.out.println("--开始保存用户");
		target.addUser();
		System.out.println("--结束保存用户");
	}

	@Override
	public void deleteUser() {
		
	}
	
	

}
