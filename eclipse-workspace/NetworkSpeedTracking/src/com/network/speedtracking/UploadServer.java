package com.network.speedtracking;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class UploadServer {

    private static final int PORT = 5001;

    public static void main(String[] args) {
        System.out.println("🟢 Upload server started on port " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("📥 Client connected: " + clientSocket.getInetAddress());

                try (InputStream in = clientSocket.getInputStream()) {
                    byte[] buffer = new byte[8192]; // 8 KB
                    long totalBytes = 0;
                    long startTime = System.nanoTime();

                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        totalBytes += bytesRead;
                    }

                    long endTime = System.nanoTime();
                    double timeSeconds = (endTime - startTime) / 1_000_000_000.0;
                    double bits = totalBytes * 8;
                    double speedMbps = (bits / timeSeconds) / 1_000_000.0;

                    System.out.printf("✅ Received %.2f MB in %.2f s (%.2f Mbps)%n",
                            totalBytes / (1024.0 * 1024.0), timeSeconds, speedMbps);
                } catch (IOException e) {
                    System.out.println("❌ Error handling client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Server error: " + e.getMessage());
        }
    }
}

