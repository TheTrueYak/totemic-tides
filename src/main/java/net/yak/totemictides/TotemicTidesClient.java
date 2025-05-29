package net.yak.totemictides;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.yak.totemictides.client.event.RematchingRenderEvent;
import net.yak.totemictides.client.render.entity.ElderGuardianSpikeEntityRenderer;
import net.yak.totemictides.init.TotemicEntityTypes;
import net.yak.totemictides.init.TotemicParticles;
import net.yak.totemictides.networking.TotemicPayload;

public class TotemicTidesClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        TotemicParticles.initClient();

        HudRenderCallback.EVENT.register(new RematchingRenderEvent());

        EntityRendererRegistry.register(TotemicEntityTypes.ELDER_GUARDIAN_SPIKE_ENTITY, ElderGuardianSpikeEntityRenderer::new);


        // S2C packets
        ClientPlayNetworking.registerGlobalReceiver(TotemicPayload.ID, ((totemicPayload, context) -> {
            context.client().execute(() -> {
                MinecraftClient client = context.client();
                if (client != null && client.player != null && client.player.getWorld() != null) {
                    World world = client.player.getWorld();
                    if (world.getEntityById(totemicPayload.entityId()) instanceof LivingEntity livingEntity) {
                        client.particleManager.addEmitter(livingEntity, TotemicUtil.getParticleFromKey(totemicPayload.totemType()), 30);
                        world.playSound(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                                SoundEvents.ITEM_TOTEM_USE, livingEntity.getSoundCategory(), 1.0f, 1.0f, false
                        );

                        if (livingEntity == client.player) {
                            client.gameRenderer.showFloatingItem(TotemicUtil.getTotemItemFromKey(totemicPayload.totemType()));
                        }
                    }
                }
            });
        }));



    }

}
