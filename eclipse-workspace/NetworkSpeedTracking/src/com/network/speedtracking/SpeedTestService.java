package com.network.speedtracking;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SpeedTestService {

    public static SpeedTestResult runSpeedTest() throws Exception {
        // Run Ookla speedtest CLI command
        ProcessBuilder pb = new ProcessBuilder(
                "speedtest",
                "--accept-license",
                "--accept-gdpr",
                "-f", "json"
        );

        pb.redirectErrorStream(true);
        Process process = pb.start();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
        );

        StringBuilder jsonOutput = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonOutput.append(line);
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Speedtest CLI failed with exit code " + exitCode);
        }

        JSONObject json = new JSONObject(jsonOutput.toString());

        double ping = json.getJSONObject("ping").getDouble("latency");
        double downloadMbps = json.getJSONObject("download").getDouble("bandwidth") * 8 / 1_000_000.0;
        double uploadMbps = json.getJSONObject("upload").getDouble("bandwidth") * 8 / 1_000_000.0;

        return new SpeedTestResult(ping, downloadMbps, uploadMbps);
    }
}

