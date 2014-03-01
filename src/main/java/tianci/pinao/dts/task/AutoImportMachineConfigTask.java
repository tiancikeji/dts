package tianci.pinao.dts.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tianci.pinao.dts.models.Machine;
import tianci.pinao.dts.service.AreaService;
import tianci.pinao.dts.util.PinaoConstants;

public class AutoImportMachineConfigTask {
	
	private Log logger = LogFactory.getLog(getClass());
	
	private String configName = "machines.txt";
	
	private AreaService areaService;

	public void run(){
		BufferedReader br = null;
		try{
			// check db
			List<Machine> tmp = areaService.getAllMachines();
			if(tmp == null || tmp.size() <= 0){
				// read from file
				br = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(configName)));
				String line = br.readLine();
				List<Machine> machines = new ArrayList<Machine>();
				while(line != null){
					line = StringUtils.trimToEmpty(line);
					if(StringUtils.isNotBlank(line) && !StringUtils.startsWith(line, PinaoConstants.FILE_COMMENT_PREFIX)){
						String[] cols = StringUtils.split(line, PinaoConstants.TEM_DATA_COL_SEP);
						if(cols != null && cols.length >= 3){
							Machine machine = new Machine();
							machine.setName(cols[0]);
							machine.setSerialPort(cols[1]);
							machine.setBaudRate(cols[2]);
							machines.add(machine);
						}
					}
					line = br.readLine();
				}
				
				// save db
				if(machines != null && machines.size() > 0)
					areaService.addMachines(machines, -1);
			}
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in AutoImportMachineConfigTask.run >> ", t);
			if(br != null)
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public AreaService getAreaService() {
		return areaService;
	}

	public void setAreaService(AreaService areaService) {
		this.areaService = areaService;
	}
}
