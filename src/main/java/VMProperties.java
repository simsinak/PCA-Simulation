import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * Created by sinaaskarnejad on 3/24/16.
 */
public class VMProperties {
    String level;
    String Type;
    String name;
    int numberOfThisType;
    double VMCost;
    Set<String> location;
    double NetworkCost;
    HashMap<String , Integer> delay;
    HashMap<String , Integer> traffic=new HashMap<String, Integer>();
    HashMap<String , Double> bandwidth=new HashMap<String, Double>();
    //responseTime //availability?? not set yet
    Properties QOS=new Properties();
    Set<String> Services=new HashSet<String>();
    //storage //memory  //cpu //hertz  //os
    Properties ExtraProperties = new Properties();
}
