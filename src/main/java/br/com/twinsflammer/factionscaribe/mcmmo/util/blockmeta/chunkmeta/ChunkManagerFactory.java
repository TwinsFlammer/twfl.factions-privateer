package br.com.twinsflammer.factionscaribe.mcmmo.util.blockmeta.chunkmeta;

import br.com.twinsflammer.factionscaribe.mcmmo.config.HiddenConfig;

public class ChunkManagerFactory {

    public static ChunkManager getChunkManager() {
        HiddenConfig hConfig = HiddenConfig.getInstance();

        if (hConfig.getChunkletsEnabled()) {
            return new HashChunkManager();
        }

        return new NullChunkManager();
    }
}
