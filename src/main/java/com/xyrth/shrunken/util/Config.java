package com.xyrth.shrunken.util;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class Config {

    public static float scale = 1.0F;

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);

        scale = (float) configuration.getFloat(
            "scale",
            Configuration.CATEGORY_GENERAL,
            scale,
            0.10F,
            15.0F,
            "Player Scale. 1.0 = Normal, 0.5 = Half Sized | Above certain amounts the world starts to load slow. ");

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
