package pojoClasses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * @author vishn
 *
 */
public class Machine {

	String name;

	int cpu;
	int ram;
	int disk, currentDisk;
	int p, currentP;
	int m, currentM;
	int pm, currentPM;

	double[] currentCpuCurve;
	double[] currentRamCurve;

	ArrayList<Instance> instances;
	HashMap<App, Integer> appVsAppCount;

	public Machine(String[] resource) {
		this(resource[0], Integer.parseInt(resource[1]), Integer.parseInt(resource[2]), Integer.parseInt(resource[3]),
				Integer.parseInt(resource[4]), Integer.parseInt(resource[5]), Integer.parseInt(resource[6]));
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

		currentDisk = 0;
		currentP = 0;
		currentM = 0;
		currentPM = 0;

		this.instances = new ArrayList<>();
		this.appVsAppCount = new HashMap<>();

		currentCpuCurve = new double[98];
		currentRamCurve = new double[98];

		for (int i = 0; i < 98; ++i) {
			currentCpuCurve[i] = 0.0f;
			currentRamCurve[i] = 0.0d;
		}
	}

	public void addInstanceBlindly(Instance newInstance) {
		updateMachineCurrentParameters(newInstance);
		this.instances.add(newInstance);
		if (!this.appVsAppCount.containsKey(newInstance.getApp())) {
			this.appVsAppCount.put(newInstance.getApp(), 0);
		}
		// mutable integer needed?
		this.appVsAppCount.put(newInstance.getApp(), this.appVsAppCount.get(newInstance.getApp()) + 1);
		newInstance.setMachine(this);
	}

	private void updateMachineCurrentParameters(Instance newInstance) {
		App newApp = newInstance.getApp();
		for (int i = 0; i < 98; ++i) {
			currentCpuCurve[i] += newApp.getCpu()[i];
			currentRamCurve[i] += newApp.getRam()[i];
		}

		this.currentDisk += newApp.getDisk();
		this.currentM += newApp.getM();
		this.currentP += newApp.getP();
		this.currentPM += newApp.getPm();

	}

	public boolean addInstanceIfPossible(Instance newInstance) {

		App newApp = newInstance.getApp();

		// real logic starts
		// 1. appserver interference check
		for (Entry<App, Integer> entry : this.appVsAppCount.entrySet()) {
			App existingApp = entry.getKey();
			Integer newAppCount = 0;
			if (this.appVsAppCount.containsKey(newApp)) {
				newAppCount = this.appVsAppCount.get(newApp);
			}
			Integer limit = existingApp.inteferenceAppAndCount.get(newApp);
			if (limit == null) {
				continue;
			}
			// if new instance is added (+1) to the existing number of such instances from the same app 
			// if the limit is not( not crossed ) then the instance is not added.
			if (!(newAppCount + 1 <= limit)) {
				return false;
			}
		}

		// at this point app interference has been solved
		// machine constraints
		for (int i = 0; i < 98; ++i) {
			if (currentCpuCurve[i] + newApp.getCpu()[i] > this.cpu
					|| currentRamCurve[i] + newApp.getRam()[i] > this.ram) {
				return false;
			}
		}

		if (this.currentDisk + newApp.getDisk() > this.disk || this.currentM + newApp.getM() > this.m
				|| this.currentP + newApp.getP() > this.p || this.currentPM + newApp.getPm() > this.pm) {
			return false;
		}

		// well done to survive all checks
		//System.out.println(newInstance + "is going to be added to " + this.getName());
		addInstanceBlindly(newInstance);
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

	public int getNumInstances() 
	{
		return this.instances.size();
	}
	
	@Override
	public String toString() {
		return "Machine [name=" + name + ", cpu=" + cpu + ", ram=" + ram + ", disk=" + disk + ", p=" + p + ", m=" + m
				+ ", pm=" + pm + ", instances=" + instances + ", apps=" + appVsAppCount + "]";
	}

}
