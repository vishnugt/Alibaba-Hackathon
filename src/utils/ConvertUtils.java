package utils;

/**
 * @author vishn
 *
 */
public class ConvertUtils {

	public static float[] convertStringArrayToFloatArray(String[] array)
	{
		int size = array.length;
		float[] returnObj = new float[size];
		for (int i = 0; i < size; ++i) {
			returnObj[i] = Float.parseFloat(array[i]);
		}
		return returnObj;
	}
	
	public static double[] convertStringArrayToDoubleArray(String[] array)
	{
		int size = array.length;
		double[] returnObj = new double[size];
		for (int i = 0; i < size; ++i) {
			returnObj[i] = Double.parseDouble(array[i]);
		}
		return returnObj;
	}
	
	public static String stringArrayToString(String[] array) 
	{
		StringBuffer  sb = new StringBuffer();
		for(String item: array) 
		{
			sb.append(item).append(PathConstants.space);
		}
		return sb.toString();
	}
	
}
