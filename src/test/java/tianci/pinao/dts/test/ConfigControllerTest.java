package tianci.pinao.dts.test;

import junit.framework.TestCase;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import tianci.pinao.dts.controllers.ConfigController;
import tianci.pinao.dts.models.Config;

public class ConfigControllerTest extends TestCase{
	
	private boolean flag = false;
	
	@Test
	public void testConfigs(){
		if(flag){
			ConfigController cs = getController();
			
			System.out.println(cs.getConfig(null, Config.TYPE_BACK_INTERVAL_FLAG));
			System.out.println(cs.getConfig(null, Config.TYPE_LIFE_TIME_FLAG));
			System.out.println(cs.getConfig(null, Config.TYPE_REFER_TEM_FLAG));
			System.out.println(cs.getConfig(null, Config.TYPE_REFRESH_INTERVAL_FLAG));
			System.out.println(cs.getConfig(null, Config.TYPE_STOCK_FLAG));
			
			Config config = new Config();
			config.setType(Config.TYPE_REFRESH_INTERVAL_FLAG);
			config.setValue(20);
			System.out.println(cs.updateConfig(null, config, 1));
			System.out.println(cs.getConfig(null, Config.TYPE_REFRESH_INTERVAL_FLAG));
		}
	}
	
	@Test
	public void testGetLog(){
		if(flag){
			ConfigController cs = getController();
			
			long userid = -1;
			String start = "2013-03-15 00:00:00";
			String end = "2014-03-16 00:00:00";
			System.out.println(cs.getLogHistory(null, userid, start, end));
		}
	}

	private ConfigController getController() {
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		ConfigController cs = ac.getBean("configController", ConfigController.class);
		return cs;
	}
}
