package tianci.pinao.dts.service;

import java.util.List;

import tianci.pinao.dts.models.Config;
import tianci.pinao.dts.models.License;

public interface ConfigService {

	// license
	public List<License> getAllLicenses();
	
	public boolean addLifeTimeByLocalMac(String mac, long time);

	public boolean checkLifeTime();

	// config
	public Config getLicenseConfig();

	public Config getConfigByType(int type);

	public boolean updateConfig(Config config, int userid);
}
