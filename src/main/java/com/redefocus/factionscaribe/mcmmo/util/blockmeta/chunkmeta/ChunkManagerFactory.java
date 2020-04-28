package com.redefocus.factionscaribe.mcmmo.util.blockmeta.chunkmeta;

import com.redefocus.factionscaribe.mcmmo.config.HiddenConfig;

public class ChunkManagerFactory {

    public static ChunkManager getChunkManager() {
        HiddenConfig hConfig = HiddenConfig.getInstance();

        if (hConfig.getChunkletsEnabled()) {
            return new HashChunkManager();
        }

        return new NullChunkManager();
    }
}
