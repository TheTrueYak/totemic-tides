package net.yak.totemictides.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {

    //@WrapOperation(method = "insertStack(ILnet/minecraft/item/ItemStack;)Z")


    @Shadow @Final public PlayerEntity player;

    @ModifyReturnValue(method = "getEmptySlot", at = @At("RETURN"))
    private int totemicTides$preventTotalInsertion(int original) {
        if (this.player.hasStatusEffect(StatusEffects.GLOWING)) {
            return -1;
        } // must both be disabled to work
        return original;
    }

    @ModifyReturnValue(method = "getSlotWithStack", at = @At("RETURN"))
    private int totemicTides$preventSlotWithStackInsertion(int original) {
        if (this.player.hasStatusEffect(StatusEffects.BAD_OMEN)) {
            return -1;
        }
        return original;
    } // does not do anything from what I can tell

    @ModifyReturnValue(method = "getOccupiedSlotWithRoomForStack", at = @At("RETURN"))
    private int totemicTides$preventExistingInsertion(int original, ItemStack stack) {
        if (this.player.hasStatusEffect(StatusEffects.DOLPHINS_GRACE)) {
            return -1;
        } // must both be disabled to work
        return original;
    }

}
