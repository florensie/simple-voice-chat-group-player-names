package de.greenman999.svcgroupplayernames;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ServerDisplayNameSync {
    private static Map<UUID, Component> displayNamesSnapshot = Map.of();

    public static void tick(MinecraftServer server) {
        Map<UUID, Component> displayNames = getDisplayNames(server);
        if (displayNames.equals(displayNamesSnapshot)) {
            return;
        }

        displayNamesSnapshot = displayNames;
        sendToAll(server);
    }

    public static void sendTo(ServerPlayer player) {
        if (ServerPlayNetworking.canSend(player, GroupDisplayNamesPayload.ID)) {
            ServerPlayNetworking.send(player, new GroupDisplayNamesPayload(displayNamesSnapshot));
        }
    }

    private static void sendToAll(MinecraftServer server) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            sendTo(player);
        }
    }

    private static Map<UUID, Component> getDisplayNames(MinecraftServer server) {
        Map<UUID, Component> displayNames = new HashMap<>();
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            displayNames.put(player.getUUID(), getDisplayName(player));
        }
        return Map.copyOf(displayNames);
    }

    private static Component getDisplayName(ServerPlayer player) {
        Component displayName = player.getDisplayName();
        if (displayName != null) {
            return displayName.copy();
        }

        return player.getName();
    }
}
