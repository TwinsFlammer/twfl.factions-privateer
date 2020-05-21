package br.com.twinsflammer.factionsprivateer.mcmmo.util.blockmeta;

import br.com.twinsflammer.factionsprivateer.mcmmo.config.HiddenConfig;

public class ChunkletManagerFactory {

    public static ChunkletManager getChunkletManager() {
        HiddenConfig hConfig = HiddenConfig.getInstance();

        if (hConfig.getChunkletsEnabled()) {
            return new HashChunkletManager();
        }

        return new NullChunkletManager();
    }
}
