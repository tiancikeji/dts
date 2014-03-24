package tianci.pinao.dts.service.impl;

import java.util.List;

import tianci.pinao.dts.dao.UserDao;
import tianci.pinao.dts.models.User;
import tianci.pinao.dts.service.UserService;

public class UserServiceImpl implements UserService {
	
	private UserDao userDao;

	@Override
	public User getUser(String name, String password) {
		return userDao.getUser(name, password);
	}

	@Override
	public User getResetUser(String name, String password) {
		return userDao.getResetUser(name, password);
	}

	@Override
	public void addUser(User user) {
		userDao.addUser(user);
	}

	@Override
	public User getUserById(long id) {
		return userDao.getUserById(id);
	}

	@Override
	public void modifyUser(User user) {
		userDao.modifyUser(user);
	}

	@Override
	public void delUser(long id, long userid) {
		userDao.delUser(id, userid);
	}

	@Override
	public List<User> getUser(int role) {
		return userDao.getUser(role);
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

}
