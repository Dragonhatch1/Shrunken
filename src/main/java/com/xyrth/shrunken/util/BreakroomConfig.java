package com.xyrth.shrunken.util;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class BreakroomConfig {

    public static int minX = 0, maxX = 0;
    public static int minY = 0, maxY = 0;
    public static int minZ = 0, maxZ = 0;

    private static Configuration config;

    public static void init(File configFile) {
        config = new Configuration(configFile);
        load();
    }

    public static void load() {
        config.load();

        minX = config.get("breakroom", "minX", minX)
            .getInt();
        maxX = config.get("breakroom", "maxX", maxX)
            .getInt();
        minY = config.get("breakroom", "minY", minY)
            .getInt();
        maxY = config.get("breakroom", "maxY", maxY)
            .getInt();
        minZ = config.get("breakroom", "minZ", minZ)
            .getInt();
        maxZ = config.get("breakroom", "maxZ", maxZ)
            .getInt();

        if (config.hasChanged()) config.save();
    }

    public static void save() {

        config.get("breakroom", "minX", minX)
            .set(minX);
        config.get("breakroom", "maxX", maxX)
            .set(maxX);
        config.get("breakroom", "minY", minY)
            .set(minY);
        config.get("breakroom", "maxY", maxY)
            .set(maxY);
        config.get("breakroom", "minZ", minZ)
            .set(minZ);
        config.get("breakroom", "maxZ", maxZ)
            .set(maxZ);
        config.save();
    }
}
