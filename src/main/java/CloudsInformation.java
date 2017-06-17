import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sinaaskarnejad on 5/19/16.
 */
public interface CloudsInformation {
    public ArrayList<Cloud> createCloudInformation(int number_provider);
    public HashMap<String , Integer> calculateBitNumber();
}
