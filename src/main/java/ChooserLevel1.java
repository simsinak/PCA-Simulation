import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

/**
 * Created by sinaaskarnejad on 3/20/16.
 */
public class ChooserLevel1 {
    public ChooserLevel2 cl2;
    public ChooserLevel1(){
        cl2=new ChooserLevel2();
    }
    public void processRequest(Request r , ExternalDispatcher ex){
        long startTime=System.currentTimeMillis();
        cl2.resetProviders();
        boolean problem=false;
        for (String key:r.properties.keySet()){
            ArrayList<Cloud> temp= RequestSender.sendResuest(MapRepository.repo , r.encodes.get(key) , key);
            if(temp.isEmpty()){
                try {
                    test.bw.write("ERROR: REQUEST "+r.requestId+" OF "+key+" CAN'T FIT IN ANY CLOUD WE KNOW! MISSION ABORTED...\r\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                problem=true;
                break;
            }else {
                cl2.addProviders(key , temp);
            }
        }
        try {
            test.bw.write("RETRIVE CLOUDS FOR REQUEST "+r.requestId+" IN "+(System.currentTimeMillis()-startTime)+" Milis\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        cl2.mapRequestToCloud(r , ex , problem);
    }

}
