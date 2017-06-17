import java.math.BigInteger;
import java.util.*;

/**
 * Created by sinaaskarnejad on 5/19/16.
 */
public class RandomCloudInformation implements CloudsInformation {
    final static String[] typeOfProvider = {"Software", "Hardware", "{Software , HardWare}"};
    final static String[] typeOfPayment = {"Use", "Hour", "Day", "Month", "Year"};
    final static String[] cloudType = {"Private", "Public"};
    final static String[] location = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"};
    final static int[] storageG = {16, 20, 40, 80, 128, 256, 512, 800, 1024};
    final static float[] memoryG = {0.5f, 1f, 2f, 4f, 8f, 16f, 32f, 64f, 160, 3.75f, 7.5f, 15f, 30f, 61f, 122f, 244f};
    final static int[] cpuCore = {1, 2, 4, 8, 16, 40, 36, 32};
    final static String[] OS = {"Linux", "RedHat", "SUSE", "Windows", "WindowsSQLS", "WindowsSQLW", "WindowsSQLE"};
    final static double[] Hertz = {1, 1.1, 1.2, 1.3, 1.7, 2, 2.1, 2.2, 2.3, 2.7, 3.0, 3.1, 3.2, 3.3, 3.4, 3.7};
    final static double[] price = {};
    final static double[] federationPriceING = {0.0, 0.01, 0.02, 0.04, 0.03};
    final static double[] federationPriceOutG = {0.0, 0.01, 0.02, 0.04, 0.03};
    final static double[] InternetPriceING = {0.0, 0.09, 0.085, 0.07, 0.05};
    final static double[] InternetPriceOutG = {0.0, 0.09, 0.085, 0.07, 0.05};
    public  ArrayList<Cloud> createCloudInformation(int number_provider) {
        ArrayList<Cloud> clouds=new ArrayList<Cloud>();
        for (int i = 0; i < number_provider; i++) {
            clouds.add(RandomCloudInformation.createCloud());
        }
        return clouds;

    }

    public HashMap<String, Integer> calculateBitNumber() {
        return null;
    }

    public static Cloud createCloud() {
        Random r = new Random();
        Cloud c = new Cloud();
        String index="";
        //type of provider
        int choosedNumber=r.nextInt(typeOfProvider.length);
        c.p.put("typeOfProvider", typeOfProvider[choosedNumber]);
        index+=Integer.toBinaryString(choosedNumber+1);
        if(index.length()==1){
            index='0'+index;
        }
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
        }
        index+=new String(bits);

        //Storage
        bits=new char[storageG.length];
        Arrays.fill(bits , '0');
        int numOfStorage = r.nextInt(storageG.length) + 1;
        c.storageG=new int[numOfStorage];
        hm.clear();
        for (int i = 0; i < numOfStorage; i++) {
            choosedNumber=r.nextInt(storageG.length);
            String choosed = String.valueOf(storageG[choosedNumber]);
            if (hm.containsKey(choosed)) {
                i--;
                continue;
            } else {
                bits[choosedNumber]='1';
                c.storageG[c.counters[3]++]=Integer.parseInt(choosed);
                hm.put(choosed , 1);
                if (c.p.containsKey("storageG")) {
                    c.p.put("storageG", ((String) c.p.get("storageG")).concat("," + choosed));

                } else {
                    c.p.put("storageG", choosed);

                }
            }
        }
        index+=new String(bits);

        //memoryG
        bits=new char[memoryG.length];
        Arrays.fill(bits , '0');
        int numOfmem = r.nextInt(memoryG.length) + 1;
        c.memoryG=new float[numOfmem];
        hm.clear();
        for (int i = 0; i < numOfmem; i++) {
            choosedNumber=r.nextInt(memoryG.length);
            String choosed = String.valueOf(memoryG[choosedNumber]);
            if (hm.containsKey(choosed)) {
                i--;
                continue;
            } else {
                bits[choosedNumber]='1';
                c.memoryG[c.counters[4]++]=Float.parseFloat(choosed);
                hm.put(choosed , 1);
                if (c.p.containsKey("memoryG")) {
                    c.p.put("memoryG", ((String) c.p.get("memoryG")).concat("," + choosed));

                } else {
                    c.p.put("memoryG", choosed);

                }
            }
        }
        index+=new String(bits);

        //cpuCore
        bits=new char[cpuCore.length];
        Arrays.fill(bits , '0');
        int cpunum = r.nextInt(cpuCore.length) + 1;
        c.cpuCore=new int[cpunum];
        hm.clear();
        for (int i = 0; i < cpunum; i++) {
            choosedNumber=r.nextInt(cpuCore.length);
            String choosed = String.valueOf(cpuCore[choosedNumber]);
            if (hm.containsKey(choosed)) {
                i--;
                continue;
            } else {
                bits[choosedNumber]='1';
                c.cpuCore[c.counters[5]++]=Integer.parseInt(choosed);
                hm.put(choosed , 1);
                if (c.p.containsKey("cpuCore")) {
                    c.p.put("cpuCore", ((String) c.p.get("cpuCore")).concat("," + choosed));

                } else {
                    c.p.put("cpuCore", choosed);

                }
            }
        }
        index+=new String(bits);

        //OS
        bits=new char[OS.length];
        Arrays.fill(bits , '0');
        int osnum = r.nextInt(OS.length) + 1;
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
        bits=new char[Hertz.length];
        Arrays.fill(bits , '0');
        int numHertz = r.nextInt(Hertz.length) + 1;
        c.Hertz=new double[numHertz];
        hm.clear();
        for (int i = 0; i < numHertz; i++) {
            choosedNumber=r.nextInt(Hertz.length);
            String choosed = String.valueOf(Hertz[choosedNumber]);
            if (hm.containsKey(choosed)) {
                i--;
                continue;
            } else {
                bits[choosedNumber]='1';
                c.Hertz[c.counters[7]++]=Double.parseDouble(choosed);

                hm.put(choosed , 1);
                if (c.p.containsKey("Hertz")) {
                    c.p.put("Hertz", ((String) c.p.get("Hertz")).concat("," + choosed));

                } else {
                    c.p.put("Hertz", choosed);

                }
            }
        }
        index+=new String(bits);

        //prices
        c.calculatePrice();
        c.federationPriceING=federationPriceING[r.nextInt(federationPriceING.length)];
        c.federationPriceOutG=federationPriceOutG[r.nextInt(federationPriceOutG.length)];
        c.InternetPriceING = InternetPriceING[r.nextInt(InternetPriceING.length)];
        c.InternetPriceOutG = InternetPriceOutG[r.nextInt(InternetPriceOutG.length)];
        //naming
        c.name= UUID.randomUUID().toString();
        c.index = new BigInteger(index , 2);
        return c;


    }

}
