import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by sinaaskarnejad on 8/20/16.
 */
public class LoadRTT {
    public static void loadRTT() throws FileNotFoundException {
        Scanner sc=new Scanner(new File("rtt.txt"));
        while (sc.hasNext()){
            String text=sc.nextLine();
            if (text.startsWith("->")){
                String[] tokens=text.substring(2).split(",");
                double min=Double.parseDouble(tokens[1]);
                double max=Double.parseDouble(tokens[2]);
                String contry=tokens[0];
                double mid=Double.parseDouble(sc.nextLine().substring(2));
                double div=Double.parseDouble(sc.nextLine().substring(2));
                double rangeStart=0.0;
                double rangeEnd=0.0;
                if (mid-div<0){
                    rangeStart = 0.0;
                }
                rangeEnd = mid+div;
                Diverse dive=new Diverse();
                dive.min=rangeStart;
                dive.max=rangeEnd;
                if (!MapRepository.divs.containsKey(contry)){
                    MapRepository.divs.put(contry , new ArrayList<Diverse>());
                }
                MapRepository.divs.get(contry).add(dive);

            }
        }
    }
}
class Diverse{
    double min;
    double max;
}
