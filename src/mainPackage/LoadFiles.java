package mainPackage;

import java.io.BufferedReader;
import java.io.FileReader;

import pojoClasses.App;
import pojoClasses.Instance;
import pojoClasses.Machine;
import utils.PathConstants;

/**
 * @author vishn
 *
 */
public class LoadFiles
{

	public static void loadAllFiles() throws Exception
	{
		BufferedReader br = null;
		String line = PathConstants.emptyString;

		// loading machineResourceFile
		br = new BufferedReader(new FileReader(PathConstants.machineResourceFile));
		while ((line = br.readLine()) != null)
		{
			String[] resource = line.split(PathConstants.csvDelimiter);
			Machine machine = new Machine(resource);
			// System.out.println("machine added: " + machine.toString());
			Constants.MACHINEVSMACHINEINFO.put(machine.getName(), machine);
		}
		br.close();

		// loading machineResourceFile
		br = new BufferedReader(new FileReader(PathConstants.appResourcesFile));
		while ((line = br.readLine()) != null)
		{
			String[] resource = line.split(PathConstants.csvDelimiter);
			App app = new App(resource);
			// System.out.println("app added: " + app.toString());
			Constants.APPVSAPPINFO.put(app.getName(), app);
		}
		br.close();

		// loading interference resource file
		br = new BufferedReader(new FileReader(PathConstants.appInterferenceFile));
		while ((line = br.readLine()) != null)
		{
			String[] resource = line.split(PathConstants.csvDelimiter);
			App firstApp = Constants.APPVSAPPINFO.get(resource[0]);
			App secondApp = Constants.APPVSAPPINFO.get(resource[1]);
			int limit = Integer.parseInt(resource[2]);
			System.out.println("app " + firstApp.getName() + " is allergic to " + limit + " instances of " + secondApp.getName());
			firstApp.addInterference(secondApp, limit);// should also add other way around??
		}
		br.close();

		// loading deploy file
		int instancesNotScheduled = 0;
		br = new BufferedReader(new FileReader(PathConstants.instanceDeployFile));
		while ((line = br.readLine()) != null)
		{
			String[] resource = line.split(PathConstants.csvDelimiter);
			String instanceName = resource[0];
			String appName = resource[1];
			App app = Constants.APPVSAPPINFO.get(appName);
			Instance instance = Constants.INSTANCEVSINSTANCEINFO.get(instanceName);
			if (instance == null)
			{
				instance = new Instance(instanceName, app);
				Constants.INSTANCEVSINSTANCEINFO.put(instanceName, instance);
			}
			app.addInstance(instance);
			System.out.println(app.getName() + " has instance: " + instanceName);

			if (resource.length < 3)
			{
				instancesNotScheduled++;
				System.out.println(instance.getName() + " belongs to app " + app.getName() + " is not scheduled yet");
				continue;
			}
			String machineName = resource[2];
			if (!machineName.matches(PathConstants.emptyString))
			{
				Machine machine = Constants.MACHINEVSMACHINEINFO.get(machineName);
				//machine.addInstanceIfPossible(instance);
				machine.addInstanceBlindly(instance, true);
				System.out.println(instance.getName() + " belongs to app " + app.getName() + " is scheduled to run on " + machine.getName());
			}
		}
		System.out.println(instancesNotScheduled + " instances are yet to be scheduled");
		br.close();
	}
}