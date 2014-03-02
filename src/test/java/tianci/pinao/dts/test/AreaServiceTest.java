package tianci.pinao.dts.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import tianci.pinao.dts.models.Area;
import tianci.pinao.dts.models.AreaChannel;
import tianci.pinao.dts.models.AreaHardwareConfig;
import tianci.pinao.dts.models.AreaTempConfig;
import tianci.pinao.dts.models.Channel;
import tianci.pinao.dts.models.LevelImage;
import tianci.pinao.dts.models.Machine;
import tianci.pinao.dts.service.AreaService;

public class AreaServiceTest extends TestCase{
	
	private boolean flag = false;
	
	@Test
	public void testAddFile(){
		if(flag){
			ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
			AreaService as = ac.getBean("areaService", AreaService.class);
			
			System.out.println(as.addFile("c:/", "data:image/png;base64,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"));
		}
	}
	
	@Test
	public void testLevels(){
		if(flag){
			ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
			AreaService as = ac.getBean("areaService", AreaService.class);
			
			System.out.println(as.getAllLevels());
			
			LevelImage level = new LevelImage();
			level.setLevel(0);
			level.setName("厂区");
			level.setImage("0.jpg");
			
			int userid = 1;
			
			System.out.println(as.addLevelImage(level, userid));
			
			level.setId(1);
			level.setLevel(2);
			System.out.println(as.modifyLevelImage(level, userid));
			
			System.out.println(as.getAllLevels());
			
			System.out.println(as.deleteLevelImage(level, userid));
		}
	}

//	@Test
	public void testMachines(){
		if(flag){
			ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
			AreaService as = ac.getBean("areaService", AreaService.class);
			Machine machine = new Machine();
			machine.setName("测试");
			machine.setBaudRate("22");
			machine.setSerialPort("232");
			
			System.out.println(as.addMachine(machine, 1));
			
			System.out.println(as.getAllMachines());
	
			machine.setId(2);
			machine.setBaudRate("33");
			System.out.println(as.updateMachine(machine, 1));
			
			System.out.println(as.deleteMachine(machine, 2));
		}
	}
	
//	@Test
	public void testChannels(){
		if(flag){
			ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
			AreaService as = ac.getBean("areaService", AreaService.class);
			
			int userid = 1;
			
			Channel channel = new Channel();
			channel.setMachineid(3);
			channel.setName("2");
			channel.setLength(2000);
			System.out.println(as.addChannel(channel, userid));
			
			System.out.println(as.getAllChannels());
			
			channel.setLength(3000);
			channel.setId(1);
			System.out.println(as.updateChannel(channel, userid));
		
			System.out.println(as.deleteChannel(channel, userid));
		}
	}
	
//	@Test
	public void testAreas(){
		if(flag){
			ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
			AreaService as = ac.getBean("areaService", AreaService.class);
			
			int userid = 1;
			Area area = new Area();
			area.setName("test13");
			area.setLevel(0);
			area.setParent(0);
			
			System.out.println(as.addArea(area, userid));
			
			area = new Area();
			area.setName("test14");
			area.setLevel(1);
			area.setParent(1);
			
			System.out.println(as.addArea(area, userid));
			
			area = new Area();
			area.setName("test15");
			area.setLevel(2);
			area.setParent(2);
			
			System.out.println(as.addArea(area, userid));
			
			System.out.println(as.getAllAreas());
			
			area = new Area();
			area.setName("test16");
			area.setLevel(0);
			area.setParent(0);
			
			System.out.println(as.addArea(area, userid));
			
			area.setId(16);
			area.setName("test444");
			System.out.println(as.updateArea(area, userid));
			
			System.out.println(as.deleteArea(area, userid));
		}
	}
	
//	@Test
	public void testAreaHardwareConfigs(){
		if(flag){
			ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
			AreaService as = ac.getBean("areaService", AreaService.class);
			
			int userid = 1;
			AreaHardwareConfig config = new AreaHardwareConfig();
			config.setAreaid(16);
			config.setLight("d");
			config.setRelay("3");
			config.setVoice("2");
			System.out.println(as.addHardwareConfig(config, userid));
			
			System.out.println(as.getAllHardwareConfigs());
			
			config.setId(1);
			config.setRelay("13");
			System.out.println(as.updateHardwareConfig(config, userid));
			
			System.out.println(as.deleteHardwareConfig(config, userid));
	
			System.out.println(as.getAllHardwareConfigs());
			
			config = new AreaHardwareConfig();
			config.setAreaid(15);
			config.setLight("d");
			config.setRelay("3");
			config.setVoice("2");
			System.out.println(as.addHardwareConfig(config, userid));
			
			System.out.println(as.getAllHardwareConfigs());
		}
	}
	
//	@Test
	public void testAreaTempConfigs(){
		if(flag){
			ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
			AreaService as = ac.getBean("areaService", AreaService.class);
			
	//		System.out.println(as.getAllChannels());
	//		System.out.println(as.getAllMachines());
	//		
	//		System.out.println(as.getAllAreas());
	//		System.out.println(as.getAllHardwareConfigs());
			
			int userid = 1;
			AreaTempConfig config = new AreaTempConfig();
			config.setAreaid(15);
			config.setTemperatureLow(40);
			config.setTemperatureHigh(60);
			config.setTemperatureDiff(30);
			config.setExotherm(30);
			System.out.println(as.addAreaTempConfig(config, userid));
			
			System.out.println(as.getAllTempConfigs());
			
			config = new AreaTempConfig();
			config.setAreaid(16);
			config.setTemperatureLow(40);
			config.setTemperatureHigh(60);
			config.setTemperatureDiff(30);
			config.setExotherm(30);
			System.out.println(as.addAreaTempConfig(config, userid));
			
			config.setId(6);
			config.setTemperatureDiff(20);
			System.out.println(as.updateAreaTempConfig(config, userid));
			
			System.out.println(as.deleteAreaTempConfig(config, userid));
		}
	}
	
	@Test
	public void testAreaChannel(){
		if(flag){
			ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
			AreaService as = ac.getBean("areaService", AreaService.class);
			
			int userid = 1;
			List<AreaChannel> channels = new ArrayList<AreaChannel>();
			AreaChannel channel = new AreaChannel();
			channel.setName("testsdfd");
			channel.setAreaid(15);
			channel.setChannelid(1);
			channel.setStart(2);
			channel.setEnd(300);
			channels.add(channel);
			System.out.println(as.addAreaChannel(channels, userid));
			
			System.out.println(as.getAllAreaChannels());
	
			channels.clear();
			channel = new AreaChannel();
			channel.setName("testsdfd");
			channel.setAreaid(16);
			channel.setChannelid(1);
			channel.setStart(400);
			channel.setEnd(500);
			channels.add(channel);
			System.out.println(as.addAreaChannel(channels, userid));
			
			System.out.println(as.getAllAreaChannels());
			
			channel.setId(2);
			channel.setStart(499);
			System.out.println(as.updateAreaChannel(channel, userid));
			
			System.out.println(as.deleteAreaChannel(channel, userid));
		}
	}
}
