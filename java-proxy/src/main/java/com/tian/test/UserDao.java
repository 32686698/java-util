package com.tian.test;

public class UserDao implements IUserDao{

	@Override
	public void addUser() {
		System.out.println("�����û�������");
	}

	@Override
	public void deleteUser() {
		System.out.println("ɾ���û�������");
	}

}
