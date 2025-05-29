package net.yak.totemictides.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.yak.totemictides.init.TotemicEntityComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    public PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyReturnValue(method = "getOffGroundSpeed", at = @At("RETURN"))
    private float totemicTides$windSailIncreasedMidairSpeed(float original) {
        if (TotemicEntityComponents.BREEZING.get(this).isWindSailing()) { // wind sailing greatly increases midair speed
            return original * (this.isSneaking() ? 1.5f : 3f); // halves the midair speed increase if sneaking
        }
        return original;
    }

}
