package be.florens.svcgroupplayernames.config;

import me.shedaniel.clothconfig2.gui.entries.TooltipListEntry;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.Optional;

final class PresetSelectorEntry extends TooltipListEntry<OpacityPreset> {
    private final OpacityEntries sliders;
    private final CycleButton<OpacityPreset> button;
    private OpacityPreset previousSliderPreset;

    PresetSelectorEntry(OpacityEntries sliders) {
        super(Component.translatable("text.autoconfig.group-simple-voice-player-chat-names-enhanced-edition.presets"),
                () -> Optional.of(new Component[]{Component.translatable(
                        "text.autoconfig.group-simple-voice-player-chat-names-enhanced-edition.presets.@Tooltip")}));
        this.sliders = sliders;
        previousSliderPreset = OpacityPreset.fromEntries(sliders);
        button = CycleButton.builder(OpacityPreset::displayName, previousSliderPreset)
                .withValues(OpacityPreset.values())
                .create(getFieldName(), (button, preset) -> {
                    preset.applyTo(sliders);
                    previousSliderPreset = OpacityPreset.fromEntries(sliders);
                });
    }

    @Override
    public void tick() {
        super.tick();
        var matchingPreset = OpacityPreset.fromEntries(sliders);
        if (matchingPreset != previousSliderPreset) {
            button.setValue(matchingPreset);
            previousSliderPreset = matchingPreset;
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int index, int y, int x, int entryWidth,
                                   int entryHeight, int mouseX, int mouseY, boolean hovered, float delta) {
        super.extractRenderState(graphics, index, y, x, entryWidth, entryHeight, mouseX, mouseY, hovered, delta);
        button.setRectangle(entryWidth, 20, x, y);
        button.active = isEditable();
        button.extractRenderState(graphics, mouseX, mouseY, delta);
    }

    @Override
    public OpacityPreset getValue() {
        return button.getValue();
    }

    @Override
    public Optional<OpacityPreset> getDefaultValue() {
        return Optional.empty();
    }

    @Override
    public boolean isEdited() {
        return false;
    }

    @Override
    public List<CycleButton<OpacityPreset>> children() {
        return List.of(button);
    }

    @Override
    public List<CycleButton<OpacityPreset>> narratables() {
        return List.of(button);
    }
}
