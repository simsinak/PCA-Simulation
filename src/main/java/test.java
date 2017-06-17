import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.io.*;
import java.util.*;

/**
 * Created by sinaaskarnejad on 3/23/16.
 */
public class test {
    final static RequestType REQUEST_TYPE = RequestType.oneByOne;
    final static int REQUEST_NUMBER = 600;
    final static int NUMBER_PROVIDER =500;
    static Request request = null;
    static Collection<Request> requests = null;
    static FeasibilityCheker fc = new FeasibilityCheker();
    static int dropedRequestCounter = 0;
    static BufferedWriter bw;
    static ArrayList<Request> requestList;
    //change this path to urs directory;)
    final static String writing_path="/Users/sinaaskarnejad/Desktop/result.txt";
    public static void main(String[] args) throws IOException {
        System.out.println("loading rtt file...");
        LoadRTT.loadRTT();
       // System.setOut(new PrintStream(new FileOutputStream("/Users/sinaaskarnejad/Desktop/ResultCloudTexts.txt")));
        // place of log writing
        bw=new BufferedWriter(new FileWriter(writing_path));
        requestList=new ArrayList<Request>();
        //creating internal topology
        HetrogeneousOrganizationTopology hot = new HetrogeneousOrganizationTopology();
        Topology topology = hot.createTopology(6609, 6, 0);
        topology.sortTopoIN();
        InternalChooser.nodes=topology.nodes;

        //creating external topology
        ExternalInternatTopology eit = new ExternalInternatTopology();
        Topology etopology = eit.createTopology(2, true, 10000000000l);
        etopology.sortTopo();
        //creating cloud providers
        CloudsInformation ci = new RandomCloudInformation2();
        RandomCloudInformation2.computeInervalsForProperties();
        ArrayList<Cloud> cloudResult = ci.createCloudInformation(NUMBER_PROVIDER);
        long startTime = System.currentTimeMillis();
        MapRepository.repo = new CloudProviderRepository(cloudResult);
        MapRepository.repo.createBPlusTree();
        bw.write("ClOUD INFORMATION STORED IN BPTREE IN " + (System.currentTimeMillis() - startTime) + " Milis\r\n");
        //creating Dispatcher
        Dispatcher dispatcher = new Dispatcher();
        bw.write("Dispatcher created...\r\n");

        //creating policies
        PolicyGenerator pg = new PolicyGenerator();
        pg.generate();


        //create request
        RequestGeneratorInterface rg = new RequestGenerator();
        //make it false if u do not want writing verbose in file
        if(true){
            for (int time=1;time<100;time++){
//                for (int i = 0; i < REQUEST_NUMBER; i++) {
//                    Request request = rg.generateRequestOneByOne();
//                    requestList.add(request);
//                    //check feasibility
//                    bw.write(String.format("#############################( REQUEST %d )##############################\r\n", request.requestId));
//                    bw.write("number of vms->" + request.numberOfVM+"\r\n");
//                    bw.write("number main->" + request.middleVMs.size()+"\r\n");
//                    bw.write("number edge->" + request.edgeVMs.size()+"\r\n");
//                    bw.write("number service->" + request.services.size()+"\r\n");
//                    boolean result = fc.checkWithInternalPolicies(request);
//                    while (!result) {
//                       bw.write("Request " + request.requestId + " Must Modify -> Sending it backward!\r\n");
//                        dropedRequestCounter++;
//                        rg.reformRequest(request);
//                        result = fc.checkWithInternalPolicies(request);
//                    }
//                    bw.write("Request " + request.requestId + " Succeed in Feasibility -> Sending it forward!\r\n");
//                    dispatcher.processRequest(request, etopology);
//                    while (ExternalDispatcher.situation==false){
//                        request=rg.generateRequestOneByOne(request);
//                        dispatcher.processRequest(request , etopology);
//                    }
//
//                }
                System.err.println("Time "+time);
                for (int i = 0; i < REQUEST_NUMBER; i++) {
                    Request request = rg.generateRequestOneByOne();
                    //check feasibility
//                    System.out.printf("#############################( REQUEST %d )##############################%n", request.requestId);
//                    System.out.println("number of vms->" + request.numberOfVM);
//                    System.out.println("number main->" + request.middleVMs.size());
//                    System.out.println("number edge->" + request.edgeVMs.size());
//                    System.out.println("number service->" + request.services.size());
                    bw.write(String.format("#############################( REQUEST %d )##############################\r\n", request.requestId));
                    bw.write("number of vms->" + request.numberOfVM+"\r\n");
                    bw.write("number main->" + request.middleVMs.size()+"\r\n");
                    bw.write("number edge->" + request.edgeVMs.size()+"\r\n");
                    bw.write("number service->" + request.services.size()+"\r\n");
                    bw.write("number dep->" + request.dependentVMs.size()+"\r\n");
                    bw.write("number indep->" + request.independentVMs.size()+"\r\n");
                    boolean result = fc.checkWithInternalPolicies(request);
                    while (!result) {
                        bw.write("Request " + request.requestId + " Must Modify -> Sending it backward!\r\n");
                        dropedRequestCounter++;
                        rg.reformRequest(request);
                        result = fc.checkWithInternalPolicies(request);
                    }
                    bw.write("Request " + request.requestId + " Succeed in Feasibility -> Sending it forward!\r\n");
                    dispatcher.processRequest(request, etopology);
                    while (ExternalDispatcher.situation==false||InternalDispatcher.status==false){
                        if (ExternalDispatcher.situation==false){
                            request=rg.generateRequestOneByOne(request);
                        }else if(InternalDispatcher.status==false){
                            bw.write("**REFORM**\r\n");
                            request=rg.generateRequestOneByOne(request);
                        }
                        dispatcher.processRequest(request , etopology);
                   //     System.out.println("param1 : "+ExternalDispatcher.situation+" param2 : "+InternalDispatcher.status);

                    }
//                    while (ExternalDispatcher.situation==false){
//                        request=rg.generateRequestOneByOne(request);
//                        dispatcher.processRequest(request , etopology);
//                    }
//                    while (InternalDispatcher.status==false){
//                        System.out.println("REFOrM");
//                        request=rg.generateRequestOneByOne(request);
//                        dispatcher.processRequest(request , etopology);
//                    }
                    requestList.add(request);

                }
                Iterator<Request> iterator=requestList.iterator();
                while (iterator.hasNext()){
                    Request tempit=iterator.next();
                    tempit.useTime-=1;
                    //release internal resources
                    if(tempit.useTime==-1){
                        for (String vmnames: tempit.independentVMs) {
                            ArrayList<CapturedHost> captures=MapRepository.assignnments.remove(vmnames);
                            if (captures!=null){
                            for (CapturedHost capthost: captures){
                                capthost.h.revert(capthost.ram,capthost.hard,0,capthost.hertz);

                            }}
                       //     dispatcher.vmplaces.remove(vmnames);
                            captures=null;
                        }
                        for (String vmnames: tempit.dependentVMs) {
                         //   dispatcher.vmplaces.remove(vmnames);
                        }
                        MapRepository.removedVMs.addAll(tempit.dependentVMs);
                        MapRepository.removedVMs.addAll(tempit.independentVMs);
                        iterator.remove();
                    }
                }
                bw.write("number of dropped request because of problems in feasibility : " + dropedRequestCounter+"\r\n");
                bw.write("number of dropped request because of problems in cloud and network : " + ExternalDispatcher.dropedBecauseOFCloudCounter+"\r\n");
            }
            bw.close();
        }
       else if (REQUEST_TYPE == RequestType.oneByOne) {
            // put a loop here with some distribution (exponential or uniform)
            ArrayList<Request> resuests = (ArrayList<Request>) rg.generateRequestOnce(REQUEST_NUMBER);
            //    request=rg.generateRequestOneByOne();
            //     request =resuests.remove(0);

//            if(fc.checkWithInternalPolicies(request)){
//                dispatcher.processRequest(request,etopology);
//
//            }
//            System.setOut(ps);
//            System.setErr(ps);
            for (int i = 0; i < REQUEST_NUMBER; i++) {
                Request request = resuests.remove(0);
                //check feasibility
                System.out.printf("#############################( REQUEST %d )##############################%n", request.requestId);
                System.out.println("number of vms->" + request.numberOfVM);
                System.out.println("number main->" + request.middleVMs.size());
                System.out.println("number edge->" + request.edgeVMs.size());
                System.out.println("number service->" + request.services.size());
                boolean result = fc.checkWithInternalPolicies(request);
                while (!result) {
                    System.out.println("Request " + request.requestId + " Must Modify -> Sending it backward!");
                    dropedRequestCounter++;
                    rg.reformRequest(request);
                    result = fc.checkWithInternalPolicies(request);
                }
                System.out.println("Request " + request.requestId + " Succeed in Feasibility -> Sending it forward!");
                dispatcher.processRequest(request, etopology);
                while (ExternalDispatcher.situation==false){
                    request=rg.generateRequestOneByOne(request);
                    dispatcher.processRequest(request , etopology);
                }
                while (InternalDispatcher.status==false){
                    System.out.println("REFOrM");
                    request=rg.generateRequestOneByOne(request);
                    dispatcher.processRequest(request , etopology);
                }

            }
            System.out.println("number of dropped request because of problems in feasibility : " + dropedRequestCounter);
            System.out.println("number of dropped request because of problems in cloud and network : " + ExternalDispatcher.dropedBecauseOFCloudCounter);
            System.out.println(topology.nodes.get(0));
        } else {
            requests = rg.generateRequestOnce(REQUEST_NUMBER);


            //check feasibility
            for (Request req : requests) {
                boolean result = fc.checkWithInternalPolicies(req);
                if (!result) {
                    System.out.println("Request " + req.requestId + " Must Modify -> Sending it backward!");
                } else {
                    System.out.println("Request " + req.requestId + " Succeed -> Sending it forward!");
                    dispatcher.processRequest(request, etopology);

                }
            }
        }

    }
}
