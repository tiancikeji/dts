package tianci.pinao.dts.controllers;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tianci.pinao.dts.models.Config;
import tianci.pinao.dts.models.Log;
import tianci.pinao.dts.models.User;
import tianci.pinao.dts.service.ConfigService;
import tianci.pinao.dts.service.LogService;
import tianci.pinao.dts.util.PinaoUtils;

@Controller
public class ConfigController {
	
	@Autowired
	private ConfigService configService;

	@Autowired
	private LogService logService;

	@RequestMapping(value="/project/info", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getProjectInfo(HttpServletRequest request, long userid){
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			userid = PinaoUtils.getUserid(request, userid);
			result.put("status", "400");
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User)obj;
				if(configService.checkLifeTime() || user.getRole() == 1){
					StringBuilder sb = new StringBuilder();
					Scanner sc = new Scanner(new FileInputStream(request.getSession().getServletContext().getRealPath("/") + "/assets/project.txt"), "utf-8");
					while(sc.hasNextLine()){
						sb.append(sc.nextLine());
						sb.append("\n");
					}
					
					Map<String, String> data = new HashMap<String, String>();
					data.put("content", sb.toString());
					data.put("image", "/assets/project.jpg");
					
					result.put("data", data);
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

	@RequestMapping(value="/log/history", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getLogHistory(HttpServletRequest request, long userid, String startDate, String endDate, Integer start, Integer step){
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			// check user role
			userid = PinaoUtils.getUserid(request, userid);
			result.put("status", "400");
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){	
				User user = (User)obj;
				if(configService.checkLifeTime() || user.getRole() == 1){	
					Date _startDate = PinaoUtils.getDate(startDate);
					Date _endDate = PinaoUtils.getDate(endDate);
					
					if(start == null)
						start = 0;
					if(step == null)
						step = 100;
					
					List<Map<String, Object>> data = null;
					if(_startDate != null && _endDate != null && !_endDate.before(_startDate) && start >= 0 && step > 0){
						data = parseLog(logService.getLogs(_startDate, _endDate, start, step));
					} else
						data = new ArrayList<Map<String,Object>>();
					result.put("data", data);
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

	private List<Map<String, Object>> parseLog(List<Log> logs) {
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		 
		if(logs != null && logs.size() > 0)
			for(Log log : logs){
				Map<String, Object> tmp = new HashMap<String, Object>();
				
				tmp.put("id", log.getId());
				tmp.put("type", log.getType());
				tmp.put("value", log.getValue());
				tmp.put("source", log.getSource());
				tmp.put("time", PinaoUtils.getDateString(log.getLastModTime()));
				
				data.add(tmp);
			}
		 
		return data;
	}

	@RequestMapping(value="/config/get", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getConfig(HttpServletRequest request, int type){
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			// check user role
			long userid = PinaoUtils.getUserid(request, 0);
			result.put("status", "400");
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() == 1){
					Config config = configService.getConfigByType(type);
					result.put("data", parseConfig(config));
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

	@RequestMapping(value="/config/update", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> updateConfig(HttpServletRequest request, Config config, long userid){
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			// check user role
			userid = PinaoUtils.getUserid(request, userid);
			result.put("status", "400");
			Object obj = PinaoUtils.getUserFromSession(request, userid);
			if(obj != null && obj instanceof User){
				User user = (User) obj;
				if(user.getRole() == 1){
					boolean success = configService.updateConfig(config, new Long(userid).intValue());
					if(success)
						result.put("status", "0");
					else
						result.put("status", "400");
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

	private Map<String, Object> parseConfig(Config config) {
		Map<String, Object> data = new HashMap<String, Object>();
		
		if(config != null){
			data.put("id", config.getId());
			data.put("type", config.getType());
			data.put("value", config.getValue());
			if(config.getType() == Config.TYPE_LIFE_TIME_FLAG)
				data.put("used", config.getUsed());
		}
		
		return data;
	}
}
