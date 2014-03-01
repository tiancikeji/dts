package tianci.pinao.dts.service.impl;

import tianci.pinao.dts.dao.LogDao;
import tianci.pinao.dts.models.Log;
import tianci.pinao.dts.service.LogService;

public class LogServiceImpl implements LogService {

	private LogDao logDao;
	
	@Override
	public boolean addLog(Log log) {
		return logDao.addLog(log);
	}

	public LogDao getLogDao() {
		return logDao;
	}

	public void setLogDao(LogDao logDao) {
		this.logDao = logDao;
	}

}
