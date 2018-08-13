package project1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main_2 {
    
    int numAssigned = 0;
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
    
    HashMap<String, HashMap<String, Integer>> machineVsAllergicApps;
    HashMap<String, String> instanceVsMachine;

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
        
    }
    public Main_2() {
        super();
    }
    
    public static void main(String[] args) throws IOException {
        //1. Load MACHINE RESOURCE FILE
        loadMachineResourcesFile(new BufferedReader(new FileReader(Utility.machineResourceFile)));
        
        //2. Load APP RESOURCE FILE
        loadAppResourcesFile(new BufferedReader(new FileReader(Utility.appResourcesFile)));
        
        //3. Load INSTANCE DEPLOY FILE
        loadInstanceDeployFile(new BufferedReader(new FileReader(Utility.instanceDeployFile)));
        
        //4. Load APP INTERFERENCE FILE
        loadAppInterferenceFile(new BufferedReader(new FileReader(Utility.appInterferenceFile)));
        
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
}
