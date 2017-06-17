/**
 * Created by sinaaskarnejad on 7/24/16.
 */
public class CapturedHost {
    Host h;
    float ram;
    int hard;
    int cores;
    double hertz;

    public CapturedHost(Host h, float ram, int hard, int cores, double hertz) {
        this.h = h;
        this.ram = ram;
        this.hard = hard;
        this.cores = cores;
        this.hertz = hertz;
    }
}
