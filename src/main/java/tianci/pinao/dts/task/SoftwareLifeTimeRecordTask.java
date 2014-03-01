package tianci.pinao.dts.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tianci.pinao.dts.service.ConfigService;
import tianci.pinao.dts.util.PinaoUtils;

public class SoftwareLifeTimeRecordTask implements Runnable {
	
	private Log logger = LogFactory.getLog(getClass());
	
	private ConfigService configService;
	
	private long time = 300;

	@Override
	public void run() {
		try{
			record();
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Excepiton in LifetimeRecordTask.run", t);
		}
	}

	private void record() {
		configService.addLifeTimeByLocalMac(PinaoUtils.LOCAL_MAC_ADDRESS, time);
	}

	public ConfigService getConfigService() {
		return configService;
	}

	public void setConfigService(ConfigService configService) {
		this.configService = configService;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

}
