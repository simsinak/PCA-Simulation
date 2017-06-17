import java.io.IOException;

/**
 * Created by sinaaskarnejad on 7/1/16.
 */
public class ExternalDispatcher {
    static int dropedBecauseOFCloudCounter = 0;
    static boolean situation=false;
    private static ExternalDispatcher ourInstance = new ExternalDispatcher();
    private ChooserLevel1 cl1;
    public static ExternalDispatcher getInstance() {
        return ourInstance;
    }
    public  Dispatcher dispatcher;
    private ExternalDispatcher() {
        cl1=new ChooserLevel1();
    }
    public void processRequest(Request r , Dispatcher dispatcher){
        ExternalDispatcher.situation=false;
        this.dispatcher=dispatcher;
        cl1.processRequest(r , this);
    }
    public void getResult(Level2Result l2r){
            ExternalDispatcher.situation=false;
            if(l2r.problem){
                try {
                    test.bw.write("ERROR: Recent Request cant be fited in Cloud because of a problem\r\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ExternalDispatcher.dropedBecauseOFCloudCounter+=1;
            }else {
                ExternalDispatcher.situation=true;
                dispatcher.getResult(l2r);
            }


    }
}
