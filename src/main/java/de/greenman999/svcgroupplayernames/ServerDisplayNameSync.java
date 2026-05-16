package de.greenman999.svcgroupplayernames;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ServerDisplayNameSync {
    private static Map<UUID, Text> displayNamesSnapshot = Map.of();

    public static void tick(MinecraftServer server) {
        Map<UUID, Text> displayNames = getDisplayNames(server);
        if (displayNames.equals(displayNamesSnapshot)) {
            return;
        }

        displayNamesSnapshot = displayNames;
        sendToAll(server);
    }

    public static void sendTo(ServerPlayerEntity player) {
        if (ServerPlayNetworking.canSend(player, GroupDisplayNamesPayload.ID)) {
            ServerPlayNetworking.send(player, new GroupDisplayNamesPayload(displayNamesSnapshot));
        }
    }

    private static void sendToAll(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            sendTo(player);
        }
    }

    private static Map<UUID, Text> getDisplayNames(MinecraftServer server) {
        Map<UUID, Text> displayNames = new HashMap<>();
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            displayNames.put(player.getUuid(), getDisplayName(player));
        }
        return Map.copyOf(displayNames);
    }

    private static Text getDisplayName(ServerPlayerEntity player) {
        Text displayName = player.getDisplayName();
        if (displayName != null) {
            return displayName.copy();
        }

        return player.getName();
    }
}
