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

	float[] currentCpuCurve;
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

		currentCpuCurve = new float[98];
		currentRamCurve = new double[98];
		
		for (int i = 0; i < 98; ++i) {
			currentCpuCurve[i] = 0.0f;
			currentRamCurve[i] = 0.0d;
		}
	}

	public void addInstanceBlindly(Instance newInstance) {
		this.instances.add(newInstance);
		if (!this.appVsAppCount.containsKey(newInstance.getApp())) {
			this.appVsAppCount.put(newInstance.getApp(), 1);
		}
		// mutable integer needed?
		this.appVsAppCount.put(newInstance.getApp(), this.appVsAppCount.get(newInstance.getApp()) + 1);
		newInstance.setMachine(this);
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
			limit = limit == null ? 0 : limit;
			if (!(newAppCount + 1 < limit)) {
				return false;
			}
		}

		// at this point app interference has been solved
		// machine constraints
		for (int i = 0; i < 98; ++i) {
			currentCpuCurve[i] += newApp.getCpu()[i];
			currentRamCurve[i] += newApp.getRam()[i];
			if (currentCpuCurve[i] > this.cpu || currentRamCurve[i] > this.ram) {
				return false;
			}
		}
		
		this.currentDisk += newApp.getDisk();
		this.currentM += newApp.getM();
		this.currentP += newApp.getP();
		this.currentPM += newApp.getPm();

		if (this.currentDisk > this.disk || this.currentM > this.m || this.currentP > this.p
				|| this.currentPM > this.pm) {
			return false;
		}

		//well done to survive all checks
		System.out.println(newInstance + "is going to be added to " + this.getName());
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

	@Override
	public String toString() {
		return "Machine [name=" + name + ", cpu=" + cpu + ", ram=" + ram + ", disk=" + disk + ", p=" + p + ", m=" + m
				+ ", pm=" + pm + ", instances=" + instances + ", apps=" + appVsAppCount + "]";
	}

}
