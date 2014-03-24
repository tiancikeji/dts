package tianci.pinao.dts.test;

import junit.framework.TestCase;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import tianci.pinao.dts.controllers.UserController;

public class UserControllerTest extends TestCase{
	
	private boolean flag = false;
	
	@Test
	public void testGetUsers(){
		if(flag){
			UserController uc = getController();
			
			System.out.println(uc.getUsers(null, -1));
		}
	}
	
	@Test
	public void testAddUser(){
		if(flag){
			UserController uc = getController();
			
			System.out.println(uc.addUser(null, -1, "test", "123456", "123456", "123456", 2, "1,2,3,4,5,6,7,8"));
		}
	}
	
	@Test
	public void testModUser(){
		if(flag){
			UserController uc = getController();
			
			System.out.println(uc.modifyUser(null, -1, 1, "test1", "123456", "123456", "", 2, "1,2,3,4,5,6,7,8"));
		}
	}
	
	@Test
	public void testDelUser(){
		if(flag){
			UserController uc = getController();
			
			System.out.println(uc.delUser(null, -1, 1));
		}
	}
	
	@Test
	public void testLogin(){
		if(flag){
			UserController uc = getController();
			
			System.out.println(uc.login(null, null, "test1", "123456"));
			System.out.println(uc.login(null, null, "test1", "2123456"));
		}
	}

	private UserController getController() {
		return new ClassPathXmlApplicationContext("classpath:applicationContext.xml").getBean("userController", UserController.class);
	}
}
