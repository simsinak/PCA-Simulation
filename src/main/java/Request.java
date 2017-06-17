import java.math.BigInteger;
import java.util.*;

/**
 * Created by sinaaskarnejad on 3/20/16.
 */
public class Request {
    HashMap<String , BigInteger> encodes=new HashMap<String, BigInteger>();
    int requestId;
    int numberOfVM;
    int start;
    String groupId;
    String cloudType;
    String typeOfPayment;
    int useTime;
    int[] counters = new int[8];
    //$$
    HashMap<String , VMProperties> properties=new HashMap<String, VMProperties>();
    // for interact
    Integer delay;
    int bandwidth;
    ArrayList<String> services=new ArrayList<String>();
    ArrayList<String> edgeVMs = new ArrayList<String>();
    ArrayList<String> middleVMs=new ArrayList<String>();
  //  int[][] independentConnections;
  //  int[][] traffic;
    ArrayList<String> independentVMs=new ArrayList<String>();
    ArrayList<String> dependentVMs=new ArrayList<String>();
    public void encodeVMs(){

        for (String key:properties.keySet()) {
            VMProperties vp=properties.get(key);
            String index = "";
            //type of provider
            if (vp.level.equals(RandomCloudInformation2.typeOfProvider[0])){
                index+="10";
            }else{
                index+="11";
            }

            //typeOfPayment
            char[] bits=new char[RandomCloudInformation2.typeOfPayment.length];
            for (int i = 0; i < RandomCloudInformation2.typeOfPayment.length ; i++) {
                if (typeOfPayment.equals(RandomCloudInformation2.typeOfPayment[i])){
                    bits[i]='1';
                    counters[0]=i;
                    continue;
                }
                bits[i]='0';
            }
            index+=new String(bits);

            //cloudType
            if (cloudType.equals(RandomCloudInformation2.cloudType[0])){
                index+="10";
                counters[1]=0;
            }else{
                index+="01";
                counters[1]=1;
            }
            //location
            bits=new char[RandomCloudInformation2.location.length];
            Arrays.fill(bits , '0');
            for (String loc: vp.location) {
                int i=Arrays.binarySearch(RandomCloudInformation2.location , loc);
                if (i>=0){
                    bits[i]='1';
                }
            }
            index+=new String(bits);
            //storage
            bits=new char[4];
            int interval=1;

            int storage=Integer.parseInt(vp.ExtraProperties.getProperty("storageG"));
            double intervalCompute = RandomCloudInformation2.storageG[0]+RandomCloudInformation2.storageGinterval;
            while (storage>intervalCompute){
                intervalCompute+=RandomCloudInformation2.storageGinterval;
                interval++;
            }
            if(interval==5){interval--;}
            Arrays.fill(bits , 0 , interval , '1');
            Arrays.fill(bits , interval , bits.length , '0');
            index+=new String(bits);
            counters[3]=interval-1;

            //memory
            bits=new char[4];
            interval=1;
            float memory=Float.parseFloat(vp.ExtraProperties.getProperty("memoryG"));
            intervalCompute = RandomCloudInformation2.memoryG[0]+RandomCloudInformation2.memoryGinterval;
            while (memory>intervalCompute){
                intervalCompute+=RandomCloudInformation2.memoryGinterval;
                interval++;
            }
            if(interval==5){interval--;}
            Arrays.fill(bits , 0 , interval , '1');
            Arrays.fill(bits , interval , bits.length , '0');
            index+=new String(bits);
            counters[4]=interval-1;

            //cpu
            bits=new char[4];
            interval=1;
            int cpu=Integer.parseInt(vp.ExtraProperties.getProperty("cpuCore"));
            intervalCompute = RandomCloudInformation2.cpuCore[0]+RandomCloudInformation2.cpuCoreinterval;
            while (cpu>intervalCompute){
                intervalCompute+=RandomCloudInformation2.cpuCoreinterval;
                interval++;
            }
            if(interval==5){interval--;}
            Arrays.fill(bits , 0 , interval , '1');
            Arrays.fill(bits , interval , bits.length , '0');
            index+=new String(bits);
            counters[5]=interval-1;

            //OS
            bits=new char[RandomCloudInformation2.OS.length];
            for (int i = 0; i < RandomCloudInformation2.OS.length ; i++) {
                if (vp.ExtraProperties.getProperty("OS").equals(RandomCloudInformation2.OS[i])){
                    bits[i]='1';
                    counters[6]=i;
                    continue;
                }
                bits[i]='0';
            }
            index+=new String(bits);

            //Hertz
            bits=new char[4];
            interval=1;
            double hertz=Double.parseDouble(vp.ExtraProperties.getProperty("Hertz"));
            intervalCompute = RandomCloudInformation2.Hertz[0]+RandomCloudInformation2.Hertzinterval;
            while (hertz>intervalCompute){
                intervalCompute+=RandomCloudInformation2.Hertzinterval;
                interval++;
            }
            if(interval==5){interval--;}
            Arrays.fill(bits , 0 , interval , '1');
            Arrays.fill(bits , interval , bits.length , '0');
            index+=new String(bits);
            counters[7]=interval-1;

            encodes.put(key , new BigInteger(index , 2));

            if(vp.location.isEmpty() || vp.location==null){
                independentVMs.add(key);
            }else{
                dependentVMs.add(key);
            }

        }
     //   independentMatrix(independentVMs);

    }
//    public void independentMatrix(ArrayList<String> independentVMs){
//            int size=independentVMs.size();
//            independentConnections=new int[size][size];
//            traffic=new int[size][size];
//        for (int i = 0; i < size-1; i++) {
//           VMProperties vpp=properties.get(independentVMs.get(i));
//            for (int j = i+1; j < size; j++) {
//                if(vpp.Services.contains(independentVMs.get(j))){
//                    independentConnections[i][j]=1;
////                    int traff=vpp.traffic.get(independentVMs.get(j));
////                    traffic[i][j]=traff;
////                    traffic[j][i]=traff;
//
//                }
//
//            }
//        }
//    }
}
