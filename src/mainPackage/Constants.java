package mainPackage;

import java.util.HashMap;

import pojoClasses.App;
import pojoClasses.Instance;
import pojoClasses.Machine;

/**
 * @author vishn
 *
 */
public class Constants
{
	public static HashMap<String, Machine> MACHINEVSMACHINEINFO;
	public static HashMap<String, App> APPVSAPPINFO;
	public static HashMap<String, App> INSTANCEVSAPP;
	public static HashMap<String, Instance> INSTANCEVSINSTANCEINFO;

	static
	{
		MACHINEVSMACHINEINFO = new HashMap<>();
		APPVSAPPINFO = new HashMap<>();
		INSTANCEVSAPP = new HashMap<>();
		INSTANCEVSINSTANCEINFO = new HashMap<>();
	}
}
