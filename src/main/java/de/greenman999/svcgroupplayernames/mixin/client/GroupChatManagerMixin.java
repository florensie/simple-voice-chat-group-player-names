package de.greenman999.svcgroupplayernames.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import de.greenman999.svcgroupplayernames.SimpleVoiceChatGroupPlayerNamesClient;
import de.maxhenkel.voicechat.voice.client.ClientVoicechat;
import de.maxhenkel.voicechat.voice.client.GroupChatManager;
import de.maxhenkel.voicechat.voice.common.PlayerState;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = GroupChatManager.class)
public class GroupChatManagerMixin {

    @WrapOperation(
            method = "renderIcons",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIFFIIII)V",
                    ordinal = 1
            )
    )
    private static void renderPlayerNames(DrawContext drawContext,
                                          Identifier sprite,
                                          int x,
                                          int y,
                                          float u,
                                          float v,
                                          int width,
                                          int height,
                                          int textureWidth,
                                          int textureHeight,
                                          Operation<Void> original,
                                          @Local(name = "state") PlayerState state,
                                          @Local(name = "scale") float scale,
                                          @Local(name = "client") ClientVoicechat client) {
        original.call(drawContext, sprite, x, y, u, v, width, height, textureWidth, textureHeight);

        SimpleVoiceChatGroupPlayerNamesClient.renderPlayerNames(drawContext, x, y, width, height, state, scale, client);
    }
}
