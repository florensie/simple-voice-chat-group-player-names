package de.greenman999.svcgroupplayernames;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class SimpleVoiceChatGroupPlayerNames implements ModInitializer {
    public static final String MOD_ID = "simple-voice-chat-group-player-names";

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playS2C().register(GroupDisplayNamesPayload.ID, GroupDisplayNamesPayload.CODEC);
        ServerTickEvents.END_SERVER_TICK.register(ServerDisplayNameSync::tick);
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
                ServerDisplayNameSync.sendTo(handler.getPlayer()));
    }
}
