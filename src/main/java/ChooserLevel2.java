import java.io.IOException;
import java.util.*;

/**
 * Created by sinaaskarnejad on 3/20/16.
 */
public class ChooserLevel2 {
    final double ALPHA = 1.0;
    final double BETA = 0.0;
    private static double gig = 1000000;


    HashMap<String , ArrayList<Cloud>> providers=new HashMap<String, ArrayList<Cloud>>();
    String templocation;
    double tempcom;
    double temprent;
    int tempPayType;
    public void resetProviders() {
        providers.clear();
        templocation="";
        tempcom=0.0;
        temprent=0.0;
        tempPayType=0;


    }

    public void mapRequestToCloud(Request r , ExternalDispatcher ex , boolean problem) {
        long startTime=System.currentTimeMillis();
        Level2Result lv = new Level2Result();
        if (problem){
           lv.problem=true;
            try {
                test.bw.write("ERROR: ClOUDS CHOOSED FOR REQUEST "+r.requestId+" IN "+(System.currentTimeMillis()-startTime)+" Milis\r\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            ex.getResult(lv);
            return;
        }else {
            // Randomchooser rd=new Randomchooser();
            // lv.random=rd.chooseRandom(r,providers);

            // MCDMImplementation md=new MCDMImplementation();
            // lv.mcdm=md.chooseCloudsBasedOnMCDM(r,providers);
            // UtiliyImplementation ut=new UtiliyImplementation();
            // lv.utility=ut.ComputeRequest(r,providers);
            // MCDMImplementation3 md3=new MCDMImplementation3();
            // lv.mcdm2 = md3.chooseCloudsBasedOnMCDM(r , providers);
            // AdaptiveImplemtation adaptive=new AdaptiveImplemtation();
            // lv.adaptive=adaptive.chooseAdaptive(r,providers);

            lv.totalcost = 0;
            lv.problem = false;
            String location = "";
            double subcost = 0;
            if (r.edgeVMs.size()==0){
                if (r.dependentVMs.size()!=0) {
                    String vmn = "";
                    int min = Integer.MAX_VALUE;
                    for (int i = 0; i < r.dependentVMs.size(); i++) {
                        String nametemp = r.dependentVMs.get(i);
                        int locc = r.properties.get(nametemp).location.size();
                        if (locc < min) {
                            min = locc;
                            vmn = nametemp;
                            if (min == 1) {
                                break;
                            }
                        }
                    }
                    r.edgeVMs.add(vmn);
                    r.middleVMs.remove(vmn);
                }else {
                    String vmn = "";
                    int min = Integer.MAX_VALUE;
                    for (int i = 0; i < r.independentVMs.size(); i++) {
                        String nametemp = r.independentVMs.get(i);
                        int locc = r.properties.get(nametemp).location.size();
                        if (locc < min) {
                            min = locc;
                            vmn = nametemp;
                            if (min == 1) {
                                break;
                            }
                        }
                    }
                    r.edgeVMs.add(vmn);
                    r.middleVMs.remove(vmn);
                }
            }
            double portion = Math.ceil(r.useTime/RandomCloudInformation2.typeOfHour.get(r.typeOfPayment));
            HashMap<String , Position>  hhi=new HashMap<>();
            long startt=System.currentTimeMillis();
            // CSSImplementation css=new CSSImplementation();
            // ArrayList<String> csss=new ArrayList<>(r.services);
            // ArrayList<String> csse=new ArrayList<>(r.edgeVMs);
            // ArrayList<String> cssm=new ArrayList<>(r.middleVMs);


            // lv.css=css.choosebasedonCSS(r,csss,csse,cssm,providers);
            // try {
            //     test.bw.write("css for request "+ r.requestId+" finished in "+(System.currentTimeMillis()-startt));
            // } catch (IOException e) {
            //     e.printStackTrace();
            // }
            // ChooseAlwaysClouds chooseAlwaysClouds=new ChooseAlwaysClouds();
            // lv.regular=chooseAlwaysClouds.ComputeRequest(r,providers);
            while (r.edgeVMs.size() != 0) {
                String temp = r.edgeVMs.remove(0);
                double cost = Integer.MAX_VALUE;
                Cloud choosed = null;

                double traffics=0;
                HashMap<String , Integer> hashMap=r.properties.get(temp).traffic;
                for (String traffskey:hashMap.keySet()){
                 //   if (r.services.contains(traffskey))
                    traffics+=hashMap.get(traffskey);
                }
                for (Cloud c : providers.get(temp)) {
                    double tt = formula(temp, c, r , portion , traffics);
                    if (tt != -1 && cost > tt) {
                        cost = tt;
                        choosed = c;
                        location = templocation;
                       // subcost = tempcom + temprent;
                        subcost = temprent;
                    }
                }
                if (choosed != null) {
                    r.services.add(temp);
                    Position p = new Position(choosed, location, subcost);
                    hhi.put(temp , p);
                    lv.mapper.put(temp, p);
                    //    lv.costMapper.put(temp , cost);
                    lv.totalcost += subcost;
                    VMProperties vp=r.properties.get(temp);
                    Iterator<String> it=r.middleVMs.iterator();
                    while (it.hasNext()){
                        String next=it.next();
                        if(vp.Services.contains(next)){
                            it.remove();
                            r.edgeVMs.add(next);
                        }
                    }
                    //for all vms which is connected to this temp remove them from r.middle and add them to r.edge
                } else {
                    try {
                        test.bw.write("ERROR: no cloud can match to this VM request!!\r\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    lv.problem = true;
                    try {
                        test.bw.write("ERROR: ClOUDS CHOOSED FOR REQUEST "+r.requestId+" IN "+(System.currentTimeMillis()-startTime)+" Milis\r\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ex.getResult(lv);
                    return;
                }
            }
            if (r.middleVMs.size() != 0) {
                try {
                    test.bw.write("ERROR: ALL VMs are not assigned to Cloud\r\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                lv.problem = true;
                try {
                    test.bw.write("ERROR: ClOUDS CHOOSED FOR REQUEST "+r.requestId+" IN "+(System.currentTimeMillis()-startTime)+" Milis\r\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ex.getResult(lv);
                return;
            }
     //       System.out.println("Request " + r.requestId + " Cost in cloud Will Be --->  " + lv.totalcost + " $");

//            if (r.requestId==1){
//            lv.sadness=new ChosseAllWrong().computecost(r,hhi);
//            }
            lv.r=r;
            try {
                test.bw.write("ClOUDS CHOOSED FOR REQUEST "+r.requestId+" IN "+(System.currentTimeMillis()-startTime)+" Milis\r\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            ex.getResult(lv);
            return;
        }

    }

    public void addProviders(String key , ArrayList<Cloud> cl) {
        providers.put(key , cl);
    }

    public double formula(String vm, Cloud cloud , Request r ,double portion , double traffs) {
        int[] counters = r.counters;
        VMProperties vp=r.properties.get(vm);
        int responseTime=0;
        String res=vp.QOS.getProperty("responseTime");
        if (res!=null){
            responseTime=Integer.parseInt(res);
        }
 //       int counter = 0;
//        if (r.typeOfPayment != null) {
//            for (int i = 0; i < cloud.typeOfPayment.length; i++) {
//                if (cloud.typeOfPayment[i].equals(vm.paymentType)) {
//                    counters[0] = i;
//                    counter++;
//                    break;
//                }
//            }
//        }
//        for (int i = 0; i < cloud.cloudType.length; i++) {
//            if (cloud.cloudType[i].equals(vm.cloudType)) {
//                counters[1] = i;
//                counter++;
//                break;
//            }
//        }
//        for (int i = 0; i < cloud.location.length; i++) {
//            if (cloud.location[i].equals(vm.location)) {
//                counters[2] = i;
//                counter++;
//                break;
//            }
//        }
//        for (int i = 0; i < cloud.storageG.length; i++) {
//            if (cloud.storageG[i] > vm.storageG) {
//                counters[3] = i;
//                counter++;
//                break;
//            }
//        }
//        for (int i = 0; i < cloud.memoryG.length; i++) {
//            if (cloud.memoryG[i] > vm.memoryG) {
//                counters[4] = i;
//                counter++;
//                break;
//            }
//        }
//        for (int i = 0; i < cloud.cpuCore.length; i++) {
//            if (cloud.cpuCore[i] > vm.cpuCore) {
//                counters[5] = i;
//                counter++;
//                break;
//            }
//        }
//        for (int i = 0; i < cloud.OS.length; i++) {
//            if (cloud.OS[i].equals(vm.OS)) {
//                counters[6] = i;
//                counter++;
//                break;
//            }
//        }
//        for (int i = 0; i < cloud.Hertz.length; i++) {
//            if (cloud.Hertz[i] > vm.Hertz) {
//                counters[7] = i;
//                counter++;
//                break;
//            }
//        }
//        if (vm.paymentType != null && counter == 8) {
//            double rentCost = cloud.priceMap[counters[0]][counters[1]][counters[2]][counters[3]][counters[4]][counters[5]][counters[6]][counters[7]];
//            double communicationprice = cloud.InternetPriceOutG;
//            if (cloud.dynamicResponsetime.get(vm.location) > vm.responseTime) {
//                System.out.println("Response Time of this cloud is not Good for NOW");
//                return -1;
//            }
//            //if cloud have Space to handle this VM ******For private clouds****implementb later
//            double tempcost = ALPHA * (rentCost + communicationprice) + BETA * (cloud.dynamicResponsetime.get(vm.location));
//            return tempcost;
//        } else if (vm.paymentType == null && counter == 7) {
//            double rentCost = Integer.MAX_VALUE;
//            for (int i = 0; i < cloud.typeOfPayment.length; i++) {
//                double tt = cloud.priceMap[i][counters[1]][counters[2]][counters[3]][counters[4]][counters[5]][counters[6]][counters[7]];
//                if (rentCost > tt) {
//                    rentCost = tt;
//                }
//            }
//            double communicationprice = cloud.InternetPriceOutG;
//            if (cloud.dynamicResponsetime.get(vm.location) > vm.responseTime) {
//                System.err.println("Response Time of this cloud is not Good");
//                return -1;
//            }
//            //if cloud have Space to handle this VM ******For private clouds****implementb later
//            double tempcost = ALPHA * (rentCost + communicationprice) + BETA * (cloud.dynamicResponsetime.get(vm.location));
//            return tempcost;
//
//        } else {
//            System.err.println("ERROR in cloud assignment---cloud doesnt match to VM");
//            return -1;
//        }
        double rentCost = Integer.MAX_VALUE;
        for (int i = 0; i < cloud.location.length; i++){
            if (vp.location.isEmpty()){
                double tt = cloud.priceMap[counters[0]][counters[1]][i][counters[3]][counters[4]][counters[5]][counters[6]][counters[7]];
                //  if (rentCost > tt) {
                //maybe in dataset it be location base
                double communicationprice = cloud.InternetPriceOutG;
                double rrttemp=cloud.dynamicResponsetime.get(cloud.location[i]).get();

                if (responseTime!=0 && rrttemp > responseTime) {
                    try {
                        test.bw.write("ERROR: Response Time of cloud "+cloud.name+" in location: "+cloud.location[i]+ "is not Good\r\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //    return -1;
                }else {
                    double tempcost =  ((tt*portion)+(communicationprice*(traffs/8/gig)*r.useTime*30))  + (BETA * rrttemp);
                //    double tempcost = ALPHA*(tt*portion+communicationprice);

                    if (tempcost<rentCost){
                        rentCost=tempcost;
                        templocation=cloud.location[i];
                        temprent=tt;
                        tempcom=communicationprice;
                    }
                }
                //    }
            }
            else if(vp.location.contains(cloud.location[i])){
                double tt = cloud.priceMap[counters[0]][counters[1]][i][counters[3]][counters[4]][counters[5]][counters[6]][counters[7]];
              //  if (rentCost > tt) {
                    //maybe in dataset it be location base
                    double communicationprice = cloud.InternetPriceOutG;
                double rrttemp=cloud.dynamicResponsetime.get(cloud.location[i]).get();
                    if (responseTime!=0 && rrttemp > responseTime) {
                        try {
                            test.bw.write("ERROR: Response Time of cloud "+cloud.name+" in location: "+cloud.location[i]+ "is not Good\r\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //    return -1;
                    }else {
                        double tempcost = ((tt*portion)+(communicationprice*(traffs/8/gig)*r.useTime*30)) + (BETA * rrttemp);
                    //    double tempcost = ALPHA*(tt*portion+communicationprice);

                        if (tempcost<rentCost){
                            rentCost=tempcost;
                            templocation=cloud.location[i];
                            temprent=tt;
                            tempcom=communicationprice;
                        }
                    }
            //    }
            }
        }
        if (rentCost==Integer.MAX_VALUE){
            return -1;
        }
        return rentCost;

    }
}
