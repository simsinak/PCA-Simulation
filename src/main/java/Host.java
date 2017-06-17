/**
 * Created by sinaaskarnejad on 3/29/16.
 */
public class Host extends Node2 {
int id;
String group;
String oS;
float ram;
int hard;
double miu;
int cores;
double hertz;

    public Host(int id, String group, String oS, float ram, int hard, double miu, int cores, double hertz) {
        this.id = id;
        this.group = group;
        this.oS = oS;
        this.ram = ram;
        this.hard = hard;
        this.miu = miu;
        this.cores = cores;
        this.hertz = hertz;
    }
    public void subtract(float ram,int hard,int cores,double hertz){
        this.ram-=ram;
        this.hard-=hard;
        this.cores-=cores;
        this.hertz-=hertz;
    }
    public void revert(float ram,int hard,int cores,double hertz){
        this.ram+=ram;
        this.hard+=hard;
        this.cores+=cores;
        this.hertz+=hertz;
    }
    public void register(InternalMonitor in){
        //send resources to InternalMonitor with computing of delay
    }

    @Override
    public String toString() {
        return String.format("ID : %d%nGROUP : %s%nOS : %s%nRAM : %f%nHARD : %d%nMIU : %f%nCORES : %d%nHERTZ : %f", id , group , oS , ram,hard,miu,cores,hertz);
    }
}
