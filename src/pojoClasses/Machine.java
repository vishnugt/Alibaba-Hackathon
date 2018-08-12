package pojoClasses;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * @author vishn
 *
 */
public class Machine
{

	String name;

	double cpu;
	double ram;
	double disk, currentDisk;
	double p, currentP;
	double m, currentM;
	double pm, currentPM;

	BigDecimal[] currentCpuCurve;
	BigDecimal[] currentRamCurve;

	public ArrayList<Instance> instances;
	HashMap<App, Integer> appVsAppCount;

	public Machine(String[] resource)
	{
		this(resource[0], Double.parseDouble(resource[1]), Double.parseDouble(resource[2]), Double.parseDouble(resource[3]), Double.parseDouble(resource[4]), Double.parseDouble(resource[5]), Double.parseDouble(resource[6]));
	}

	public Machine(String name, double cpu, double ram, double disk, double p, double m, double pm)
	{
		super();
		this.name = name;
		this.cpu = cpu;
		this.ram = ram;
		this.disk = disk;
		this.p = p;
		this.m = m;
		this.pm = pm;

		currentDisk = 0;
		currentP = 0;
		currentM = 0;
		currentPM = 0;

		this.instances = new ArrayList<>();
		this.appVsAppCount = new HashMap<>();

		currentCpuCurve = new BigDecimal[98];
		currentRamCurve = new BigDecimal[98];

		for (int i = 0; i < 98; ++i)
		{
			currentCpuCurve[i] = new BigDecimal(0);
			currentRamCurve[i] = new BigDecimal(0);
		}
	}

	public boolean addInstanceBlindly(Instance newInstance, boolean isDefault)
	{
		if (!updateMachineCurrentParameters(newInstance))
		{
			return false;
		}
		this.instances.add(newInstance);
		if (!this.appVsAppCount.containsKey(newInstance.getApp()))
		{
			this.appVsAppCount.put(newInstance.getApp(), 0);
		}
		// mutable integer needed?
		this.appVsAppCount.put(newInstance.getApp(), this.appVsAppCount.get(newInstance.getApp()) + 1);
		newInstance.setMachine(this);
		return true;
	}

	private boolean updateMachineCurrentParameters(Instance newInstance)
	{
		App newApp = newInstance.getApp();
		for (int i = 0; i < 98; ++i)
		{
			currentCpuCurve[i] = currentCpuCurve[i].add(newApp.getCpu()[i]);
			currentRamCurve[i] = currentRamCurve[i].add(newApp.getCpu()[i]);

		}

		this.currentDisk += newApp.getDisk();
		this.currentM += newApp.getM();
		this.currentP += newApp.getP();
		this.currentPM += newApp.getPm();

		if (currentDisk > disk || currentM > m || currentP > p || currentPM > pm)
		{
			return false;
		}
		return true;
	}

	// both counts will be >= 1
	private boolean validate(App app1, int app1Count, App app2, int app2Count)
	{

		int k1 = app1.inteferenceAppAndCount.get(app2) == null ? Integer.MAX_VALUE : app1.inteferenceAppAndCount.get(app2);
		int k2 = app2.inteferenceAppAndCount.get(app1) == null ? Integer.MAX_VALUE : app2.inteferenceAppAndCount.get(app1);

		if (k1 == 0 || k2 == 0)
		{
			return false;
		}

		// if(k1<Integer.MAX_VALUE && (app1Count > 2 || app2Count >2) && k2<
		// Integer.MAX_VALUE && !app1.getName().matches(app2.getName()))
		// {
		// int a = 1;
		// System.out.println(a);
		// }

		if (app1.getName().equals(app2.getName()))
		{
			if (app1Count > k1)
			{
				return false;
			}
		}

		if (app2Count > k1)
		{
			return false;
		}
		if (app1Count > k2)
		{
			return false;
		}
		return true;
	}

	public boolean addInstanceIfPossible(Instance newInstance)
	{

		App currentApp = newInstance.getApp();

		// real logic starts
		// 1. appserver interference check
		for (Entry<App, Integer> entry : this.appVsAppCount.entrySet())
		{
			App app1 = currentApp;
			App app2 = entry.getKey();

			int app1Count = this.appVsAppCount.get(app1) == null ? 1 : this.appVsAppCount.get(app1) + 1;
			int app2Count = entry.getValue();

			if (!validate(app1, app1Count, app2, app2Count))
			{
				return false;
			}
		}

		// at this point app interference has been solved
		// machine constraints
		for (int i = 0; i < 98; ++i)
		{
			if (currentCpuCurve[i].add(currentApp.getCpu()[i]).doubleValue() > this.cpu || currentRamCurve[i].add(currentApp.getRam()[i]).doubleValue() > this.ram)
			{
				return false;
			}
		}

		if (this.currentDisk + currentApp.getDisk() > this.disk || this.currentM + currentApp.getM() > this.m || this.currentP + currentApp.getP() > this.p || this.currentPM + currentApp.getPm() > this.pm)
		{
			return false;
		}

		// well done to survive all checks
		// System.out.println(newInstance + "is going to be added to " +
		// this.getName());
		return addInstanceBlindly(newInstance, false);
	}

	public double getCpu()
	{
		return cpu;
	}

	public void setCpu(int cpu)
	{
		this.cpu = cpu;
	}

	public double getRam()
	{
		return ram;
	}

	public void setRam(int ram)
	{
		this.ram = ram;
	}

	public double getDisk()
	{
		return disk;
	}

	public void setDisk(int disk)
	{
		this.disk = disk;
	}

	public double getP()
	{
		return p;
	}

	public void setP(int p)
	{
		this.p = p;
	}

	public double getM()
	{
		return m;
	}

	public void setM(int m)
	{
		this.m = m;
	}

	public double getPm()
	{
		return pm;
	}

	public void setPm(int pm)
	{
		this.pm = pm;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getNumInstances()
	{
		return this.instances.size();
	}

	@Override
	public String toString()
	{
		return "Machine [name=" + name + ", cpu=" + cpu + ", ram=" + ram + ", disk=" + disk + ", currentDisk=" + currentDisk + ", p=" + p + ", currentP=" + currentP + ", m=" + m + ", currentM=" + currentM + ", pm=" + pm + ", currentPM=" + currentPM + ", currentCpuCurve=" + Arrays.toString(currentCpuCurve) + ", currentRamCurve=" + Arrays.toString(currentRamCurve) + "]";
	}

}
