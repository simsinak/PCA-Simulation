import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

/**
 * Created by sinaaskarnejad on 5/19/16.
 */
public class RandomCloudInformation2 implements CloudsInformation {
    final static String[] typeOfProvider = {"Software", "Hardware"};
    final static String[] typeOfPayment = {"Use", "Hour", "Day", "Month", "Year"};
    final static HashMap<String , Double> typeOfHour = new HashMap<String, Double>();
    final static String[] cloudType = {"Private", "Public"};
    final static String[] location = {"US East", "US West", "Ireland", "Frankfurt","Singapore", "Japan", "Sydney", "Seoul", "Mumbai", "South America", "Australia", "China" , "Malaysia" , "Pakistan" , "Netherlands" , "Korea"};
    final static int[] storageG = {4 , 16, 32, 40, 80, 128, 160, 256,320, 512, 800, 1000 , 2000};
    final static float[] memoryG = {0.5f, 1f, 2f,3.75f, 4f,7.5f, 8f, 15f,  16f, 30f, 32f, 61f, 64f, 122f, 160, 244f};
    final static int[] cpuCore = {1, 2, 4, 8, 16, 32, 36 ,40};
    final static String[] OS = {"Linux", "RedHat", "SUSE", "Windows", "WindowsSQLS", "WindowsSQLW", "WindowsSQLE"};
    final static double[] Hertz = {1, 1.1, 1.2, 1.3, 1.7, 2, 2.1, 2.2, 2.3, 2.7, 3.0, 3.1, 3.2, 3.3, 3.4, 3.7};
    final static double[] price = {};
    final static double[] federationPriceING = {0.001, 0.01, 0.02, 0.04, 0.03};
    final static double[] federationPriceOutG = {0.001, 0.01, 0.02, 0.04, 0.03};
    final static double[] InternetPriceING = {0.05, 0.12, 0.25, 0.5 , 0.75 , 1.00, 1.5, 2.00};
    final static double[] InternetPriceOutG = {0.05, 0.12, 0.25, 0.5 , 0.75 , 1.00, 1.5 ,2.00};
    static double storageGinterval;
    static double memoryGinterval;
    static double cpuCoreinterval;
    static double Hertzinterval;
    //ms must be greater than 30
    final static int BOUND=400;
    public  ArrayList<Cloud> createCloudInformation(int number_provier)  {
        typeOfHour.put("Use" , 1.0);
        typeOfHour.put("Hour" , 2.0);
        typeOfHour.put("Day" , 48.0);
        typeOfHour.put("Month" ,1440.0 );
        typeOfHour.put("Year" , 17280.0);

        long startTime=System.currentTimeMillis();
        ArrayList<Cloud> clouds=new ArrayList<Cloud>();
        Scanner scanner=null;
        HashMap<String, Double> ranks=new HashMap<String, Double>();
        HashMap<String, Double> availabilityrank=new HashMap<String, Double>();
        Random random=new Random();
        loadRanks(ranks , availabilityrank);
        try {
            scanner=new Scanner(new File("available IAASclouds.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < number_provier; i++) {

            String cn=scanner.nextLine();
            boolean ff=false;
            for (String keys:ranks.keySet()){
                if (keys.toLowerCase().contains(cn.toLowerCase())){
                    clouds.add(RandomCloudInformation2.createCloud(cn , ranks.get(keys)));
                    ff=true;
                    break;
                }
            }
            if (!ff)
            clouds.add(RandomCloudInformation2.createCloud(cn , random.nextInt(5)+1));
        }
        clouds.sort(new Comparator<Cloud>() {
            public int compare(Cloud o1, Cloud o2) {
                return  o1.index.compareTo(o2.index);
            }
        });
        //System.out.println(clouds.get(0).index+" <"+clouds.get(1).index);
        try {
            test.bw.write("CLOUD INFORMATION CREATED RANDOMLY IN "+(System.currentTimeMillis()-startTime)+" Milis\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clouds;

    }

    private void loadRanks(HashMap<String, Double> ranks , HashMap<String, Double> availability) {
        Scanner sc=null;
        try {
            sc=new Scanner(new File("ranks.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (sc.hasNext()){
            String text=sc.nextLine();
            if(!text.startsWith("#")){
                String[] tokens=text.split(",");
                ranks.put(tokens[0] , Double.parseDouble(tokens[2]));
                availability.put(tokens[0] ,Double.parseDouble(tokens[1]) );
            }
        }
    }

    public HashMap<String, Integer> calculateBitNumber() {
        return null;
    }

    public static Cloud createCloud(String cloudName , double rank) {
        Random r = new Random();
        Cloud c = new Cloud();
        String index="";
        //type of provider
        int choosedNumber=r.nextInt(typeOfProvider.length);
        c.p.put("typeOfProvider", typeOfProvider[choosedNumber]);
        index+=Integer.toBinaryString(choosedNumber+2);
        //type of payment;
        char[] bits=new char[typeOfPayment.length];
        Arrays.fill(bits , '0');
        int numtypeOfPayment = r.nextInt(typeOfPayment.length) + 1;
        c.typeOfPayment=new String[numtypeOfPayment];
        HashMap<String, Integer> hm = new HashMap<String, Integer>();

        for (int i = 0; i < numtypeOfPayment; i++) {
            choosedNumber=r.nextInt(typeOfPayment.length);
            String choosed = typeOfPayment[choosedNumber];
            if (hm.containsKey(choosed)) {
                i--;
                continue;
            } else {
                bits[choosedNumber]='1';
                c.typeOfPayment[c.counters[0]++]=choosed;
                hm.put(choosed , 1);
                if (c.p.containsKey("typeOfPayment")) {
                    c.p.put("typeOfPayment", ((String) c.p.get("typeOfPayment")).concat("," + choosed));

                } else {
                    c.p.put("typeOfPayment", choosed);

                }
            }
        }
        index+=new String(bits);
        //cloudType
        bits=new char[cloudType.length];
        Arrays.fill(bits , '0');
        int numOfct = r.nextInt(cloudType.length) + 1;
        c.cloudType=new String[numOfct];
        hm.clear();
        for (int i = 0; i < numOfct; i++) {
            choosedNumber=r.nextInt(cloudType.length);
            String choosed = cloudType[choosedNumber];
            if (hm.containsKey(choosed)) {
                i--;
                continue;
            } else {
                bits[choosedNumber]='1';
                c.cloudType[c.counters[1]++]=choosed;

                hm.put(choosed , 1);
                if (c.p.containsKey("cloudType")) {
                    c.p.put("cloudType", ((String) c.p.get("cloudType")).concat("," + choosed));

                } else {
                    c.p.put("cloudType", choosed);

                }
            }
        }
        index+=new String(bits);

        //locations
        bits=new char[location.length];
        Arrays.fill(bits , '0');
        if (cloudName.equals("amazon.com")){
            int numOfLocation = 10;
            c.location=new String[numOfLocation];
            hm.clear();
            choosedNumber=-1;
            for (int i = 0; i < numOfLocation; i++) {
                choosedNumber++;
                String choosed = location[choosedNumber];
                    bits[choosedNumber]='1';
                    c.location[c.counters[2]++]=choosed;
                    if (c.p.containsKey("location")) {
                        c.p.put("location", ((String) c.p.get("location")).concat("," + choosed));

                    } else {
                        c.p.put("location", choosed);

                    }

            }
        }else {
        int numOfLocation = r.nextInt(location.length) + 1;
        c.location=new String[numOfLocation];
        hm.clear();
        for (int i = 0; i < numOfLocation; i++) {
            choosedNumber=r.nextInt(location.length);
            String choosed = location[choosedNumber];
            if (hm.containsKey(choosed)) {
                i--;
                continue;
            } else {
                bits[choosedNumber]='1';
                c.location[c.counters[2]++]=choosed;
                hm.put(choosed , 1);
                if (c.p.containsKey("location")) {
                    c.p.put("location", ((String) c.p.get("location")).concat("," + choosed));

                } else {
                    c.p.put("location", choosed);

                }
            }
        }}
        index+=new String(bits);

        //Storage
        bits=new char[4];
        int max=0;
        if (cloudName.equals("amazon.com")){
            int numOfStorage = storageG.length;
            c.storageG=new int[numOfStorage];
            hm.clear();
            choosedNumber=-1;
            for (int i = 0; i < numOfStorage; i++) {
                choosedNumber++;
                String choosed = String.valueOf(storageG[choosedNumber]);
                if(max<storageG[choosedNumber]){
                    max=storageG[choosedNumber];
                }
                    c.storageG[c.counters[3]++]=Integer.parseInt(choosed);
                    if (c.p.containsKey("storageG")) {
                        c.p.put("storageG", ((String) c.p.get("storageG")).concat("," + choosed));

                    } else {
                        c.p.put("storageG", choosed);

                    }

            }
        }else {
        int numOfStorage = r.nextInt(storageG.length) + 1;
        c.storageG=new int[numOfStorage];
        hm.clear();
        for (int i = 0; i < numOfStorage; i++) {
            choosedNumber=r.nextInt(storageG.length);
            String choosed = String.valueOf(storageG[choosedNumber]);
            if(max<storageG[choosedNumber]){
                max=storageG[choosedNumber];
            }
            if (hm.containsKey(choosed)) {
                i--;
                continue;
            } else {
                c.storageG[c.counters[3]++]=Integer.parseInt(choosed);
                hm.put(choosed , 1);
                if (c.p.containsKey("storageG")) {
                    c.p.put("storageG", ((String) c.p.get("storageG")).concat("," + choosed));

                } else {
                    c.p.put("storageG", choosed);

                }
            }
        }}

        c.storageMax=max;
        int interval=1;
        double intervalCompute = storageG[0]+storageGinterval;
        while (intervalCompute<max){
            intervalCompute+=storageGinterval;
            interval++;
        }
        if(interval==5){interval--;}
        Arrays.fill(bits , 0 , interval , '1');
        Arrays.fill(bits , interval , bits.length , '0');
        index+=new String(bits);

        //memoryG
        bits=new char[4];
        double maxd=0;
        Arrays.fill(bits , '0');
        int numOfmem = r.nextInt(memoryG.length) + 1;
        if (cloudName.equals("amazon.com")){
            numOfmem=memoryG.length;
        }
        c.memoryG=new float[numOfmem];
        hm.clear();
        for (int i = 0; i < numOfmem; i++) {
            choosedNumber=r.nextInt(memoryG.length);
            String choosed = String.valueOf(memoryG[choosedNumber]);
            if(maxd<memoryG[choosedNumber]){
                maxd=memoryG[choosedNumber];
            }
            if (hm.containsKey(choosed)) {
                i--;
                continue;
            } else {
                c.memoryG[c.counters[4]++]=Float.parseFloat(choosed);
                hm.put(choosed , 1);
                if (c.p.containsKey("memoryG")) {
                    c.p.put("memoryG", ((String) c.p.get("memoryG")).concat("," + choosed));

                } else {
                    c.p.put("memoryG", choosed);

                }
            }
        }
        c.memoryMax=maxd;
        interval=1;

        intervalCompute = memoryG[0]+memoryGinterval;
        while (intervalCompute<maxd){
            intervalCompute+=memoryGinterval;
            interval++;
        }
        if(interval==5){interval--;}
        Arrays.fill(bits , 0 , interval , '1');
        Arrays.fill(bits , interval , bits.length , '0');
        index+=new String(bits);

        //cpuCore
        bits=new char[4];
        max=0;
        Arrays.fill(bits , '0');
        if (cloudName.equals("amazon.com")){
            int cpunum = cpuCore.length;
            c.cpuCore=new int[cpunum];
            hm.clear();
            choosedNumber=-1;
            for (int i = 0; i < cpunum; i++) {
                choosedNumber++;
                String choosed = String.valueOf(cpuCore[choosedNumber]);
                if(max<cpuCore[choosedNumber]){
                    max=cpuCore[choosedNumber];
                }
                    c.cpuCore[c.counters[5]++]=Integer.parseInt(choosed);
                    if (c.p.containsKey("cpuCore")) {
                        c.p.put("cpuCore", ((String) c.p.get("cpuCore")).concat("," + choosed));

                    } else {
                        c.p.put("cpuCore", choosed);

                    }

            }
        }else{
        int cpunum = r.nextInt(cpuCore.length) + 1;
        c.cpuCore=new int[cpunum];
        hm.clear();
        for (int i = 0; i < cpunum; i++) {
            choosedNumber=r.nextInt(cpuCore.length);
            String choosed = String.valueOf(cpuCore[choosedNumber]);
            if(max<cpuCore[choosedNumber]){
                max=cpuCore[choosedNumber];
            }
            if (hm.containsKey(choosed)) {
                i--;
                continue;
            } else {
                c.cpuCore[c.counters[5]++]=Integer.parseInt(choosed);
                hm.put(choosed , 1);
                if (c.p.containsKey("cpuCore")) {
                    c.p.put("cpuCore", ((String) c.p.get("cpuCore")).concat("," + choosed));

                } else {
                    c.p.put("cpuCore", choosed);

                }
            }
        }}
        c.coreMax=max;
        interval=1;
        intervalCompute = cpuCore[0]+cpuCoreinterval;
        while (intervalCompute<max){
            intervalCompute+=cpuCoreinterval;
            interval++;
        }
        if(interval==5){interval--;}
        Arrays.fill(bits , 0 , interval , '1');
        Arrays.fill(bits , interval , bits.length , '0');
        index+=new String(bits);

        //OS
        bits=new char[OS.length];
        Arrays.fill(bits , '0');
        int osnum = r.nextInt(OS.length) + 1;
        if (cloudName.equals("amazon.com")){
            osnum=OS.length;
        }
        c.OS=new String[osnum];
        hm.clear();
        for (int i = 0; i < osnum; i++) {
            choosedNumber=r.nextInt(OS.length);
            String choosed = OS[choosedNumber];
            if (hm.containsKey(choosed)) {
                i--;
                continue;
            } else {
                bits[choosedNumber]='1';
                c.OS[c.counters[6]++]=choosed;
                hm.put(choosed , 1);
                if (c.p.containsKey("OS")) {
                    c.p.put("OS", ((String) c.p.get("OS")).concat("," + choosed));

                } else {
                    c.p.put("OS", choosed);

                }
            }
        }
        index+=new String(bits);

        //Hertz
        bits=new char[4];
        maxd=0;
        Arrays.fill(bits , '0');
        int numHertz = r.nextInt(Hertz.length) + 1;
        c.Hertz=new double[numHertz];
        hm.clear();
        for (int i = 0; i < numHertz; i++) {
            choosedNumber=r.nextInt(Hertz.length);
            String choosed = String.valueOf(Hertz[choosedNumber]);
            if(maxd<Hertz[choosedNumber]){
                maxd=Hertz[choosedNumber];
            }
            if (hm.containsKey(choosed)) {
                i--;
                continue;
            } else {
                c.Hertz[c.counters[7]++]=Double.parseDouble(choosed);

                hm.put(choosed , 1);
                if (c.p.containsKey("Hertz")) {
                    c.p.put("Hertz", ((String) c.p.get("Hertz")).concat("," + choosed));

                } else {
                    c.p.put("Hertz", choosed);

                }
            }
        }
        interval=1;
        c.hertzMax=maxd;
        intervalCompute = Hertz[0]+Hertzinterval;
        while (intervalCompute<maxd){
            intervalCompute+=Hertzinterval;
            interval++;
        }
        if(interval==5){interval--;}
        Arrays.fill(bits , 0 , interval , '1');
        Arrays.fill(bits , interval , bits.length , '0');
        index+=new String(bits);

        //prices
        c.calculatePrice2(rank);
        c.fillDynamicResponseTime();
        c.availability = 99+ Math.random();
        c.throughput = 16.3+Math.random()*33.7;
        c.federationPriceING=federationPriceING[r.nextInt(federationPriceING.length)];
        c.federationPriceOutG=federationPriceOutG[r.nextInt(federationPriceOutG.length)];
        c.InternetPriceING = InternetPriceING[r.nextInt(InternetPriceING.length)];
        c.InternetPriceOutG =InternetPriceOutG[r.nextInt(InternetPriceOutG.length)];
        if (cloudName.equals("amazon.com")){
            c.federationPriceING=federationPriceING[r.nextInt(federationPriceING.length-4)];
            c.federationPriceOutG=federationPriceOutG[r.nextInt(federationPriceOutG.length-4)];
            c.InternetPriceING = InternetPriceING[r.nextInt(InternetPriceING.length-4)];
            c.InternetPriceOutG = InternetPriceOutG[r.nextInt(InternetPriceOutG.length-4)];
        }
        //naming
        c.name= cloudName;
        c.index = new BigInteger(index , 2);
        return c;


    }
    public static void computeInervalsForProperties(){
        Arrays.sort(storageG);
        Arrays.sort(memoryG);
        Arrays.sort(cpuCore);
        Arrays.sort(Hertz);


        int min=storageG[0];
        int max=storageG[storageG.length-1];
        storageGinterval=((double)(max-min))/4;

        float minf=memoryG[0];
        float maxf=memoryG[memoryG.length-1];
        memoryGinterval=(maxf-minf)/4;

        min=cpuCore[0];
        max=cpuCore[cpuCore.length-1];
        cpuCoreinterval=((double)(max-min))/4;

        double mind=Hertz[0];
        double maxd=Hertz[Hertz.length-1];
        Hertzinterval=(maxd-mind)/4;

    }

}
