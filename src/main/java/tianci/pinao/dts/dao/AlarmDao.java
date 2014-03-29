package tianci.pinao.dts.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;

import tianci.pinao.dts.models.Alarm;
import tianci.pinao.dts.models.AlarmHistory;
import tianci.pinao.dts.models.Check;

public interface AlarmDao {

	public List<Alarm> getAlarms(List<Integer> areaids, Date date);

	public List<Alarm> getAlarmsByAreaIds(List<Integer> areaIds, Integer[] status, int start, int end);

	public int getAlarmCountByAreaIds(List<Integer> areaIds, Integer[] status);

	public List<Alarm> getAlarmsByChannelIds(List<Integer> ids, Integer[] status, int start, int step);

	public int getAlarmCountByChannelIds(List<Integer> ids, Integer[] status);

	public List<AlarmHistory> getAlarmHistorysByAlarmIds(Set<Long> alarmIds);

	public boolean updateAlarm(long id, int status, long userid);

	public boolean addAlarmHistory(long id, int status, long userid);

	public void addChecks(List<Check> checks, long userid);

	public void addAlarm(Alarm alarm);

}
