package tianci.pinao.dts.service;

import java.util.Date;
import java.util.List;

import tianci.pinao.dts.models.Area;
import tianci.pinao.dts.models.AreaMonitorData;
import tianci.pinao.dts.models.ReportData;
import tianci.pinao.dts.models.Channel;
import tianci.pinao.dts.models.ChannelMonitorData;

public interface TemService {

	public AreaMonitorData getAreaData(Area area, long time);

	public ReportData getAreaReportData(Area area, Date startDate, Date endDate);

	public ChannelMonitorData getChannelData(List<Channel> channels, long time);

	public ReportData getChannelReportlData(List<Channel> channels, Date startDate, Date endDate);

}
