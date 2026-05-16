package de.greenman999.svcgroupplayernames;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public record GroupDisplayNamesPayload(Map<UUID, Text> displayNames) implements CustomPayload {
    public static final Id<GroupDisplayNamesPayload> ID = new Id<>(Identifier.of(SimpleVoiceChatGroupPlayerNames.MOD_ID, "group_display_names"));
    public static final PacketCodec<RegistryByteBuf, GroupDisplayNamesPayload> CODEC = PacketCodec.ofStatic(
            GroupDisplayNamesPayload::write,
            GroupDisplayNamesPayload::read
    );

    private static GroupDisplayNamesPayload read(RegistryByteBuf buf) {
        int size = buf.readVarInt();
        Map<UUID, Text> displayNames = new LinkedHashMap<>(size);
        for (int i = 0; i < size; i++) {
            UUID uuid = buf.readUuid();
            Text displayName = TextCodecs.UNLIMITED_REGISTRY_PACKET_CODEC.decode(buf);
            displayNames.put(uuid, displayName);
        }
        return new GroupDisplayNamesPayload(displayNames);
    }

    private static void write(RegistryByteBuf buf, GroupDisplayNamesPayload payload) {
        buf.writeVarInt(payload.displayNames.size());
        for (Map.Entry<UUID, Text> entry : payload.displayNames.entrySet()) {
            buf.writeUuid(entry.getKey());
            TextCodecs.UNLIMITED_REGISTRY_PACKET_CODEC.encode(buf, entry.getValue());
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
