package de.greenman999.svcgroupplayernames.mixin.client;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import de.greenman999.svcgroupplayernames.SimpleVoiceChatGroupPlayerNamesClient;
import de.maxhenkel.voicechat.voice.client.GroupChatManager;
import de.maxhenkel.voicechat.voice.common.PlayerState;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = GroupChatManager.class)
public class GroupChatManagerMixin {

    @Definition(id = "blit", method = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blit(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIFFIIII)V")
    @Definition(id = "GUI_TEXTURED", field = "Lnet/minecraft/client/renderer/RenderPipelines;GUI_TEXTURED:Lcom/mojang/blaze3d/pipeline/RenderPipeline;")
    @Definition(id = "body", method = "Lnet/minecraft/world/entity/player/PlayerSkin;body()Lnet/minecraft/core/ClientAsset$Texture;")
    @Definition(id = "texturePath", method = "Lnet/minecraft/core/ClientAsset$Texture;texturePath()Lnet/minecraft/resources/Identifier;")
    @Expression("?.blit(GUI_TEXTURED, ?.body().texturePath(), ?, ?, ?, ?, ?, ?, ?, ?)")
    @WrapOperation(
            method = "renderIcons",
            at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 1)
    )
    private static void renderPlayerNames(
            GuiGraphicsExtractor guiGraphics,
            RenderPipeline renderPipeline,
            Identifier texture,
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
            @Local(name = "scale") float scale
    ) {
        original.call(guiGraphics, renderPipeline, texture, x, y, u, v, width, height, textureWidth, textureHeight);

        SimpleVoiceChatGroupPlayerNamesClient.renderPlayerNames(guiGraphics, x, y, width, height, state, scale);
    }
}
