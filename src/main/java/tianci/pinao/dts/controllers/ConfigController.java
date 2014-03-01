package tianci.pinao.dts.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tianci.pinao.dts.models.Config;
import tianci.pinao.dts.service.ConfigService;

@Controller
public class ConfigController {
	
	@Autowired
	private ConfigService configService;

	@RequestMapping(value="/config/get", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getConfig(int type){
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			Config config = configService.getConfigByType(type);
			result.put("data", parseConfig(config));
			result.put("status", "0");
		} catch(Throwable t){
			t.printStackTrace();
			result.put("status", "400");
		}
		return result;
	}

	@RequestMapping(value="/config/update", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> updateConfig(Config config, int userid){
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			boolean success = configService.updateConfig(config, userid);
			if(success)
				result.put("status", "0");
			else
				result.put("status", "400");
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
