# PCA (A Cloud Service Selector in the Peer Cloud Assisted Environments)
This is the simulation code based on [VMLCB](https://github.com/simsinak/VMLCB) algorithm. It has been used in the **Network and Application-Aware Cloud Service Selection in Peer-Assisted Environments** paper.
___
To use real RTT, write them in **rtt.txt**. the format is as follows:
-> *Location , min_rtt, max_rtt*
##*mid_rtt*
##*div_rtt*
Also, change LoadRTT.java to change the behavior of **rtt.txt** loading based on your needs.
___
To add another parameters, do the same and load them in **test.java**
___
The simulation starts from the main method in **test.java**.
Change number of **REQUESTS** and **PROVIDERS** in it by static fields.
*final static int REQUEST_NUMBER = 600; (in each round)
final static int NUMBER_PROVIDER =500;*
___
Change the **RequestGenerator.java** to change specifications of the requests.
___
Change **RandomCloudInformation2.java** to change specifications of the cloud providers. It loads **available IAASclouds.txt** and **ranks.txt** files.
___
#Class Diagram#
![PCA class diagram](http://www.axgig.com/images/28547218659415721727.png "PCA class diagram")
