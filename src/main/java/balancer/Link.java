package balancer;

/**
 * Created by sinaaskarnejad on 4/13/16.
 */
public class Link implements Comparable{
    Node start;
    Node end;
    int value;
    int round;
    boolean big;
    int minorvalue;
    public void setRound(int round) {
        this.round = round;
    }

    public Link(Node start, Node end, int value , boolean big) {
        this.start = start;
        this.end = end;
        this.value = value;
        this.big = big;

    }
    public Link(Node start, Node end, int value , boolean big , int minorvalue) {
        this.start = start;
        this.end = end;
        this.value = value;
        this.big = big;
        this.minorvalue=minorvalue;

    }
    public Link(Node start, Node end, int value ) {
        this.start = start;
        this.end = end;
        this.value = value;
        this.big = big;
    }

    @Override
    public String toString() {
        return start.id+"->"+end.id;
    }
    public Link(Link l){
        start=l.start;
        end=l.end;
        value=l.value;
        big=l.big;
    }
    public int compareTo(Object o) {
        if(o instanceof Link){
            Link t=(Link)o;
            if(t.value>this.value){
                return -1;
            }else if(t.value<this.value){
                return 1;
            }else{
                return 0;
            }
        }
        else return 0;
    }
}
