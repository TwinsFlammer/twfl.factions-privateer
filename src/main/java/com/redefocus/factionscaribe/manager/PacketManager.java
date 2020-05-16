package com.redefocus.factionscaribe.manager;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;

public class PacketManager {
    public static void registerPackets(PacketAdapter... packetAdapters) {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

        for (PacketAdapter packetAdapter : packetAdapters)
            protocolManager.addPacketListener(packetAdapter);
    }
}
