package pojoClasses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import utils.ConvertUtils;
import utils.PathConstants;



/**
 * @author vishn
 *
 */
public class App {

	String name;
	
	float[] cpu;
	double[] ram;
	int disk;
	int p; 
	int m;
	int pm;
	ArrayList<Instance> instances;
	HashMap<App, Integer> inteferenceAppAndCount;
	
	
	public App(String[] resource) {
		this(
				resource[0],
				ConvertUtils.convertStringArrayToFloatArray(resource[1].split(PathConstants.orDelimiter)),
				ConvertUtils.convertStringArrayToDoubleArray(resource[2].split(PathConstants.orDelimiter)),
				Integer.parseInt(resource[3]),
				Integer.parseInt(resource[4]),
				Integer.parseInt(resource[5]),
				Integer.parseInt(resource[6])
				);
	}
	
	public App(String name, float[] cpu, double[] ram, int disk, int p, int m, int pm) {
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
	

	public int getNumPossibleApps(App newApp) 
	{
		Integer count = this.inteferenceAppAndCount.get(newApp);
		if(count == null) 
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
	
	public float[] getCpu() {
		return cpu;
	}
	public void setCpu(float[] cpu) {
		this.cpu = cpu;
	}
	public double[] getRam() {
		return ram;
	}
	public void setRam(double[] ram) {
		this.ram = ram;
	}
	public int getDisk() {
		return disk;
	}
	public void setDisk(int disk) {
		this.disk = disk;
	}
	public int getP() {
		return p;
	}
	public void setP(int p) {
		this.p = p;
	}
	public int getM() {
		return m;
	}
	public void setM(int m) {
		this.m = m;
	}
	public int getPm() {
		return pm;
	}
	public void setPm(int pm) {
		this.pm = pm;
	}
	public ArrayList<Instance> getInstances() {
		return instances;
	}
	public void setInstances(ArrayList<Instance> instances) {
		this.instances = instances;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "App [name=" + name + ", cpu=" + Arrays.toString(cpu) + ", ram=" + Arrays.toString(ram) + ", disk="
				+ disk + ", p=" + p + ", m=" + m + ", pm=" + pm + ", instances=" + instances
				+ ", inteferenceAppAndCount=" + inteferenceAppAndCount + "]";
	}
	
}
