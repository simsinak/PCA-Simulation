/**
 * Created by sinaaskarnejad on 4/4/16.
 */
public class ISP extends Node2 {
    private double uploadCost;
    int id;
    public long remainingTraffic;
    public ISP(int id){
        this.id = id;
//        downloadCost = uploadCost = Math.random();
        if (id==1)
        downloadCost = uploadCost = 0.05;
        else
            downloadCost = uploadCost = 0.07;
    }

    public double getUploadCost() {
        return uploadCost;
    }

    public void setUploadCost(double uploadCost) {
        this.uploadCost = uploadCost;
    }

    public double getDownloadCost() {
        return downloadCost;
    }

    public void setDownloadCost(double downloadCost) {
        this.downloadCost = downloadCost;
    }
}
