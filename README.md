# PCA (A Cloud Service Seletor in the Peer Cloud Assisted Environments)
This is the simulation code based on VMLCB algorithm.
___
to use real RTT write them in **rtt.txt**. the format is as follows:
-> Location , min_rtt, max_rtt
##mid_rtt
##div_rtt
Also, change LoadRTT.java to change the behavior of loading the rtt.txt based on your needs.
___

to add another parameters do the Same and load them in test.java
___
the simulation starts from test.java.
change number of REQUESTS and PROVIDERS in it by static fields
final static int REQUEST_NUMBER = 600; //in each round
final static int NUMBER_PROVIDER =500;
___
change the RequestGenerator.java to change specifications of the requests.
___
change RandomCloudInformation2.java to change specifications of the cloud providers. it loads available IAASclouds.txt and ranks.txt files