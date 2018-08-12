package mainPackage;

import java.io.File;
import java.io.PrintWriter;
import java.text.DateFormat;

import pojoClasses.Instance;
import pojoClasses.Machine;
import utils.PathConstants;

/**
 * @author vishn
 *
 */
public class Main
{

	public static void main(String[] args) throws Exception
	{
		System.out.println("Program Init - " + DateFormat.getTimeInstance().getCalendar().getTime());
		init();
		assignInstances();
		writeOutput();
	}

	private static void writeOutput() throws Exception
	{
		PrintWriter pw = new PrintWriter(new File(PathConstants.outputFileName));
		StringBuilder sb = new StringBuilder();
		for (Instance instance : Constants.INSTANCEVSINSTANCEINFO.values())
		{
			if (instance.getMachine() == null || instance.getMachine().getIsDefault())
			{
				continue;
			}
			sb.append(instance.getName());
			sb.append(PathConstants.csvDelimiter);
			sb.append(instance.getMachine().getName());
			sb.append(PathConstants.newLine);
		}
		pw.write(sb.toString());
		pw.close();
		System.out.println("Output ready!");
	}

	private static void init()
	{
		try
		{
			LoadFiles.loadAllFiles();
		}
		catch (Exception e)
		{
			System.out.println("Exception while loading all files");
			e.printStackTrace();
		}
	}

	private static void assignInstances()
	{
		System.out.println("Total number of machines: " + Constants.MACHINEVSMACHINEINFO.size());
		int logicInsufficient = 0;
		int numAssigned = 0;
		for (Instance instance : Constants.INSTANCEVSINSTANCEINFO.values())
		{
			boolean isAssigned = false;
			if (instance.getMachine() == null)
			{
				for (Machine machine : Constants.MACHINEVSMACHINEINFO.values())
				{
					if (machine.addInstanceIfPossible(instance))
					{
						isAssigned = true;
						numAssigned++;
						if (numAssigned % 1000 == 0)
						{
							System.out.println("Assigned " + numAssigned);
						}
						break;
					}
				}
				if (!isAssigned)
				{
					logicInsufficient++;
					// System.out.println("Logic Insufficient");
				}
			}
		}
		System.out.println(logicInsufficient + " instances not able to assign");
		System.out.println(numAssigned + " instances assigned");

		int machineUsed = 0;
		for (Machine machine : Constants.MACHINEVSMACHINEINFO.values())
		{
			if (machine.getNumInstances() > 0)
			{
				machineUsed++;
			}
		}
		System.out.println(machineUsed + " number of machines used");
		debugMethod();

	}

	public static void reassign(String instanceName)
	{
		Instance instance = Constants.INSTANCEVSINSTANCEINFO.get(instanceName);
		if (instance == null)
		{
			return;
		}
		instance.getMachine().instances.remove(instance);
		instance.setMachine(null);
		int i = 0;
		for (Machine machine : Constants.MACHINEVSMACHINEINFO.values())
		{
			i++;
			if (i > 4000)
			{
				if (machine.addInstanceIfPossible(instance))
				{
					System.out.println("reassigned " + instanceName);
					break;
				}
			}
		}
		
	}
	
	
	public static void debugMethod()
	{
		reassign("inst_33721");
		reassign("inst_57715");
	}
}
