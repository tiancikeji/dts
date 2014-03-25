package tianci.pinao.dts.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import tianci.pinao.dts.dao.ConfigDao;
import tianci.pinao.dts.models.Config;
import tianci.pinao.dts.models.License;
import tianci.pinao.dts.service.ConfigService;

public class ConfigServiceImpl implements ConfigService{
	
	private ConfigDao configDao;

	@Override
	public Config getLicenseConfig() {
		return configDao.getConfigByType(Config.TYPE_LIFE_TIME_FLAG);
	}

	@Override
	public Config getConfigByType(int type) {
		Config config = configDao.getConfigByType(type);
		
		if(type == Config.TYPE_LIFE_TIME_FLAG){
			List<License> licenses = getAllLicenses();
			long max = -1;
			if(licenses != null && licenses.size() > 0)
				for(License license : licenses)
					if(license != null && license.getUseTime() > max)
						max = license.getUseTime();
			
			config.setUsed(max);
		}
		
		return config;
	}

	@Override
	public boolean updateConfig(Config config, int userid) {
		if(config != null){
			config.setLastModUserid(userid);
			Config tmp = configDao.getConfigByType(config.getType());
			if(tmp == null)
				return configDao.addConfig(config);
			else
				return configDao.updateConfig(config);
		} else
			return false;
	}

	@Override
	public List<License> getAllLicenses() {
		return configDao.getAllLicenses();
	}

	@Override
	public boolean addLifeTimeByLocalMac(String mac, long time) {
		List<License> licenses = getAllLicenses();
		List<License> toL = new ArrayList<License>();
		if(licenses != null && licenses.size() > 0)
			for(License license : licenses)
				if(license != null && StringUtils.equals(license.getMac(), mac))
					toL.add(license);
		
		if(toL != null && toL.size() > 0)
			return configDao.addLifeTimeByLocalMac(mac, time);
		else
			return configDao.addLifeTime(mac, time);
	}
	
	@Override
	public boolean checkLifeTime() {
		Config config = getLicenseConfig();
		if(config != null){
			List<License> licenses = getAllLicenses();
			if(licenses != null && licenses.size() > 0)
				for(License license : licenses)
					if(license != null && license.getUseTime() >= config.getValue())
						return false;
			return true;
		} else
			return false;
	}

	public ConfigDao getConfigDao() {
		return configDao;
	}

	public void setConfigDao(ConfigDao configDao) {
		this.configDao = configDao;
	}

}
