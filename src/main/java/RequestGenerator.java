import java.io.IOException;
import java.util.*;

/**
 * Created by sinaaskarnejad on 6/30/16.
 */
public class RequestGenerator implements RequestGeneratorInterface {
    int idCounter = 1;
    int VMCounter = 1;
    int tempVMCounter = 0;
    int start = 0;
    int maxvmsizeN = 19;
    static Set<Integer> interactives = new HashSet<Integer>();
    static String[] types = {"T1", "T2", "T3", "T4", "T5"};
    final String VM_NAME = "VM";
    final String IVM_NAME = "IVM";
    int[] bandwidth = new int[]{128000, 256000, 512000, 1024000, 2048000, 4096000, 8192000};
    double[] bandwidth2 = new double[]{512000, 1024000, 2048000, 4096000, 8192000, 1073741824000.0, 2147483648000.0, 4294967296000.0};

    String[] serviceName;
    public void reformRequest(Request oldrequest){
        Random random=new Random();
        oldrequest.bandwidth=bandwidth[random.nextInt(bandwidth.length)];;
        for (VMProperties vps: oldrequest.properties.values()){
            Iterator<String> servicenames=vps.bandwidth.keySet().iterator();
            while (servicenames.hasNext()){
                String servicename=servicenames.next();
                double bandwidthB = bandwidth2[random.nextInt(bandwidth2.length)];
                int trafficB = random.nextInt((int) bandwidthB / 1000);
                vps.bandwidth.put(servicename , bandwidthB);
                vps.traffic.put(servicename , trafficB);

            }
        }

    }

    public Request generateRequestOneByOne() {
        start = VMCounter;
        tempVMCounter = VMCounter - 1;
        Random random = new Random();
        Request request = new Request();
        request.requestId = idCounter;
        request.start=start;
        // 3 month 0.5 hour base
        request.useTime=random.nextInt(50)+1;
        idCounter++;
        interactives.add(start);

        request.numberOfVM = random.nextInt(maxvmsizeN) + 2;
//        System.out.println("%%%%%%%%%%%%");
//        for (int i = start; i <request.numberOfVM; i++) {
//            System.out.println(i);
//        }
//        System.out.println("%%%%%%%%%%");
        request.groupId = ((char) (random.nextInt(58) + 65)) + "";
        request.cloudType =  RandomCloudInformation2.cloudType[random.nextInt(2)];
        if (random.nextBoolean()) {
            request.typeOfPayment = RandomCloudInformation2.typeOfPayment[random.nextInt(RandomCloudInformation2.typeOfPayment.length)];
        } else {
            request.typeOfPayment = "Hour";
        }
      //  request.delay = random.nextInt(800) + 200;
        request.delay=0;
        request.bandwidth = bandwidth[random.nextInt(bandwidth.length)];
        createConnectionMatrix(request.numberOfVM, request);
        int serviceNumber = request.numberOfVM / 5;

        if (serviceNumber != 0) {
            serviceNumber = random.nextInt(serviceNumber);
        }
        int[][] tempServices = createServiceMatrix(request.numberOfVM, serviceNumber, request , tempVMCounter);
        if (serviceNumber > 0 && tempVMCounter != 0) {
            serviceName = new String[serviceNumber];
            for (int i = 0; i < serviceNumber; i++) {
                boolean connected = false;
                int cn = 0;
                for (int j = 0; j < request.numberOfVM; j++) {
                    if (tempServices[j][i] == 1) {
                        if (!connected) {
                            cn = random.nextInt(tempVMCounter) + 1;
                            while (interactives.contains(cn) || MapRepository.removedVMs.contains(VM_NAME+cn)) {
                                cn = random.nextInt(tempVMCounter) + 1;
                            }
                            serviceName[i] = VM_NAME + cn;
                            request.services.add(VM_NAME + cn);
                            connected = true;
                        }
                        if (j == 0) {
                            VMProperties vmptemp=request.properties.get(IVM_NAME + start);
                            double bandwidthB = bandwidth2[random.nextInt(bandwidth2.length)];
                            int trafficB = random.nextInt((int) bandwidthB / 1000);
                            vmptemp.bandwidth.put(VM_NAME + cn , bandwidthB);
                            vmptemp.traffic.put(VM_NAME + cn , trafficB);
                            vmptemp.Services.add(VM_NAME + cn);
                            request.edgeVMs.add(IVM_NAME + start);

                        } else {
                            VMProperties vmptemp=request.properties.get(VM_NAME + (start + j));
                            double bandwidthB = bandwidth2[random.nextInt(bandwidth2.length)];
                            int trafficB = random.nextInt((int) bandwidthB / 1000);
                            vmptemp.bandwidth.put(VM_NAME + cn , bandwidthB);
                            vmptemp.traffic.put(VM_NAME + cn , trafficB);
                            vmptemp.Services.add(VM_NAME + cn);
                            request.edgeVMs.add(VM_NAME + (j + start));


                        }
                    }
                }
            }
        }

        request.encodeVMs();
        return request;
    }

    public Collection<Request> generateRequestOnce(int numberOfRequests) {
        long startTime=System.currentTimeMillis();
        ArrayList<Request> requests = new ArrayList<Request>(numberOfRequests);
        for (int i = 0; i < numberOfRequests; i++) {
            requests.add(generateRequestOneByOne());
        }
        try {
            test.bw.write(numberOfRequests+" REQUEST CREATED IN "+(System.currentTimeMillis()-startTime)+" Milis\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return requests;
    }

    public void createConnectionMatrix(int size, Request request) {
        Random random2 = new Random();
        int[][] temp = new int[size][size];
        for (int i = 0; i < size; i++) {
            if (i >= 1) {
                int index = random2.nextInt(i);
                temp[index][i] = 1;
            }
            VMProperties vmProperties = null;
            vmProperties = new VMProperties();
            vmProperties.Type = types[random2.nextInt(types.length)];
            if(i==0){
                vmProperties.name = IVM_NAME+start;

            }else{
                vmProperties.name = VM_NAME+(start+i);
             //   System.out.println("->"+VM_NAME+(start+i));
            }
            MapRepository.typeNames.put(vmProperties.name,vmProperties.Type);
            vmProperties.location = new HashSet<String>();
            if (Math.random() < 0.3) {
                int fixlocationNumber = random2.nextInt(2) + 1;
                for (int j = 0; j < fixlocationNumber; j++) {
                    String locationName = RandomCloudInformation2.location[random2.nextInt(RandomCloudInformation2.location.length)];
                    vmProperties.location.add(locationName);
                }
            }
            if (i == 0) {
                vmProperties.QOS.put("responseTime", request.delay);
            } else {
                vmProperties.QOS.put("responseTime", request.delay);
             //   vmProperties.QOS.put("responseTime", random2.nextInt(190) + 10);
            }
            vmProperties.level = "Hardware";
            vmProperties.ExtraProperties.put("storageG", String.valueOf(RandomCloudInformation2.storageG[random2.nextInt(RandomCloudInformation2.storageG.length)]));
            vmProperties.ExtraProperties.put("memoryG", String.valueOf(RandomCloudInformation2.memoryG[random2.nextInt(RandomCloudInformation2.memoryG.length)]));
            vmProperties.ExtraProperties.put("cpuCore", String.valueOf(RandomCloudInformation2.cpuCore[random2.nextInt(RandomCloudInformation2.cpuCore.length)]));
            vmProperties.ExtraProperties.put("OS", String.valueOf(RandomCloudInformation2.OS[random2.nextInt(RandomCloudInformation2.OS.length)]));
            vmProperties.ExtraProperties.put("Hertz", String.valueOf(RandomCloudInformation2.Hertz[random2.nextInt(RandomCloudInformation2.Hertz.length)]));
            if (i == 0) {
                request.properties.put(IVM_NAME + start, vmProperties);
            } else {
                request.properties.put(VM_NAME + (start + i), vmProperties);

            }
            VMCounter++;
        }
        for (int i = 0; i < size - 1; i++) {


            for (int j = i + 1; j < size; j++) {
                if (random2.nextBoolean()) {
                    temp[i][j] = 1;
                }
                if (temp[i][j] == 1) {
                    VMProperties vmp1 = null;
                    VMProperties vmp2 = null;
                    if (i == 0) {
                        vmp1 = request.properties.get(IVM_NAME + start);
                        vmp2 = request.properties.get(VM_NAME + (start + j));

                        double bandwidthB = bandwidth2[random2.nextInt(bandwidth2.length)];
                        vmp1.bandwidth.put(VM_NAME + (start + j), bandwidthB);
                        vmp2.bandwidth.put(IVM_NAME + (start), bandwidthB);
                        int trafficB = random2.nextInt((int) bandwidthB / 1000);
                        vmp1.traffic.put(VM_NAME + (start + j), trafficB);
                        vmp2.traffic.put(IVM_NAME + (start), trafficB);
                        vmp1.Services.add(VM_NAME + (start + j));
                        vmp2.Services.add(IVM_NAME + (start));
                    } else {
                        vmp1 = request.properties.get(VM_NAME + (start + i));
                        vmp2 = request.properties.get(VM_NAME + (start + j));
                        double bandwidthB = bandwidth2[random2.nextInt(bandwidth2.length)];
                        vmp1.bandwidth.put(VM_NAME + (start + j), bandwidthB);
                        vmp2.bandwidth.put(VM_NAME + (start + i), bandwidthB);
                        int trafficB = random2.nextInt((int) bandwidthB / 1000);
                        vmp1.traffic.put(VM_NAME + (start + j), trafficB);
                        vmp2.traffic.put(VM_NAME + (start + i), trafficB);
                        vmp1.Services.add(VM_NAME + (start + j));
                        vmp2.Services.add(VM_NAME + (start + i));
                    }

                }
            }
        }
    }

    public int[][] createServiceMatrix(int row, int col, Request request , int possiblecounter) {
        Random random2 = new Random();
        int[][] temp=null;
        if (col!=0){
            temp=new int[row][col];;
        }
        for (int i = 0; i < row; i++) {
            if (random2.nextBoolean() && possiblecounter>0) {
                boolean isconnected = false;
                for (int j = 0; j < col; j++) {
                    if (Math.random() < 0.3) {
                        temp[i][j] = 1;
                        isconnected = true;
                        //   vmp1.Services.add("VMNAME");
                    }
                }
                if (!isconnected) {
                    if (i == 0)
                        request.middleVMs.add(IVM_NAME + (i + start));
                    else
                        request.middleVMs.add(VM_NAME + (i + start));
                }
            } else {
                if (i == 0)
                    request.middleVMs.add(IVM_NAME + (i + start));
                else
                    request.middleVMs.add(VM_NAME + (i + start));
            }
        }
        return temp;

    }
    public Request generateRequestOneByOne(Request oldrequest) {
        start = oldrequest.start;
        VMCounter = start;
        tempVMCounter = start - 1;

        Random random = new Random();
        Request request = new Request();
        request.requestId = oldrequest.requestId;
        request.start=oldrequest.start;
        request.numberOfVM = oldrequest.numberOfVM;
//        System.out.println("recreated");
//        System.out.println("%%%%%%%%%%%%");
//        for (int i = start; i <request.numberOfVM; i++) {
//            System.out.println(i);
//        }
//        System.out.println("%%%%%%%%%%");
        request.groupId = oldrequest.groupId;
        request.cloudType = RandomCloudInformation2.cloudType[random.nextInt(2)];
        request.useTime=oldrequest.useTime;
        if (random.nextBoolean()) {
            request.typeOfPayment = RandomCloudInformation2.typeOfPayment[random.nextInt(RandomCloudInformation2.typeOfPayment.length)];
        } else {
            request.typeOfPayment = "Hour";
        }
     //   request.delay = random.nextInt(800) + 200;
        request.delay=0;
        request.bandwidth = bandwidth[random.nextInt(bandwidth.length)];
        createConnectionMatrix(request.numberOfVM, request);
        int serviceNumber = request.numberOfVM / 5;

        if (serviceNumber != 0) {
            serviceNumber = random.nextInt(serviceNumber);
        }
        int[][] tempServices = createServiceMatrix(request.numberOfVM, serviceNumber, request , tempVMCounter);
        if (serviceNumber > 0 && tempVMCounter != 0) {
            serviceName = new String[serviceNumber];
            for (int i = 0; i < serviceNumber; i++) {
                boolean connected = false;
                int cn = 0;
                for (int j = 0; j < request.numberOfVM; j++) {
                    if (tempServices[j][i] == 1) {
                        if (!connected) {
                            cn = random.nextInt(tempVMCounter) + 1;
                            while (interactives.contains(cn) || MapRepository.removedVMs.contains(VM_NAME+cn)) {
                                cn = random.nextInt(tempVMCounter) + 1;
                            }
                            serviceName[i] = VM_NAME + cn;
                            request.services.add(VM_NAME + cn);
                            connected = true;
                        }
                        if (j == 0) {
                            VMProperties vmptemp=request.properties.get(IVM_NAME + start);
                            double bandwidthB = bandwidth2[random.nextInt(bandwidth2.length)];
                            int trafficB = random.nextInt((int) bandwidthB / 1000);
                            vmptemp.bandwidth.put(VM_NAME + cn , bandwidthB);
                            vmptemp.traffic.put(VM_NAME + cn , trafficB);
                            vmptemp.Services.add(VM_NAME + cn);
                            request.edgeVMs.add(IVM_NAME + start);

                        } else {
                            VMProperties vmptemp=request.properties.get(VM_NAME + (start + j));
                            double bandwidthB = bandwidth2[random.nextInt(bandwidth2.length)];
                            int trafficB = random.nextInt((int) bandwidthB / 1000);
                            vmptemp.bandwidth.put(VM_NAME + cn , bandwidthB);
                            vmptemp.traffic.put(VM_NAME + cn , trafficB);
                            vmptemp.Services.add(VM_NAME + cn);
                            request.edgeVMs.add(VM_NAME + (j + start));


                        }
                    }
                }
            }
        }

        request.encodeVMs();
        return request;
    }

}
