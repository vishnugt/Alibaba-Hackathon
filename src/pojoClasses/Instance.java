package pojoClasses;


/**
 * @author vishn
 *
 */
public class Instance {
	App app;
	String name;

	public Instance(String name, App app) {
		super();
		this.app = app;
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

}
