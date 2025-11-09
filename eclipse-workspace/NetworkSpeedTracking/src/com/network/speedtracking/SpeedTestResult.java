package com.network.speedtracking;

public class SpeedTestResult {
    private double ping;
    private double download;
    private double upload;

    public SpeedTestResult(double ping, double download, double upload) {
        this.ping = ping;
        this.download = download;
        this.upload = upload;
    }

    public double getPing() { return ping; }
    public double getDownload() { return download; }
    public double getUpload() { return upload; }
}
