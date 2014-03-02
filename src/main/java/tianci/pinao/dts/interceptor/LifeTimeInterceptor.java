package tianci.pinao.dts.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import tianci.pinao.dts.models.Config;
import tianci.pinao.dts.models.License;
import tianci.pinao.dts.service.ConfigService;

public class LifeTimeInterceptor extends HandlerInterceptorAdapter {
	
	private ConfigService configService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if(checkLifeTime())
			return super.preHandle(request, response, handler);
		else{
			request.getRequestDispatcher("/lifetime.jsp").forward(request, response);  
            return false; 
		}
	}

	private boolean checkLifeTime() {
		System.out.println("##########################");
		System.out.println("##########################");
		System.out.println("##########################");
		System.out.println("##########################");
		System.out.println("##########################");
		Config config = configService.getLicenseConfig();
		if(config != null){
			List<License> licenses = configService.getAllLicenses();
			if(licenses != null && licenses.size() > 0)
				for(License license : licenses)
					if(license != null && license.getUseTime() >= config.getValue())
						return false;
			return true;
		} else
			return false;
	}

	public ConfigService getConfigService() {
		return configService;
	}

	public void setConfigService(ConfigService configService) {
		this.configService = configService;
	}

}
