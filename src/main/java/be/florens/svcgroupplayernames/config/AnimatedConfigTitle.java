package be.florens.svcgroupplayernames.config;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public final class AnimatedConfigTitle {

    private static final double WAVE_SPEED = 2.0;
    private static final double WAVE_SPACING = 0.2;
    private static final float WAVE_HEIGHT = 2F;

    private static final double HUE_SPEED = 0.10;
    private static final double HUE_OFFSET = 0.03;
    private static final float COLOR_SATURATION = 0.7F;
    private static final int OPAQUE_ALPHA = 0xFF000000;

    private AnimatedConfigTitle() {
    }

    public static void render(GuiGraphicsExtractor graphics, Font font, Component title, int centerX, int y) {
        double epochSeconds = System.nanoTime() / 1_000_000_000.0;
        int width = font.width(title);
        graphics.pose().pushMatrix();
        graphics.pose().translate(centerX, y);

        float x = -width / 2F;
        int index = 0;
        for (int codePoint : title.getString().codePoints().toArray()) {
            String letter = new String(Character.toChars(codePoint));
            int letterWidth = font.width(letter);
            float wave = (float) Math.sin(epochSeconds * WAVE_SPEED - index * WAVE_SPACING);
            float hue = (float) ((epochSeconds * HUE_SPEED + index * HUE_OFFSET) % 1.0);
            int color = OPAQUE_ALPHA | Mth.hsvToRgb(hue, COLOR_SATURATION, 1F);
            graphics.pose().pushMatrix();
            graphics.pose().translate(x, wave * WAVE_HEIGHT);
            graphics.text(font, letter, 0, 0, color, true);
            graphics.pose().popMatrix();
            x += letterWidth;
            index++;
        }
        graphics.pose().popMatrix();
    }
}
