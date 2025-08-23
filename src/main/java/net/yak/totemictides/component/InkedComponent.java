package net.yak.totemictides.component;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.yak.totemictides.init.TotemicEntityComponents;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class InkedComponent implements AutoSyncedComponent, CommonTickingComponent {
    private final LivingEntity livingEntity;
    private int inkedTicks = -1;
    private int splotchCount = 0; // number of ink splotches on screen
    private int splotchSeed = 0; // seed used for drawing splotches randomly but still the same within an instance of being inked
    private boolean quickDecay = false; // if damaged, ink will decay 2x-3x faster

    public InkedComponent(LivingEntity livingEntity) {
        this.livingEntity = livingEntity;
    }

    @Override
    public void readFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup) {
        inkedTicks = nbt.getInt("InkedTicks");
        splotchCount = nbt.getInt("SplotchCount");
        splotchSeed = nbt.getInt("SplotchSeed");
        quickDecay = nbt.getBoolean("QuickDecay");
    }

    @Override
    public void writeToNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup) {
        nbt.putInt("InkedTicks", inkedTicks);
        nbt.putInt("SplotchCount", splotchCount);
        nbt.putInt("SplotchSeed", splotchSeed);
        nbt.putBoolean("QuickDecay", quickDecay);
    }

    @Override
    public void tick() {
        if (inkedTicks > 0) {
            inkedTicks--;
            if ((inkedTicks + 1) % 10 == 0) { // tweak value?
                if (livingEntity.getWorld() instanceof ServerWorld serverWorld) {
                    int particleCount = MathHelper.clamp(inkedTicks / 20, 1, 15);
                    for (int i = 0; i < particleCount; i++) {
                        serverWorld.addParticle(ParticleTypes.SQUID_INK, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 0.0 + (i - (0.1f * particleCount / 2f) /*?*/), 0.1, 0.0); // add particles around head if inked
                    }
                }
            }
            if (quickDecay) {
                inkedTicks -= Math.min(inkedTicks, 2); // can tweak value as needed, don't forget that 1 has already been subtracted
            }
        }
        if (inkedTicks == 0) {
            inkedTicks--;
            splotchCount = 0;
            quickDecay = false;
        }
    }

    public int getInkedTicks() {
        return inkedTicks;
    }

    public void setInkedTicks(int ticks) {
        if (inkedTicks == 0 || ticks == 0) {
            inkedTicks = ticks;
            if (livingEntity.isPlayer() && ticks != 0) {
                splotchCount = 3 + livingEntity.getRandom().nextInt(5); // 3-7 splotches
                splotchSeed = livingEntity.getRandom().nextInt(100);
            }
        }
        else {
            inkedTicks += ticks;
        }
        sync();
    }

    public int getSplotchCount() {
        return splotchCount;
    }

    public int getSplotchSeed() {
        return splotchSeed;
    }

    public boolean shouldDecayQuickly() {
        return quickDecay;
    }

    public void setDecayQuickly(boolean value) {
        quickDecay = value;
        //sync();
    }

    public void sync() {
        TotemicEntityComponents.INKED.sync(livingEntity);
    }
}
