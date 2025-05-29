package net.yak.totemictides.component;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.yak.totemictides.TotemicUtil;
import net.yak.totemictides.init.TotemicEntityComponents;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class BlazingComponent implements AutoSyncedComponent, CommonTickingComponent {
    private final LivingEntity livingEntity;
    private int blazingTicks = 0;

    public BlazingComponent(LivingEntity livingEntity) {
        this.livingEntity = livingEntity;
    }

    @Override
    public void readFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup) {
        blazingTicks = nbt.getInt("BlazingTicks");
    }

    @Override
    public void writeToNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup) {
        nbt.putInt("BlazingTicks", blazingTicks);
    }

    @Override
    public void tick() {
        if (blazingTicks > 0) {
            blazingTicks--;
            if (livingEntity.getWorld() instanceof ServerWorld serverWorld) {
                serverWorld.addParticle(ParticleTypes.FLAME, livingEntity.getX(), livingEntity.getY() + 1, livingEntity.getZ(), (0.1 * livingEntity.getRandom().nextInt(10)) - 0.5, (0.1 * livingEntity.getRandom().nextInt(5)) - 0.25, (0.1 * livingEntity.getRandom().nextInt(10)) - 0.5);

                for (LivingEntity entity : serverWorld.getNonSpectatingEntities(LivingEntity.class, livingEntity.getBoundingBox().expand(5, 1, 5))) {
                    if (TotemicUtil.shouldBuff(entity, livingEntity)) {
                        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 90, 0, true, true));
                        serverWorld.addParticle(ParticleTypes.SMALL_FLAME, entity.getX(), entity.getY() + 2, entity.getZ(), 0.0, 0.0, 0.0);
                    }
                    else if (TotemicUtil.shouldHarm(entity, livingEntity)) {
                        entity.setOnFireForTicks((int) (80 / entity.distanceTo(livingEntity)));
                        serverWorld.addParticle(ParticleTypes.FLAME, entity.getX(), entity.getY() + 2, entity.getZ(), 0.0, 0.0, 0.0);
                    }
                }
            }
        }


    }

    public int getBlazingTicks() {
        return blazingTicks;
    }

    public void setBlazingTicks(int ticks) {
        blazingTicks = ticks;
        sync();
    }

    public void sync() {
        TotemicEntityComponents.BLAZING.sync(livingEntity);
    }
}
