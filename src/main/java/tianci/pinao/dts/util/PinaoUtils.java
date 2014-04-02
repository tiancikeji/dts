package tianci.pinao.dts.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

public class PinaoUtils {

	public static String LOCAL_MAC_ADDRESS = "unknown";
	
	static{
		try {
			StringBuffer sb = new StringBuffer();
			NetworkInterface ni = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
			byte[] macs = ni.getHardwareAddress();

			if(macs != null)
				for (int i = 0; i < macs.length; i++) {
					LOCAL_MAC_ADDRESS = Integer.toHexString(macs[i] & 0xFF);
	
					if (LOCAL_MAC_ADDRESS.length() == 1)
						LOCAL_MAC_ADDRESS = '0' + LOCAL_MAC_ADDRESS;
	
					sb.append(LOCAL_MAC_ADDRESS + "-");
	
					LOCAL_MAC_ADDRESS = sb.toString();
					LOCAL_MAC_ADDRESS = LOCAL_MAC_ADDRESS.substring(0, LOCAL_MAC_ADDRESS.length() - 1);
				}

		} catch (Throwable e) {
			InetAddress addr = null;  
	        try {  
	            addr = InetAddress.getLocalHost();  
	        } catch (Throwable t) {  
	            t.printStackTrace();
	        }  
	        if(addr != null){
		        byte[] ipAddr = addr.getAddress();  
		        String ipAddrStr = "";  
		        for (int i = 0; i < ipAddr.length; i++) {  
		            if (i > 0) {  
		                ipAddrStr += ".";  
		            }  
		            ipAddrStr += ipAddr[i] & 0xFF;  
		        }  
		        LOCAL_MAC_ADDRESS = ipAddrStr;
	        }
		}
	}

	public static Date getDate(String date) {
		if(StringUtils.isNotBlank(date)){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				return sdf.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String getDateString(Date date) {
		if(date != null )
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		return null;
	}

	public static long getUserid(HttpServletRequest request, long userid) {
		if(request != null){
			Cookie[] cookies = request.getCookies();
			if(cookies != null && cookies.length > 0)
				for(Cookie cookie : cookies)
					if(StringUtils.equals("userid", cookie.getName())){
						return NumberUtils.toLong(cookie.getValue(), 0);
					}
		}
		
		return 0;
	}

	public static Object getUserFromSession(HttpServletRequest request, long userid) {
		return request.getSession().getAttribute("" + userid);
		/*User user = new User();
		user.setRole(1);
		request.getSession().setAttribute("" + userid, user);
		return user;*/
	}
	
	public static Double getFloat(Object obj){
		return Double.parseDouble(String.format("%.1f", obj));
	}
}
