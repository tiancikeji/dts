package tianci.pinao.dts.controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tianci.pinao.dts.models.Alarm;
import tianci.pinao.dts.models.AlarmHistory;
import tianci.pinao.dts.models.Area;
import tianci.pinao.dts.models.AreaMonitorData;
import tianci.pinao.dts.models.Channel;
import tianci.pinao.dts.models.ChannelMonitorData;
import tianci.pinao.dts.models.Config;
import tianci.pinao.dts.models.Machine;
import tianci.pinao.dts.models.ReportData;
import tianci.pinao.dts.models.User;
import tianci.pinao.dts.service.AreaService;
import tianci.pinao.dts.service.ConfigService;
import tianci.pinao.dts.service.TemService;
import tianci.pinao.dts.service.UserService;
import tianci.pinao.dts.util.PinaoConstants;
import tianci.pinao.dts.util.PinaoUtils;

@Controller
public class TemperatureController {
	
	@Autowired
	private AreaService areaService;
	
	@Autowired
	private TemService temService;
	
	@Autowired
	private ConfigService configService;
	
	@Autowired
	private UserService userService;

	@RequestMapping(value="/check/hardware", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> checkHardware(HttpServletRequest request, long userid){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			userid = PinaoUtils.getUserid(request, userid);
			result.put("status", "400");
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			// check user role
			if(obj != null && obj instanceof User){
				User user = (User)obj;
				if(user.getRole() == 1){
					temService.checkHardware(userid);
					result.put("status", "0");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/alarm/notify", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> notifyAlarm(HttpServletRequest request, long userid, String id){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			userid = PinaoUtils.getUserid(request, userid);
			result.put("status", "400");
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){	
				User user = (User)obj;
				if(configService.checkLifeTime() || user.getRole() == 1){
					List<Long> ids = new ArrayList<Long>();
					for(String _id : StringUtils.split(id, ","))
						ids.add(NumberUtils.toLong(_id, -1));
					if(temService.updateAlarm(ids, Alarm.STATUS_NOTIFY, userid))
						result.put("status", "0");
					else
						result.put("status", "400");
				} else
					result.put("status", "1000");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/alarm/mute", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> muteAlarm(HttpServletRequest request, long userid, String id){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			userid = PinaoUtils.getUserid(request, userid);
			result.put("status", "400");
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){	
				User user = (User)obj;
				if(configService.checkLifeTime() || user.getRole() == 1){	
					List<Long> ids = new ArrayList<Long>();
					for(String _id : StringUtils.split(id, ","))
						ids.add(NumberUtils.toLong(_id, -1));
					if(temService.updateAlarm(ids, Alarm.STATUS_MUTE, userid))
						result.put("status", "0");
					else
						result.put("status", "400");
				} else
					result.put("status", "1000");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/alarm/reset", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> resetAlarm(HttpServletRequest request, long userid, String id, String loginname, String password){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			userid = PinaoUtils.getUserid(request, userid);
			result.put("status", "400");
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = userService.getResetUser(loginname, password);
				if(user != null && StringUtils.equals(user.getPasswordReset(), password)){
					if(user.getRole() < 4){
						if(configService.checkLifeTime() || user.getRole() == 1){
							List<Long> ids = new ArrayList<Long>();
							for(String _id : StringUtils.split(id, ","))
								ids.add(NumberUtils.toLong(_id, -1));
							if(temService.updateAlarm(ids, Alarm.STATUS_RESET, userid))
								result.put("status", "0");
							else
								result.put("status", "400");
						} else
							result.put("status", "1000");
					} else
						result.put("status", "700");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/alarm/resetAll", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> resetAllAlarm(HttpServletRequest request, long userid, String loginname, String password){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			userid = PinaoUtils.getUserid(request, userid);
			result.put("status", "400");
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = userService.getResetUser(loginname, password);
				if(user != null && StringUtils.equals(user.getPasswordReset(), password)){
					if(user.getRole() < 4){
						if(configService.checkLifeTime() || user.getRole() == 1){
							List<Long> ids = null;
							if(user.getRole() == 1)
								ids = temService.getAllAlarmIds(Alarm.STATUS_RESET);
							else
								ids = temService.getAlarmIdsByAreaIds(user.getAreaIds(), Alarm.STATUS_RESET);
							
							if(temService.updateAlarm(ids, Alarm.STATUS_RESET, userid))
								result.put("status", "0");
							else
								result.put("status", "400");
						} else
							result.put("status", "1000");
					} else
						result.put("status", "700");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/alarm/check", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> checkAlarms(HttpServletRequest request, long userid, long time){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			userid = PinaoUtils.getUserid(request, userid);
			result.put("status", "400");
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User)obj;
				if(configService.checkLifeTime() || user.getRole() == 1){	
					List<Area> areas = areaService.getAllAreas(userid, user);
	
					List<Map<String, Object>> tmp = null;
					if(areas != null && areas.size() > 0)
						tmp = parseCheckAlarmData(temService.getAreasAlarmData(areas, time));
					else
						tmp = new ArrayList<Map<String,Object>>();
					
					result.put("data", tmp);
					result.put("time", System.currentTimeMillis());
					result.put("interval", configService.getConfigByType(Config.TYPE_REFRESH_INTERVAL_FLAG).getValue());
					result.put("status", "0");
				} else
					result.put("status", "1000");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	private List<Map<String, Object>> parseCheckAlarmData(List<Alarm> data) {
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		
		if(data != null && data.size() > 0){
			Map<String, Map<String, Object>> maps = new HashMap<String, Map<String,Object>>();
			for(Alarm alarm : data){
				Map<String, Object> tmp = maps.get(alarm.getAreaId() + "-" + alarm.getType() + "-" + alarm.getAlarmName()); 
				
				if(tmp == null){
					tmp = new HashMap<String, Object>();
					maps.put(alarm.getAreaId() + "-" + alarm.getType() + "-" + alarm.getAlarmName(), tmp);

					tmp.put("id", alarm.getId());
					tmp.put("type", alarm.getType());
					tmp.put("areaName", alarm.getAreaName());
					tmp.put("alarmName", alarm.getAlarmName());
				}
				
				tmp.put("id", tmp.get("id") + "," + alarm.getId());
			}
			
			for(Map<String, Object> tmp : maps.values())
				result.add(tmp);
		}
		
		return result;
	}

	@RequestMapping(value="/report/area/alarm", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getAreaAlarmReportData(HttpServletRequest request, long userid, int id, Integer start, Integer step, String startDate, String endDate){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			userid = PinaoUtils.getUserid(request, userid);
			result.put("status", "400");
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			// check user role
			if(obj != null && obj instanceof User){
				User user = (User)obj;
				if(user.getRole() < 4){
					if(configService.checkLifeTime() || user.getRole() == 1){
						Area area = searchArea(areaService.getAllAreas(userid, user), id);

						if(start == null || start < 0)
							start = 0;
						if(step == null || step <= 0)
							step = 100;
						List<Map<String, Object>> tmp = null;
						Date _startDate = PinaoUtils.getDate(startDate);
						Date _endDate = PinaoUtils.getDate(endDate);
						int count = 0;
						if(area != null && _startDate != null && _endDate != null && !_endDate.before(_startDate)){
							tmp = parseAlarmData(temService.getAreaAlarmReportData(area, start, step, _startDate, _endDate));
							count = temService.getAreaAlarmReportCount(area, _startDate, _endDate);
						} else
							tmp = new ArrayList<Map<String,Object>>();
						
						result.put("data", tmp);
						result.put("count", count);
						result.put("status", "0");
					} else
						result.put("status", "1000");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/report/area/data", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getAreaReportData(HttpServletRequest request, long userid, int id, String start, String end){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			userid = PinaoUtils.getUserid(request, userid);
			result.put("status", "400");
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			// check user role
			if(obj != null && obj instanceof User){
				User user = (User)obj;
				if(configService.checkLifeTime() || user.getRole() == 1){
					Area area = searchArea(areaService.getAllAreas(userid, user), id);
					Map<String, Object> tmp = null;
					Date startDate = PinaoUtils.getDate(start);
					Date endDate = PinaoUtils.getDate(end);
					if(area != null && startDate != null && endDate != null && !endDate.before(startDate))
						tmp = parseAreaReportData(temService.getAreaReportData(area, startDate, endDate));
					else
						tmp = new HashMap<String, Object>();
					
					result.put("data", tmp);
					result.put("status", "0");
				} else
					result.put("status", "1000");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	private Map<String, Object> parseAreaReportData(ReportData data) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		if(data != null){
			result.put("max", PinaoUtils.getFloat(data.getMax()));
			result.put("min", PinaoUtils.getFloat(data.getMin()));
			result.put("avg", PinaoUtils.getFloat(data.getAvg()));
			
			Map<String, Object> tmp = new HashMap<String, Object>();
			if(data.getTems() != null && data.getTems().size() > 0){
				Set<Date> _dates = new HashSet<Date>();
				for(String name : data.getTems().keySet()){
					_dates.addAll(data.getTems().get(name).keySet());
				}
				List<Date> dates = new ArrayList<Date>(_dates);
				sortDate(dates);
				
				Map<String, Object> _tmp = new HashMap<String, Object>();
				for(String name : data.getTems().keySet()){
					Map<Date, Double> _tems = data.getTems().get(name);
					
					List<Double> _list = new ArrayList<Double>();
					for(Date _date : dates)
						if(_tems.containsKey(_date))
							_list.add(PinaoUtils.getFloat(_tems.get(_date)));
						else
							_list.add(0d);
					
					_tmp.put(name, _list);
				}
				
				List<String> dateStr = new ArrayList<String>();
				for(Date _date : dates)
					dateStr.add(PinaoUtils.getDateString(_date));
						
				tmp.put("dates", dateStr);
				tmp.put("data", _tmp);
			}
			result.put("tems", tmp);
		}
		
		return result;
	}
	
	@RequestMapping(value="/report/area/download", method = RequestMethod.GET)
	public void downloadAllAreas(HttpServletRequest request, HttpServletResponse response, long userid, int id, String start, String end, Integer type) throws IOException{
		OutputStream out = null;
		try{
			// check user role
			userid = PinaoUtils.getUserid(request, userid);
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() < 4){
					if(configService.checkLifeTime() || user.getRole() == 1){
						if(type == null)
							type = 0;
						
						// txt
						if(type == 0){
							response.setContentType("text/plain");
							String oriFileName = "历史厂区趋势数据.txt";
							String agent = request.getHeader("USER-AGENT");
							if (null != agent && -1 != agent.indexOf("Firefox")) {
								response.setHeader("Content-disposition", "attachment;filename=" + new String(oriFileName.getBytes("utf-8"),"iso-8859-1"));
							} else {
								response.setHeader("Content-disposition", "attachment;filename=" + java.net.URLEncoder.encode(oriFileName, "UTF-8"));
							}
							out = response.getOutputStream();
							out.write((PinaoConstants.FILE_COMMENT_PREFIX + "厂区-距离" + PinaoConstants.TEM_DATA_COL_SEP + "时间" + PinaoConstants.TEM_DATA_COL_SEP + "温度" + PinaoConstants.TEM_DATA_LINE_SEP).getBytes());
							
							Area area = searchArea(areaService.getAllAreas(userid, user), id);
		
							Date startDate = PinaoUtils.getDate(start);
							Date endDate = PinaoUtils.getDate(end);
							if(area != null && startDate != null && endDate != null && !endDate.before(startDate)){
								ReportData data = temService.getAreaReportData(area, startDate, endDate);
								if(data != null){
									Map<String, Map<Date, Double>> tems = data.getTems();
									
									for(String key : tems.keySet()){
										Map<Date, Double> _temps = data.getTems().get(key);
										
										for(Date _key : _temps.keySet()){
											out.write(key.getBytes());
											out.write(PinaoConstants.TEM_DATA_COL_SEP.getBytes());
											out.write(PinaoUtils.getDateString(_key).getBytes());
											out.write(PinaoConstants.TEM_DATA_COL_SEP.getBytes());
											out.write(_temps.get(_key).toString().getBytes());
											out.write(PinaoConstants.TEM_DATA_LINE_SEP.getBytes());
										}
									}
								}
							}
						} else {
							// xls
							response.setContentType("application/msexcel");
							String oriFileName = "历史厂区趋势数据.xls";
							String agent = request.getHeader("USER-AGENT");
							if (null != agent && -1 != agent.indexOf("Firefox")) {
								response.setHeader("Content-disposition", "attachment;filename=" + new String(oriFileName.getBytes("utf-8"),"iso-8859-1"));
							} else {
								response.setHeader("Content-disposition", "attachment;filename=" + java.net.URLEncoder.encode(oriFileName, "UTF-8"));
							}
							
							HSSFWorkbook book = new HSSFWorkbook();
							HSSFSheet sheet = book.createSheet("历史厂区趋势数据");
							int rowindex = 0;
							HSSFRow row = sheet.createRow(rowindex++);
							int columnIndex = 0;
							HSSFCell cell = row.createCell(columnIndex++);
							cell.setCellValue("厂区-距离");
							cell = row.createCell(columnIndex++);
							cell.setCellValue("时间");
							cell = row.createCell(columnIndex++);
							cell.setCellValue("温度");
							
							Area area = searchArea(areaService.getAllAreas(userid, user), id);
		
							Date startDate = PinaoUtils.getDate(start);
							Date endDate = PinaoUtils.getDate(end);
							if(area != null && startDate != null && endDate != null && !endDate.before(startDate)){
								ReportData data = temService.getAreaReportData(area, startDate, endDate);
								if(data != null){
									Map<String, Map<Date, Double>> tems = data.getTems();
									
									for(String key : tems.keySet()){
										Map<Date, Double> _temps = data.getTems().get(key);
										
										for(Date _key : _temps.keySet()){
											row = sheet.createRow(rowindex++);
											columnIndex = 0;
											cell = row.createCell(columnIndex++);
											cell.setCellValue(key);
											cell = row.createCell(columnIndex++);
											cell.setCellValue(PinaoUtils.getDateString(_key));
											cell = row.createCell(columnIndex++);
											cell.setCellValue(_temps.get(_key));
										}
									}
								}
							}

							out = response.getOutputStream();
							book.write(out);
						}
					} else
						response.sendRedirect(request.getContextPath() + "/" + "system/expire.html");
				} else
					response.sendRedirect(request.getContextPath() + "/" + "system/error.html");
			} else
				response.sendRedirect(request.getContextPath() + "/" + "system/error.html");
		} catch(Throwable t){
			t.printStackTrace();
			response.sendRedirect(request.getContextPath() + "/" + "system/error.html");
		} finally {
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (Exception e) {
				// 文件已下载完毕，不需要处理
			}
		}
	}

	private Map<String, Object> parseReportData(ReportData data) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		if(data != null){
			result.put("max", PinaoUtils.getFloat(data.getMax()));
			result.put("min", PinaoUtils.getFloat(data.getMin()));
			result.put("avg", PinaoUtils.getFloat(data.getAvg()));
			
			Map<String, Object> tmp = new HashMap<String, Object>();
			if(data.getTems() != null && data.getTems().size() > 0){
				Set<Date> _dates = new HashSet<Date>();
				for(String name : data.getTems().keySet()){
					_dates.addAll(data.getTems().get(name).keySet());
				}
				List<Date> dates = new ArrayList<Date>(_dates);
				sortDate(dates);
				
				Map<String, Object> _tmp = new HashMap<String, Object>();
				for(String name : data.getTems().keySet()){
					Map<Date, Double> _tems = data.getTems().get(name);
					
					List<Double> _list = new ArrayList<Double>();
					for(Date _date : dates)
						if(_tems.containsKey(_date))
							_list.add(PinaoUtils.getFloat(_tems.get(_date)));
						else
							_list.add(0d);
					
					_tmp.put(name, _list);
				}
				
				List<String> dateStr = new ArrayList<String>();
				for(Date _date : dates)
					dateStr.add(PinaoUtils.getDateString(_date));
						
				tmp.put("dates", dateStr);
				tmp.put("data", _tmp);
			}
			result.put("tems", tmp);
			
			tmp = new HashMap<String, Object>();
			if(data.getStocks() != null && data.getStocks().size() > 0){
				Set<Date> _dates = new HashSet<Date>();
				for(String name : data.getTems().keySet()){
					_dates.addAll(data.getTems().get(name).keySet());
				}
				List<Date> dates = new ArrayList<Date>(_dates);
				sortDate(dates);

				Map<String, Object> _tmp = new HashMap<String, Object>();
				for(String name : data.getStocks().keySet()){
					Map<Date, Double> _stocks = data.getStocks().get(name);
					
					List<Double> _list = new ArrayList<Double>();
					for(Date _date : dates)
						if(_stocks.containsKey(_date))
							_list.add(PinaoUtils.getFloat(_stocks.get(_date)));
						else
							_list.add(0d);
					
					_tmp.put(name, _list);
				}

				List<String> dateStr = new ArrayList<String>();
				for(Date _date : dates)
					dateStr.add(PinaoUtils.getDateString(_date));
						
				tmp.put("dates", dateStr);
				tmp.put("data", _tmp);
			}
			result.put("stocks", tmp);
			
			tmp = new HashMap<String, Object>();
			if(data.getUnstocks() != null && data.getUnstocks().size() > 0){
				Set<Date> _dates = new HashSet<Date>();
				for(String name : data.getTems().keySet()){
					_dates.addAll(data.getTems().get(name).keySet());
				}
				List<Date> dates = new ArrayList<Date>(_dates);
				sortDate(dates);

				Map<String, Object> _tmp = new HashMap<String, Object>();
				for(String name : data.getUnstocks().keySet()){
					Map<Date, Double> _unstocks = data.getUnstocks().get(name);
					
					List<Double> _list = new ArrayList<Double>();
					for(Date _date : dates)
						if(_unstocks.containsKey(_date))
							_list.add(_unstocks.get(_date));
						else
							_list.add(0d);
					
					_tmp.put(name, _list);
				}

				List<String> dateStr = new ArrayList<String>();
				for(Date _date : dates)
					dateStr.add(PinaoUtils.getDateString(_date));
						
				tmp.put("dates", dateStr);
				tmp.put("data", _tmp);
			}
			result.put("unstocks", tmp);
		}
		
		return result;
	}

	private List<Date> sortDate(List<Date> dates) {
		if(dates != null && dates.size() > 0)
			Collections.sort(dates, new Comparator<Date>(){

				@Override
				public int compare(Date s1, Date s2) {
					return s1.before(s2) ? 0 : 1;
				}
				
			});
		return dates;
	}

	@RequestMapping(value="/monitor/area/alarm", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getAreaAlarmData(HttpServletRequest request, long userid, int id, Integer start, Integer step){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			userid = PinaoUtils.getUserid(request, userid);
			result.put("status", "400");
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			// check user role
			if(obj != null && obj instanceof User){
				User user = (User)obj;
				if(configService.checkLifeTime() || user.getRole() == 1){
					Area area = searchArea(areaService.getAllAreas(userid, user), id);

					if(start == null || start < 0)
						start = 0;
					if(step == null || step <= 0)
						step = 100;
					List<Map<String, Object>> tmp = null;
					if(area != null)
						tmp = parseAlarmData(temService.getAreaAlarmData(area, start, step));
					else
						tmp = new ArrayList<Map<String,Object>>();
					
					result.put("data", tmp);
					result.put("count", temService.getAreaAlarmCount(area));
					result.put("status", "0");
				} else
					result.put("status", "1000");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	private List<Map<String, Object>> parseAlarmData(List<Alarm> data) {
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		
		if(data != null && data.size() > 0)
			for(Alarm alarm : data){
				Map<String, Object> tmp = new HashMap<String, Object>();
				
				tmp.put("id", alarm.getId());
				tmp.put("type", alarm.getType());
				tmp.put("machineName", alarm.getMachineName());
				tmp.put("channelName", alarm.getChannelName());
				tmp.put("areaName", alarm.getAreaName());
				tmp.put("length", alarm.getLength());
				tmp.put("alarmName", alarm.getAlarmName());
				tmp.put("light", alarm.getLight());
				tmp.put("relay", alarm.getRelay());
				tmp.put("relay1", alarm.getRelay1());
				tmp.put("voice", alarm.getVoice());
				tmp.put("temperature", alarm.getTemperatureCurr());
				tmp.put("temperaturePre", alarm.getTemperaturePre());
				tmp.put("status", alarm.getStatus());
				tmp.put("addTime", PinaoUtils.getDateString(alarm.getAddTime()));
				tmp.put("lastModTime", PinaoUtils.getDateString(alarm.getLastModTime()));
				if(alarm.getLastModUserid() > 0)
					tmp.put("lastModUserid", alarm.getLastModUserid());
				else
					tmp.put("lastModUserid", StringUtils.EMPTY);
				
				tmp.put("history", parseAlarmHistory(alarm.getHistory()));
				
				result.add(tmp);
			}
		
		return result;
	}

	private List<Map<String, Object>> parseAlarmHistory(List<AlarmHistory> historys) {
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		
		if(historys != null && historys.size() > 0)
			for(AlarmHistory history : historys){
				Map<String, Object> tmp = new HashMap<String, Object>();
				
				tmp.put("operation", history.getOperation());
				tmp.put("lastModTime", PinaoUtils.getDateString(history.getAddTime()));
				tmp.put("lastModUserid", history.getAddUserid());
				
				result.add(tmp);
			}
		
		return result;
	}

	@RequestMapping(value="/monitor/area/data", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getAreaData(HttpServletRequest request, long userid, int id, long time){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			userid = PinaoUtils.getUserid(request, userid);
			result.put("status", "400");
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			// check user role
			if(obj != null && obj instanceof User){
				User user = (User)obj;
				if(configService.checkLifeTime() || user.getRole() == 1){
					Area area = searchArea(areaService.getAllAreas(userid, user), id);
		
					Map<String, Object> tmp = null;
					if(area != null)
						tmp = parseAreaData(temService.getAreaData(area, time));
					else
						tmp = new HashMap<String, Object>();
					
					result.put("data", tmp);
					result.put("interval", configService.getConfigByType(Config.TYPE_REFRESH_INTERVAL_FLAG).getValue());
					result.put("status", "0");
				} else
					result.put("status", "1000");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}
	
	private Area searchArea(List<Area> areas, int id){
		if(areas != null && areas.size() > 0)
			for(Area area : areas)
				if(area.getId() == id)
					return area;
				else{
					Area tmp = searchArea(area.getChildren(), id);
					if(tmp != null)
						return tmp;
				}
		
		return null;
	}

	private Map<String, Object> parseAreaData(AreaMonitorData data) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		if(data != null){
			result.put("max", PinaoUtils.getFloat(data.getMax()));
			result.put("min", PinaoUtils.getFloat(data.getMin()));
			result.put("avg", PinaoUtils.getFloat(data.getAvg()));
			result.put("time", data.getTime());
			if(data.getAlarmIds() != null && data.getAlarmIdx() != null
					&& data.getAlarmIds().size() > 0 && data.getAlarmIdx().size() > 0){
				result.put("alarmIdxs", data.getAlarmIdx());
				result.put("alarmIds", data.getAlarmIds());
			}
			if(StringUtils.isNotBlank(data.getAlarmName()) && data.getAlarmType() > 0){
				result.put("alarmName", data.getAlarmName());
				result.put("alarmType", data.getAlarmType());
			}
		}
		
		return result;
	}

	@RequestMapping(value="/report/areas", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getReportAreas(HttpServletRequest request, long userid){
		return getAreas(request, userid);
	}

	@RequestMapping(value="/monitor/areas", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getAreas(HttpServletRequest request, long userid){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			userid = PinaoUtils.getUserid(request, userid);
			result.put("status", "400");
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			// check user role
			if(obj != null && obj instanceof User){
				User user = (User)obj;
				if(configService.checkLifeTime() || user.getRole() == 1){
					List<Area> areas = areaService.getAllAreas(userid, user);
					result.put("data", parseAreas(areas));
					result.put("status", "0");
				} else
					result.put("status", "1000");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	private Object parseAreas(List<Area> areas) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		if(areas != null && areas.size() > 0){
			for(Area area : areas)
				result.add(parseArea(area));
		}
		return result;
	}

	private Map<String, Object> parseArea(Area area) {
		Map<String, Object> result = new HashMap<String, Object>();
		if(area != null){
			result.put("id", area.getId());
			result.put("name", area.getName());
			result.put("level", area.getLevel());
			result.put("index", area.getIndex());
			result.put("image", area.getImage());
			result.put("parent", area.getParent());
			
			result.put("children", area.getChildren());
		}
		return result;
	}

	@RequestMapping(value="/report/channel/alarm", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getChannelAlarmReportData(HttpServletRequest request, long userid, int id, Integer start, Integer step, String startDate, String endDate){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			userid = PinaoUtils.getUserid(request, userid);
			result.put("status", "400");
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			// check user role
			if(obj != null && obj instanceof User){
				User user = (User)obj;
				if(user.getRole() == 1){
					Map<Machine, List<Channel>> machines = areaService.getAllChannels(userid);
					List<Channel> channels = new ArrayList<Channel>();
					if(machines != null && machines.size() > 0)
						for(Machine machine : machines.keySet())
							for(Channel channel : machines.get(machine))
								if(channel.getId() == id){
									channels.add(channel);
									break;
								}

					if(start == null || start < 0)
						start = 0;
					if(step == null || step <= 0)
						step = 100;
					List<Map<String, Object>> data = null;
					Date _startDate = PinaoUtils.getDate(startDate);
					Date _endDate = PinaoUtils.getDate(endDate);
					int count = 0;
					if(channels != null && channels.size() > 0 && _startDate != null && _endDate != null && !_endDate.before(_startDate)){
						data = parseAlarmData(temService.getChannelAlarmReportData(channels, start, step, _startDate, _endDate));
						count = temService.getChannelAlarmReportCount(channels, _startDate, _endDate);
					} else
						data = new ArrayList<Map<String,Object>>();
					result.put("data", data);
					result.put("count", count);
					result.put("status", "0");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/report/channel/data", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getChannelReportData(HttpServletRequest request, long userid, int id, String start, String end){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			userid = PinaoUtils.getUserid(request, userid);
			result.put("status", "400");
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			// check user role
			if(obj != null && obj instanceof User){
				User user = (User)obj;
				if(user.getRole() == 1){
					Map<Machine, List<Channel>> machines = areaService.getAllChannels(userid);
					List<Channel> channels = new ArrayList<Channel>();
					if(machines != null && machines.size() > 0)
						for(Machine machine : machines.keySet())
							for(Channel channel : machines.get(machine))
								if(channel.getId() == id){
									channels.add(channel);
									break;
								}

					Date _startDate = PinaoUtils.getDate(start);
					Date _endDate = PinaoUtils.getDate(end);
					Map<String, Object> data = null;
					if(channels != null && channels.size() > 0)
						data = parseReportData(temService.getChannelReportData(channels, _startDate, _endDate));
					else
						data = new HashMap<String, Object>();
					result.put("data", data);
					result.put("status", "0");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}
	
	@RequestMapping(value="/report/channel/download", method = RequestMethod.GET)
	public void downloadAllChannels(HttpServletRequest request, HttpServletResponse response, long userid, int id, String start, String end, Integer type) throws IOException{
		OutputStream out = null;
		try{
			// check user role
			userid = PinaoUtils.getUserid(request, userid);
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() == 1){
					if(type == null)
						type = 0;
					
					// txt
					if(type == 0){
						response.setContentType("text/plain");
						String oriFileName = "历史通道趋势数据.txt";
						String agent = request.getHeader("USER-AGENT");
						if (null != agent && -1 != agent.indexOf("Firefox")) {
							response.setHeader("Content-disposition", "attachment;filename=" + new String(oriFileName.getBytes("utf-8"),"iso-8859-1"));
						} else {
							response.setHeader("Content-disposition", "attachment;filename=" + java.net.URLEncoder.encode(oriFileName, "UTF-8"));
						}
						out = response.getOutputStream();
						out.write((PinaoConstants.FILE_COMMENT_PREFIX + "机器-通道-距离" + PinaoConstants.TEM_DATA_COL_SEP + "时间" + PinaoConstants.TEM_DATA_COL_SEP + "温度" + PinaoConstants.TEM_DATA_COL_SEP + "斯托克斯" + PinaoConstants.TEM_DATA_COL_SEP + "反斯托克斯" + PinaoConstants.TEM_DATA_LINE_SEP).getBytes());
						
						Map<Machine, List<Channel>> machines = areaService.getAllChannels(userid);
						List<Channel> channels = new ArrayList<Channel>();
						if(machines != null && machines.size() > 0)
							for(Machine machine : machines.keySet())
								for(Channel channel : machines.get(machine))
									if(channel.getId() == id){
										channels.add(channel);
										break;
									}
	
						Date startDate = PinaoUtils.getDate(start);
						Date endDate = PinaoUtils.getDate(end);
						if(channels != null && channels.size() > 0 && startDate != null && endDate != null && !endDate.before(startDate)){
							ReportData data = temService.getChannelReportData(channels, startDate, endDate);
							if(data != null){
								Map<String, Map<Date, Double>> tems = data.getTems();
								Map<String, Map<Date, Double>> stocks = data.getStocks();
								Map<String, Map<Date, Double>> unstocks = data.getUnstocks();
								
								for(String key : tems.keySet()){
									Map<Date, Double> _temps = data.getTems().get(key);
									Map<Date, Double> _stocks = null;
									Map<Date, Double> _unstocks = null;
									if(stocks.containsKey(key))
										_stocks = stocks.get(key);
									if(unstocks.containsKey(key))
										_unstocks = unstocks.get(key);
									
									for(Date _key : _temps.keySet()){
										out.write(key.getBytes());
										out.write(PinaoConstants.TEM_DATA_COL_SEP.getBytes());
										out.write(PinaoUtils.getDateString(_key).getBytes());
										out.write(PinaoConstants.TEM_DATA_COL_SEP.getBytes());
										out.write(_temps.get(_key).toString().getBytes());
										out.write(PinaoConstants.TEM_DATA_COL_SEP.getBytes());
										if(_stocks != null && _stocks.containsKey(_key))
											out.write(_stocks.get(_key).toString().getBytes());
										out.write(PinaoConstants.TEM_DATA_COL_SEP.getBytes());
										if(_unstocks != null && _unstocks.containsKey(_key))
											out.write(_unstocks.get(_key).toString().getBytes());
										out.write(PinaoConstants.TEM_DATA_LINE_SEP.getBytes());
									}
								}
							}
						}
					} else {
						// xls
						response.setContentType("application/msexcel");
						String oriFileName = "历史通道趋势数据.xls";
						String agent = request.getHeader("USER-AGENT");
						if (null != agent && -1 != agent.indexOf("Firefox")) {
							response.setHeader("Content-disposition", "attachment;filename=" + new String(oriFileName.getBytes("utf-8"),"iso-8859-1"));
						} else {
							response.setHeader("Content-disposition", "attachment;filename=" + java.net.URLEncoder.encode(oriFileName, "UTF-8"));
						}
						
						HSSFWorkbook book = new HSSFWorkbook();
						HSSFSheet sheet = book.createSheet("历史通道趋势数据");
						int rowindex = 0;
						HSSFRow row = sheet.createRow(rowindex++);
						int columnIndex = 0;
						HSSFCell cell = row.createCell(columnIndex++);
						cell.setCellValue("机器-通道-距离");
						cell = row.createCell(columnIndex++);
						cell.setCellValue("时间");
						cell = row.createCell(columnIndex++);
						cell.setCellValue("温度");
						cell = row.createCell(columnIndex++);
						cell.setCellValue("斯托克斯");
						cell = row.createCell(columnIndex++);
						cell.setCellValue("反斯托克斯");
						
						Map<Machine, List<Channel>> machines = areaService.getAllChannels(userid);
						List<Channel> channels = null;
						if(machines != null && machines.size() > 0)
							for(Machine machine : machines.keySet())
								if(machine.getId() == id){
									channels = machines.get(machine);
									break;
								}
	
						Date startDate = PinaoUtils.getDate(start);
						Date endDate = PinaoUtils.getDate(end);
						if(channels != null && channels.size() > 0 && startDate != null && endDate != null && !endDate.before(startDate)){
							ReportData data = temService.getChannelReportData(channels, startDate, endDate);
							if(data != null){
								Map<String, Map<Date, Double>> tems = data.getTems();
								Map<String, Map<Date, Double>> stocks = data.getStocks();
								Map<String, Map<Date, Double>> unstocks = data.getUnstocks();
								
								for(String key : tems.keySet()){
									Map<Date, Double> _temps = data.getTems().get(key);
									Map<Date, Double> _stocks = null;
									Map<Date, Double> _unstocks = null;
									if(stocks.containsKey(key))
										_stocks = stocks.get(key);
									if(unstocks.containsKey(key))
										_unstocks = unstocks.get(key);
									
									for(Date _key : _temps.keySet()){
										row = sheet.createRow(rowindex++);
										columnIndex = 0;
										cell = row.createCell(columnIndex++);
										cell.setCellValue(key);
										cell = row.createCell(columnIndex++);
										cell.setCellValue(PinaoUtils.getDateString(_key));
										cell = row.createCell(columnIndex++);
										cell.setCellValue(_temps.get(_key));
										if(_stocks != null && _stocks.containsKey(_key)){
											cell = row.createCell(columnIndex++);
											cell.setCellValue(_stocks.get(_key));
										}
										if(_unstocks != null && _unstocks.containsKey(_key)){
											cell = row.createCell(columnIndex++);
											cell.setCellValue(_unstocks.get(_key));
										}
									}
								}
							}
						}

						out = response.getOutputStream();
						book.write(out);
					}
				} else
					response.sendRedirect(request.getContextPath() + "/" + "system/error.html");
			} else
				response.sendRedirect(request.getContextPath() + "/" + "system/error.html");
		} catch(Throwable t){
			t.printStackTrace();
			response.sendRedirect(request.getContextPath() + "/" + "system/error.html");
		} finally {
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (Exception e) {
				// 文件已下载完毕，不需要处理
			}
		}
	}

	@RequestMapping(value="/monitor/channel/alarm", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getChannelAlarmData(HttpServletRequest request, long userid, int id, Integer start, Integer step){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			userid = PinaoUtils.getUserid(request, userid);
			result.put("status", "400");
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			// check user role
			if(obj != null && obj instanceof User){
				User user = (User)obj;
				if(user.getRole() == 1){
					Map<Machine, List<Channel>> machines = areaService.getAllChannels(userid);
					List<Channel> channels = new ArrayList<Channel>();
					if(machines != null && machines.size() > 0)
						for(Machine machine : machines.keySet())
							for(Channel channel : machines.get(machine))
								if(channel.getId() == id){
									channels.add(channel);
									break;
								}

					if(start == null || start < 0)
						start = 0;
					if(step == null || step <= 0)
						step = 100;
					List<Map<String, Object>> data = null;
					if(channels != null && channels.size() > 0)
						data = parseAlarmData(temService.getChannelAlarmData(channels, start, step));
					else
						data = new ArrayList<Map<String,Object>>();
					result.put("data", data);
					result.put("count", temService.getChannelAlarmCount(channels));
					result.put("status", "0");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/monitor/channel/data", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getChannelData(HttpServletRequest request, long userid, int id, long time){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			userid = PinaoUtils.getUserid(request, userid);
			result.put("status", "400");
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			// check user role
			if(obj != null && obj instanceof User){
				User user = (User)obj;
				if(user.getRole() == 1){
					Map<Machine, List<Channel>> machines = areaService.getAllChannels(userid);
					List<Channel> channels = new ArrayList<Channel>();
					if(machines != null && machines.size() > 0)
						for(Machine machine : machines.keySet())
							for(Channel channel : machines.get(machine))
								if(channel.getId() == id){
									channels.add(channel);
									break;
								}

					Map<String, Object> data = null;
					if(channels != null && channels.size() > 0)
						data = parseChannelData(temService.getChannelData(channels, time));
					else
						data = new HashMap<String, Object>();
					result.put("data", data);
					result.put("interval", configService.getConfigByType(Config.TYPE_REFRESH_INTERVAL_FLAG).getValue());
					result.put("status", "0");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/report/machine/alarm", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getMachineAlarmReportData(HttpServletRequest request, long userid, int id, Integer start, Integer step, String startDate, String endDate){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			userid = PinaoUtils.getUserid(request, userid);
			result.put("status", "400");
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			// check user role
			if(obj != null && obj instanceof User){
				User user = (User)obj;
				if(user.getRole() == 1){
					Map<Machine, List<Channel>> machines = areaService.getAllChannels(userid);
					List<Channel> channels = null;
					if(machines != null && machines.size() > 0)
						for(Machine machine : machines.keySet())
							if(machine.getId() == id){
								channels = machines.get(machine);
								break;
							}

					if(start == null || start < 0)
						start = 0;
					if(step == null || step <= 0)
						step = 100;
					List<Map<String, Object>> data = null;
					Date _startDate = PinaoUtils.getDate(startDate);
					Date _endDate = PinaoUtils.getDate(endDate);
					int count = 0;
					if(channels != null && channels.size() > 0 && _startDate != null && _endDate != null && !_endDate.before(_startDate)){
						data = parseAlarmData(temService.getChannelAlarmReportData(channels, start, step, _startDate, _endDate));
						count = temService.getChannelAlarmReportCount(channels, _startDate, _endDate);
					} else
						data = new ArrayList<Map<String,Object>>();
					result.put("data", data);
					result.put("count", count);
					result.put("status", "0");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/report/machine/data", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getMachineReportData(HttpServletRequest request, long userid, int id, String start, String end){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			userid = PinaoUtils.getUserid(request, userid);
			result.put("status", "400");
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			// check user role
			if(obj != null && obj instanceof User){
				User user = (User)obj;
				if(user.getRole() == 1){
					Map<Machine, List<Channel>> machines = areaService.getAllChannels(userid);
					List<Channel> channels = null;
					if(machines != null && machines.size() > 0)
						for(Machine machine : machines.keySet())
							if(machine.getId() == id){
								channels = machines.get(machine);
								break;
							}

					Date _startDate = PinaoUtils.getDate(start);
					Date _endDate = PinaoUtils.getDate(end);
					Map<String, Object> data = null;
					if(channels != null && channels.size() > 0)
						data = parseReportData(temService.getChannelReportData(channels, _startDate, _endDate));
					else
						data = new HashMap<String, Object>();
					result.put("data", data);
					result.put("status", "0");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}
	

	
	@RequestMapping(value="/report/machine/download", method = RequestMethod.GET)
	public void downloadAllMachines(HttpServletRequest request, HttpServletResponse response, long userid, int id, String start, String end, Integer type) throws IOException{
		OutputStream out = null;
		try{
			// check user role
			userid = PinaoUtils.getUserid(request, userid);
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() == 1){
					if(type == null)
						type = 0;
					
					// txt
					if(type == 0){
						response.setContentType("text/plain");
						String oriFileName = "历史机器趋势数据.txt";
						String agent = request.getHeader("USER-AGENT");
						if (null != agent && -1 != agent.indexOf("Firefox")) {
							response.setHeader("Content-disposition", "attachment;filename=" + new String(oriFileName.getBytes("utf-8"),"iso-8859-1"));
						} else {
							response.setHeader("Content-disposition", "attachment;filename=" + java.net.URLEncoder.encode(oriFileName, "UTF-8"));
						}
						out = response.getOutputStream();
						out.write((PinaoConstants.FILE_COMMENT_PREFIX + "机器-通道-距离" + PinaoConstants.TEM_DATA_COL_SEP + "时间" + PinaoConstants.TEM_DATA_COL_SEP + "温度" + PinaoConstants.TEM_DATA_COL_SEP + "斯托克斯" + PinaoConstants.TEM_DATA_COL_SEP + "反斯托克斯" + PinaoConstants.TEM_DATA_LINE_SEP).getBytes());
						
						Map<Machine, List<Channel>> machines = areaService.getAllChannels(userid);
						List<Channel> channels = null;
						if(machines != null && machines.size() > 0)
							for(Machine machine : machines.keySet())
								if(machine.getId() == id){
									channels = machines.get(machine);
									break;
								}
	
						Date startDate = PinaoUtils.getDate(start);
						Date endDate = PinaoUtils.getDate(end);
						if(channels != null && channels.size() > 0 && startDate != null && endDate != null && !endDate.before(startDate)){
							ReportData data = temService.getChannelReportData(channels, startDate, endDate);
							if(data != null){
								Map<String, Map<Date, Double>> tems = data.getTems();
								Map<String, Map<Date, Double>> stocks = data.getStocks();
								Map<String, Map<Date, Double>> unstocks = data.getUnstocks();
								
								for(String key : tems.keySet()){
									Map<Date, Double> _temps = data.getTems().get(key);
									Map<Date, Double> _stocks = null;
									Map<Date, Double> _unstocks = null;
									if(stocks.containsKey(key))
										_stocks = stocks.get(key);
									if(unstocks.containsKey(key))
										_unstocks = unstocks.get(key);
									
									for(Date _key : _temps.keySet()){
										out.write(key.getBytes());
										out.write(PinaoConstants.TEM_DATA_COL_SEP.getBytes());
										out.write(PinaoUtils.getDateString(_key).getBytes());
										out.write(PinaoConstants.TEM_DATA_COL_SEP.getBytes());
										out.write(_temps.get(_key).toString().getBytes());
										out.write(PinaoConstants.TEM_DATA_COL_SEP.getBytes());
										if(_stocks != null && _stocks.containsKey(_key))
											out.write(_stocks.get(_key).toString().getBytes());
										out.write(PinaoConstants.TEM_DATA_COL_SEP.getBytes());
										if(_unstocks != null && _unstocks.containsKey(_key))
											out.write(_unstocks.get(_key).toString().getBytes());
										out.write(PinaoConstants.TEM_DATA_LINE_SEP.getBytes());
									}
								}
							}
						}
					} else{
						// xls
						response.setContentType("application/msexcel");
						String oriFileName = "历史机器趋势数据.xls";
						String agent = request.getHeader("USER-AGENT");
						if (null != agent && -1 != agent.indexOf("Firefox")) {
							response.setHeader("Content-disposition", "attachment;filename=" + new String(oriFileName.getBytes("utf-8"),"iso-8859-1"));
						} else {
							response.setHeader("Content-disposition", "attachment;filename=" + java.net.URLEncoder.encode(oriFileName, "UTF-8"));
						}
						
						HSSFWorkbook book = new HSSFWorkbook();
						HSSFSheet sheet = book.createSheet("历史机器趋势数据");
						int rowindex = 0;
						HSSFRow row = sheet.createRow(rowindex++);
						int columnIndex = 0;
						HSSFCell cell = row.createCell(columnIndex++);
						cell.setCellValue("机器-通道-距离");
						cell = row.createCell(columnIndex++);
						cell.setCellValue("时间");
						cell = row.createCell(columnIndex++);
						cell.setCellValue("温度");
						cell = row.createCell(columnIndex++);
						cell.setCellValue("斯托克斯");
						cell = row.createCell(columnIndex++);
						cell.setCellValue("反斯托克斯");
						
						Map<Machine, List<Channel>> machines = areaService.getAllChannels(userid);
						List<Channel> channels = null;
						if(machines != null && machines.size() > 0)
							for(Machine machine : machines.keySet())
								if(machine.getId() == id){
									channels = machines.get(machine);
									break;
								}
	
						Date startDate = PinaoUtils.getDate(start);
						Date endDate = PinaoUtils.getDate(end);
						if(channels != null && channels.size() > 0 && startDate != null && endDate != null && !endDate.before(startDate)){
							ReportData data = temService.getChannelReportData(channels, startDate, endDate);
							if(data != null){
								Map<String, Map<Date, Double>> tems = data.getTems();
								Map<String, Map<Date, Double>> stocks = data.getStocks();
								Map<String, Map<Date, Double>> unstocks = data.getUnstocks();
								
								for(String key : tems.keySet()){
									Map<Date, Double> _temps = data.getTems().get(key);
									Map<Date, Double> _stocks = null;
									Map<Date, Double> _unstocks = null;
									if(stocks.containsKey(key))
										_stocks = stocks.get(key);
									if(unstocks.containsKey(key))
										_unstocks = unstocks.get(key);
									
									for(Date _key : _temps.keySet()){
										row = sheet.createRow(rowindex++);
										columnIndex = 0;
										cell = row.createCell(columnIndex++);
										cell.setCellValue(key);
										cell = row.createCell(columnIndex++);
										cell.setCellValue(PinaoUtils.getDateString(_key));
										cell = row.createCell(columnIndex++);
										cell.setCellValue(_temps.get(_key));
										if(_stocks != null && _stocks.containsKey(_key)){
											cell = row.createCell(columnIndex++);
											cell.setCellValue(_stocks.get(_key));
										}
										if(_unstocks != null && _unstocks.containsKey(_key)){
											cell = row.createCell(columnIndex++);
											cell.setCellValue(_unstocks.get(_key));
										}
									}
								}
							}
						}

						out = response.getOutputStream();
						book.write(out);
					}
				} else
					response.sendRedirect(request.getContextPath() + "/" + "system/error.html");
			} else
				response.sendRedirect(request.getContextPath() + "/" + "system/error.html");
		} catch(Throwable t){
			t.printStackTrace();
			response.sendRedirect(request.getContextPath() + "/" + "system/error.html");
		} finally {
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (Exception e) {
				// 文件已下载完毕，不需要处理
			}
		}
	}

	@RequestMapping(value="/monitor/machine/alarm", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getMachineAlarmData(HttpServletRequest request, long userid, int id, Integer start, Integer step){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			userid = PinaoUtils.getUserid(request, userid);
			result.put("status", "400");
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			// check user role
			if(obj != null && obj instanceof User){
				User user = (User)obj;
				if(user.getRole() == 1){
					Map<Machine, List<Channel>> machines = areaService.getAllChannels(userid);
					List<Channel> channels = null;
					if(machines != null && machines.size() > 0)
						for(Machine machine : machines.keySet())
							if(machine.getId() == id){
								channels = machines.get(machine);
								break;
							}
					if(start == null || start < 0)
						start = 0;
					if(step == null || step <= 0)
						step = 100;
					List<Map<String, Object>> data = null;
					if(channels != null && channels.size() > 0)
						data = parseAlarmData(temService.getChannelAlarmData(channels, start, step));
					else
						data = new ArrayList<Map<String,Object>>();
					result.put("data", data);
					result.put("count", temService.getChannelAlarmCount(channels));
					result.put("status", "0");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/monitor/machine/data", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getMachineData(HttpServletRequest request, long userid, int id, long time){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			userid = PinaoUtils.getUserid(request, userid);
			result.put("status", "400");
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			// check user role
			if(obj != null && obj instanceof User){
				User user = (User)obj;
				if(user.getRole() == 1){
					Map<Machine, List<Channel>> machines = areaService.getAllChannels(userid);
					List<Channel> channels = null;
					if(machines != null && machines.size() > 0)
						for(Machine machine : machines.keySet())
							if(machine.getId() == id){
								channels = machines.get(machine);
								break;
							}
					Map<String, Object> data = null;
					if(channels != null && channels.size() > 0)
						data = parseChannelData(temService.getChannelData(channels, time));
					else
						data = new HashMap<String, Object>();
					result.put("data", data);
					result.put("interval", configService.getConfigByType(Config.TYPE_REFRESH_INTERVAL_FLAG).getValue());
					result.put("status", "0");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	private Map<String, Object> parseChannelData(ChannelMonitorData data) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		if(data != null && data.getData() != null && data.getData().size() > 0){
			result.put("max", PinaoUtils.getFloat(data.getMax()));
			result.put("min", PinaoUtils.getFloat(data.getMin()));
			result.put("avg", PinaoUtils.getFloat(data.getAvg()));
			result.put("time", data.getTime());
			result.put("data", data.getData());
		}
		
		return result;
	}

	@RequestMapping(value="/report/channels", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getReportChannels(HttpServletRequest request, long userid){
		return getChannels(request, userid);
	}

	@RequestMapping(value="/monitor/channels", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getChannels(HttpServletRequest request, long userid){
		Map<Object, Object> result = new HashMap<Object, Object>();

		try{
			userid = PinaoUtils.getUserid(request, userid);
			result.put("status", "400");
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			// check user role
			if(obj != null && obj instanceof User){
				User user = (User)obj;
				if(user.getRole() == 1){
					Map<Machine, List<Channel>> channels = areaService.getAllChannels(userid);
					result.put("data", parseMachines(channels));
					result.put("status", "0");
				} else
					result.put("status", "600");
			} else
				result.put("status", "500");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	private List<Map<String, Object>> parseMachines(Map<Machine, List<Channel>> channels) {
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		
		if(channels != null && channels.size() > 0)
			for(Machine machine : channels.keySet()){
				Map<String, Object> tmp = new HashMap<String, Object>();
				
				tmp.put("id", machine.getId());
				tmp.put("name", machine.getName());
				tmp.put("channels", parseChannels(channels.get(machine)));
				
				data.add(tmp);
			}
		
		return data;
	}

	private List<Map<String, Object>> parseChannels(List<Channel> channels) {
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		
		if(channels != null)
			for(Channel channel : channels){
				Map<String, Object> tmp = new HashMap<String, Object>();
				
				tmp.put("id", channel.getId());
				tmp.put("name", channel.getName());
				
				data.add(tmp);
			}
		
		return data;
	}
}
