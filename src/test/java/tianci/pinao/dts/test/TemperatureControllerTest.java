package tianci.pinao.dts.test;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import tianci.pinao.dts.controllers.TemperatureController;
import tianci.pinao.dts.service.TemService;

public class TemperatureControllerTest extends TestCase {

	private boolean flag = false;
	
	public void testGetAreas(){
		if(flag){
			TemperatureController tc = getController();
			long userid = -1;
			System.out.println(tc.getAreas(null, userid));
		}
	}
	
	public void testGetAreaData(){
		if(flag){
			TemperatureController tc = getController();
			/*long userid = -1;
			int id = 15;
			long time = 1395562918064l;
			System.out.println(tc.getAreaData(userid, id, time));*/
			long userid = -1;
			int id = 13;
			long time = 1395562918064l;
			System.out.println(tc.getAreaData(null, userid, id, time));
		}
	}
	
	public void testGetAreaAlarmData(){
		if(flag){
			TemperatureController tc = getController();
			long userid = -1;
			int id = 15;
			System.out.println(tc.getAreaAlarmData(null, userid, id, 0, 100));
			/*long userid = -1;
			int id = 13;
			System.out.println(tc.getAreaAlarmData(userid, id));*/
		}
	}
	
	public void testGetAreaReportData(){
		if(flag){
			TemperatureController tc = getController();
			long userid = -1;
			int id = 15;
			String start = "2014-03-15 00:00:00";
			String end = "2014-03-16 00:00:00";
			System.out.println(tc.getAreaReportData(null, userid, id, start, end));
		}
	}
	
	public void testGetMachineData(){
		if(flag){
			TemperatureController tc = getController();
			long userid = -1;
			int id = 3;
			long time = -1;
			System.out.println(tc.getMachineData(null, userid, id, time));
		}
	}
	
	public void testGetMachineAlarmData(){
		if(flag){
			TemperatureController tc = getController();
			long userid = -1;
			int id = 3;
			System.out.println(tc.getMachineAlarmData(null, userid, id, 0, 100));
		}
	}
	
	public void testGetMachineReportData(){
		if(flag){
			TemperatureController tc = getController();
			long userid = -1;
			int id = 3;
			String start = "2014-03-15 00:00:00";
			String end = "2014-03-16 00:00:00";
			System.out.println(tc.getMachineReportData(null, userid, id, start, end));
		}
	}
	
	public void testGetMachineAlarmReportData(){
		if(flag){
			TemperatureController tc = getController();
			long userid = -1;
			int id = 3;
			System.out.println(tc.getMachineAlarmReportData(null, userid, id, 0, 100, "", ""));
		}
	}
	
	public void testGetChannels(){
		if(flag){
			TemperatureController tc = getController();
			long userid = -1;
			System.out.println(tc.getChannels(null, userid));
		}
	}
	
	public void testGetChannelReportData(){
		if(flag){
			TemperatureController tc = getController();
			long userid = -1;
			int id = 2;
			String start = "2014-03-15 00:00:00";
			String end = "2014-03-16 00:00:00";
			System.out.println(tc.getChannelReportData(null, userid, id, start, end));
		}
	}
	
	public void testGetChannelData(){
		if(flag){
			TemperatureController tc = getController();
			long userid = -1;
			int id = 2;
			long time = -1;
			System.out.println(tc.getChannelData(null, userid, id, time));
		}
	}
	
	public void testGetChannelAlarmData(){
		if(flag){
			TemperatureController tc = getController();
			long userid = -1;
			int id = 1;
			System.out.println(tc.getChannelAlarmData(null, userid, id, 0, 100));
		}
	}
	
	public void testGetChannelAlarmReportData(){
		if(flag){
			TemperatureController tc = getController();
			long userid = -1;
			int id = 1;
			System.out.println(tc.getChannelAlarmReportData(null, userid, id, 0, 100, "", ""));
		}
	}
	
	public void testCheckAlarm(){
		if(flag){
			TemperatureController tc = getController();
			long userid = -1;
//			long time = -1;
			long time = 1395562918064l;
			System.out.println(tc.checkAlarms(null, userid, time));
		}
	}
	
	public void testNotifyAlarm(){
		if(flag){
			TemperatureController tc = getController();
			long userid = -1;
			long id= 2;
			System.out.println(tc.notifyAlarm(null, userid, null));
		}
	}
	
	public void testMuteAlarm(){
		if(flag){
			TemperatureController tc = getController();
			long userid = -1;
			long id= 2;
			System.out.println(tc.muteAlarm(null, userid, null));
		}
	}
	
	public void testResetAlarm(){
		if(flag){
			TemperatureController tc = getController();
			long userid = -1;
			long id= 2;
			System.out.println(tc.resetAlarm(null, userid, null, "", ""));
		}
	}
	
	public void testCheck(){
		if(flag){
			ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
			
			ac.getBean("temService", TemService.class).checkHardware(1);
		}
	}
	
	private TemperatureController getController(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		
		return ac.getBean("temperatureController", TemperatureController.class);
	}
}
