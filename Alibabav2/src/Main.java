import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class Main
{
	static HashMap<String, Double[]> machineVsmachineResource;
	static HashMap<String, Double[]> machineVsusedMachineResource;
	static HashMap<String, Double[]> appVsAppResource;
	static HashMap<String, String> instanceVsApp;
	static HashMap<String, String> instanceVsMachine;
	static HashMap<String, List<String>> machineVsInstances;
	static HashMap<String, HashMap<String, Integer>> appVsAppInterference;

	static
	{
		machineVsmachineResource = new HashMap<>();
		machineVsusedMachineResource = new HashMap<>();
		appVsAppResource = new HashMap<>();
		instanceVsApp = new HashMap<>();
		instanceVsMachine = new HashMap<>();
		machineVsInstances = new HashMap<>();
		appVsAppInterference = new HashMap<>();
	}

	static int numConstraints = 200;

	public static void main(String[] args) throws Exception
	{
		String line = new String();
		BufferedReader br = null;

		br = new BufferedReader(new FileReader(Utility.machineResourceFile));
		while ((line = br.readLine()) != null)
		{
			String[] resource = line.split(",");
			Double[] machineResource = new Double[200];
			Double[] machineUsedResource = new Double[200];
			for (int i = 3; i < 7; ++i)
			{
				machineResource[i + 193] = Double.parseDouble(resource[i]);
			}
			for (int i = 0; i < 98; ++i)
			{
				machineResource[i] = Double.parseDouble(resource[1]);
				machineResource[i + 98] = Double.parseDouble(resource[2]);
			}
			for (int i = 0; i < 200; ++i)
			{
				machineUsedResource[i] = 0.0d;
			}
			machineVsmachineResource.put(resource[0], machineResource);
			machineVsusedMachineResource.put(resource[0], machineUsedResource);
		}
		br.close();

		br = new BufferedReader(new FileReader(Utility.appResourcesFile));
		while ((line = br.readLine()) != null)
		{
			String[] resource = line.split(",");
			Double[] appResources = new Double[200];
			String cpuResource[] = resource[1].split("\\|");
			String ramResource[] = resource[2].split("\\|");
			for (int i = 0; i < 98; ++i)
			{
				appResources[i] = Double.parseDouble(cpuResource[i]);
				appResources[i + 98] = Double.parseDouble(ramResource[i]);
			}
			for (int i = 196; i < 200; ++i)
			{
				appResources[i] = Double.parseDouble(resource[i - 193]);
			}
			appVsAppResource.put(resource[0], appResources);
		}
		br.close();

		br = new BufferedReader(new FileReader(Utility.instanceDeployFile));
		while ((line = br.readLine()) != null)
		{
			String[] resource = line.split(",");
			String instanceName = resource[0];
			String appName = resource[1];
			instanceVsApp.put(instanceName, appName);
			if (resource.length > 2)
			{
				String machineName = resource[2];
				instanceVsMachine.put(instanceName, machineName);
				List<String> instanceList = machineVsInstances.get(machineName);
				if (instanceList == null)
				{
					machineVsInstances.put(machineName, new ArrayList<>());
				}
				machineVsInstances.get(machineName).add(resource[0]);
				Double[] appResource = appVsAppResource.get(instanceVsApp.get(instanceName));
				Double[] machineUsedResource = machineVsusedMachineResource.get(machineName);

				for (int i = 0; i < 200; ++i)
				{
					machineUsedResource[i] += appResource[i];
				}
				machineVsusedMachineResource.put(machineName, machineUsedResource);
			}
		}
		br.close();

		int numNotAssigned = 0;
		int numAssigned = 0;

		Set<String> instanceList = instanceVsApp.keySet();
		for (String instance : instanceList)
		{
			if (instanceVsMachine.get(instance) == null)
			{
				boolean isAssigned = true;
				for (String machine : machineVsmachineResource.keySet())
				{
					Double[] appResource = appVsAppResource.get(instanceVsApp.get(instance));
					Double[] machineUsedResource = machineVsusedMachineResource.get(machine);
					Double[] machineResource = machineVsmachineResource.get(machine);
					Double[] updatedMachineUsedResources = machineVsusedMachineResource.get(machine);

					for (int i = 0; i < 200; ++i)
					{
						if (appResource[i] + machineUsedResource[i] < machineResource[i])
						{
							updatedMachineUsedResources[i] = appResource[i] + machineUsedResource[i];
						}
						else
						{
							isAssigned = false;
							break;
						}
					}
					if (isAssigned)
					{
						numAssigned++;
						machineVsusedMachineResource.put(machine, updatedMachineUsedResources);
						List<String> machineInstanceList = machineVsInstances.get(machine);
						if (machineInstanceList == null)
						{
							machineVsInstances.put(machine, new ArrayList<>());
						}
						machineVsInstances.get(machine).add(instance);
						instanceVsMachine.put(instance, machine);
						if (numAssigned % 1000 == 0)
						{
							System.out.println("Num assinged: " + numAssigned);
						}
						break;
					}
					numNotAssigned++;
				}
			}
		}

		System.out.println("Number of machines assigned: " + numAssigned);
		System.out.println("Number of machines not assigned " + numNotAssigned);

		PrintWriter pw = new PrintWriter(new File(Utility.outputFileName));
		StringBuilder sb = new StringBuilder();
		for (String instance : instanceVsMachine.keySet())
		{
			sb.append(instance);
			sb.append(",");
			sb.append(instanceVsMachine.get(instance));
			sb.append("\n");
		}
		pw.write(sb.toString());
		pw.close();
		System.out.println("Output ready!");

	}
}
