package me.kneesnap.CommandAliases;

import org.bukkit.Bukkit;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

/**
 * Good god what was I thinking.
 * Created sometime in 2016.
 */
public class Updater {
    private String pluginName;
    private String pluginFileName;
    private String currentVersion;

    private static final String UPDATE_ENDPOINT = "https://plugin-version-manager.herokuapp.com/plugins/";

    public Updater(CommandAliases plugin) {
        this.pluginName = plugin.getName();
        this.pluginFileName = plugin.getFile().getName();
        this.currentVersion = plugin.getDescription().getVersion();
    }

    public boolean checkForUpdate() {
        if (!isUpToDate()) {
            try {
                downloadUpdate();
                return true;
            } catch (IOException e) {
                System.out.println("Failed to download update! (" + e + ")");
            }
        }
        return false;
    }

    private static String httpRequest(String url) {
        try {
            URLConnection connection = new URL(url).openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            String response = in.readLine();
            in.close();
            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean isUpToDate() {
        final String versionURL = UPDATE_ENDPOINT + this.pluginName + "/checkVersion?version=" + this.currentVersion; // /version
        String apiResponse = httpRequest(versionURL);
        if (apiResponse == null)
            return true;

        try {
            return "true".equalsIgnoreCase(apiResponse); // This sucks. Too bad!
        } catch (Exception e) {
            // There was some kind of error, maybe fire-wall, maybe the update server goes down (again), either way we don't want to start spamming people, and if it can't be accessed,
        }
        return true;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void downloadUpdate() throws IOException {
        final String fileURL = UPDATE_ENDPOINT + this.pluginName + "/latest";

        URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();
        if (responseCode == 200) {
            InputStream inputStream = httpConn.getInputStream();

            if (!Bukkit.getUpdateFolderFile().exists())
                Bukkit.getUpdateFolderFile().mkdirs();
            FileOutputStream outputStream = new FileOutputStream(new File(Bukkit.getUpdateFolderFile(), this.pluginFileName));

            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = inputStream.read(buffer)) != -1)
                outputStream.write(buffer, 0, bytesRead);
            outputStream.close();
            inputStream.close();
        }
        httpConn.disconnect();
    }
}
