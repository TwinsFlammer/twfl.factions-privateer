package br.com.twinsflammer.factionscaribe.mcmmo.util.blockmeta;

import br.com.twinsflammer.factionscaribe.mcmmo.config.HiddenConfig;

public class ChunkletManagerFactory {

    public static ChunkletManager getChunkletManager() {
        HiddenConfig hConfig = HiddenConfig.getInstance();

        if (hConfig.getChunkletsEnabled()) {
            return new HashChunkletManager();
        }

        return new NullChunkletManager();
    }
}
