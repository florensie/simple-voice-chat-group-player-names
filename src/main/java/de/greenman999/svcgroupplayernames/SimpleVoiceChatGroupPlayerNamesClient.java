package de.greenman999.svcgroupplayernames;

import de.maxhenkel.voicechat.VoicechatClient;
import de.maxhenkel.voicechat.voice.client.ClientManager;
import de.maxhenkel.voicechat.voice.client.ClientVoicechat;
import de.maxhenkel.voicechat.voice.client.GroupPlayerIconOrientation;
import de.maxhenkel.voicechat.voice.common.PlayerState;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleVoiceChatGroupPlayerNamesClient implements ClientModInitializer {
    private static final Map<UUID, Component> DISPLAY_NAMES = new ConcurrentHashMap<>();

    @Override
    public void onInitializeClient() {
        AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);
        //noinspection resource
        ClientPlayNetworking.registerGlobalReceiver(GroupDisplayNamesPayload.ID, (payload, context) -> context.client().execute(() -> {
            DISPLAY_NAMES.clear();
            DISPLAY_NAMES.putAll(payload.displayNames());
        }));
    }

    public static void renderPlayerNames(
            GuiGraphicsExtractor guiGraphics,
            int x,
            int y,
            int width,
            int height,
            PlayerState state,
            float scale
    ) {
        Minecraft minecraftClient = Minecraft.getInstance();
        Font font = minecraftClient.font;
        ClientVoicechat client = ClientManager.getClient();
        if (client == null) return;

        Component playerName = getDisplayName(state);
        int playerNameWidth = font.width(playerName);

        guiGraphics.pose().pushMatrix();
        float invScale = 1.0f / scale;
        guiGraphics.pose().scale(invScale, invScale);

        int nameOffsetX = (int) (x + (width * scale) + (scale - 1) + 4 + scale - 1);
        int nameOffsetY = (int) ((y + scale - 1) + ((height * scale) / 2) - (float) (7 / 2) - 1);

        int hudX = VoicechatClient.CLIENT_CONFIG.groupPlayerIconPosX.get();
        int hudY = VoicechatClient.CLIENT_CONFIG.groupPlayerIconPosY.get();
        boolean horizontal = VoicechatClient.CLIENT_CONFIG.groupPlayerIconOrientation.get().equals(GroupPlayerIconOrientation.HORIZONTAL);
        if (horizontal) {
            guiGraphics.pose().rotate((float) (Math.PI / 2));
            if (hudX < 0 && hudY < 0) {
                nameOffsetX = (int) (-playerNameWidth - (height * scale) - (scale - 1) - 4 - (scale - 1));
                nameOffsetY = (int) (scale + (width * scale) / 2 - (float) (7 / 2) - 1);
            } else if (hudX < 0) {
                nameOffsetX = (int) ((int) (height * scale) + (scale - 1) + 4 + (scale - 1));
            } else if (hudY < 0) {
                nameOffsetY = (int) (y - (width * scale) + 7 + (2 - scale) + (width * scale) / 2 - (float) (7 / 2) - 1);
                nameOffsetX = (int) (-playerNameWidth - (height * scale) - (scale - 1) - 4 - (scale - 1));
            } else {
                nameOffsetY = (int) (y - (width * scale) - scale + (width * scale) / 2 - (float) (7 / 2) - 1);
            }
        } else {
            if (hudX < 0) {
                nameOffsetX = (int) (-playerNameWidth - (width * scale) - (scale - 1) - 4 - (scale - 1));
            }
            if (hudY < 0) {
                nameOffsetY = (int) (y - (width * scale) + 7 + (2 - scale) + ((height * scale) / 2) - (float) (7 / 2) - 1);
            }
        }

        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        int transparencyWhenTalking = whiteWithAlpha(config.transparencyWhenTalking);
        int transparencyWhenNotTalking = whiteWithAlpha(config.transparencyWhenNotTalking);
        if (config.onlyShowNamesWhenTalking && !client.getTalkCache().isTalking(state.getUuid())) {
            guiGraphics.pose().popMatrix();
            return;
        }
        guiGraphics.text(font, playerName, nameOffsetX, nameOffsetY, client.getTalkCache().isTalking(state.getUuid()) ? transparencyWhenTalking : transparencyWhenNotTalking, false);
        guiGraphics.pose().popMatrix();
    }

    private static Component getDisplayName(PlayerState state) {
        Component displayName = DISPLAY_NAMES.get(state.getUuid());
        if (displayName != null) {
            return displayName;
        }
        return Component.literal(state.getName());
    }

    public static int whiteWithAlpha(int percent) {
        percent = Math.clamp(percent, 0, 100);
        int alpha = Math.round(percent / 100.0f * 255.0f);
        alpha = Math.clamp(alpha, 0, 255);
        return ((alpha & 0xFF) << 24) | 0x00FFFFFF;
    }

}
