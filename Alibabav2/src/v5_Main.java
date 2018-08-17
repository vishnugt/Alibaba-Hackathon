package project1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.Arrays;
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
    
    static String[] failedList = {"inst_21728","inst_45675","inst_21593","inst_94433","inst_72966","inst_7291","inst_18562","inst_43530","inst_7243","inst_79460","inst_55344","inst_57992","inst_43353","inst_69966","inst_67305","inst_57807","inst_33848","inst_50955","inst_96281","inst_98882","inst_23207","inst_7935","inst_13856","inst_98821","inst_59147","inst_72184","inst_13778","inst_62769","inst_59126","inst_23196","inst_5558","inst_96843","inst_70210","inst_5582","inst_69294","inst_69296","inst_94210","inst_11886","inst_35758","inst_33111","inst_82092","inst_11683","inst_84536","inst_99598","inst_51484","inst_75447","inst_90980","inst_99229","inst_53832","inst_38221","inst_28851","inst_26944","inst_14961","inst_63994","inst_12366","inst_99821","inst_24203","inst_26859","inst_63794","inst_38756","inst_97097","inst_90255","inst_79776","inst_43734","inst_67682","inst_90029","inst_31695","inst_1834","inst_80639","inst_53784","inst_75020","inst_65567","inst_53506","inst_53478","inst_53460","inst_19464","inst_95947","inst_68200","inst_44284","inst_22780","inst_44061","inst_95688","inst_56710","inst_30164","inst_68668","inst_6151","inst_32602","inst_93543","inst_46097","inst_10062","inst_73476","inst_34455","inst_4539","inst_22225","inst_83232","inst_40785","inst_76709","inst_4786","inst_50056","inst_62084","inst_74080","inst_40621","inst_52607","inst_27594","inst_27567","inst_88585","inst_62680","inst_98657","inst_35027","inst_62329","inst_98301","inst_39989","inst_40904","inst_27961","inst_27968","inst_66630","inst_91529","inst_29435","inst_17356","inst_81954","inst_17318","inst_91203","inst_78273","inst_52346","inst_30832","inst_9469","inst_18144","inst_57653","inst_70629","inst_94541","inst_7045","inst_21405","inst_69343","inst_96947","inst_94350","inst_45312","inst_72959","inst_84927","inst_92330","inst_70922","inst_80216","inst_31247","inst_18274","inst_69755","inst_31179","inst_94738","inst_82761","inst_55168","inst_5080","inst_47440","inst_11464","inst_72398","inst_98854","inst_84220","inst_59212","inst_86831","inst_35283","inst_60142","inst_5396","inst_60145","inst_5394","inst_35178","inst_23951","inst_11971","inst_5492","inst_84823","inst_23874","inst_69152","inst_69154","inst_59723","inst_70063","inst_33065","inst_45092","inst_5729","inst_60608","inst_23583","inst_5965","inst_26538","inst_63370","inst_75222","inst_53874","inst_28860","inst_63912","inst_1289","inst_87723","inst_12108","inst_12126","inst_12075","inst_61028","inst_63697","inst_87660","inst_92977","inst_43945","inst_43963","inst_43966","inst_80802","inst_18862","inst_28114","inst_90046","inst_53059","inst_65047","inst_55620","inst_67671","inst_1857","inst_28789","inst_87123","inst_90710","inst_77589","inst_65533","inst_89493","inst_95920","inst_56345","inst_68317","inst_71920","inst_58956","inst_34916","inst_10969","inst_56282","inst_93209","inst_93256","inst_20270","inst_34838","inst_46867","inst_8319","inst_19228","inst_10851","inst_83759","inst_81175","inst_44090","inst_56830","inst_68832","inst_81825","inst_81822","inst_91157","inst_78143","inst_66164","inst_17113","inst_6067","inst_32702","inst_68691","inst_93647","inst_56477","inst_95116","inst_85787","inst_58079","inst_95056","inst_10007","inst_10086","inst_48624","inst_73411","inst_10664","inst_10611","inst_22554","inst_58551","inst_95377","inst_83396","inst_95398","inst_61988","inst_83312","inst_58250","inst_71236","inst_64884","inst_52841","inst_13273","inst_52786","inst_88730","inst_4787","inst_27736","inst_4722","inst_25048","inst_49055","inst_2208","inst_35082","inst_86630","inst_86620","inst_59002","inst_74626","inst_23062","inst_62577","inst_2528","inst_49490","inst_25394","inst_25387","inst_2706","inst_49302","inst_37332","inst_132","inst_29603","inst_78599","inst_30394","inst_32915","inst_56924","inst_78216","inst_91251","inst_54292","inst_93890","inst_88487","inst_39483","inst_27379","inst_66913","inst_76252","inst_793","inst_29858","inst_39138"};

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
        
        reReAssign(failedList);
        
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
            if(Arrays.asList(failedList).contains(instance)){
                return;
            }
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
            if(Arrays.asList(failedList).contains(instance)){
                return;
            }
            assignMachineToInstance(instance);
            if(numAssigned %1000 == 0){
                System.out.println("Number assigned : "+numAssigned);
            }
        }
    }

    private static void reReAssign(String[] failedList) {
        for (String instance : failedList){
            String machine = instanceVsMachine.get(instance);
            removeInstanceFromMachine(instance, machine);
            assignMachineToInstance(instance);
            if(numAssigned %50 == 0){
                System.out.println("Number assigned : "+numAssigned);
            }
        }
    }
}
