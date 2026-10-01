package be.florens.svcgroupplayernames.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import de.maxhenkel.voicechat.integration.clothconfig.ClothConfigIntegration;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.gui.AbstractConfigScreen;
import me.shedaniel.clothconfig2.gui.entries.IntegerSliderEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ModMenuIntegration::createConfigScreen;
    }

    private static Screen createConfigScreen(Screen parent) {
        var holder = AutoConfig.getConfigHolder(ModConfig.class);
        var builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("text.autoconfig.group-simple-voice-player-chat-names-enhanced-edition.title"));
        addOpacityOptions(builder, holder.getConfig());
        builder.setSavingRunnable(holder::save);
        addSvcOptionsCategory(builder, parent);
        return builder.build();
    }

    private static void addOpacityOptions(ConfigBuilder builder, ModConfig config) {
        var category = builder.getOrCreateCategory(Component.translatable(
                "text.autoconfig.group-simple-voice-player-chat-names-enhanced-edition.category.default"));
        var defaults = new ModConfig();

        var sliders = new OpacityEntries(
                createOpacitySlider(builder, "opacityWhenTalking", config.opacityWhenTalking,
                        defaults.opacityWhenTalking, value -> config.opacityWhenTalking = value),
                createOpacitySlider(builder, "opacityWhenNotTalking", config.opacityWhenNotTalking,
                        defaults.opacityWhenNotTalking, value -> config.opacityWhenNotTalking = value),
                createOpacitySlider(builder, "iconOpacityWhenTalking", config.iconOpacityWhenTalking,
                        defaults.iconOpacityWhenTalking, value -> config.iconOpacityWhenTalking = value),
                createOpacitySlider(builder, "iconOpacityWhenNotTalking", config.iconOpacityWhenNotTalking,
                        defaults.iconOpacityWhenNotTalking, value -> config.iconOpacityWhenNotTalking = value));

        category.addEntry(new PresetSelectorEntry(sliders));
        sliders.entries().forEach(category::addEntry);
    }

    private static IntegerSliderEntry createOpacitySlider(ConfigBuilder builder, String field, int value,
                                                          int defaultValue, Consumer<Integer> save) {
        String key = "text.autoconfig.group-simple-voice-player-chat-names-enhanced-edition.option." + field;
        return builder.entryBuilder().startIntSlider(Component.translatable(key), value, 0, 100)
                .setDefaultValue(defaultValue)
                .setTooltip(Component.translatable(key + ".@Tooltip"))
                .setSaveConsumer(save)
                .build();
    }

    private static void addSvcOptionsCategory(ConfigBuilder builder, Screen parent) {
        var voiceChatScreen = (AbstractConfigScreen) ClothConfigIntegration.createConfigScreen(parent);
        var groupHud = builder.getOrCreateCategory(Component.translatable("text.autoconfig.group-simple-voice-player-chat-names-enhanced-edition.category.group_hud"));

        addSvcOptionsWarning(groupHud, builder);
        yoinkSvcGroupHudEntries(groupHud, voiceChatScreen);
    }

    private static void addSvcOptionsWarning(ConfigCategory groupHud, ConfigBuilder builder) {
        groupHud.addEntry(builder.entryBuilder().startTextDescription(Component.translatable(
                        "text.autoconfig.group-simple-voice-player-chat-names-enhanced-edition.category.group_hud.description"))
                .setColor(0xFFFFFF55).build());
    }

    private static void yoinkSvcGroupHudEntries(ConfigCategory groupHud, AbstractConfigScreen voiceChatScreen) {
        var hudEntries = voiceChatScreen.getCategorizedEntries()
                .get(Component.translatable("cloth_config.voicechat.category.hud_icons"));

        for (var entry : hudEntries) {
            if (entry instanceof AbstractConfigListEntry<?> listEntry && isSvcGroupHudOption(entry.getFieldName())) {
                groupHud.addEntry(listEntry);
            }
        }
    }

    private static boolean isSvcGroupHudOption(Component fieldName) {
        if (!(fieldName.getContents() instanceof TranslatableContents translation)) {
            return false;
        }
        String key = translation.getKey();
        return key.startsWith("cloth_config.voicechat.config.group_") || key.equals("cloth_config.voicechat.config.show_own_group_icon");
    }
}
