package de.greenman999.svcgroupplayernames;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public record GroupDisplayNamesPayload(Map<UUID, Component> displayNames) implements CustomPacketPayload {
    public static final Type<GroupDisplayNamesPayload> ID = new Type<>(Identifier.fromNamespaceAndPath(SimpleVoiceChatGroupPlayerNames.MOD_ID, "group_display_names"));
    public static final StreamCodec<RegistryFriendlyByteBuf, GroupDisplayNamesPayload> CODEC = CustomPacketPayload.codec(
            GroupDisplayNamesPayload::write,
            GroupDisplayNamesPayload::read
    );

    private static GroupDisplayNamesPayload read(RegistryFriendlyByteBuf buf) {
        int size = buf.readVarInt();
        Map<UUID, Component> displayNames = new LinkedHashMap<>(size);
        for (int i = 0; i < size; i++) {
            UUID uuid = buf.readUUID();
            Component displayName = ComponentSerialization.TRUSTED_STREAM_CODEC.decode(buf);
            displayNames.put(uuid, displayName);
        }
        return new GroupDisplayNamesPayload(displayNames);
    }

    private static void write(GroupDisplayNamesPayload payload, RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(payload.displayNames.size());
        for (Map.Entry<UUID, Component> entry : payload.displayNames.entrySet()) {
            buf.writeUUID(entry.getKey());
            ComponentSerialization.TRUSTED_STREAM_CODEC.encode(buf, entry.getValue());
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
