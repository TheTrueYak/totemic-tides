package net.yak.totemictides.component;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.yak.totemictides.init.TotemicEntityComponents;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class BreezingComponent implements AutoSyncedComponent, CommonTickingComponent {
    private final LivingEntity livingEntity;
    private boolean windSailing = false; // used to check if wind sailing
    private int windSailingTicks = 0; // used as a grace period for initial application of wind sailing

    public BreezingComponent(LivingEntity livingEntity) {
        this.livingEntity = livingEntity;
    }

    @Override
    public void readFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup) {
        windSailing = nbt.getBoolean("WindSailing");
        windSailingTicks = nbt.getInt("WindSailingTicks");
    }

    @Override
    public void writeToNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup) {
        nbt.putBoolean("WindSailing", windSailing);
        nbt.putInt("WindSailingTicks", windSailingTicks);
    }

    @Override
    public void tick() {
        if (windSailing) {
            if (windSailingTicks > 0) {
                windSailingTicks--;
            }
            else if (windSailingTicks == 0 && (livingEntity.isOnGround() || livingEntity.isSubmergedInWater() || livingEntity.isHoldingOntoLadder() || livingEntity.hasVehicle())) {
                windSailing = false;
                /*if (livingEntity.getWorld() instanceof ServerWorld serverWorld) {
                    serverWorld.addParticle(); // some silly particles to indicate loss of wind sailing on land
                }*/
            }
        }
    }

    public boolean isWindSailing() {
        return windSailing;
    }

    public void setWindSailing(boolean value) {
        windSailing = value;
        windSailingTicks = value ? 20 : 0; // wind sailing ticks are set to 20 if true, 0 if false
        sync();
    }

    public void sync() {
        TotemicEntityComponents.BREEZING.sync(livingEntity);
    }
}
