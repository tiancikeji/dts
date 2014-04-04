package tianci.pinao.dts.service.impl;

import java.util.Date;
import java.util.List;

import tianci.pinao.dts.dao.LogDao;
import tianci.pinao.dts.models.Log;
import tianci.pinao.dts.service.LogService;

public class LogServiceImpl implements LogService {

	private LogDao logDao;
	
	@Override
	public boolean addLog(Log log) {
		return logDao.addLog(log);
	}

	@Override
	public List<Log> getLogs(Date startDate, Date endDate, Integer start, Integer step) {
		return logDao.getLogs(startDate, endDate, start, step);
	}

	@Override
	public int getLogCount(Date start, Date end) {
		return logDao.getLogCount(start, end);
	}

	public LogDao getLogDao() {
		return logDao;
	}

	public void setLogDao(LogDao logDao) {
		this.logDao = logDao;
	}

}
