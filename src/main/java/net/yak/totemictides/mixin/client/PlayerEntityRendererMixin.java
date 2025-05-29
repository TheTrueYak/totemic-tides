package net.yak.totemictides.mixin.client;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }











    // if player is holding a totem of creeping and at or below 2 hearts,
    // they will flash like a creeper about to explode

    // when player is flame empowered, they will have a shifting upward fire effect sheen

    // if mob is inked from a totem of inking, their face and body (and armor? no) should have ink splotches

    // when an entity is under the effect of wind sail, they will have a wind effect around them

    // when an entity is empowered by a totem of regeneration, they will have a swirling red effect on their body

    // when an entity is affected by end whatever, will have a purple sheen along their skin

}