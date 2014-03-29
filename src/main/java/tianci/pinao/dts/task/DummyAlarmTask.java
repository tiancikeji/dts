package tianci.pinao.dts.task;

import java.util.List;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tianci.pinao.dts.dao.AlarmDao;
import tianci.pinao.dts.models.Alarm;
import tianci.pinao.dts.models.AreaChannel;
import tianci.pinao.dts.service.AreaService;

public class DummyAlarmTask implements Runnable {
	
	private Log logger = LogFactory.getLog(getClass());
	
	private AreaService areaService;
	
	private AlarmDao alarmDao;
	
	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
	}

	@Override
	public void run() {
		try{
			Random random = new Random(); 
			if(random.nextBoolean()){
				List<AreaChannel> acs = areaService.getAllAreaChannels();
				if(acs != null && acs.size() > 0){
					AreaChannel ac = acs.get(random.nextInt(acs.size()));
					Alarm alarm = new Alarm();
					alarm.setType(random.nextInt(9));
					alarm.setMachineId(ac.getMachineid());
					alarm.setMachineName(ac.getMachineName());
					alarm.setChannelId(ac.getChannelid());
					alarm.setChannelName(ac.getChannelName());
					alarm.setLength(ac.getStart() + random.nextInt(ac.getEnd() - ac.getStart()));
					alarm.setAreaId(ac.getAreaid());
					alarm.setAreaName(ac.getAreaName());
					alarm.setAlarmName(ac.getName());
					alarm.setLight("48");
					alarm.setRelay1("24");
					alarm.setRelay("25");
					alarm.setVoice("86");
					alarm.setTemperatureCurr(random.nextDouble());
					alarm.setTemperaturePre(random.nextDouble());
					alarm.setTemperatureMax(random.nextDouble());
					alarm.setStatus(Alarm.STATUS_ALARMED);
					
					alarmDao.addAlarm(alarm);
				}
			}
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Excepiton in DummyAlarmTask.run", t);
		}
	}

	public AreaService getAreaService() {
		return areaService;
	}

	public void setAreaService(AreaService areaService) {
		this.areaService = areaService;
	}

	public AlarmDao getAlarmDao() {
		return alarmDao;
	}

	public void setAlarmDao(AlarmDao alarmDao) {
		this.alarmDao = alarmDao;
	}

}
