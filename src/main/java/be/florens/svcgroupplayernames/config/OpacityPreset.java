package be.florens.svcgroupplayernames.config;

import net.minecraft.network.chat.Component;

import java.util.Locale;

public enum OpacityPreset {
    CUSTOM(0, 0, 0, 0),
    ALWAYS_VISIBLE(100, 100, 100, 100),
    FADE_SILENT(100, 50, 100, 50),
    ONLY_TALKING(100, 0, 100, 0),
    ICONS_ONLY(0, 0, 100, 100);

    private final int nameOpacityWhenTalking;
    private final int nameOpacityWhenNotTalking;
    private final int iconOpacityWhenTalking;
    private final int iconOpacityWhenNotTalking;

    OpacityPreset(int nameOpacityWhenTalking, int nameOpacityWhenNotTalking, int iconOpacityWhenTalking, int iconOpacityWhenNotTalking) {
        this.nameOpacityWhenTalking = nameOpacityWhenTalking;
        this.nameOpacityWhenNotTalking = nameOpacityWhenNotTalking;
        this.iconOpacityWhenTalking = iconOpacityWhenTalking;
        this.iconOpacityWhenNotTalking = iconOpacityWhenNotTalking;
    }

    public Component displayName() {
        return Component.translatable("text.autoconfig.group-simple-voice-player-chat-names-enhanced-edition.presets."
                + name().toLowerCase(Locale.ROOT));
    }

    @SuppressWarnings("deprecation")
    public void applyTo(OpacityEntries sliders) {
        if (this == CUSTOM) return;
        sliders.nameTalking().setValue(nameOpacityWhenTalking);
        sliders.nameSilent().setValue(nameOpacityWhenNotTalking);
        sliders.iconTalking().setValue(iconOpacityWhenTalking);
        sliders.iconSilent().setValue(iconOpacityWhenNotTalking);
    }

    public static OpacityPreset fromEntries(OpacityEntries sliders) {
        for (var preset : values()) {
            if (preset != CUSTOM
                    && preset.nameOpacityWhenTalking == sliders.nameTalking().getValue()
                    && preset.nameOpacityWhenNotTalking == sliders.nameSilent().getValue()
                    && preset.iconOpacityWhenTalking == sliders.iconTalking().getValue()
                    && preset.iconOpacityWhenNotTalking == sliders.iconSilent().getValue()) {
                return preset;
            }
        }
        return CUSTOM;
    }
}
