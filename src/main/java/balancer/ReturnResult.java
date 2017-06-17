package balancer;

/**
 * Created by sinaaskarnejad on 7/7/16.
 */
public class ReturnResult {
    //in , out , mix
    public String place;
    public boolean[] in;
    public int sumin=0;
    public int sumout=0;
    public int summix=0;
    public double realsum=0;
    public double optimalcost=0;
    public boolean allO=false;
}
