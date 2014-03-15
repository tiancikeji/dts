package tianci.pinao.dts.dao;

import java.util.Date;
import java.util.List;

import tianci.pinao.dts.models.Log;

public interface LogDao {

	public boolean addLog(Log log);

	public List<Log> getLogs(Date startDate, Date endDate);
}
