package net.yak.totemictides.client.event;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.yak.totemictides.init.TotemicEntityComponents;

public class InkedSplotchRenderEvent implements HudRenderCallback {


    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter renderTickCounter) {
        TotemicEntityComponents.INKED.maybeGet(MinecraftClient.getInstance().cameraEntity).ifPresent((inkedComponent -> {

        }));

    }
}
