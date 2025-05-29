package net.yak.totemictides.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.yak.totemictides.init.TotemicEntityComponents;
import net.yak.totemictides.init.TotemicItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {

    public MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyReturnValue(method = "interactMob", at = @At("RETURN"))
    private ActionResult totemic$bottleVex(ActionResult original, PlayerEntity user, Hand hand) {
        if (((MobEntity) (Object) this) instanceof VexEntity vex) {
            ItemStack handStack = user.getStackInHand(hand);
            if (vex.getHealth() < 5.0f && handStack.isOf(Items.GLASS_BOTTLE)) {
                World world = user.getWorld();
                user.incrementStat(Stats.USED.getOrCreateStat(TotemicItems.BOTTLED_VEX));
                handStack.decrementUnlessCreative(1, user);
                ItemStack newStack = new ItemStack(TotemicItems.BOTTLED_VEX);
                if (handStack.isEmpty()) { // if hand stack is empty after use, new item replaces it
                    user.setStackInHand(hand, newStack);
                }
                else {
                    if (!user.getInventory().insertStack(newStack)) { // otherwise the item is inserted like normal
                        user.dropItem(newStack, false);
                    }
                }

                world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_VEX_AMBIENT, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                world.emitGameEvent(vex, GameEvent.ENTITY_ACTION, vex.getPos());
                vex.discard();
                return ActionResult.success(user.getWorld().isClient());
            }
        }
        return original;
    }

    @ModifyReturnValue(method = "getTarget", at = @At("RETURN"))
    private LivingEntity totemic$inkedPreventsMobTargeting(LivingEntity original) {
        TotemicEntityComponents.INKED.get(this);
        if (TotemicEntityComponents.INKED.get(this).getInkedTicks() > 0 /*&& !blind*/) {
            return null;
        }
        return original;
    }

    @ModifyReturnValue(method = "getTargetInBrain", at = @At("RETURN"))
    private LivingEntity totemic$inkedPreventsBrainMobTargeting(LivingEntity original) {
        TotemicEntityComponents.INKED.get(this);
        if (TotemicEntityComponents.INKED.get(this).getInkedTicks() > 0 /*&& !(this.isIn(blind mobs tag, wardens for example))*/) {
            return null;
        }
        return original;
    }

}
