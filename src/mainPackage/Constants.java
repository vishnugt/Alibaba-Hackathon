package mainPackage;

import java.util.HashMap;

import pojoClasses.App;
import pojoClasses.Machine;

/**
 * @author vishn
 *
 */
public class Constants 
{
	public static HashMap<String, Machine> MACHINEVSMACHINEINFO;
	public static HashMap<String, App> APPVSAPPINFO;
	
	static
	{
		MACHINEVSMACHINEINFO = new HashMap<>();
		APPVSAPPINFO = new HashMap<>();
	}
}
