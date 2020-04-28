package com.redefocus.factionscaribe.mcmmo.util.blockmeta;

import com.redefocus.factionscaribe.mcmmo.config.HiddenConfig;

public class ChunkletManagerFactory {

    public static ChunkletManager getChunkletManager() {
        HiddenConfig hConfig = HiddenConfig.getInstance();

        if (hConfig.getChunkletsEnabled()) {
            return new HashChunkletManager();
        }

        return new NullChunkletManager();
    }
}
