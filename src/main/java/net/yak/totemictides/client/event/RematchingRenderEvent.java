package net.yak.totemictides.client.event;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import net.yak.totemictides.TotemicTides;
import net.yak.totemictides.init.TotemicEntityComponents;

public class RematchingRenderEvent implements HudRenderCallback {
    public static final Identifier emptyHourglass = TotemicTides.id("hud/rematching_hourglass_empty");
    public static final Identifier fullHourglass = TotemicTides.id("hud/rematching_hourglass_full");

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter renderTickCounter) {
        if (!MinecraftClient.getInstance().options.hudHidden) {
            TotemicEntityComponents.REMATCHING.maybeGet(MinecraftClient.getInstance().cameraEntity).ifPresent((rematchingComponent -> {
                if (rematchingComponent.getRemainingTime() > -1 /*|| true*/) {
                    RenderSystem.enableBlend();
                    int x = 0; //drawContext.getScaledWindowWidth() / 2; // + 90;
                    int y = drawContext.getScaledWindowHeight() / 2 - 7; // + 116;
                    if (rematchingComponent.getRemainingTime() > 5) {
                        drawContext.drawGuiTexture(emptyHourglass, x, y, 16, 16);
                        drawContext.drawGuiTexture(fullHourglass, x, y, 16, (16 * rematchingComponent.getRemainingTime() / 200));
                    }
                    else {
                        drawContext.drawGuiTexture(emptyHourglass, x, y, 16, 16);
                    }

                    drawContext.setShaderColor(1, 1, 1, 1);
                    RenderSystem.disableBlend();
                }

            }));
        }
    }
}
