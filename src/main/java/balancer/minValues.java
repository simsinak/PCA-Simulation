package balancer;

/**
 * Created by sinaaskarnejad on 4/12/16.
 */
public class minValues implements Comparable {
    int value;
    int pathnumber;
    int from;
    int to;
    @Override
    public int hashCode() {
        return pathnumber;
    }



    public int compareTo(Object o) {
        if(o instanceof minValues){
            minValues t=(minValues)o;
            if(t.value>this.value){
                return 1;
            }else if(t.value<this.value){
                return -1;
            }else{
                return 0;
            }
        }
        else return 0;
    }
}
