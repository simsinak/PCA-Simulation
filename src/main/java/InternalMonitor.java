/**
 * Created by sinaaskarnejad on 3/20/16.
 */
public class InternalMonitor {
    public void UpdateInfo(int interval){
        //update resources view in each interval
    }
    public void getRegisterMessage(){
        //save register message in profile
        //after that set the route on switch routingTable
    }
    public void getTopologyUpdated(){
        //maybe some link became down
        //after that inform Mapper changingRoutes()
    }
}
