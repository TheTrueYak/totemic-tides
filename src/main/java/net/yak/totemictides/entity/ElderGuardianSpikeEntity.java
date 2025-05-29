package net.yak.totemictides.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import net.yak.totemictides.init.TotemicEntityTypes;
import net.yak.totemictides.init.TotemicItems;
import net.yak.totemictides.init.TotemicStatusEffects;

public class ElderGuardianSpikeEntity extends PersistentProjectileEntity {

    public ElderGuardianSpikeEntity(EntityType<? extends ElderGuardianSpikeEntity> entityType, World world) {
        super(entityType, world);
    }

    public ElderGuardianSpikeEntity(World world, LivingEntity owner, ItemStack stack) {
        super(TotemicEntityTypes.ELDER_GUARDIAN_SPIKE_ENTITY, owner, world, stack, null);
    }

    public ElderGuardianSpikeEntity(World world, double x, double y, double z, ItemStack stack) {
        super(TotemicEntityTypes.ELDER_GUARDIAN_SPIKE_ENTITY, x, y, z, world, stack, stack);
    }

    @Override
    public void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
    }

    @Override
    public void tick() {
        if (this.getWorld().isClient() && !this.inGround) {
            this.getWorld().addParticle(ParticleTypes.INSTANT_EFFECT, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
        }
        super.tick();
    }

    @Override
    public void onHit(LivingEntity target) {
        super.onHit(target);
        StatusEffectInstance statusEffectInstance = new StatusEffectInstance(TotemicStatusEffects.ATONEMENT, 900);
        target.addStatusEffect(statusEffectInstance, this.getEffectCause());
        this.discard();
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        Entity owner = this.getOwner();
        DamageSource damageSource = this.getDamageSources().trident(this, owner == null ? this : owner);
        if (entity.damage(damageSource, 4f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }
            if (entity instanceof LivingEntity livingEntity) {
                this.onHit(livingEntity);
            }
        }
        this.setVelocity(this.getVelocity().multiply(-0.01, -0.1, -0.01));
        this.playSound(SoundEvents.ITEM_TRIDENT_HIT, 1.0f, 0.5f);
    }

    @Override
    public boolean tryPickup(PlayerEntity player) {
        return super.tryPickup(player) || this.isNoClip() && this.isOwner(player) && player.getInventory().insertStack(this.asItemStack());
    }

    @Override
    public ItemStack getDefaultItemStack() {
        return new ItemStack(TotemicItems.ELDER_GUARDIAN_SPIKE);
    }

    protected SoundEvent getHitSound() {
        return SoundEvents.ITEM_TRIDENT_HIT_GROUND;
    }

    public void onPlayerCollision(PlayerEntity player) {
        if (this.isOwner(player) || this.getOwner() == null || age > 1200) {
            super.onPlayerCollision(player);
        }
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
    }

    public void age() {
        if (this.pickupType != PickupPermission.ALLOWED) {
            super.age();
        }
    }

    public float getDragInWater() {
        return 0.49F;
    }

    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }

}
