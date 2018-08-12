package pojoClasses;

/**
 * @author vishn
 *
 *         just a wrapper class that holds app and number of instance its
 *         allergic to
 *
 */
@Deprecated
public class Inteference
{

	App app;
	int allergyCount;

	public Inteference(App app, int allergyCount)
	{
		super();
		this.app = app;
		this.allergyCount = allergyCount;
	}

	public App getApp()
	{
		return app;
	}

	public void setApp(App app)
	{
		this.app = app;
	}

	public int getAllergyCount()
	{
		return allergyCount;
	}

	public void setAllergyCount(int allergyCount)
	{
		this.allergyCount = allergyCount;
	}
}
