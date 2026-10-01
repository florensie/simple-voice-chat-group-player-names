package be.florens.svcgroupplayernames.mixin.client;

import be.florens.svcgroupplayernames.config.AnimatedConfigTitle;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.shedaniel.clothconfig2.gui.ClothConfigScreen;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClothConfigScreen.class)
public class ClothConfigScreenMixin {
    @Definition(id = "centeredText", method = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;centeredText(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)V")
    @Definition(id = "title", field = "Lme/shedaniel/clothconfig2/gui/ClothConfigScreen;title:Lnet/minecraft/network/chat/Component;")
    @Expression("?.centeredText(?, this.title, ?, ?, ?)")
    @WrapOperation(method = "extractRenderState", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
    private void renderTitle(GuiGraphicsExtractor graphics, Font font, Component title, int x, int y, int color,
                             Operation<Void> original) {
        if (title.getContents() instanceof TranslatableContents translation
                && translation.getKey().equals("text.autoconfig.group-simple-voice-player-chat-names-enhanced-edition.title")) {
            AnimatedConfigTitle.render(graphics, font, title, x, y);
        } else {
            original.call(graphics, font, title, x, y, color);
        }
    }
}
