package tianci.pinao.dts.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;

import tianci.pinao.dts.models.Alarm;
import tianci.pinao.dts.models.AlarmHistory;
import tianci.pinao.dts.models.Check;

public interface AlarmDao {

	public List<Alarm> getAlarms(List<Integer> areaids, Date date);

	public List<Alarm> getAlarmsByAreaIds(List<Integer> areaIds, Integer[] status, int start, int end, Date startDate, Date endDate);

	public int getAlarmCountByAreaIds(List<Integer> areaIds, Integer[] status, Date startDate, Date endDate);

	public List<Alarm> getAlarmsByChannelIds(List<Integer> ids, Integer[] status, int start, int step, Date startDate, Date endDate);

	public int getAlarmCountByChannelIds(List<Integer> ids, Integer[] status, Date startDate, Date endDate);

	public List<AlarmHistory> getAlarmHistorysByAlarmIds(Set<Long> alarmIds);

	public boolean updateAlarm(List<Long> id, int status, long userid);

	public boolean addAlarmHistory(List<Long> id, int status, long userid);

	public void addChecks(List<Check> checks, long userid);

	public void addAlarm(Alarm alarm);

	public List<Long> getAllAlarmIds(int status);

	public List<Long> getAlarmIdsByAreaIds(List<Integer> areaIds, int status);

}
