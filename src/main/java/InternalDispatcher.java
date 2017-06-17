import balancer.CreateGraph;
import balancer.ReturnResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by sinaaskarnejad on 3/20/16.
 */
public class InternalDispatcher {
    InternalChooser ic;
    Dispatcher d;
    int totalVMSout = 0;
    int totalVMSin = 0;
    double totalCostBefore = 0;
    double totalcostAfter = 0;
    static boolean status=false;
    double totalCostBeforeProxy = 0;
    double totalcostAfterProxy = 0;
    double CostInternalProxy = 0;
    double CostInternalProxy2 = 0;
    double CostExternalProxy =0;
    double costOptimal =0.0;
    double dependentvmCostProxy= 0.0;
    double ExtraProxy=0;
    double ccu=0;
    long elappsed=0;
    double portion =1;
    HashMap<String , HashSet<String>> mapped=new HashMap<>();
    //   Set<Integer> internalVms;
    //   Set<Integer> externalVms;
    //   Topology etopo;
    //   int countbranch = 0;
    //  int numberOfbranch = 0;
    //   HashMap<Integer, Integer> vmplaces ;
    boolean change = false;
    private static InternalDispatcher id;
    private static double gig = 1000000;
    public double dependentvmCost = 0.0;

    private InternalDispatcher(Dispatcher d) {
        this.d = d;
        ic = new InternalChooser(this);
    }

    private InternalDispatcher() {

    }

    public static InternalDispatcher getInstance(Dispatcher d) {
        if (id == null) {
            id = new InternalDispatcher(d);
        }
        return id;
    }

    public void processRequest(Level2Result l2r) {
        mapped.clear();
        InternalDispatcher.status=false;
        long startTime = System.currentTimeMillis();
        //todo if one of this independent vm cant be fit in internal(must 100% be in cloud) delete it from vm list and add them to
        //dependent list
        InternalChooser.checkPossibility2(l2r.r);
        dependentvmCost = 0.0;
        ArrayList<String> temp = l2r.r.dependentVMs;
        CostInternalProxy = 0;
        CostExternalProxy =0;
        CostInternalProxy2=0;
        ccu=0;
        dependentvmCostProxy = 0;
        ExtraProxy=0;
        double dependentTrafficCost=0;
        Position place=null;
        for (int i = 0; i < temp.size(); i++) {
            Position p = l2r.mapper.get(temp.get(i));
            dependentvmCost += p.cost;
            d.vmplaces.put(temp.get(i), p);
            int trafficPerSec = 0;
            double vmcost = 0;
            int trafficPerSec2 = 0;
            // double vmcost2=0;
            VMProperties vp = l2r.r.properties.get(temp.get(i));
            Set<String> services = vp.Services;
            Position place2 = l2r.mapper.get(temp.get(i));
            //   System.out.println(d.vmplaces);
            //   System.out.println("now\n"+l2r.mapper);
            for (String service : services) {
                place=null;
                int traf = vp.traffic.get(service);
                int index = temp.indexOf(service);
                int index2 = l2r.r.independentVMs.indexOf(service);
                //not dependent
                if (index == -1 && index2==-1) {
                    place = d.vmplaces.get(service);
                    //not service
//                    if (place == null) {
//                        place = l2r.mapper.get(service);
//
//                    }
                    //out
                    if (!place.location.equals("in")) {
                        if (place.cloud!=place2.cloud || !place.location.equals(place2.location)){
                        trafficPerSec += traf;
                        vmcost += (((traf / 8) / gig) * place.cloud.InternetPriceOutG);}
                        //in
                    }
                else {
                        trafficPerSec2 += traf;
                        //  vmcost2+=(traf*place2.cloud.InternetPriceOutG);
                    }
                    //independent
                } else if (index!=-1){
                    place = l2r.mapper.get(service);
                    if (!mapped.containsKey(service) ||  !mapped.get(service).contains(temp.get(i))){
                 //   if (index > i) {
                        if (place.cloud!=place2.cloud || !place.location.equals(place2.location)){
                        dependentTrafficCost += (((traf / 8) / gig) * (place2.cloud.InternetPriceING+place.cloud.InternetPriceING));
                    if (!mapped.containsKey(temp.get(i))) {
                        mapped.put(temp.get(i) , new HashSet<>());
                    }
                        mapped.get(temp.get(i)).add(service);


                    }
                    }
                }
            }
            double ttt=((((trafficPerSec / 8) / gig) * (place2.cloud.InternetPriceING)) + vmcost);
            ttt += (((trafficPerSec2 / 8) / gig) * (place2.cloud.InternetPriceING+((ISP) d.etopo.nodes.get(0)).getDownloadCost()));
            ccu+=(ttt);
          //  dependentTrafficCost*=l2r.r.useTime*30;
            ccu+=dependentTrafficCost;
            dependentTrafficCost=0;


        }
        ccu*=l2r.r.useTime*30;

        portion = Math.ceil(l2r.r.useTime/RandomCloudInformation2.typeOfHour.get(l2r.r.typeOfPayment));
        dependentvmCostProxy=portion*dependentvmCost;
        try {
            test.bw.write("SOME ClOUD MAPS PURIFIED AND ADDED TO PROFILE FOR REQUEST " + l2r.r.requestId + " IN " + (System.currentTimeMillis() - startTime) + " Milis\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //instead of this we compute it in preapreForProcess
        // l2r.r.independentMatrix(l2r.r.independentVMs);
        prepareForProcess(l2r);

    }

    public void prepareForProcess(Level2Result l2r) {
        long startTime = System.currentTimeMillis();
        int size = l2r.r.independentVMs.size();
        int[] coste = new int[size];
        int[] costi = new int[size];
        int[][] independentConnections = new int[size][size];
        int[][] traffic = new int[size][size];

        double[] costeProxy = new double[size];
        double[] costiProxy = new double[size];
        double[][] trafficProxy=new double[size][size];

        Position place = null;
        ArrayList<String> temp = l2r.r.independentVMs;
        double extraCost = 0;
        //fill coste and cosi and mid
        for (int i = 0; i < size; i++) {
            int trafficPerSec = 0;
            double vmcost = 0;
            int trafficPerSec2 = 0;
            // double vmcost2=0;
            VMProperties vp = l2r.r.properties.get(temp.get(i));
            Set<String> services = vp.Services;
            Position place2 = l2r.mapper.get(temp.get(i));
            //   System.out.println(d.vmplaces);
            //   System.out.println("now\n"+l2r.mapper);
            for (String service : services) {
                place=null;
                int traf = vp.traffic.get(service);
                int index = temp.indexOf(service);
                //not independent
                if (index == -1) {
                    place = d.vmplaces.get(service);
                    //not service
                    if (place == null) {
                        place = l2r.mapper.get(service);

                    }
                    //out
                    if (!place.location.equals("in")) {
                        if (place.cloud!=place2.cloud || !place.location.equals(place2.location)){
                        trafficPerSec += traf;
                        vmcost += (((traf / 8) / gig) * place.cloud.InternetPriceOutG);}
                        //in
                    }
                    else {
                        trafficPerSec2 += traf;
                        //  vmcost2+=(traf*place2.cloud.InternetPriceOutG);
                    }
                    //independent
                } else {
                    place = l2r.mapper.get(service);
                    if (index > i) {
                        independentConnections[i][index] = 1;
                        if (place.cloud!=place2.cloud || !place.location.equals(place2.location)){
                        extraCost += (((traf / 8) / gig) * (place2.cloud.InternetPriceING+place.cloud.InternetPriceING));}
                    }

                    boolean canfit = false;
                    for (Node2 n : d.etopo.nodes) {
                        if (((ISP) n).remainingTraffic > traf) {
                            //todo how monitor traffic
                            double ttt=(((traf / 8) / gig) * (((ISP) n).getDownloadCost() + place2.cloud.InternetPriceING)) ;
                            trafficProxy[index][i]=ttt*l2r.r.useTime*30;
                            traffic[index][i] = (int) (ttt * 1000);

                          //  traffic[index][i] = (int) (ttt* 1000);
                            canfit = true;
                            break;
                        }
                    }
                    if (!canfit) {
                        try {
                            test.bw.write("ERROR: no traffic remained in external connection please emty some you didnt do it yet!\r\n");
                        test.bw.write("PUT REQUEST IN CLOUD FOR NOW TILL I IMPLEMENT NEAT IDEA!\r\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return;
                    }

                }
            }
            boolean canfit = false;
            boolean canfit2 = false;
            for (Node2 n : d.etopo.nodes) {
                if (((ISP) n).remainingTraffic > trafficPerSec && !canfit) {
                    //todo how monitor traffic
                    //     ((ISP)n).remainingTraffic-=trafficPerSec;
                    //// TODO: 7/21/16 does it need vmcost?
                    double ttt=((((trafficPerSec / 8) / gig) * ((ISP) n).getDownloadCost()) + vmcost);
                    CostExternalProxy+=ttt;
                    coste[i] = (int) (ttt * 1000);
                    costeProxy[i]=ttt*l2r.r.useTime*30;
                   // coste[i] = (int) (ttt * 1000);
                    //    costi[i]=(trafficPerSec2*((ISP) n).getDownloadCost())+vmcost2+place.cost;
                    canfit = true;
                }
                if (((ISP) n).remainingTraffic > trafficPerSec2 && !canfit2) {
                    //how monitor traffic
                    //     ((ISP)n).remainingTraffic-=trafficPerSec;
                    //coste[i]=(trafficPerSec*((ISP) n).getDownloadCost())+vmcost;
                    double ttt=(((trafficPerSec2 / 8) / gig) * (((ISP) n).getDownloadCost() + place2.cloud.InternetPriceING)) + (((trafficPerSec / 8) / gig) * (place2.cloud.InternetPriceING))+vmcost;
                    CostInternalProxy+=ttt;
                    CostInternalProxy2+=place2.cost;
                    costi[i] = (int) (ttt+ place2.cost) * 1000;
                    costiProxy[i] = (ttt*l2r.r.useTime*30)+(place2.cost*portion);
                 //   costi[i] = (int) (((((trafficPerSec2 / 8) / gig) * (((ISP) n).getDownloadCost() + place2.cloud.InternetPriceING)) + (((trafficPerSec / 8) / gig) * (place2.cloud.InternetPriceING)) + place2.cost) * 1000);
                    canfit2 = true;
                    break;
                }
                if (canfit && canfit2) break;
            }
            if (!canfit || !canfit2) {
                try {
                    test.bw.write("ERROR: no traffic remained in external connection please emty some you didnt do it yet!\r\n");
                test.bw.write("PUT REQUEST IN CLOUD FOR NOW TILL I IMPLEMENT NEAT IDEA!\r\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }

        }
        //real values in time
        CostExternalProxy*=(l2r.r.useTime*30);
        CostInternalProxy*=(l2r.r.useTime*30);
        CostInternalProxy2*=portion;
        CostInternalProxy+=CostInternalProxy2;
        ExtraProxy = extraCost*l2r.r.useTime*30;
        try {
            test.bw.write("PREPARING FOR LAST STEP...COMPUTING MATRIXES FOR REQUEST " + l2r.r.requestId + " IN " + (System.currentTimeMillis() - startTime) + " Milis\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        execute(l2r, coste, costi, traffic, independentConnections, (int) (extraCost * 1000),  trafficProxy , costeProxy , costiProxy);
    }

    public void execute(Level2Result l2r, int[] coste, int[] costi, int[][] midcost, int[][] interactions, int extra , double[][] trafficProxy , double realCoste[] , double[] realCosti) {
//        System.out.println(Arrays.toString(coste));
//        System.out.println(Arrays.toString(costi));
//        for (int i = 0; i < midcost.length; i++) {
//            System.out.println(Arrays.toString(midcost[i]));
//
//        }
        int[][] trafficproxy2=new int[trafficProxy.length][trafficProxy.length];
        int[] realcoste2=new int[realCoste.length];
        int[] realcosti2=new int[realCosti.length];

        if (true){
            extra = (int)ExtraProxy*1000;
            for (int i = 0; i < trafficproxy2.length; i++) {
                for (int j = 0; j < trafficProxy[i].length; j++) {
                    trafficproxy2[i][j]=(int)(trafficProxy[i][j]*1000);
                }
            }
            for (int i = 0; i < realcoste2.length; i++) {
                realcoste2[i] = (int)(realCoste[i]*1000);
            }
            for (int i = 0; i < realcosti2.length; i++) {
                realcosti2[i] = (int)(realCosti[i]*1000);
            }
        }
        elappsed=0;
//        System.out.println("interactions..." + interactions.length);
//        System.out.println(l2r.r.independentVMs.size());
//        System.out.println(l2r.r.dependentVMs.size());
//        System.out.println(costi.length);
//        for (int i = 0; i < interactions.length; i++) {
//            System.err.println("coste->" + coste.length);
//            System.err.println(Arrays.toString(interactions[i]));
//
//        }
        //todo split to autonoumous matrixes and solve them seperately
        if (coste.length > 0) {
            try {
                test.bw.write("test values\n");
                test.bw.write(CostExternalProxy+"\n");
                test.bw.write(CostInternalProxy+"\n");
                test.bw.write(CostInternalProxy2+"\n");
                test.bw.write(ExtraProxy+"\n");
                test.bw.write(dependentvmCostProxy+"\n");
                test.bw.write(ccu+"\n");
                test.bw.write(l2r.r.dependentVMs.size()+"\n");
                test.bw.write(l2r.r.independentVMs.size()+"\n");
                test.bw.write(l2r.r.middleVMs.size()+"\n");
                test.bw.write(l2r.r.edgeVMs.size()+"\n");




            } catch (IOException e) {
                e.printStackTrace();
            }
            Long startTime = System.currentTimeMillis();
            CreateGraph cg = new CreateGraph();
            ReturnResult rt = cg.graphCreator(realcoste2, interactions, realcosti2, trafficproxy2 , trafficProxy , realCoste , realCosti);
            if (rt==null){
                for (int ii = 0; ii < l2r.r.dependentVMs.size(); ii++) {

                    d.vmplaces.remove(l2r.r.dependentVMs.get(ii));
                }
                for (ArrayList<CapturedHost> cha:InternalChooser.assignnments.values()){
                    for (CapturedHost ch: cha){
                        ch.h.revert(ch.ram,ch.hard,0,ch.hertz);
                    }
                }
                    return;



            }
            InternalDispatcher.status=true;
            ArrayList<String> temp = l2r.r.independentVMs;
            int countInVMS = 0;
            int minimal=0;
            double minimal2=0;
            if (rt.place.equals("mix")) {
                minimal = rt.summix;
                minimal2=rt.realsum;
                if (rt.sumin < minimal) {
                    minimal = rt.sumin;
                    minimal2=CostExternalProxy;
                }
                if (rt.sumout + extra <= minimal) {
                    minimal = rt.sumout + extra;
                    minimal2=CostInternalProxy+ExtraProxy;
                }
                if (minimal == rt.summix) {
                        for (int i = 0; i < temp.size(); i++) {
                            Position p;
                            String nname=temp.get(i);
                            if (rt.in[i]) {
                                countInVMS++;
                                p = new Position(null, "in", 0);

                            } else {
                                p = l2r.mapper.get(temp.get(i));
                                ArrayList<CapturedHost> cha=InternalChooser.assignnments.get(nname);
                                    for (CapturedHost ch: cha){
                                        ch.h.revert(ch.ram,ch.hard,0,ch.hertz);
                                    }
                                InternalChooser.assignnments.remove(nname);
                                }
                            d.vmplaces.put(nname, p);
                        }

                } else if (minimal == rt.sumin) {
                        countInVMS = rt.in.length;

                        for (int i = 0; i < temp.size(); i++) {
                            Position p = new Position(null, "in", 0);
                            d.vmplaces.put(temp.get(i), p);
                        }

                } else {
                    for (int i = 0; i < temp.size(); i++) {
                        Position p = l2r.mapper.get(temp.get(i));
                        d.vmplaces.put(temp.get(i), p);
                        ArrayList<CapturedHost> cha=InternalChooser.assignnments.get(temp.get(i));
                        for (CapturedHost ch: cha){
                            ch.h.revert(ch.ram,ch.hard,0,ch.hertz);
                        }
                    }
                    InternalChooser.assignnments.clear();

                }
            } else {
                minimal = rt.sumin;
                minimal2=CostExternalProxy;
                if (rt.sumout + extra <= minimal) {
                    minimal = rt.sumout + extra;
                    minimal2 = CostInternalProxy+ExtraProxy;
                    for (int i = 0; i < temp.size(); i++) {
                        Position p = l2r.mapper.get(temp.get(i));
                        d.vmplaces.put(temp.get(i), p);
                        ArrayList<CapturedHost> cha=InternalChooser.assignnments.get(temp.get(i));
                        for (CapturedHost ch: cha){
                            ch.h.revert(ch.ram,ch.hard,0,ch.hertz);
                        }
                    }
                    InternalChooser.assignnments.clear();

                } else {
                    countInVMS = rt.in.length;
                    for (int i = 0; i < rt.in.length; i++) {
                        Position p = new Position(null, "in", 0);
                        d.vmplaces.put(temp.get(i), p);
                    }
                }

            }
            int outvms = l2r.r.numberOfVM - countInVMS;
            totalVMSout += outvms;
            totalVMSin += countInVMS;
            double costb = (((rt.sumout + extra) / 1000.0) + dependentvmCost);
            double costa = ((((double) minimal) / 1000.0) + dependentvmCost);
            double costb2 = (CostInternalProxy + ExtraProxy + dependentvmCostProxy+ccu);
            double costa2 = (minimal2 + dependentvmCostProxy+ccu);
            double tempcostOptimal;
            if(rt.allO){
                tempcostOptimal=costb2;
            }else{
                tempcostOptimal=(rt.optimalcost+dependentvmCostProxy+ccu);
            }
            costOptimal+=tempcostOptimal;
            totalcostAfter += costa;
            totalCostBefore += costb;
            totalcostAfterProxy += costa2;
            totalCostBeforeProxy += costb2;

            try {
                test.bw.write("OPTIMAL PLCAE FOUND FOR REQUEST " + l2r.r.requestId + " IN " + (System.currentTimeMillis() - startTime-elappsed) + " Milis\r\n");
            test.bw.write("number of vms out : " + outvms+"\r\n");
            test.bw.write("number of vms in : " + countInVMS+"\r\n");
            test.bw.write(String.format("before this computation cost was : %.4f $ \r\n", costb));
            test.bw.write(String.format("optimal cost for request %d : %.4f $ \r\n", l2r.r.requestId, costa));
            test.bw.write(String.format("TIMED before this computation cost was : %.4f $ \r\n", costb2));
            test.bw.write(String.format("TIMED optimal cost for request %d : %.4f $ \r\n", l2r.r.requestId, costa2));




            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                test.bw.write("test values\n");
                test.bw.write(CostExternalProxy+"\n");
                test.bw.write(CostInternalProxy+"\n");
                test.bw.write(CostInternalProxy2+"\n");
                test.bw.write(ExtraProxy+"\n");
                test.bw.write(dependentvmCostProxy+"\n");
                test.bw.write(ccu+"\n");
                test.bw.write(l2r.r.dependentVMs.size()+"\n");
                test.bw.write(l2r.r.independentVMs.size()+"\n");
                test.bw.write(l2r.r.middleVMs.size()+"\n");
                test.bw.write(l2r.r.edgeVMs.size()+"\n");




            } catch (IOException e) {
                e.printStackTrace();
            }
            InternalDispatcher.status=true;
            //todo can improve it!
            Long startTime = System.currentTimeMillis();
            if (l2r.r.independentVMs.size()>0){
                System.err.println("more than zero");
            }
            for (String sss : l2r.r.independentVMs) {
                d.vmplaces.put(sss, l2r.mapper.get(sss));
            }
            for (ArrayList<CapturedHost> cha:InternalChooser.assignnments.values()){
                for (CapturedHost ch: cha){
                    ch.h.revert(ch.ram,ch.hard,0,ch.hertz);
                }
            }
            InternalChooser.assignnments.clear();
            int outvms = l2r.r.numberOfVM;
            totalVMSout += outvms;
            double costb = ((extra / 1000.0) + dependentvmCost);
            double costa = costb;
            double costb2 = (ExtraProxy + dependentvmCostProxy+ccu);
            double costa2 = costb2;
            costOptimal+=costb2;

            totalcostAfter += costa;
            totalCostBefore += costb;
            totalcostAfterProxy += costa2;
            totalCostBeforeProxy += costb2;



            try {
                test.bw.write("OPTIMAL PLCAE FOUND FOR REQUEST " + l2r.r.requestId + " IN " + (System.currentTimeMillis() - startTime-elappsed) + " Milis\r\n");
            test.bw.write("number of vms out : " + outvms+"\r\n");
            test.bw.write("number of vms in : 0"+"\r\n");
            test.bw.write(String.format("before this computation cost was : %.4f $ \r\n", costb));
            test.bw.write(String.format("optimal cost for request %d : %.4f $ \r\n", l2r.r.requestId, costa));
            test.bw.write(String.format("TIMED before this computation cost was : %.4f $ \r\n", costb2));
            test.bw.write(String.format("TIMED optimal cost for request %d : %.4f $ \r\n", l2r.r.requestId, costa2));
            test.bw.write(String.format("optimum %d : %.4f $ \r\n", l2r.r.requestId, costb2));






            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            test.bw.write("TOTAL\r\n");
        test.bw.write("total number of vms out : " + totalVMSout+"\r\n");
        test.bw.write("total number of vms in : " + totalVMSin+"\r\n");
        test.bw.write(String.format("before this computation toal cost was : %.4f $ \r\n", totalCostBefore));
        test.bw.write(String.format("optimal total cost is : %.4f $ \r\n", totalcostAfter));
        test.bw.write(String.format("TIMED before this computation toal cost was : %.4f $ \r\n", totalCostBeforeProxy));
        test.bw.write(String.format("TIMED optimal total cost is : %.4f $ \r\n", totalcostAfterProxy));
        test.bw.write(String.format("Toptimum %d : %.4f $ \r\n", l2r.r.requestId, costOptimal));

            test.bw.write(l2r.sadness+"\n");
//            for (String vv:l2r.r.properties.keySet()){
//                test.bw.write(!d.vmplaces.get(vv).location.equals("in")? d.vmplaces.get(vv).cloud.name+"\n" : "in\n");
//            }
//            test.bw.write("Fuck god\n");
//            for (String vv:l2r.r.properties.keySet()){
//                test.bw.write(UtiliyImplementation.places.get(vv).cloud.name+"\n");
//            }
//            test.bw.write("Fuck god\n");
//            for (String vv:l2r.r.properties.keySet()){
//                test.bw.write(ChooseAlwaysClouds.places.get(vv).cloud.name+"\n");
//            }
//            test.bw.write("Fuck god\n");





            MapRepository.assignnments.putAll(InternalChooser.assignnments);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

    //  System.out.println("optimal cost for request "+l2r.r.requestId+" is "+((((double)minimal)/1000)+dependentvmCost)+" $");

    public boolean findInternalVMsForChecking(boolean[] internals, Request r) {
        return true;
//        long startTime=System.currentTimeMillis();
//        ArrayList<VMProperties> choosedIndependent = new ArrayList<VMProperties>();
//        HashMap<String, VMProperties> properties = r.properties;
//        ArrayList<String> independentVMs = r.independentVMs;
//        for (int i = 0; i < internals.length; i++) {
//            if (internals[i]) {
//                choosedIndependent.add(properties.get(independentVMs.get(i)));
//            }
//        }
//        boolean answer=InternalChooser.checkPossibility(choosedIndependent);;
//        elappsed = (System.currentTimeMillis() - startTime);
//        System.out.println("INNER FEASIBILITY SYSTEMS CHECK FOR REQUEST " + r.requestId + " IN " + elappsed + " Milis");
//        System.out.println("fesibility inner-> "+answer);
//        return answer;
    }


}
//    public void dispatch(Request r , HashSet<Integer> vms){
//        //first send this request to InternalChooser to see if its possible so
//        HashSet<Integer> temp=new HashSet<Integer>(vms);
//        HashMap<Integer , Integer> deploy=ic.checkRequestPosibility(r , vms);
//        if(temp.size()==vms.size()){
//            //so the organization have enough resources for all vms => dispatch them
//        }else{
//            if(vms.isEmpty()){
//                return;
//                //and send all vms to cloud
//            }else{
//                vmplaces = new HashMap<Integer, Integer>();
//                internalVms =new HashSet<Integer>(vms);
//                externalVms = new HashSet<Integer>(temp);
//                externalVms.removeAll(vms);
//                for(Integer i:externalVms){
//                    vmplaces.put(i,2);
//                }
//                while (internalVms.size() + externalVms.size() != vmplaces.size()) {
//                    for (Integer i : internalVms) {
//                        countbranch = 0;
//                        if (!vmplaces.containsKey(i)) {
//                            HashSet<Integer> otherVms = new HashSet<Integer>(internalVms);
//                            otherVms.remove(i);
//                            numberOfbranch = computeCounter(otherVms.size());
//                            findplace(i, temp);
//                            change = false;
//                        }
//                    }
//                }
//                dispatchPhase2(r , internalVms);
//                d.sendVmsToCloud(r , externalVms);
//            }
//        }
//
//
//    }
//    public void dispatchPhase2(Request r , Set<Integer> vms){
//        //first send this request to InternalChooser to check Place again??
//        HashMap<Integer , Integer> deploy= ic.checkPlace(r , vms);
//        //deploy them
//    }
//    private int computeCounter(int c) {
//        switch (c) {
//            case 0:
//                return 0;
//            case 1:
//                return 1;
//            case 2:
//                return 4;
//            case 3:
//                return 15;
//            default:
//                return c * computeCounter(c - 1) + c;
//        }
//    }
//    private void findplace(Integer i, HashSet<Integer> otherVms) {
//        //find amount of interaction between internal and external services
//        //if it is better be internal do the following
//        if (otherVms.size() == 0 && countbranch == numberOfbranch && !change) {
//            for (int m = 0; m < internalVms.size(); m++) {
//                vmplaces.put(m, 1);
//            }
//            return;
//        }
//        for (Integer j : otherVms) {
//            countbranch++;
//            if (change == false) {
//                HashSet<Integer> temp2 = new HashSet<Integer>(otherVms);
//                temp2.remove(j);
//                findplace(j, temp2);
//            } else {
//                return;
//            }
//        }
//        //else do following
//        change = true;
//        vmplaces.put(i, 2);
//        internalVms.remove(i);
//        externalVms.add(i);
//
//
//        return;
//
//    }
