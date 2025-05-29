package net.yak.totemictides.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ProjectileItem;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import net.yak.totemictides.entity.ElderGuardianSpikeEntity;
import net.yak.totemictides.init.TotemicStatusEffects;

import java.util.List;

public class ElderGuardianSpikeItem extends Item implements ProjectileItem {

    public ElderGuardianSpikeItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 72000;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            int i = this.getMaxUseTime(stack, user) - remainingUseTicks;
            if (i >= 10) {
                if (!world.isClient()) {
                    ElderGuardianSpikeEntity elderGuardianSpikeEntity = new ElderGuardianSpikeEntity(world, playerEntity, stack.copyWithCount(1));
                    elderGuardianSpikeEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 1.25F, 1.0F);
                    if (playerEntity.isInCreativeMode()) {
                        elderGuardianSpikeEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                    }
                    world.spawnEntity(elderGuardianSpikeEntity);
                    world.playSoundFromEntity(null, elderGuardianSpikeEntity, SoundEvents.ITEM_TRIDENT_THROW.value(), SoundCategory.PLAYERS, 0.5F, 1.2F);
                    if (!playerEntity.isInCreativeMode()) {
                        stack.decrement(1);
                    }
                }
                playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
            }
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.addStatusEffect(new StatusEffectInstance(TotemicStatusEffects.ATONEMENT, 300));
        stack.decrement(1);
        if (!attacker.getWorld().isClient()) {
            attacker.getWorld().playSoundFromEntity(null, attacker, SoundEvents.ITEM_TRIDENT_HIT, SoundCategory.NEUTRAL, 0.5F, 1.2F / (attacker.getWorld().getRandom().nextFloat() * 0.2F + 0.6F));
        }
        super.postDamageEntity(stack, target, attacker);
    }

    @Override
    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        ElderGuardianSpikeEntity elderGuardianSpikeEntity = new ElderGuardianSpikeEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack.copyWithCount(1));
        elderGuardianSpikeEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
        return elderGuardianSpikeEntity;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        tooltip.add(Text.translatable("tooltip.totemictides.on_hit").formatted(Formatting.DARK_PURPLE));
        tooltip.add(Text.translatable("potion.withDuration", Text.translatable("effect.totemictides.atonement"), StringHelper.formatTicks(300, context.getUpdateTickRate())).formatted(Formatting.BLUE));
        tooltip.add(Text.literal(""));
        tooltip.add(Text.translatable("tooltip.totemictides.when_thrown").formatted(Formatting.DARK_PURPLE));
        tooltip.add(Text.translatable("potion.withDuration", Text.translatable("effect.totemictides.atonement"), StringHelper.formatTicks(900, context.getUpdateTickRate())).formatted(Formatting.BLUE));
    }
}
