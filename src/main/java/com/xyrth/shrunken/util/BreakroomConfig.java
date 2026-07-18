package com.xyrth.shrunken.util;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class BreakroomConfig {

    public static double minX = 0, maxX = 0;
    public static double minY = 0, maxY = 0;
    public static double minZ = 0, maxZ = 0;

    private static Configuration config;

    public static void init(File configFile){
        config = new Configuration(configFile);
        load();
    }

    public static void load(){
        config.load();

        minX = config.get("breakroom", "minX", minX).getDouble();
        maxX = config.get("breakroom", "maxX", maxX).getDouble();
        minY = config.get("breakroom", "minY", minY).getDouble();
        maxY = config.get("breakroom", "maxY", maxY).getDouble();
        minZ = config.get("breakroom", "minZ", minZ).getDouble();
        maxZ = config.get("breakroom", "maxZ", maxZ).getDouble();

        if (config.hasChanged()) config.save();
    }

    public static void save(){

        config.get("breakroom", "minX", minX).set(minX);
        config.get("breakroom", "maxX", maxX).set(maxX);
        config.get("breakroom", "minY", minY).set(minY);
        config.get("breakroom", "maxY", maxY).set(maxY);
        config.get("breakroom", "minZ", minZ).set(minZ);
        config.get("breakroom", "maxZ", maxZ).set(maxZ);
        config.save();
    }
}
