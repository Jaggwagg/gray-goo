package jaggwagg.gray_goo.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jaggwagg.gray_goo.GrayGoo;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class GrayGooConfig {
    private static final String CONFIG_PATH = System.getProperty("user.dir") + File.separator + "/config/gray_goo.json";
    private final int empRadius;
    private final boolean allowGooSpread;

    public GrayGooConfig() {
        this.empRadius = 64;
        this.allowGooSpread = true;
    }

    public int getEmpRadius() {
        return this.empRadius;
    }

    public boolean getAllowGooSpread() {
        return this.allowGooSpread;
    }

    public static GrayGooConfig getConfig() {
        GrayGooConfig config = new GrayGooConfig();
        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        new File(System.getProperty("user.dir") + File.separator + "/config").mkdir();

        File configFile = new File(CONFIG_PATH);

        try {
            if (configFile.createNewFile()) {
                FileWriter writer = new FileWriter(configFile);
                config = new GrayGooConfig();

                writer.write(gson.toJson(config));
                writer.close();

                GrayGoo.LOGGER.warn("Created a new config file because it could not be found.");
            } else {
                String json = Files.readString(Path.of(CONFIG_PATH));

                config = gson.fromJson(json, GrayGooConfig.class);

                GrayGoo.LOGGER.info("Successfully read config file.");
            }
        } catch (IOException e) {
            GrayGoo.LOGGER.error("Could not read or create config file: " + e.getMessage());
        }

        return config;
    }
}
