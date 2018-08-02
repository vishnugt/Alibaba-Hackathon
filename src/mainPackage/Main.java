package mainPackage;

import java.text.DateFormat;


/**
 * @author vishn
 *
 */
public class Main {

	public static void main(String[] args) {
		System.out.println("Program Init - " + DateFormat.getTimeInstance().getCalendar().getTime());
		init();
	}

	
	/**
	 * Executed only once to load files to in memory
	 */
	private static void init() 
	{
		try 
		{			
			LoadFiles.loadAll();
		}
		catch(Exception e) 
		{
			System.out.println("Exception while loading all files" );
			e.printStackTrace();
		}
	}
}
