package com.xyrth.shrunken.client;

public class ShrunkenState {

    // TODO move to Config File
    private static float scale = 5.0F; // 1.0 normal size | 1.5 50% bigger
    private static float defaultHeight = 1.8F;

    public static float getEyeOffset() {
        return defaultHeight * (1.0F - scale);
    }

    public static float getScale() {
        return scale;
    }
}
