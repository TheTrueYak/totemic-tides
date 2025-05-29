package net.yak.totemictides.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.yak.totemictides.init.TotemicEntityTypes;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class InkCloudEntity extends Entity implements Ownable {


    @Nullable
    private LivingEntity owner;
    @Nullable
    private UUID ownerUuid;

    public InkCloudEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public InkCloudEntity(World world, LivingEntity user) {
        super(TotemicEntityTypes.INK_CLOUD_ENTITY, world);
        setOwner(user);
        setPos(user.getX(), user.getY(), user.getZ());
    }

    public InkCloudEntity(World world, LivingEntity user, double x, double y, double z) {
        super(TotemicEntityTypes.INK_CLOUD_ENTITY, world);
        setOwner(user);
        setPos(x, y, z);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {

    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }

    @Override
    public void tick() {
        if (age > 200) {
            this.discard();
        }
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            for (int i = 0; i < 5; i++) {
                serverWorld.addParticle(ParticleTypes.SQUID_INK, this.getX() + this.getRandom().nextBetween((int) this.getX() - 5, (int) this.getX() + 5), this.getY() + this.getRandom().nextBetween((int) this.getX() - 2, (int) this.getX() + 2), this.getZ() + this.getRandom().nextBetween((int) this.getX() - 5, (int) this.getX() + 5), 0.0, 0.0, 0.0);
            }
        }
    }




    public void setOwner(@Nullable LivingEntity owner) {
        this.owner = owner;
        this.ownerUuid = owner == null ? null : owner.getUuid();
    }

    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUuid != null && this.getWorld() instanceof ServerWorld) {
            Entity entity = ((ServerWorld)this.getWorld()).getEntity(this.ownerUuid);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity)entity;
            }
        }

        return this.owner;
    }
}
