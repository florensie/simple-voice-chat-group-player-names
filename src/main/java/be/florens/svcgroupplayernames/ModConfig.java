package be.florens.svcgroupplayernames;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "svcgroupplayernames")
public class ModConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip
    boolean onlyShowNamesWhenTalking = false;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    int transparencyWhenTalking = 100;
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    int transparencyWhenNotTalking = 50;
}
