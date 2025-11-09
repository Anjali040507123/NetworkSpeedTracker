package models;


import java.time.LocalDateTime;

public class SpeedRecord {
    private int id;
    private double ping;
    private double download;
    private double upload;
    private LocalDateTime timestamp;

    public SpeedRecord(int id, double ping, double download, double upload, LocalDateTime timestamp) {
        this.id = id;
        this.ping = ping;
        this.download = download;
        this.upload = upload;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public double getPing() {
        return ping;
    }

    public double getDownload() {
        return download;
    }

    public double getUpload() {
        return upload;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
