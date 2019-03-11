package com.tian.test;

public class UserDao implements IUserDao{

	@Override
	public void addUser() {
		System.out.println("保存用户！！！");
	}

	@Override
	public void deleteUser() {
		System.out.println("删除用户！！！");
	}

}
