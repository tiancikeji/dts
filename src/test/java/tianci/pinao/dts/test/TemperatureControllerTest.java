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
			/*long userid = -1;
			int id = 15;
			long time = 1395562918064l;
			System.out.println(tc.getAreaData(userid, id, time));*/
			long userid = -1;
			int id = 13;
			long time = 1395562918064l;
			System.out.println(tc.getAreaData(userid, id, time));
		}
	}
	
	public void testGetAreaAlarmData(){
		if(flag){
			TemperatureController tc = getController();
			long userid = -1;
			int id = 15;
			System.out.println(tc.getAreaAlarmData(userid, id));
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
	
	public void testGetMachineAlarmData(){
		if(flag){
			TemperatureController tc = getController();
			long userid = -1;
			int id = 3;
			System.out.println(tc.getMachineAlarmData(userid, id));
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
	
	public void testGetMachineAlarmReportData(){
		if(flag){
			TemperatureController tc = getController();
			long userid = -1;
			int id = 3;
			System.out.println(tc.getMachineAlarmReportData(userid, id));
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
	
	public void testGetChannelAlarmData(){
		if(flag){
			TemperatureController tc = getController();
			long userid = -1;
			int id = 1;
			System.out.println(tc.getChannelAlarmData(userid, id));
		}
	}
	
	public void testGetChannelAlarmReportData(){
		if(flag){
			TemperatureController tc = getController();
			long userid = -1;
			int id = 1;
			System.out.println(tc.getChannelAlarmReportData(userid, id));
		}
	}
	
	public void testCheckAlarm(){
		if(flag){
			TemperatureController tc = getController();
			long userid = -1;
//			long time = -1;
			long time = 1395562918064l;
			System.out.println(tc.checkAlarms(userid, time));
		}
	}
	
	public void testNotifyAlarm(){
		if(flag){
			TemperatureController tc = getController();
			long userid = -1;
			long id= 2;
			System.out.println(tc.notifyAlarm(userid, id));
		}
	}
	
	public void testMuteAlarm(){
		if(flag){
			TemperatureController tc = getController();
			long userid = -1;
			long id= 2;
			System.out.println(tc.muteAlarm(userid, id));
		}
	}
	
	public void testResetAlarm(){
		if(flag){
			TemperatureController tc = getController();
			long userid = -1;
			long id= 2;
			System.out.println(tc.resetAlarm(userid, id, "", ""));
		}
	}
	
	private TemperatureController getController(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		
		return ac.getBean("temperatureController", TemperatureController.class);
	}
}
