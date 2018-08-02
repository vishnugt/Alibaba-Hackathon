package pojoClasses;

import java.util.ArrayList;


/**
 * @author vishn
 *
 */
public class Machine {
	
	String name;
	
	int cpu;
	int ram;
	int disk;
	int p;
	int m;
	int pm;
	ArrayList<Instance> instances;
	ArrayList<App> apps;
	
	public Machine(String[] resource) 
	{
		this(
				resource[0],
				Integer.parseInt(resource[1]),
				Integer.parseInt(resource[2]),
				Integer.parseInt(resource[3]),
				Integer.parseInt(resource[4]),
				Integer.parseInt(resource[5]),
				Integer.parseInt(resource[6])
				);
	}

	public Machine(String name, int cpu, int ram, int disk, int p, int m, int pm) {
		super();
		this.name = name;
		this.cpu = cpu;
		this.ram = ram;
		this.disk = disk;
		this.p = p;
		this.m = m;
		this.pm = pm;
		
		instances = new ArrayList<>();
		apps = new ArrayList<>();
	} 
	
	
	public boolean addInstanceIfPossible(Instance newInstance) 
	{
		//validate
		//update applist
		return true;
	}
	
	
	
	public int getCpu() {
		return cpu;
	}

	public void setCpu(int cpu) {
		this.cpu = cpu;
	}

	public int getRam() {
		return ram;
	}

	public void setRam(int ram) {
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Machine [name=" + name + ", cpu=" + cpu + ", ram=" + ram + ", disk=" + disk + ", p=" + p + ", m=" + m
				+ ", pm=" + pm + ", instances=" + instances + ", apps=" + apps + "]";
	}

}
