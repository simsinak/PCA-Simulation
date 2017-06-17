import java.math.BigInteger;
import java.util.*;
import java.util.function.Supplier;

/**
 * Created by sinaaskarnejad on 5/20/16.
 */
public class Cloud {
    final int K=3;
    String name;
    Properties p=new Properties();
    String[] typeOfPayment;
    String[] cloudType;
    String[] location ;
    int[] storageG;
    float[] memoryG;
    int storageMax;
    double memoryMax;
    int coreMax;
    double hertzMax;
    double availability;
    double throughput;
    int[] cpuCore ;
    String[] OS;
    double[] Hertz;
    double federationPriceING ;
    double federationPriceOutG;
    double InternetPriceING ;
    double InternetPriceOutG ;
    int[] counters=new int[8];
    double[][][][][][][][] priceMap = null;
    BigInteger index;
    // in MS metric

    Map<String , Supplier<Double>> dynamicResponsetime=new HashMap<String, Supplier<Double>>();
    //TODO do it dynamic its static now
    public void fillDynamicResponseTime(){
        final Random random=new Random();
        for (int i = 0; i < location.length; i++) {
            ArrayList<Diverse> tempp;
            if (MapRepository.divs.containsKey(location[i])){
                tempp=MapRepository.divs.get(location[i]);
            }else {
                tempp=MapRepository.divs.get("others");
            }
            final Diverse diverse=tempp.get(random.nextInt(tempp.size()));
            Supplier<Double> supplier=()-> Math.random()*(diverse.max-diverse.min)+diverse.min;
            dynamicResponsetime.put(location[i],supplier);
        }
    }
    public void calculatePrice(){
        priceMap = new double[typeOfPayment.length ][cloudType.length][location.length][storageG.length][memoryG.length][cpuCore.length][OS.length][Hertz.length];
        Random r=new Random();
        for (int i = 0; i < typeOfPayment.length; i++) {
            for (int j = 0; j < cloudType.length; j++) {
                for (int k = 0; k < location.length; k++) {
                    for (int l = 0; l < storageG.length; l++) {
                        for (int m = 0; m < memoryG.length; m++) {
                            for (int n = 0; n < cpuCore.length; n++) {
                                for (int o = 0; o < OS.length; o++) {
                                    for (int q = 0; q < Hertz.length; q++) {
                                        priceMap[i][j][k][l][m][n][o][q]=r.nextDouble()*K;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    public void calculatePrice2(double rank){
        int top=RandomCloudInformation2.typeOfPayment.length;
        int ct=RandomCloudInformation2.cloudType.length;
        int lo=location.length;
        int os=RandomCloudInformation2.OS.length;
        priceMap = new double[top][ct][lo][4][4][4][os][4];
        Random r=new Random();

        for (int i = 0; i < top; i++) {
            for (int j = 0; j < ct; j++) {
                for (int k = 0; k < lo; k++) {
                    for (int l = 0; l < 4; l++) {
                        for (int m = 0; m < 4; m++) {
                            for (int n = 0; n < 4; n++) {
                                for (int o = 0; o < os; o++) {
                                    for (int q = 0; q < 4; q++) {
                                        if(rank<=2){
                                            if (m<=0 && n<=0){
                                                if (!RandomCloudInformation2.typeOfPayment[i].equals("Use") && !RandomCloudInformation2.typeOfPayment[i].equals("Hour")){
                                                    priceMap[i][j][k][l][m][n][o][q]=3.5+r.nextDouble()*76.06;

                                                }
                                                else if (RandomCloudInformation2.OS[o].equals("WindowsSQLS")){
                                                    priceMap[i][j][k][l][m][n][o][q]=0.519+r.nextDouble()*0.679;
                                                }else if(RandomCloudInformation2.OS[o].equals("WindowsSQLE")){
                                                    priceMap[i][j][k][l][m][n][o][q]=r.nextDouble()*K;

                                                }else {
                                                    priceMap[i][j][k][l][m][n][o][q]=0.0065+r.nextDouble()*0.3095;
                                                }

                                            }else if(m<=1 && n<=1){
                                                if (!RandomCloudInformation2.typeOfPayment[i].equals("Use") && !RandomCloudInformation2.typeOfPayment[i].equals("Hour")){
                                                    priceMap[i][j][k][l][m][n][o][q]=129.72+r.nextDouble()*167.68;

                                                }
                                                else if (RandomCloudInformation2.OS[o].equals("WindowsSQLS")){
                                                    priceMap[i][j][k][l][m][n][o][q]=1.31+r.nextDouble()*2.827;
                                                }else if(RandomCloudInformation2.OS[o].equals("WindowsSQLE")){
                                                    priceMap[i][j][k][l][m][n][o][q]=5.791+r.nextDouble()*0.003;


                                                }else {
                                                    priceMap[i][j][k][l][m][n][o][q]=0.339+r.nextDouble()*0.345;

                                                }


                                            }else {
                                                if (!RandomCloudInformation2.typeOfPayment[i].equals("Use") && !RandomCloudInformation2.typeOfPayment[i].equals("Hour")){
                                                    priceMap[i][j][k][l][m][n][o][q]=490.34+r.nextDouble()*730.51;

                                                }
                                                else if (RandomCloudInformation2.OS[o].equals("WindowsSQLS")){
                                                    priceMap[i][j][k][l][m][n][o][q]=4.994+r.nextDouble()*9.244;

                                                }else if(RandomCloudInformation2.OS[o].equals("WindowsSQLE")){
                                                    priceMap[i][j][k][l][m][n][o][q]=11.266+r.nextDouble()*7.745;

                                                }else {
                                                    priceMap[i][j][k][l][m][n][o][q]=1.100+r.nextDouble()*2.699;

                                                }


                                            }

                                        }else if(rank<=4){
                                           if (m<=0 && n<=0){
                                               if (!RandomCloudInformation2.typeOfPayment[i].equals("Use") && !RandomCloudInformation2.typeOfPayment[i].equals("Hour")){
                                                   priceMap[i][j][k][l][m][n][o][q]=3.29+r.nextDouble()*66.06;

                                               }
                                               else if (RandomCloudInformation2.OS[o].equals("WindowsSQLS")){
                                                   priceMap[i][j][k][l][m][n][o][q]=0.419+r.nextDouble()*0.579;
                                               }else if(RandomCloudInformation2.OS[o].equals("WindowsSQLE")){
                                                   priceMap[i][j][k][l][m][n][o][q]=r.nextDouble()*K;

                                               }else {
                                                   priceMap[i][j][k][l][m][n][o][q]=0.0065+r.nextDouble()*0.2095;
                                               }

                                           }else if(m<=1 && n<=1){
                                               if (!RandomCloudInformation2.typeOfPayment[i].equals("Use") && !RandomCloudInformation2.typeOfPayment[i].equals("Hour")){
                                                   priceMap[i][j][k][l][m][n][o][q]=119.72+r.nextDouble()*157.68;

                                               }
                                               else if (RandomCloudInformation2.OS[o].equals("WindowsSQLS")){
                                                   priceMap[i][j][k][l][m][n][o][q]=1.11+r.nextDouble()*2.427;
                                               }else if(RandomCloudInformation2.OS[o].equals("WindowsSQLE")){
                                                   priceMap[i][j][k][l][m][n][o][q]=5.491+r.nextDouble()*0.003;


                                               }else {
                                                   priceMap[i][j][k][l][m][n][o][q]=0.239+r.nextDouble()*0.545;

                                               }


                                           }else {
                                               if (!RandomCloudInformation2.typeOfPayment[i].equals("Use") && !RandomCloudInformation2.typeOfPayment[i].equals("Hour")){
                                                   priceMap[i][j][k][l][m][n][o][q]=480.34+r.nextDouble()*720.51;

                                               }
                                               else if (RandomCloudInformation2.OS[o].equals("WindowsSQLS")){
                                                   priceMap[i][j][k][l][m][n][o][q]=4.594+r.nextDouble()*8.244;

                                               }else if(RandomCloudInformation2.OS[o].equals("WindowsSQLE")){
                                                   priceMap[i][j][k][l][m][n][o][q]=10.766+r.nextDouble()*7.245;

                                               }else {
                                                   priceMap[i][j][k][l][m][n][o][q]=0.954+r.nextDouble()*2.599;

                                               }


                                           }
                                        }else{
                                            if (m<=0 && n<=0){
                                                if (!RandomCloudInformation2.typeOfPayment[i].equals("Use") && !RandomCloudInformation2.typeOfPayment[i].equals("Hour")){
                                                    priceMap[i][j][k][l][m][n][o][q]=3.29+r.nextDouble()*50.06;

                                                }
                                                else if (RandomCloudInformation2.OS[o].equals("WindowsSQLS")){
                                                    priceMap[i][j][k][l][m][n][o][q]=0.319+r.nextDouble()*0.479;
                                                }else if(RandomCloudInformation2.OS[o].equals("WindowsSQLE")){
                                                    priceMap[i][j][k][l][m][n][o][q]=r.nextDouble()*K;

                                                }else {
                                                    priceMap[i][j][k][l][m][n][o][q]=0.0065+r.nextDouble()*0.1095;
                                                }

                                            }else if(m<=1 && n<=1){
                                                if (!RandomCloudInformation2.typeOfPayment[i].equals("Use") && !RandomCloudInformation2.typeOfPayment[i].equals("Hour")){
                                                    priceMap[i][j][k][l][m][n][o][q]=103.72+r.nextDouble()*140.68;

                                                }
                                                else if (RandomCloudInformation2.OS[o].equals("WindowsSQLS")){
                                                    priceMap[i][j][k][l][m][n][o][q]=1+r.nextDouble()*1.427;
                                                }else if(RandomCloudInformation2.OS[o].equals("WindowsSQLE")){
                                                    priceMap[i][j][k][l][m][n][o][q]=4.491+r.nextDouble()*0.003;


                                                }else {
                                                    priceMap[i][j][k][l][m][n][o][q]=0.139+r.nextDouble()*0.445;

                                                }


                                            }else {
                                                if (!RandomCloudInformation2.typeOfPayment[i].equals("Use") && !RandomCloudInformation2.typeOfPayment[i].equals("Hour")){
                                                    priceMap[i][j][k][l][m][n][o][q]=466.34+r.nextDouble()*704.51;

                                                }
                                                else if (RandomCloudInformation2.OS[o].equals("WindowsSQLS")){
                                                    priceMap[i][j][k][l][m][n][o][q]=3.594+r.nextDouble()*7.244;

                                                }else if(RandomCloudInformation2.OS[o].equals("WindowsSQLE")){
                                                    priceMap[i][j][k][l][m][n][o][q]=8.766+r.nextDouble()*6.245;

                                                }else {
                                                    priceMap[i][j][k][l][m][n][o][q]=0.854+r.nextDouble()*1.599;

                                                }


                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    public void calculatePrice3(){
        int top=RandomCloudInformation2.typeOfPayment.length;
        int ct=RandomCloudInformation2.cloudType.length;
        int lo=location.length;
        int os=RandomCloudInformation2.OS.length;
        priceMap = new double[top][ct][lo][4][4][4][os][4];
        Random r=new Random();

        for (int i = 0; i < top; i++) {
            for (int j = 0; j < ct; j++) {
                for (int k = 0; k < lo; k++) {
                    for (int l = 0; l < 4; l++) {
                        for (int m = 0; m < 4; m++) {
                            for (int n = 0; n < 4; n++) {
                                for (int o = 0; o < os; o++) {
                                    for (int q = 0; q < 4; q++) {
                                        priceMap[i][j][k][l][m][n][o][q]=r.nextDouble()*K;


                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
