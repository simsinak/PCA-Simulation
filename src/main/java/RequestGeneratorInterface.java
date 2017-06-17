import java.util.Collection;

/**
 * Created by sinaaskarnejad on 6/30/16.
 */
public interface RequestGeneratorInterface {
    public Request generateRequestOneByOne();
    public Collection<Request> generateRequestOnce(int size);

    void reformRequest(Request oldRequest);
    public Request generateRequestOneByOne(Request oldrequest);
}
