package cn.dioxide.extension;

import cn.dioxide.HoverLib;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * @author Dioxide.CN
 * @date 2023/6/19
 * @since 1.0
 */
public class Config {

    // The static instance of the config.
    private static Config config;
    // The list of config filenames.
    private static final String[] filenames = { "config.yml" };
    // The list of files.
    private static File[] files;
    // The list of configs.
    private static YamlConfiguration[] configs;

    public final int version;
    public final Feature feature;
    public final Display display;

    public static void init() {
        try {
            initializeConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initializeConfig() {
        files = new File[filenames.length];
        configs = new YamlConfiguration[filenames.length];
        config = new Config();
    }

    private Config() {
        HoverLib instance = HoverLib.getInstance();
        for (String filename : filenames) {
            if (!new File(instance.getDataFolder(), filename).exists()) {
                instance.saveResource(filename, false);
            }
        }

        for (int i = 0; i < filenames.length; i++) {
            File file = new File(instance.getDataFolder(), filenames[i]);
            if (!file.exists()) {
                instance.getLogger().info("Failed to load config file: " + filenames[i]);
                continue;
            }
            files[i] = file;
            configs[i] = YamlConfiguration.loadConfiguration(files[i]);
        }

        // configuration
        this.version = configs[0].getInt("version", 1);

        this.feature = new Feature();
        this.feature.protectTerrain = configs[0].getBoolean("feature.protect-terrain", true);
        this.feature.craftingTable = configs[0].getBoolean("feature.crafting-table", true);
        this.feature.ironGolem = configs[0].getBoolean("feature.iron-golem", true);
        this.feature.stupidVillager = configs[0].getBoolean("feature.stupid-villager", true);
        this.feature.trimUpgrade = configs[0].getBoolean("feature.trim-upgrade", true);

        this.display = new Display();
        this.display.item.placeRadius = configs[0].getInt("display.item.place-radius", 3);
        this.display.item.recycleRadius = configs[0].getInt("display.item.recycle-radius", 2);
        this.display.item.consume = configs[0].getBoolean("display.item.consume", true);

        this.display.block.placeRadius = configs[0].getInt("display.block.place-radius", 3);
        this.display.block.recycleRadius = configs[0].getInt("display.block.recycle-radius", 2);
        this.display.block.consume = configs[0].getBoolean("display.block.consume", true);

        this.display.book.consume = configs[0].getBoolean("display.book.consume", true);
    }

    @Getter
    public static class Feature {
        private boolean protectTerrain;
        private boolean craftingTable;
        private boolean ironGolem;
        private boolean stupidVillager;
        private boolean trimUpgrade;
    }

    public static class Display {
        public final Item item = new Item();
        public final Block block = new Block();
        public final Book book = new Book();

        @Getter
        public static class Item {
            private int placeRadius;
            private int recycleRadius;
            private boolean consume;
        }

        @Getter
        public static class Block {
            private int placeRadius;
            private int recycleRadius;
            private boolean consume;
        }

        @Getter
        public static class Book {
            private boolean consume;
        }
    }

    public static Config get() {
        return config;
    }

}
