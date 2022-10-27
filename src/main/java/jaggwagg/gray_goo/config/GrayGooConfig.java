package jaggwagg.gray_goo.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import jaggwagg.gray_goo.GrayGoo;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.Registry;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GrayGooConfig {
    private static final String CONFIG_PATH = System.getProperty("user.dir") + File.separator + "/config/gray_goo.json";
    private final int empRadius;
    private final boolean allowGooSpread;
    private final ArrayList<String> biologicalBlocks;
    private final ArrayList<String> fluidBlocks;
    private final ArrayList<String> solidBlocks;

    private static GrayGooConfig createNewDefaultConfig(File configFile, Gson gson) throws IOException {
        FileWriter writer = new FileWriter(configFile);
        GrayGooConfig config = new GrayGooConfig();

        writer.write(gson.toJson(config));
        writer.close();

        return config;
    }

    public GrayGooConfig() {
        this.empRadius = 64;
        this.allowGooSpread = true;
        this.biologicalBlocks = new ArrayList<>();
        this.fluidBlocks = new ArrayList<>();
        this.solidBlocks = new ArrayList<>();

        ArrayList<Block> biologicalBlocks = new ArrayList<>(List.of(Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.MUD, Blocks.FARMLAND,
                Blocks.OAK_LOG, Blocks.ACACIA_LOG, Blocks.BIRCH_LOG, Blocks.DARK_OAK_LOG, Blocks.JUNGLE_LOG, Blocks.MANGROVE_LOG,
                Blocks.SPRUCE_LOG, Blocks.STRIPPED_OAK_LOG, Blocks.STRIPPED_ACACIA_LOG, Blocks.STRIPPED_BIRCH_LOG,
                Blocks.STRIPPED_DARK_OAK_LOG, Blocks.STRIPPED_JUNGLE_LOG, Blocks.STRIPPED_MANGROVE_LOG, Blocks.STRIPPED_SPRUCE_LOG,
                Blocks.OAK_LEAVES, Blocks.ACACIA_LEAVES, Blocks.BIRCH_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.JUNGLE_LEAVES,
                Blocks.MANGROVE_LEAVES, Blocks.SPRUCE_LEAVES, Blocks.OAK_STAIRS, Blocks.ACACIA_STAIRS, Blocks.BIRCH_STAIRS,
                Blocks.DARK_OAK_STAIRS, Blocks.JUNGLE_STAIRS, Blocks.MANGROVE_STAIRS, Blocks.SPRUCE_STAIRS
        ));

        ArrayList<Block> fluidBlocks = new ArrayList<>(List.of(Blocks.WATER, Blocks.LAVA, Blocks.KELP, Blocks.KELP_PLANT,
                Blocks.SEAGRASS, Blocks.TALL_SEAGRASS, Blocks.SEA_PICKLE, Blocks.BUBBLE_COLUMN
        ));

        ArrayList<Block> solidBlocks = new ArrayList<>(List.of(Blocks.SANDSTONE, Blocks.SAND, Blocks.GRAVEL, Blocks.STONE,
                Blocks.ANDESITE, Blocks.DIORITE, Blocks.GRANITE, Blocks.DEEPSLATE, Blocks.NETHERRACK, Blocks.BLACKSTONE,
                Blocks.BASALT, Blocks.TUFF, Blocks.COPPER_ORE, Blocks.COAL_ORE, Blocks.IRON_ORE, Blocks.GOLD_ORE, Blocks.DIAMOND_ORE,
                Blocks.REDSTONE_ORE, Blocks.EMERALD_ORE, Blocks.LAPIS_ORE, Blocks.ANCIENT_DEBRIS, Blocks.DEEPSLATE_COPPER_ORE,
                Blocks.DEEPSLATE_COAL_ORE, Blocks.DEEPSLATE_IRON_ORE, Blocks.DEEPSLATE_GOLD_ORE, Blocks.DEEPSLATE_DIAMOND_ORE,
                Blocks.DEEPSLATE_REDSTONE_ORE, Blocks.DEEPSLATE_EMERALD_ORE, Blocks.DEEPSLATE_LAPIS_ORE, Blocks.COBBLESTONE,
                Blocks.COBBLED_DEEPSLATE
        ));

        biologicalBlocks.forEach(value -> this.biologicalBlocks.add(Registry.BLOCK.getId(value).toString()));
        fluidBlocks.forEach(value -> this.fluidBlocks.add(Registry.BLOCK.getId(value).toString()));
        solidBlocks.forEach(value -> this.solidBlocks.add(Registry.BLOCK.getId(value).toString()));
    }

    public int getEmpRadius() {
        return this.empRadius;
    }

    public boolean getAllowGooSpread() {
        return this.allowGooSpread;
    }

    public ArrayList<String> getBiologicalBlocks() {
        return this.biologicalBlocks;
    }

    public ArrayList<String> getFluidBlocks() {
        return this.fluidBlocks;
    }

    public ArrayList<String> getSolidBlocks() {
        return this.solidBlocks;
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
                config = createNewDefaultConfig(configFile, gson);
                GrayGoo.LOGGER.warn("Created a new config file because it could not be found");
            } else {
                String json = Files.readString(Path.of(CONFIG_PATH));

                try {
                    config = gson.fromJson(json, GrayGooConfig.class);
                } catch(JsonSyntaxException e) {
                    config = createNewDefaultConfig(configFile, gson);
                    GrayGoo.LOGGER.warn("Invalid JSON in config file, overwriting with default config");
                }

                if (config == null) {
                    config = createNewDefaultConfig(configFile, gson);
                    GrayGoo.LOGGER.warn("Could not parse current config file, created a new one");
                }

                GrayGoo.LOGGER.info("Successfully read config file");
            }
        } catch (IOException e) {
            GrayGoo.LOGGER.error("Could not read or create config file: " + e.getMessage());
        }

        return config;
    }
}
