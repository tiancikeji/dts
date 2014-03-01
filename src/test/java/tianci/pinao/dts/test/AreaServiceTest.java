package tianci.pinao.dts.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import tianci.pinao.dts.models.Area;
import tianci.pinao.dts.models.AreaChannel;
import tianci.pinao.dts.models.AreaHardwareConfig;
import tianci.pinao.dts.models.AreaTempConfig;
import tianci.pinao.dts.models.Channel;
import tianci.pinao.dts.models.Machine;
import tianci.pinao.dts.service.AreaService;
import junit.framework.TestCase;

public class AreaServiceTest/* extends TestCase*/{

//	@Test
	public void testMachines(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:test.xml");
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
	
//	@Test
	public void testChannels(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:test.xml");
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
	
//	@Test
	public void testAreas(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:test.xml");
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
	
//	@Test
	public void testAreaHardwareConfigs(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:test.xml");
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
	
//	@Test
	public void testAreaTempConfigs(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:test.xml");
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
	
	@Test
	public void testAreaChannel(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:test.xml");
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
