import java.io.IOException;
import java.util.*;

/**
 * Created by sinaaskarnejad on 8/15/16.
 */

public class Randomchooser {
    private static double gig = 1000000;
    HashMap<String , HashSet<String>> mapped=new HashMap<>();

    static HashMap<String , Position> places=new HashMap<String, Position>();
    public Level2Result chooseRandom(Request request ,HashMap<String , ArrayList<Cloud>> providers){
        long startt=System.currentTimeMillis();

        double vmcosts=0;
        double portion = Math.ceil(request.useTime/RandomCloudInformation2.typeOfHour.get(request.typeOfPayment));

        Random random=new Random();
        Level2Result lv = new Level2Result();
        for (String vms: request.edgeVMs){
            ArrayList<Cloud> al=providers.get(vms);
            int rand=random.nextInt(al.size());
            Cloud cloud=al.get(rand);
            int[] counters = request.counters;
            VMProperties vp=request.properties.get(vms);
            int responseTime=0;
            String res=vp.QOS.getProperty("responseTime");
            if (res!=null){
                responseTime=Integer.parseInt(res);
            }
            boolean found=false;
            boolean[] checkall=new boolean[cloud.location.length];
            int counter=0;
            while (!found){
            while (!found && counter<cloud.location.length){
                int i = random.nextInt(cloud.location.length);
                if(!checkall[i]){
                    checkall[i]=true;
                    counter++;
                }
                if (vp.location.isEmpty()){
                    double tt = cloud.priceMap[counters[0]][counters[1]][i][counters[3]][counters[4]][counters[5]][counters[6]][counters[7]];
                    //  if (rentCost > tt) {
                    //maybe in dataset it be location base
                    //     double communicationprice = cloud.InternetPriceOutG;
                    if (responseTime!=0 && cloud.dynamicResponsetime.get(cloud.location[i]).get() > responseTime) {
                        try {
                            test.bw.write("ERROR Random: Response Time of cloud "+cloud.name+" in location: "+cloud.location[i]+ "is not Good\r\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //    return -1;
                    }else{
                        Position p = new Position(cloud, cloud.location[i], tt);
                        lv.mapper.put(vms, p);
                        Randomchooser.places.put(vms , p);
                        vmcosts+=tt;
                        found =true;
                    }
                    //    }
                }
                else if(vp.location.contains(cloud.location[i])){
                    double tt = cloud.priceMap[counters[0]][counters[1]][i][counters[3]][counters[4]][counters[5]][counters[6]][counters[7]];
                    //  if (rentCost > tt) {
                    //maybe in dataset it be location base
                    //  double communicationprice = cloud.InternetPriceOutG;
                    if (responseTime!=0 && cloud.dynamicResponsetime.get(cloud.location[i]).get() >responseTime) {
                        try {
                            test.bw.write("ERROR Random: Response Time of cloud "+cloud.name+" in location: "+cloud.location[i]+ "is not Good\r\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //    return -1;
                    }else {
                        Position p = new Position(cloud, cloud.location[i], tt);
                        lv.mapper.put(vms, p);
                        Randomchooser.places.put(vms , p);
                        vmcosts+=tt;
                        found = true;
                    }
                    //    }
                }
            }
            if (!found){
                rand=random.nextInt(al.size());
                cloud=al.get(rand);
                counter =0;
                checkall=new boolean[cloud.location.length];

            }
            }
        }
        for (String vms: request.middleVMs){
            ArrayList<Cloud> al=providers.get(vms);
            int rand=random.nextInt(al.size());
            Cloud cloud=al.get(rand);
            int[] counters = request.counters;
            VMProperties vp=request.properties.get(vms);
            int responseTime=0;
            String res=vp.QOS.getProperty("responseTime");
            if (res!=null){
                responseTime=Integer.parseInt(res);
            }
            boolean found=false;
            boolean[] checkall=new boolean[cloud.location.length];
            int counter=0;
            while (!found){
            while (!found && counter<cloud.location.length){
                int i = random.nextInt(cloud.location.length);
                if(!checkall[i]){
                    checkall[i]=true;
                    counter++;
                }
                if (vp.location.isEmpty()){
                    double tt = cloud.priceMap[counters[0]][counters[1]][i][counters[3]][counters[4]][counters[5]][counters[6]][counters[7]];
                    //  if (rentCost > tt) {
                    //maybe in dataset it be location base
               //     double communicationprice = cloud.InternetPriceOutG;
                    if (responseTime!=0 && cloud.dynamicResponsetime.get(cloud.location[i]).get() > responseTime) {
                        try {
                            test.bw.write("ERROR Random: Response Time of cloud "+cloud.name+" in location: "+cloud.location[i]+ "is not Good\r\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //    return -1;
                    }else{
                        Position p = new Position(cloud, cloud.location[i], tt);
                        lv.mapper.put(vms, p);
                        Randomchooser.places.put(vms , p);
                        vmcosts+=tt;
                        found =true;
                    }
                    //    }
                }
                else if(vp.location.contains(cloud.location[i])){
                    double tt = cloud.priceMap[counters[0]][counters[1]][i][counters[3]][counters[4]][counters[5]][counters[6]][counters[7]];
                    //  if (rentCost > tt) {
                    //maybe in dataset it be location base
                  //  double communicationprice = cloud.InternetPriceOutG;
                    if (responseTime!=0 && cloud.dynamicResponsetime.get(cloud.location[i]).get() >responseTime) {
                        try {
                            test.bw.write("ERROR Random: Response Time of cloud "+cloud.name+" in location: "+cloud.location[i]+ "is not Good\r\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //    return -1;
                    }else {
                        Position p = new Position(cloud, cloud.location[i], tt);
                        lv.mapper.put(vms, p);
                        Randomchooser.places.put(vms , p);
                        vmcosts+=tt;
                        found = true;
                    }
                    //    }
                }
            }
                if (!found){
                    rand=random.nextInt(al.size());
                    cloud=al.get(rand);
                    counter =0;
                    checkall=new boolean[cloud.location.length];

                }}
        }
        vmcosts*=portion;
        Position place=null;
        int i=0;
        double ccu=0;
        for (String vms: request.properties.keySet()){
            int trafficPerSec = 0;
            double vmcost = 0;
            int trafficPerSec2 = 0;
            VMProperties vp = request.properties.get(vms);
            Set<String> services = vp.Services;
            Position place2 = lv.mapper.get(vms);
            for (String service : services) {
                place=null;
                int traf = vp.traffic.get(service);
                int index = request.dependentVMs.indexOf(service);
                int index2 = request.independentVMs.indexOf(service);
                //not dependent
                if (index == -1 && index2==-1) {
                    place = Randomchooser.places.get(service);
                    //not service
                    if (place == null) {
                        place = lv.mapper.get(service);

                    }
                 //   if (place.cloud!=place2.cloud || place.location!=place2.location){
                        trafficPerSec += traf;
                        vmcost += (((traf / 8) / gig) * place.cloud.InternetPriceOutG);}

               // }
            else{
                    place = lv.mapper.get(service);
                    if (!mapped.containsKey(service) ||  !mapped.get(service).contains(vms)){
                        //    if (index > i) {
                    //    if (place.cloud!=place2.cloud || place.location!=place2.location){
                        vmcost += (((traf / 8) / gig) * (place2.cloud.InternetPriceING+place.cloud.InternetPriceING));
                        if (!mapped.containsKey(vms) ){
                            mapped.put(vms , new HashSet<>());
                        }
                        mapped.get(vms).add(service);

                    }
                 //   }
                }
            }
            double ttt=((((trafficPerSec / 8) / gig) * (place2.cloud.InternetPriceING)) + vmcost);
            ccu+=(ttt*request.useTime*30);
            i++;
        }
        ccu+=vmcosts;
        lv.totalcost=ccu;
        try {
            test.bw.write("random for request "+ request.requestId+" finished in "+(System.currentTimeMillis()-startt));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lv;
    }
}
