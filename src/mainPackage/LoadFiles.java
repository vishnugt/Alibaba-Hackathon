package mainPackage;

import java.io.BufferedReader;
import java.io.FileReader;

import pojoClasses.App;
import pojoClasses.Machine;
import utils.PathConstants;

/**
 * @author vishn
 *
 */
public class LoadFiles {

	public static void loadAll() throws Exception 
	{
		BufferedReader br = null;
		String line = PathConstants.emptyString;
		
		//loading machineResourceFile
		br = new BufferedReader(new FileReader(PathConstants.machineResourceFile));
		while ((line = br.readLine()) != null) 
		{
			String[] resource = line.split(PathConstants.csvDelimiter);
			Machine machine = new Machine(resource);
			System.out.println("machine added: " + machine.toString());
			Constants.MACHINEVSMACHINEINFO.put(machine.getName(), machine);
		}
		br.close();
		
		//loading machineResourceFile
		br = new BufferedReader(new FileReader(PathConstants.appResourcesFile));
		while ((line = br.readLine()) != null) 
		{
			String[] resource = line.split(PathConstants.csvDelimiter);
			App app = new App(resource);
			System.out.println("app added: " + app.toString());
			Constants.APPVSAPPINFO.put(app.getName(), app);
		}
		
		
		
		br.close();
	}
}