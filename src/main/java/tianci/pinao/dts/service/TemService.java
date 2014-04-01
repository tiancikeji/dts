package tianci.pinao.dts.service;

import java.util.Date;
import java.util.List;

import tianci.pinao.dts.models.Alarm;
import tianci.pinao.dts.models.Area;
import tianci.pinao.dts.models.AreaMonitorData;
import tianci.pinao.dts.models.Channel;
import tianci.pinao.dts.models.ChannelMonitorData;
import tianci.pinao.dts.models.ReportData;

public interface TemService {

	public AreaMonitorData getAreaData(Area area, long time);

	public List<Alarm> getAreaAlarmData(Area area, int start, int step);

	public int getAreaAlarmCount(Area area);

	public List<Alarm> getAreasAlarmData(List<Area> areas, long time);

	public ReportData getAreaReportData(Area area, Date startDate, Date endDate);

	public List<Alarm> getAreaAlarmReportData(Area area, int start, int end, Date startDate, Date endDate);

	public int getAreaAlarmReportCount(Area area, Date startDate, Date endDate);

	public ChannelMonitorData getChannelData(List<Channel> channels, long time);

	public List<Alarm> getChannelAlarmData(List<Channel> channels, int start, int step);

	public int getChannelAlarmCount(List<Channel> channels);

	public int getChannelAlarmReportCount(List<Channel> channels, Date startDate, Date endDate);

	public ReportData getChannelReportData(List<Channel> channels, Date startDate, Date endDate);

	public List<Alarm> getChannelAlarmReportData(List<Channel> channels, int start, int step, Date startDate, Date endDate);

	public boolean updateAlarm(List<Long> id, int status, long userid);

	public void checkHardware(long userid);

	public List<Long> getAllAlarmIds(int status);

	public List<Long> getAlarmIdsByAreaIds(List<Integer> areaIds, int status);

}
