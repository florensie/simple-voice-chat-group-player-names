package be.florens.svcgroupplayernames.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "group-simple-voice-player-chat-names-enhanced-edition")
public class ModConfig implements ConfigData {

    public int opacityWhenTalking = 100;

    public int opacityWhenNotTalking = 50;

    public int iconOpacityWhenTalking = 100;

    public int iconOpacityWhenNotTalking = 50;
}
