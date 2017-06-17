import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by sinaaskarnejad on 3/20/16.
 */

public class Dispatcher {
    Set<String> internalVms = new HashSet<String>();
    Set<String> externalVms = new HashSet<String>();
    int countbranch = 0;
    int numberOfbranch = 0;
    //2 means external 1 means internal(new interpret-> In means internal otherwise External)
    HashMap<String, Position> vmplaces = new HashMap<String, Position>();
    boolean change = false;
    Topology etopo ;
    public void processRequest(Request r , Topology t) {
        etopo=t;
        ExternalDispatcher ed=ExternalDispatcher.getInstance();
        ed.processRequest(r , this);
        //after that do the following

    }

    public void getResult(Level2Result l2r) {
         InternalDispatcher id= InternalDispatcher.getInstance(this);
         id.processRequest(l2r);

    }
      /*previous Method


        1 location is empty
        boolean locationISEmpty = true;
        for (VMProperties vp: r.properties.values()) {
            if (!vp.location.isEmpty()){
               locationISEmpty = false;
            }
            if (!locationISEmpty){
                break;
            }
        }
        if (!locationISEmpty){
            findVMsCannotDispatchInternally(r);
            /*send request to InternalDispatcher but check
            in internalDispatcher which services interacting with is internal or external
            if interact between this vm and services is less than
            interact between user and vm send this vm request to internalDipatcher and residual to external

        }
         2 services is only between this vms
        boolean externalServices = false;
        for (VMProperties vp: r.properties.values()){
            for(String service: vp.Services){
               if(!StringUtils.isNumeric(service)){
                   if interact between this vm and services is less than
                    interact between user and vm send this vm request to internal
                    Dipatcher

                   externalServices = true;
                   break;
               }

                if (externalServices){
                    break;
                }
            }
        }
        if (!externalServices && locationISEmpty){
            //send request to InternalDispatcher
        }

    private void findVMsCannotDispatchInternally(Request r) {
        Set<String> services=new HashSet<String>();
        for(VMProperties vp:r.properties.values()){
            //if interact between this vm and services is more than interact between vm add that vm to services
        }
        for(int i = 0;i<r.properties.size();i++){
            if(!services.contains(i)){

            }

        }
    }*/
}
