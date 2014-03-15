package tianci.pinao.dts.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import tianci.pinao.dts.controllers.TemperatureController;
import junit.framework.TestCase;

public class TemperatureControllerTest extends TestCase {

	private boolean flag = false;
	
	public void testGetAreas(){
		if(flag){
			TemperatureController tc = getController();
			long userid = -1;
			System.out.println(tc.getAreas(userid));
		}
	}
	
	public void testGetAreaData(){
		if(flag){
			TemperatureController tc = getController();
			long userid = -1;
			int id = 15;
			long time = -1;
			System.out.println(tc.getAreaData(userid, id, time));
		}
	}
	
	public void testGetAreaReportData(){
		if(flag){
			TemperatureController tc = getController();
			long userid = -1;
			int id = 15;
			String start = "2014-03-15 00:00:00";
			String end = "2014-03-16 00:00:00";
			System.out.println(tc.getAreaReportData(userid, id, start, end));
		}
	}
	
	public void testGetMachineData(){
		if(flag){
			TemperatureController tc = getController();
			long userid = -1;
			int id = 3;
			long time = -1;
			System.out.println(tc.getMachineData(userid, id, time));
		}
	}
	
	public void testGetMachineReportData(){
		if(flag){
			TemperatureController tc = getController();
			long userid = -1;
			int id = 3;
			String start = "2014-03-15 00:00:00";
			String end = "2014-03-16 00:00:00";
			System.out.println(tc.getMachineReportData(userid, id, start, end));
		}
	}
	
	public void testGetChannels(){
		if(flag){
			TemperatureController tc = getController();
			long userid = -1;
			System.out.println(tc.getChannels(userid));
		}
	}
	
	public void testGetChannelReportData(){
		if(flag){
			TemperatureController tc = getController();
			long userid = -1;
			int id = 2;
			String start = "2014-03-15 00:00:00";
			String end = "2014-03-16 00:00:00";
			System.out.println(tc.getChannelReportData(userid, id, start, end));
		}
	}
	
	public void testGetChannelData(){
		if(flag){
			TemperatureController tc = getController();
			long userid = -1;
			int id = 2;
			long time = -1;
			System.out.println(tc.getChannelData(userid, id, time));
		}
	}
	
	private TemperatureController getController(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		
		return ac.getBean("temperatureController", TemperatureController.class);
	}
}
