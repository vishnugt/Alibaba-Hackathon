package mainPackage;

import java.text.DateFormat;

import pojoClasses.Instance;
import pojoClasses.Machine;

/**
 * @author vishn
 *
 */
public class Main {

	public static void main(String[] args) {
		System.out.println("Program Init - " + DateFormat.getTimeInstance().getCalendar().getTime());
		init();
		assignInstances();
	}

	private static void init() {
		try {
			LoadFiles.loadAllFiles();
		} catch (Exception e) {
			System.out.println("Exception while loading all files");
			e.printStackTrace();
		}
	}

	private static void assignInstances() {
		int logicInsufficient = 0;
		int numAssigned = 0;
		for (Instance instance : Constants.INSTANCEVSINSTANCEINFO.values()) {
			boolean isAssigned = false;
			if (instance.getMachine() == null) {
				for (Machine machine : Constants.MACHINEVSMACHINEINFO.values()) {
					if (machine.addInstanceIfPossible(instance)) {
						numAssigned++;
						isAssigned = true;
						break;
					}
				}
				if (!isAssigned) {
					logicInsufficient++;
					// System.out.println("Logic Insufficient");
				}
			}
		}
		System.out.println(logicInsufficient + " instances not able to assign");
		System.out.println(numAssigned + " instances assigned");
		
	}
}
