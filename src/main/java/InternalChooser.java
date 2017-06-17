import java.io.IOException;
import java.util.*;

/**
 * Created by sinaaskarnejad on 3/20/16.
 */
public class InternalChooser {
    InternalDispatcher id;
    static ArrayList<Node2> nodes;
    public InternalChooser(InternalDispatcher id){
        this.id=id;
    }
    static HashMap<String , ArrayList<CapturedHost>> assignnments=new HashMap<String, ArrayList<CapturedHost>>();

    public HashMap<Integer , Integer> checkRequestPosibility(Request r , HashSet<Integer> vms){
        //TODO
        return null;
    }
    public HashMap<Integer , Integer> checkPlace(Request r , Set<Integer> vms){
        //TODO
        return null;
    }
    public static boolean checkPossibility(ArrayList<VMProperties> VMList){
        Random r=new Random();
        HashMap<String , ArrayList<CapturedHost>> assignnments=new HashMap<String, ArrayList<CapturedHost>>();
        for(VMProperties vmproperies:VMList){
            int storage=Integer.parseInt(vmproperies.ExtraProperties.getProperty("storageG"));
            float memory=Float.parseFloat(vmproperies.ExtraProperties.getProperty("memoryG"));
            int cpu=Integer.parseInt(vmproperies.ExtraProperties.getProperty("cpuCore"));
            String OS=vmproperies.ExtraProperties.getProperty("OS");
            double hertz=Double.parseDouble(vmproperies.ExtraProperties.getProperty("Hertz"));
            //copy in random hosts 3..5
            int counter=r.nextInt(3)+3;
            ArrayList<CapturedHost> temp=new ArrayList<CapturedHost>(counter);
            for (int number=0; counter!=0 && number<nodes.size() ; number++){
                Host host= (Host) nodes.get(number);
                if(OS.equals(host.oS) && storage<=host.hard && memory<=host.ram && cpu<=host.cores && hertz<=host.hertz){
                    host.subtract(memory,storage,0,hertz);
                    temp.add(new CapturedHost(host, memory,storage , cpu ,hertz));
                    counter--;
                }
            }
            if(counter==0){
                assignnments.put(vmproperies.name , temp);
            }
            else {
              for (ArrayList<CapturedHost> ach:assignnments.values()){
                  for(CapturedHost ch:ach){
                      ch.h.revert(ch.ram,ch.hard,0,ch.hertz);
                  }
              }
              return false;
            }
        }
        MapRepository.assignnments.putAll(assignnments);
        return true;
    }
    public static void checkPossibility2(Request request){
long startTime=System.currentTimeMillis();
        Random r=new Random();
        assignnments.clear();
        for(VMProperties vmproperies:request.properties.values()){
            if (vmproperies.location==null || vmproperies.location.isEmpty()){
            int storage=Integer.parseInt(vmproperies.ExtraProperties.getProperty("storageG"));
            float memory=Float.parseFloat(vmproperies.ExtraProperties.getProperty("memoryG"));
            int cpu=Integer.parseInt(vmproperies.ExtraProperties.getProperty("cpuCore"));
            String OS=vmproperies.ExtraProperties.getProperty("OS");
            double hertz=Double.parseDouble(vmproperies.ExtraProperties.getProperty("Hertz"));
            //copy in random hosts 3..5
            int counter=r.nextInt(3)+3;
            ArrayList<CapturedHost> temp=new ArrayList<CapturedHost>(counter);
            for (int number=0; counter!=0 && number<nodes.size() ; number++){
                Host host= (Host) nodes.get(number);
                if(OS.equals(host.oS) && storage<=host.hard && memory<=host.ram && cpu<=host.cores && hertz<=host.hertz){
                    host.subtract(memory,storage,0,hertz);
                    temp.add(new CapturedHost(host, memory,storage , cpu ,hertz));
                    counter--;
                }
            }
            if(counter==0){
                assignnments.put(vmproperies.name , temp);

            }
            else {
                if(request.independentVMs.remove(vmproperies.name)){
                request.dependentVMs.add(vmproperies.name);}
                    for(CapturedHost ch:temp){
                        ch.h.revert(ch.ram,ch.hard,0,ch.hertz);
                    }
                }
            }else{
if (request.independentVMs.remove(vmproperies.name)){
    request.dependentVMs.add(vmproperies.name);
}


        }}


        try {
            test.bw.write("FOUND INTERNAL SYSTEMS FOR REQUEST "+ request.requestId+" IN "+ (System.currentTimeMillis()-startTime)+" Milis\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
