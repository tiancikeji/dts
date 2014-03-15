package tianci.pinao.dts.service;

import java.util.List;

import tianci.pinao.dts.models.User;

public interface UserService {

	public User getUser(String name, String password);

	public void addUser(User user);

	public void modifyUser(User user);

	public void delUser(long id, long userid);

	public List<User> getUser(int role);

}
