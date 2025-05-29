package net.yak.totemictides.component;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.yak.totemictides.init.TotemicEntityComponents;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class RematchingComponent implements AutoSyncedComponent, CommonTickingComponent {
    private final PlayerEntity player;
    private int remainingTime = -1;
    private double rematchX = 0;
    private double rematchY = 100;
    private double rematchZ = 0;

    public RematchingComponent(PlayerEntity playerEntity) {
        this.player = playerEntity;
    }






    @Override
    public void readFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup) {
        remainingTime = nbt.getInt("RemainingTime");
        rematchX = nbt.getDouble("RematchX");
        rematchY = nbt.getDouble("RematchY");
        rematchZ = nbt.getDouble("RematchZ");
    }

    @Override
    public void writeToNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup) {
        nbt.putInt("RemainingTime", remainingTime);
        nbt.putDouble("RematchX", rematchX);
        nbt.putDouble("RematchY", rematchY);
        nbt.putDouble("RematchZ", rematchZ);
    }

    @Override
    public void tick() {
        if (remainingTime > 0) {
            remainingTime--;
        }
        if (remainingTime == 0) {
            remainingTime--;
            player.requestTeleport(rematchX, rematchY, rematchZ);
            // do return packet particle stuff
        }
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRematchTime(int ticks) {
        remainingTime = ticks;
    }

    public double getRematchX() {
        return rematchX;
    }

    public double getRematchY() {
        return rematchY;
    }

    public double getRematchZ() {
        return rematchZ;
    }

    public void setRematchPos(double x, double y, double z) {
        rematchX = x;
        rematchY = y;
        rematchZ = z;
    }

    public void sync() {
        TotemicEntityComponents.REMATCHING.sync(player);
    }
}
