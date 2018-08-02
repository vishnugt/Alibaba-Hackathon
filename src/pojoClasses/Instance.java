package pojoClasses;


/**
 * @author vishn
 *
 */
public class Instance extends Object{
	App app;
	String name;
	Machine machine;

	public Instance(String name, App app) {
		super();
		this.name = name;
		this.app = app;
	}

	public Machine getMachine() {
		return machine;
	}

	public void setMachine(Machine machine) {
		this.machine = machine;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	@Override
	public String toString() {
		return "Instance [app=" + app.toString() + ", name=" + name + "]";
	}
	
	@Override
	public int hashCode() 
	{
		return name.hashCode();
	}

}
