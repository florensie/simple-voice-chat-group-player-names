package be.florens.svcgroupplayernames.mixin.client;

import be.florens.svcgroupplayernames.SimpleVoiceChatGroupPlayerNamesClient;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import de.maxhenkel.voicechat.voice.client.GroupChatManager;
import de.maxhenkel.voicechat.voice.common.PlayerState;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(value = GroupChatManager.class)
public class GroupChatManagerMixin {

    @ModifyExpressionValue(method = "renderIcons", at = @At(value = "INVOKE",
            target = "Lde/maxhenkel/voicechat/voice/client/GroupChatManager;getGroupMembers(Z)Ljava/util/List;"))
    private static List<PlayerState> hideInvisibleRows(List<PlayerState> players) {
        return players.stream()
                .filter(state -> SimpleVoiceChatGroupPlayerNamesClient.getNameOpacity(state) > 0
                        || SimpleVoiceChatGroupPlayerNamesClient.getIconOpacity(state) > 0)
                .toList();
    }

    @Definition(id = "blitSprite", method = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIIIIIII)V")
    @Definition(id = "GUI_TEXTURED", field = "Lnet/minecraft/client/renderer/RenderPipelines;GUI_TEXTURED:Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;")
    @Definition(id = "TALK_OUTLINE", field = "Lde/maxhenkel/voicechat/voice/client/GroupChatManager;TALK_OUTLINE:Lnet/minecraft/resources/Identifier;")
    @Expression("?.blitSprite(GUI_TEXTURED, TALK_OUTLINE, ?, ?, ?, ?, ?, ?, ?, ?)")
    @WrapOperation(method = "renderIcons", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
    private static void renderTalkingOutline(
            GuiGraphicsExtractor graphics, RenderPipeline pipeline, Identifier sprite,
            int textureWidth, int textureHeight, int u, int v, int x, int y, int width, int height,
            Operation<Void> original, @Local(name = "state") PlayerState state
    ) {
        int opacity = SimpleVoiceChatGroupPlayerNamesClient.getIconOpacity(state);
        if (opacity > 0) {
            graphics.blitSprite(pipeline, sprite, textureWidth, textureHeight, u, v, x, y, width, height,
                    SimpleVoiceChatGroupPlayerNamesClient.whiteWithAlpha(opacity));
        }
    }

    @Definition(id = "blitSprite", method = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V")
    @Definition(id = "GUI_TEXTURED", field = "Lnet/minecraft/client/renderer/RenderPipelines;GUI_TEXTURED:Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;")
    @Definition(id = "SPEAKER_OFF_ICON", field = "Lde/maxhenkel/voicechat/voice/client/GroupChatManager;SPEAKER_OFF_ICON:Lnet/minecraft/resources/Identifier;")
    @Expression("?.blitSprite(GUI_TEXTURED, SPEAKER_OFF_ICON, ?, ?, ?, ?)")
    @WrapOperation(method = "renderIcons", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
    private static void renderSpeakerOffIcon(
            GuiGraphicsExtractor graphics, RenderPipeline pipeline, Identifier sprite,
            int x, int y, int width, int height,
            Operation<Void> original, @Local(name = "state") PlayerState state
    ) {
        int opacity = SimpleVoiceChatGroupPlayerNamesClient.getIconOpacity(state);
        if (opacity > 0) {
            graphics.blitSprite(pipeline, sprite, x, y, width, height,
                    SimpleVoiceChatGroupPlayerNamesClient.whiteWithAlpha(opacity));
        }
    }

    @Definition(id = "blit", method = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blit(Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIFFIIII)V")
    @Definition(id = "GUI_TEXTURED", field = "Lnet/minecraft/client/renderer/RenderPipelines;GUI_TEXTURED:Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;")
    @Definition(id = "body", method = "Lnet/minecraft/world/entity/player/PlayerSkin;body()Lnet/minecraft/core/ClientAsset$Texture;")
    @Definition(id = "texturePath", method = "Lnet/minecraft/core/ClientAsset$Texture;texturePath()Lnet/minecraft/resources/Identifier;")
    @Expression("?.blit(GUI_TEXTURED, ?.body().texturePath(), ?, ?, 8.0, 8.0, ?, ?, ?, ?)")
    @WrapOperation(method = "renderIcons", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
    private static void renderHeadBaseLayer(
            GuiGraphicsExtractor graphics, RenderPipeline pipeline, Identifier texture,
            int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight,
            Operation<Void> original, @Local(name = "state") PlayerState state
    ) {
        renderSkinWithOpacity(graphics, pipeline, texture, x, y, u, v, width, height, textureWidth, textureHeight, state);
    }

    @Definition(id = "blit", method = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blit(Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIFFIIII)V")
    @Definition(id = "GUI_TEXTURED", field = "Lnet/minecraft/client/renderer/RenderPipelines;GUI_TEXTURED:Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;")
    @Definition(id = "body", method = "Lnet/minecraft/world/entity/player/PlayerSkin;body()Lnet/minecraft/core/ClientAsset$Texture;")
    @Definition(id = "texturePath", method = "Lnet/minecraft/core/ClientAsset$Texture;texturePath()Lnet/minecraft/resources/Identifier;")
    @Expression("?.blit(GUI_TEXTURED, ?.body().texturePath(), ?, ?, 40.0, 8.0, ?, ?, ?, ?)")
    @WrapOperation(method = "renderIcons", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
    private static void renderHeadOuterLayerAndName(
            GuiGraphicsExtractor graphics, RenderPipeline pipeline, Identifier texture,
            int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight,
            Operation<Void> original,
            @Local(name = "state") PlayerState state, @Local(name = "scale") float scale
    ) {
        renderSkinWithOpacity(graphics, pipeline, texture, x, y, u, v, width, height, textureWidth, textureHeight, state);
        SimpleVoiceChatGroupPlayerNamesClient.renderPlayerNames(graphics, x, y, width, height, state, scale);
    }

    @Unique
    private static void renderSkinWithOpacity(
            GuiGraphicsExtractor graphics, RenderPipeline pipeline, Identifier texture,
            int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight,
            PlayerState state
    ) {
        int opacity = SimpleVoiceChatGroupPlayerNamesClient.getIconOpacity(state);
        if (opacity > 0) {
            graphics.blit(pipeline, texture, x, y, u, v, width, height, textureWidth, textureHeight,
                    SimpleVoiceChatGroupPlayerNamesClient.whiteWithAlpha(opacity));
        }
    }
}
