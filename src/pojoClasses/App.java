package pojoClasses;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import utils.ConvertUtils;
import utils.PathConstants;

/**
 * @author vishn
 *
 */
public class App
{

	String name;

	BigDecimal[] cpu;
	BigDecimal[] ram;
	double disk;
	double p;
	double m;
	double pm;
	ArrayList<Instance> instances;
	HashMap<App, Integer> inteferenceAppAndCount;

	public App(String[] resource)
	{
		this(resource[0], ConvertUtils.convertStringArrayToBigDecimalArray(resource[1].split(PathConstants.orDelimiter)), ConvertUtils.convertStringArrayToBigDecimalArray(resource[2].split(PathConstants.orDelimiter)), Double.parseDouble(resource[3]), //should change this, too sleepy to fix in all places
				Double.parseDouble(resource[4]), Double.parseDouble(resource[5]), Double.parseDouble(resource[6]));
	}

	public App(String name, BigDecimal[] cpu, BigDecimal[] ram, double disk, double p, double m, double pm)
	{
		super();
		this.name = name;
		this.cpu = cpu;
		this.ram = ram;
		this.disk = disk;
		this.p = p;
		this.m = m;
		this.pm = pm;

		this.instances = new ArrayList<>();
		this.inteferenceAppAndCount = new HashMap<>();

	}

	public void addInstance(Instance instance)
	{
		this.instances.add(instance);
	}

	public void addInterference(App newApp, int count)
	{
		this.inteferenceAppAndCount.put(newApp, count);
	}

	public int getNumPossibleApps(App newApp)
	{
		Integer count = this.inteferenceAppAndCount.get(newApp);
		if (count == null)
		{
			//high cost?? is -1 better?
			//problems while adding with this number?
			return Integer.MAX_VALUE;
		}
		else
		{
			return count;
		}
	}

	public BigDecimal[] getCpu()
	{
		return cpu;
	}

	public BigDecimal[] getRam()
	{
		return ram;
	}

	public double getDisk()
	{
		return disk;
	}

	public double getP()
	{
		return p;
	}

	public double getM()
	{
		return m;
	}

	public double getPm()
	{
		return pm;
	}

	public ArrayList<Instance> getInstances()
	{
		return instances;
	}

	public void setInstances(ArrayList<Instance> instances)
	{
		this.instances = instances;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public int hashCode()
	{
		return name.hashCode();
	}

	@Override
	public String toString()
	{
		return "App [name=" + name + "]";
		//+ ", cpu=" + Arrays.toString(cpu) + ", ram=" + Arrays.toString(ram) + ", disk="
		//	+ disk + ", p=" + p + ", m=" + m + ", pm=" + pm + ", instances=" + instances
		//+ ", inteferenceAppAndCount=" + inteferenceAppAndCount + "]";
	}

}
