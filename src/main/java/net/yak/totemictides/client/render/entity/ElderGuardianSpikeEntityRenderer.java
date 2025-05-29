package net.yak.totemictides.client.render.entity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;
import net.yak.totemictides.TotemicTides;
import net.yak.totemictides.entity.ElderGuardianSpikeEntity;

public class ElderGuardianSpikeEntityRenderer extends ProjectileEntityRenderer<ElderGuardianSpikeEntity> {
    public static final Identifier TEXTURE = TotemicTides.id("textures/entity/elder_guardian_spike.png");

    public ElderGuardianSpikeEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(ElderGuardianSpikeEntity entity) {
        return TEXTURE;
    }
}
