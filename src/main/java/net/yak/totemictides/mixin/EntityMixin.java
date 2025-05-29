package net.yak.totemictides.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @ModifyReturnValue(method = "isInvisibleTo", at = @At("RETURN"))
    private boolean blindSight$enchantedLensVision(boolean original, PlayerEntity player) {
        if (player.hasStatusEffect(StatusEffects.GLOWING)) {
            return false;
        }
        return original;
    }


}
