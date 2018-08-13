package project1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Main_2 {
    
    static int numAssigned = 0;
    
    // For machine resources
    static HashMap<String, Double[]> machineVsmachineResource;
    static HashMap<String, Double[]> machineVsusedMachineResource;
    
    // For app resources
    static HashMap<String, Double[]> appVsAppResource;
    
    // For instances deployed
    static HashMap<String, String> instanceVsApp;
    static HashMap<String, List<String>> machineVsInstances;
    static HashMap<String, HashMap<String, Integer>> machineVsappVsAppCount;
    static HashMap<String, String> nonAssignedInstances;
    static HashMap<String, String> alreadyAssignedInstances;

    // For app interferences
    static HashMap<String, HashMap<String, Integer>> appVsAppInterference;
    static HashMap<String, HashMap<String, Integer>> inverseAppVsAppInterference;
    
    static HashMap<String, String> instanceVsMachine;
    
    static PrintWriter pw;
    static StringBuilder sb;

    static {
        machineVsmachineResource = new HashMap<>();
        machineVsusedMachineResource = new HashMap<>();

        appVsAppResource = new HashMap<>();

        instanceVsApp = new HashMap<>();
        machineVsInstances = new HashMap<>();
        machineVsappVsAppCount = new HashMap<>();
        alreadyAssignedInstances = new HashMap<>();
        nonAssignedInstances = new HashMap<>();

        appVsAppInterference = new HashMap<>();
        inverseAppVsAppInterference = new HashMap<>();
        
        instanceVsMachine = new HashMap<>();
        
    }
    public Main_2() {
        super();
    }
    
    public static void main(String[] args) throws IOException {
        
        pw = new PrintWriter(new File(Utility.outputFileName));
        sb = new StringBuilder();
        
        //1. Load MACHINE RESOURCE FILE
        loadMachineResourcesFile(new BufferedReader(new FileReader(Utility.machineResourceFile)));
        
        //2. Load APP RESOURCE FILE
        loadAppResourcesFile(new BufferedReader(new FileReader(Utility.appResourcesFile)));
        
        //3. Load INSTANCE DEPLOY FILE
        loadInstanceDeployFile(new BufferedReader(new FileReader(Utility.instanceDeployFile)));
        
        //4. Load APP INTERFERENCE FILE
        loadAppInterferenceFile(new BufferedReader(new FileReader(Utility.appInterferenceFile)));
        
        //5. Reassign previously assigned instances
        reassignInstances(alreadyAssignedInstances);
        
        //6. Assign unassigned instances 
        assignInstances(nonAssignedInstances);
        
        pw.write(sb.toString());
        pw.close();
        
        System.out.println("Number of assigned instances are = "+numAssigned);
        System.out.println("Number of unassigned instances are = "+(instanceVsApp.size()-numAssigned));
    }
    
    public static void loadMachineResourcesFile(BufferedReader br) throws IOException
    {

        String line = new String();
        while ((line = br.readLine()) != null)
        {
            String[] resource = line.split(",");
            Double[] machineResource = new Double[200];
            Double[] machineUsedResource = new Double[200];
            //machineResource = {98xcpu, 98xmem, disk, p, m, pm}
            for (int i = 0; i < 98; ++i) {
                machineResource[i] = Double.parseDouble(resource[1]); //cpu
                machineResource[i + 98] = Double.parseDouble(resource[2]); //mem
            }
            for (int i = 3; i < 7; ++i) {
                machineResource[i + 193] = Double.parseDouble(resource[i]); //disk, p, m, pm
            }
            //Initialize machine used resource to {98xzero}
            for (int i = 0; i < 200; ++i)
            {
                    machineUsedResource[i] = 0.0d;
            }
            machineVsmachineResource.put(resource[0], machineResource);
            machineVsusedMachineResource.put(resource[0], machineUsedResource);
        }
        br.close();
//        System.out.println(machineVsmachineResource);
//        System.out.println(machineVsusedMachineResource);
    }
    
    public static void loadAppResourcesFile(BufferedReader br) throws IOException
    {
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
//            System.out.println(appResources[197]);
        }
        br.close();
    }
    
    public static void loadInstanceDeployFile(BufferedReader br) throws IOException
    {
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
                    // Add machine to instance
                    alreadyAssignedInstances.put(instanceName,machineName); 
                    
                    // Add instance to list of instances running on machine
                    List<String> instanceList = machineVsInstances.get(machineName);
                    if (instanceList == null)
                    {
                         machineVsInstances.put(machineName, new ArrayList<>());
                    }
                    machineVsInstances.get(machineName).add(instanceName);
                    
                    // Add app count to machine
                    String app = instanceVsApp.get(instanceName);
                    HashMap<String, Integer> appVsAppCountMap = machineVsappVsAppCount.get(machineName);
                    if(appVsAppCountMap == null){
                        appVsAppCountMap = new HashMap<>();
                        machineVsappVsAppCount.put(machineName, appVsAppCountMap);
                        appVsAppCountMap.put(app,0);
                    }
                    appVsAppCountMap.put(app,appVsAppCountMap.get(app)!=null ? appVsAppCountMap.get(app)+1 : 1);
                    
                    // Update machine resources used
                    Double[] appResource = appVsAppResource.get(app);
                    Double[] machineUsedResource = machineVsusedMachineResource.get(machineName);
                    for (int i = 0; i < 200; ++i)
                    {
                         machineUsedResource[i] += appResource[i];
                    }
                    machineVsusedMachineResource.put(machineName, machineUsedResource);
                } else{
                    // Add instance to list of non-assigned instances. Add into map as instance, null(machine)
                    nonAssignedInstances.put(instanceName,null);
                }
        }
        br.close();
    }
    
    public static void loadAppInterferenceFile(BufferedReader br) throws IOException
    {
        String line = new String();
        while ((line = br.readLine()) != null)
        {
            String[] resource = line.split(",");
            String app1Name = resource[0];
            String app2Name = resource[1];
            Integer k = Integer.parseInt(resource[2]);

            HashMap<String, Integer> interferenceToFirstInstance = appVsAppInterference.get(app1Name);

            if (interferenceToFirstInstance == null)
            {
                    interferenceToFirstInstance = new HashMap<String, Integer>();
                    appVsAppInterference.put(app1Name, interferenceToFirstInstance);
            }
            interferenceToFirstInstance.put(app2Name, k);

            HashMap<String, Integer> interferenceToSecondInstance = inverseAppVsAppInterference.get(app1Name);

            if (interferenceToSecondInstance == null)
            {
                    interferenceToSecondInstance = new HashMap<String, Integer>();
                    inverseAppVsAppInterference.put(app2Name, interferenceToSecondInstance);
            }
            interferenceToSecondInstance.put(app1Name, k);
        }
        br.close();
    }
    
    public static void reassignInstances(HashMap<String, String> alreadyAssignedInstancesVsMachines) {
        for (String instance : alreadyAssignedInstancesVsMachines.keySet()){
            String machine = alreadyAssignedInstancesVsMachines.get(instance);
            removeInstanceFromMachine(instance, machine);
            assignMachineToInstance(instance);
            if(numAssigned %1000 == 0){
                System.out.println("Number assigned : "+numAssigned);
            }
        }
    }
    
    public static boolean checkMachineResourceAvailability(String instance, String machine) {
        String app = instanceVsApp.get(instance);
        Double[] appResource = appVsAppResource.get(app);
        Double[] machineUsedResource = machineVsusedMachineResource.get(machine);
        Double[] machineResource = machineVsmachineResource.get(machine);
        for (int i = 0; i < 200; ++i)
        {
            if (appResource[i] + machineUsedResource[i] > machineResource[i])
            {
                return false;
            }
        }
        return true;
    }
    
    public static void removeInstanceFromMachine(String instance, String machine){
        // Remove instance from machine
        machineVsInstances.get(machine).remove(instance);
        
        // Reduce / remove app from machine
        String app = instanceVsApp.get(instance);
        int appcount = machineVsappVsAppCount.get(machine).get(app) - 1;
        if (appcount == 0){
            machineVsappVsAppCount.get(machine).remove(app);
        } else {
            machineVsappVsAppCount.get(machine).put(app, appcount);
        }
        
        // Reduce machine resource usage
        Double[] mUsedResource = machineVsusedMachineResource.get(machine);
        Double[] appResource = appVsAppResource.get(app);
        for (int i=0; i<200; ++i){
            mUsedResource[i] -= appResource[i];
        }
    }

    private static void assignMachineToInstance(String instance) {
        for (String machine : machineVsmachineResource.keySet()){
            boolean isMachineResourceAvailable = checkMachineResourceAvailability(instance, machine);
            if(isMachineResourceAvailable){
                boolean isAppCompatible = checkIfAppsAreCompatible(instance,machine);
                if(isAppCompatible){
                    numAssigned++;
                    String app = instanceVsApp.get(instance);
                    
                    // Update machine instance list
                    List<String> instanceList = machineVsInstances.get(machine);
                    if (instanceList == null)
                    {
                         machineVsInstances.put(machine, new ArrayList<>());
                    }
                    machineVsInstances.get(machine).add(instance);
                    
                    // Update machine app list
                    HashMap<String, Integer> appVsAppCountMap = machineVsappVsAppCount.get(machine);
                    if(appVsAppCountMap == null){
                        appVsAppCountMap = new HashMap<>();
                        machineVsappVsAppCount.put(machine, appVsAppCountMap);
                        appVsAppCountMap.put(app,0);
                    }
                    appVsAppCountMap.put(app,appVsAppCountMap.get(app)!= null ? appVsAppCountMap.get(app)+1 : 1);
                    
                    // Update machine resource list
                    Double[] appResource = appVsAppResource.get(app);
                    Double[] machineUsedResource = machineVsusedMachineResource.get(machine);
                    for (int i = 0; i < 200; ++i)
                    {
                         machineUsedResource[i] += appResource[i];
                    }
                    machineVsusedMachineResource.put(machine, machineUsedResource);
                    
                    // Add entry to instanceVsMachine
                    instanceVsMachine.put(instance, machine);
                    
                    // Write to output file
                    writeToFile(instance, machine);
                    break;
                } 
            }
        }
    }

    private static boolean checkIfAppsAreCompatible(String currentInstance, String machine) {
        String currentApp = instanceVsApp.get(currentInstance);
        HashMap<String, Integer> machineApps = machineVsappVsAppCount.get(machine);
        if(machineApps == null){
            return true;
        }
        Set<String> appsRunningOnMachine = machineVsappVsAppCount.get(machine).keySet();
        for(String app : appsRunningOnMachine){
            String app1 = currentApp;
            int a1 = machineVsappVsAppCount.get(machine).get(app1) == null ? 1 : machineVsappVsAppCount.get(machine).get(app1) + 1;
            String app2 = app;
            int a2 = machineVsappVsAppCount.get(machine).get(app2) + 1;
            int k = Integer.MAX_VALUE;
            if (appVsAppInterference.get(app1) != null)
            {
                k = appVsAppInterference.get(app1).get(app2) == null ? Integer.MAX_VALUE : appVsAppInterference.get(app1).get(app2);
            }

            if (a2 > k || k == 0)
            {
                return false;
            }
            if (appVsAppInterference.get(app2) != null && appVsAppInterference.get(app2).get(app1) != null && appVsAppInterference.get(app2).get(app1) == 0)
            {
                return true;
            }
            //not checking for app2count, coz app2count has to be greater than or equal 1
            if (appVsAppInterference.get(app1) != null && appVsAppInterference.get(app1).get(app2) != null && appVsAppInterference.get(app1).get(app2) == 0)
            {
                return false;
            }

            if (inverseAppVsAppInterference.get(app2) != null && inverseAppVsAppInterference.get(app2).get(app1) != null && inverseAppVsAppInterference.get(app2).get(app1) == 0)
            {
                return false;
            }
            //not checking for app2count, coz app2count has to be greater than or equal 1
            if (inverseAppVsAppInterference.get(app1) != null && inverseAppVsAppInterference.get(app1).get(app2) != null && inverseAppVsAppInterference.get(app1).get(app2) == 0)
            {
                return false;
            }

            // app_1391,app_8191,1
            // app2 getting added
            // a2=k and a1>1, can't be added
            // or else, can add.

            app1 = app;
            a1 = machineVsappVsAppCount.get(machine).get(app2) + 1;
            app2 = currentApp;
            a2 = machineVsappVsAppCount.get(machine).get(app1) == null ? 1 : machineVsappVsAppCount.get(machine).get(app1) + 1;
            k = Integer.MAX_VALUE;
            if (appVsAppInterference.get(app1) != null)
            {
                k = appVsAppInterference.get(app1).get(app2) == null ? Integer.MAX_VALUE : appVsAppInterference.get(app1).get(app2);
            }
            if ((a2 > k && a1 >= 1) || k == 0)
            {
                return false;
            }
        }
        return true;
    }

    private static void writeToFile(String instance, String machine) {
        sb.append(instance);
        sb.append(",");
        sb.append(machine);
        sb.append("\n");
    }

    private static void assignInstances(HashMap<String, String> nonAssignedInstances) {
        for (String instance : nonAssignedInstances.keySet()){
            assignMachineToInstance(instance);
            if(numAssigned %1000 == 0){
                System.out.println("Number assigned : "+numAssigned);
            }
        }
    }
}
