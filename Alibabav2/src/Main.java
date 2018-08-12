import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Main
{
    static HashMap<String, Double[]> machineVsmachineResource;
    static HashMap<String, Double[]> machineVsusedMachineResource;
    static HashMap<String, Double[]> appVsAppResource;
    static HashMap<String, String> instanceVsApp;
    static HashMap<String, String> instanceVsMachine;
    static HashMap<String, List<String>> machineVsInstances;
    static HashMap<String, HashMap<String, Integer>> machineVsappVsAppCount;
    static HashMap<String, HashMap<String, Integer>> appVsAppInterference;
    static HashMap<String, HashMap<String, Integer>> inverseAppVsAppInterference;
    static HashMap<String, HashMap<String, Integer>> machineVsAllergicApps;

    static
    {
            machineVsmachineResource = new HashMap<>();
            machineVsusedMachineResource = new HashMap<>();
            appVsAppResource = new HashMap<>();
            instanceVsApp = new HashMap<>();
            instanceVsMachine = new HashMap<>();
            machineVsInstances = new HashMap<>();
            machineVsappVsAppCount = new HashMap<>() ;
            appVsAppInterference = new HashMap<>();
            inverseAppVsAppInterference = new HashMap<>();
    }

    static int numConstraints = 200;

    @SuppressWarnings("oracle.jdeveloper.java.nested-assignment")
    public static void main(String[] args) throws Exception
    {
        loadMachineResourceFile(new BufferedReader(new FileReader(Utility.machineResourceFile)));
        loadAppResourcesFile(new BufferedReader(new FileReader(Utility.appResourcesFile)));
        loadAppInterferenceFile(new BufferedReader(new FileReader(Utility.appInterferenceFile)));
        loadInstanceDeployFile(new BufferedReader(new FileReader(Utility.instanceDeployFile)));

        int numAssigned = 0;

        Set<String> instanceList = instanceVsApp.keySet();
        for (String instance : instanceList)
        {
            if (instanceVsMachine.get(instance) == null)
            {
                for (String machine : machineVsmachineResource.keySet())
                {
                    boolean isAssigned = true;
                    Double[] appResource = appVsAppResource.get(instanceVsApp.get(instance));
                    Double[] machineUsedResource = machineVsusedMachineResource.get(machine);
                    Double[] machineResource = machineVsmachineResource.get(machine);
                    Double[] updatedMachineUsedResources = machineVsusedMachineResource.get(machine);

                    for (int i = 0; i < 200; ++i)
                    {
                        if (appResource[i] + machineUsedResource[i] <= machineResource[i])
                        {
                            updatedMachineUsedResources[i] = appResource[i] + machineUsedResource[i];
                        }
                        else
                        {
                            isAssigned = false;
                            break;
                        }
                    }
                    if (isAssigned && !checkIfMachineIsAllergic(machine, instance))
                    {
                        numAssigned++;
                        machineVsusedMachineResource.put(machine, updatedMachineUsedResources);
                        List<String> machineInstanceList = machineVsInstances.get(machine);
                        if (machineInstanceList == null)
                        {
                            machineVsInstances.put(machine, new ArrayList<>());
                        }
                        machineVsInstances.get(machine).add(instance);
                        instanceVsMachine.put(instance, machine);
                        if (numAssigned % 1000 == 0)
                        {
                            System.out.println("Num assigned: " + numAssigned);
                        }
                        break;
                    }
                }
            } else{
                numAssigned += 1;
            }
        }

        System.out.println("Number of machines assigned: " + numAssigned);
        System.out.println("Number of machines not assigned " + (instanceVsApp.size() - numAssigned));

        PrintWriter pw = new PrintWriter(new File(Utility.outputFileName));
        StringBuilder sb = new StringBuilder();
        for (String instance : instanceVsMachine.keySet())
        {
                sb.append(instance);
                sb.append(",");
                sb.append(instanceVsMachine.get(instance));
                sb.append("\n");
        }
        pw.write(sb.toString());
        pw.close();
        System.out.println("Output ready!");

    }

    @SuppressWarnings("oracle.jdeveloper.java.nested-assignment")
    public static void loadMachineResourceFile(BufferedReader br) throws IOException {
        @SuppressWarnings("oracle.jdeveloper.java.string-constructor")
        String line = new String();
        while ((line = br.readLine()) != null)
        {
                String[] resource = line.split(",");
                Double[] machineResource = new Double[200];
                Double[] machineUsedResource = new Double[200];
                for (int i = 3; i < 7; ++i)
                {
                        machineResource[i + 193] = Double.parseDouble(resource[i]);
                }
                for (int i = 0; i < 98; ++i)
                {
                        machineResource[i] = Double.parseDouble(resource[1]);
                        machineResource[i + 98] = Double.parseDouble(resource[2]);
                }
                for (int i = 0; i < 200; ++i)
                {
                        machineUsedResource[i] = 0.0d;
                }
                machineVsmachineResource.put(resource[0], machineResource);
                machineVsusedMachineResource.put(resource[0], machineUsedResource);
        }
        br.close();
    }
    
    @SuppressWarnings("oracle.jdeveloper.java.nested-assignment")
    public static void loadAppResourcesFile(BufferedReader br) throws IOException {
        @SuppressWarnings("oracle.jdeveloper.java.string-constructor")
        String line = new String();
            while ((line = br.readLine()) != null)
            {
                    String[] resource = line.split(",");
                    Double[] appResources = new Double[200];
                    String cpuResource[] = resource[1].split("\\|");
                    String ramResource[] = resource[2].split("\\|");
                    for (int i = 0; i < 98; ++i)
                    {
                            appResources[i] = Double.parseDouble(cpuResource[i]);
                            appResources[i + 98] = Double.parseDouble(ramResource[i]);
                    }
                    for (int i = 196; i < 200; ++i)
                    {
                            appResources[i] = Double.parseDouble(resource[i - 193]);
                    }
                    appVsAppResource.put(resource[0], appResources);
            }
            br.close();
    }
    
    @SuppressWarnings("oracle.jdeveloper.java.nested-assignment")
    public static void loadInstanceDeployFile(BufferedReader br) throws IOException {
        @SuppressWarnings("oracle.jdeveloper.java.string-constructor")
        String line = new String();
            while ((line = br.readLine()) != null)
            {
                    String[] resource = line.split(",");
                    String instanceName = resource[0];
                    String appName = resource[1];
                    instanceVsApp.put(instanceName, appName);
                    if (resource.length > 2)
                    {
                            String machineName = resource[2];
                            instanceVsMachine.put(instanceName, machineName);
                            List<String> instanceList = machineVsInstances.get(machineName);
                            if (instanceList == null)
                            {
                                    machineVsInstances.put(machineName, new ArrayList<>());
                            }
                            machineVsInstances.get(machineName).add(resource[0]);
                            // add app count to machine
                            String app = instanceVsApp.get(instanceName);
                            HashMap<String, Integer> appVsAppCountMap = machineVsappVsAppCount.get(machineName);
                            if(appVsAppCountMap == null){
                                appVsAppCountMap = new HashMap<>();
                                machineVsappVsAppCount.put(machineName, appVsAppCountMap);
                                appVsAppCountMap.put(app,0);
                            }
                            appVsAppCountMap.put(app,appVsAppCountMap.get(app)+1);
                            
                            Double[] appResource = appVsAppResource.get(instanceVsApp.get(instanceName));
                            Double[] machineUsedResource = machineVsusedMachineResource.get(machineName);

                            for (int i = 0; i < 200; ++i)
                            {
                                    machineUsedResource[i] += appResource[i];
                            }
                            machineVsusedMachineResource.put(machineName, machineUsedResource);
                    }
            }
            br.close();
    }
    
    @SuppressWarnings("oracle.jdeveloper.java.nested-assignment")
    public static void loadAppInterferenceFile(BufferedReader br) throws IOException {
        @SuppressWarnings("oracle.jdeveloper.java.string-constructor")
        String line = new String();
        while ((line = br.readLine()) != null)
        {
            String[] resource = line.split(",");
            String app1Name = resource[0];
            String app2Name = resource[1];
            Integer app2Count = Integer.parseInt(resource[2]);

            HashMap<String, Integer> interferenceToFirstInstance = appVsAppInterference.get(app1Name);

            if(interferenceToFirstInstance == null){
                interferenceToFirstInstance = new HashMap<String,Integer>();
                appVsAppInterference.put(app1Name, interferenceToFirstInstance);
            }
            interferenceToFirstInstance.put(app2Name, app2Count);

            HashMap<String, Integer> interferenceToSecondInstance = inverseAppVsAppInterference.get(app1Name);

            if(interferenceToSecondInstance == null){
                interferenceToSecondInstance = new HashMap<String,Integer>();
                inverseAppVsAppInterference.put(app2Name, interferenceToSecondInstance);
            }
            interferenceToSecondInstance.put(app1Name, app2Count);
        }
        br.close();
    }
    
    public static boolean checkIfMachineIsAllergic(String machine, String currentInstance){
        String app = instanceVsApp.get(currentInstance);
        int currentInstanceCount = 0;
        if(machineVsappVsAppCount.get(machine) == null){
            return false;
        }
        currentInstanceCount = machineVsappVsAppCount.get(machine).get(app) == null ? 0 : machineVsappVsAppCount.get(machine).get(app);
        
        for(Map.Entry<String,Integer> existingAppVsAppCount : machineVsappVsAppCount.get(machine).entrySet()){
            //app1, app2, k
            //app1 count = a1
            //app2 count = a2
            // app 1 getting added
            // if a2>k, can't add app 1
            
            String app1 = app;
            int a1 = currentInstanceCount;
            String app2 = existingAppVsAppCount.getKey();
            int a2 = existingAppVsAppCount.getValue();
            int k = Integer.MAX_VALUE;
            if(appVsAppInterference.get(app1) != null){
                k =
                    appVsAppInterference.get(app1).get(app2) == null ? Integer.MAX_VALUE :
                    appVsAppInterference.get(app1).get(app2);
            }

            if(a2>k){
                return true;
            }
            
            // app2 getting added
            // a2=k and a1>1, can't be added
            // or else, can add.
            
            if(a1 == k && a2 > 1){
                return true;
            }
   
        }
        return false;
    }
}
