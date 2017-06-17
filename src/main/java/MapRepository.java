import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by sinaaskarnejad on 6/30/16.
 */
public class MapRepository implements Repository{
    public static HashMap<String , HashMap<String , Object>> groupPermit=new HashMap<String, HashMap<String, Object>>();
    public static HashMap<String , HashMap<String , Object>> groupDeny=new HashMap<String, HashMap<String, Object>>();
    public static HashMap<String , HashMap<String , Object>> servicePermit=new HashMap<String, HashMap<String, Object>>();
    public static HashMap<String , HashMap<String , Object>> serviceDeny=new HashMap<String, HashMap<String, Object>>();
    public static HashMap<String , HashMap<String , Object>> allGroupsPermit=new HashMap<String, HashMap<String, Object>>();
    public static HashMap<String , HashMap<String , Object>> allGroupsDeny=new HashMap<String, HashMap<String, Object>>();
    public static HashMap<String , HashMap<String , Object>> allServicesPermit=new HashMap<String, HashMap<String, Object>>();
    public static HashMap<String , HashMap<String , Object>> allServicesDeny=new HashMap<String, HashMap<String, Object>>();
    public static CloudProviderRepository repo;
    public static HashMap<String , String> typeNames=new HashMap<String, String>();
    public static HashMap<String , ArrayList<CapturedHost>> assignnments=new HashMap<String, ArrayList<CapturedHost>>();
    public static int Time = 1;
    public static HashSet<String> removedVMs=new HashSet<String>();
    public static HashMap<String , ArrayList<Diverse>> divs=new HashMap<String, ArrayList<Diverse>>();


}
