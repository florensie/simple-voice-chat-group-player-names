package be.florens.svcgroupplayernames.config;

import me.shedaniel.clothconfig2.gui.entries.IntegerSliderEntry;

import java.util.List;

public record OpacityEntries(
        IntegerSliderEntry nameTalking,
        IntegerSliderEntry nameSilent,
        IntegerSliderEntry iconTalking,
        IntegerSliderEntry iconSilent
) {
    public List<IntegerSliderEntry> entries() {
        return List.of(nameTalking, nameSilent, iconTalking, iconSilent);
    }
}
