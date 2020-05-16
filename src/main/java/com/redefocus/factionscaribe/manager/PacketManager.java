package com.redefocus.factionscaribe.manager;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;

public class PacketManager {
    private static ProtocolManager protocolManager;

    public PacketManager() {
        PacketManager.protocolManager = ProtocolLibrary.getProtocolManager();
    }

    public static void registerPackets(PacketAdapter... packetAdapters) {
        for (PacketAdapter packetAdapter : packetAdapters)
            PacketManager.protocolManager.addPacketListener(packetAdapter);
    }
}
