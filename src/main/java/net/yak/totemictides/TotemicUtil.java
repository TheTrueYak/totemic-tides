package net.yak.totemictides;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.AdvancedExplosionBehavior;
import net.yak.totemictides.init.TotemicEntityComponents;
import net.yak.totemictides.init.TotemicItems;
import net.yak.totemictides.init.TotemicParticles;
import net.yak.totemictides.init.TotemicStatusEffects;
import net.yak.totemictides.item.TotemicItem;
import net.yak.totemictides.networking.TotemicPayload;

import java.util.Optional;
import java.util.function.Function;

public class TotemicUtil {


    public static void getTotemBehavior(ItemStack stack, LivingEntity livingEntity) {
        if (stack.isOf(TotemicItems.TOTEM_OF_CREEPING)) {
            if (livingEntity.getWorld() instanceof ServerWorld) { // normal explosion
                if (livingEntity instanceof ServerPlayerEntity serverPlayerEntity) {
                    ServerPlayNetworking.send(serverPlayerEntity, new TotemicPayload(livingEntity.getId(), 0));
                }
                for (ServerPlayerEntity player : PlayerLookup.tracking(livingEntity)) {
                    ServerPlayNetworking.send(player, new TotemicPayload(livingEntity.getId(), 0));
                }
                livingEntity.getWorld().createExplosion(livingEntity, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 3.0f, World.ExplosionSourceType.NONE);
            }
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));


        }
        else if (stack.isOf(TotemicItems.TOTEM_OF_BLAZING)) {
            if (livingEntity.getWorld() instanceof ServerWorld serverWorld) { // flame aura :fire:
                if (livingEntity instanceof ServerPlayerEntity serverPlayerEntity) {
                    ServerPlayNetworking.send(serverPlayerEntity, new TotemicPayload(livingEntity.getId(), 1));
                }
                for (ServerPlayerEntity player : PlayerLookup.tracking(livingEntity)) {
                    ServerPlayNetworking.send(player, new TotemicPayload(livingEntity.getId(), 1));
                }

                for (LivingEntity entity : serverWorld.getNonSpectatingEntities(LivingEntity.class, livingEntity.getBoundingBox().expand(5, 1, 5))) {
                    if (livingEntity != entity) {
                        Vec3d vec3d = livingEntity.getPos().subtract(entity.getPos());
                        Vec3d vec3d2 = vec3d.normalize().multiply(3);
                        entity.addVelocity(vec3d2.x, 0.3D, vec3d2.z);
                    }
                }
            }
            TotemicEntityComponents.BLAZING.maybeGet(livingEntity).ifPresent((blazingComponent -> {
                TotemicEntityComponents.BLAZING.get(livingEntity).setBlazingTicks(200);
            }));
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 1600, 0));

        }
        else if (stack.isOf(TotemicItems.TOTEM_OF_INKING)) {
            if (livingEntity.getWorld() instanceof ServerWorld serverWorld) {
                if (livingEntity instanceof ServerPlayerEntity serverPlayerEntity) { // the inklet
                    ServerPlayNetworking.send(serverPlayerEntity, new TotemicPayload(livingEntity.getId(), 2));
                }
                for (ServerPlayerEntity player : PlayerLookup.tracking(livingEntity)) {
                    ServerPlayNetworking.send(player, new TotemicPayload(livingEntity.getId(), 2));
                }
                for (LivingEntity entity : serverWorld.getNonSpectatingEntities(LivingEntity.class, livingEntity.getBoundingBox().expand(7, 2, 7))) {
                    TotemicEntityComponents.INKED.maybeGet(entity).ifPresent((inkedComponent -> {
                        TotemicEntityComponents.INKED.get(entity).setInkedTicks(100);
                    }));
                }
            }
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 300, 0));
        }
        else if (stack.isOf(TotemicItems.TOTEM_OF_BREEZING)) {
            if (livingEntity.getWorld() instanceof ServerWorld serverWorld) { // wind burst explosion
                if (livingEntity instanceof ServerPlayerEntity serverPlayerEntity) {
                    ServerPlayNetworking.send(serverPlayerEntity, new TotemicPayload(livingEntity.getId(), 3));
                }
                for (ServerPlayerEntity player : PlayerLookup.tracking(livingEntity)) {
                    ServerPlayNetworking.send(player, new TotemicPayload(livingEntity.getId(), 3));
                }
                serverWorld.createExplosion(livingEntity, null, new AdvancedExplosionBehavior(true, false, Optional.of(1.22F),
                                Registries.BLOCK.getEntryList(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).map(Function.identity())), livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 1.2F,
                        false, World.ExplosionSourceType.TRIGGER, ParticleTypes.GUST_EMITTER_SMALL, ParticleTypes.GUST_EMITTER_LARGE, SoundEvents.ENTITY_WIND_CHARGE_WIND_BURST);
            }
            TotemicEntityComponents.BREEZING.maybeGet(livingEntity).ifPresent((breezingComponent -> {
                TotemicEntityComponents.BREEZING.get(livingEntity).setWindSailing(true);
            }));
            livingEntity.addVelocity(0, 8, 0);
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));
        }
        else if (stack.isOf(TotemicItems.TOTEM_OF_REMATCHING)) {
            if (livingEntity.getWorld() instanceof ServerWorld) { // back and better than ever
                if (livingEntity instanceof ServerPlayerEntity serverPlayerEntity) {
                    ServerPlayNetworking.send(serverPlayerEntity, new TotemicPayload(livingEntity.getId(), 4));
                }
                for (ServerPlayerEntity player : PlayerLookup.tracking(livingEntity)) {
                    ServerPlayNetworking.send(player, new TotemicPayload(livingEntity.getId(), 4));
                }
            }
            if (livingEntity instanceof PlayerEntity player) {
                TotemicEntityComponents.REMATCHING.maybeGet(player).ifPresent((rematchingComponent -> {
                    TotemicEntityComponents.REMATCHING.get(player).setRematchTime(200); // 30 seconds is 600 ticks
                    TotemicEntityComponents.REMATCHING.get(player).setRematchPos(player.getX(), player.getY(), player.getZ());
                }));
            }
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 4));

        }
        else if (stack.isOf(TotemicItems.TOTEM_OF_REGENERATING)) {
            if (livingEntity.getWorld() instanceof ServerWorld) { // there he is!
                if (livingEntity instanceof ServerPlayerEntity serverPlayerEntity) {
                    ServerPlayNetworking.send(serverPlayerEntity, new TotemicPayload(livingEntity.getId(), 5));
                }
                for (ServerPlayerEntity player : PlayerLookup.tracking(livingEntity)) {
                    ServerPlayNetworking.send(player, new TotemicPayload(livingEntity.getId(), 5));
                }
            }
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));

        }
        else if (stack.isOf(TotemicItems.TOTEM_OF_EVOKING)) {
            if (livingEntity.getWorld() instanceof ServerWorld) { // hey, do that one again!
                if (livingEntity instanceof ServerPlayerEntity serverPlayerEntity) {
                    ServerPlayNetworking.send(serverPlayerEntity, new TotemicPayload(livingEntity.getId(), 6));
                }
                for (ServerPlayerEntity player : PlayerLookup.tracking(livingEntity)) {
                    ServerPlayNetworking.send(player, new TotemicPayload(livingEntity.getId(), 6));
                }
                if (livingEntity instanceof PlayerEntity player) { // evoking totem cycling logic
                    if (!moveInventoryStackToHand(TotemicItems.TOTEM_OF_EVOKING, player)) { // TODO: possibly move inside server player?
                        if (inventoryContainsTotemicItem(player)) {
                            moveInventoryStackToHand(getInventoryTotemicItem(player), player);
                        }
                    }
                }
            }
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 50, 0));
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 0));
        }
        else if (stack.isOf(TotemicItems.TOTEM_OF_SHULKING)) {
            if (livingEntity.getWorld() instanceof ServerWorld) { // the shulklet
                if (livingEntity instanceof ServerPlayerEntity serverPlayerEntity) {
                    ServerPlayNetworking.send(serverPlayerEntity, new TotemicPayload(livingEntity.getId(), 7));
                }
                for (ServerPlayerEntity player : PlayerLookup.tracking(livingEntity)) {
                    ServerPlayNetworking.send(player, new TotemicPayload(livingEntity.getId(), 7));
                }
            }
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));
            if (livingEntity.getWorld().getRegistryKey() == World.END && livingEntity.getY() < -65) {
                livingEntity.requestTeleport(livingEntity.getX(), 0, livingEntity.getZ()); // special handling for end void deaths
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 400, 4));
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 600, 0));
            }
            else {
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 200, 4));
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 400, 0));
            }
        }
    }


    public static ItemStack getTotemItemFromKey(int totemKey) {
        return switch (totemKey) {
            case 0 -> new ItemStack(TotemicItems.TOTEM_OF_CREEPING);
            case 1 -> new ItemStack(TotemicItems.TOTEM_OF_BLAZING);
            case 2 -> new ItemStack(TotemicItems.TOTEM_OF_INKING);
            case 3 -> new ItemStack(TotemicItems.TOTEM_OF_BREEZING);
            case 4 -> new ItemStack(TotemicItems.TOTEM_OF_REMATCHING);
            case 5 -> new ItemStack(TotemicItems.TOTEM_OF_REGENERATING);
            case 6 -> new ItemStack(TotemicItems.TOTEM_OF_EVOKING);
            case 7 -> new ItemStack(TotemicItems.TOTEM_OF_SHULKING);
            default -> new ItemStack(Items.TOTEM_OF_UNDYING); // idk fallback
        };
    }

    public static SimpleParticleType getParticleFromKey(int totemKey) {
        return switch (totemKey) {
            case 0 -> TotemicParticles.TOTEM_OF_CREEPING_PARTICLE;
            case 1 -> TotemicParticles.TOTEM_OF_BLAZING_PARTICLE;
            case 2 -> TotemicParticles.TOTEM_OF_INKING_PARTICLE;
            case 3 -> TotemicParticles.TOTEM_OF_BREEZING_PARTICLE;
            case 4 -> TotemicParticles.TOTEM_OF_REMATCHING_PARTICLE;
            case 5 -> TotemicParticles.TOTEM_OF_REGENERATING_PARTICLE;
            case 6 -> TotemicParticles.TOTEM_OF_EVOKING_PARTICLE;
            case 7 -> TotemicParticles.TOTEM_OF_SHULKING_PARTICLE;
            default -> ParticleTypes.TOTEM_OF_UNDYING; // fallback
        };
    }

    public static boolean inventoryContainsItem(Item item, PlayerEntity player) {
        for (ItemStack stack : player.getInventory().main) {
            if (!stack.isEmpty() && stack.getItem().equals(item)) {
                return true;
            }
        }
        return false;
    }

    public static boolean inventoryContainsTotemicItem(PlayerEntity player) {
        for (ItemStack stack : player.getInventory().main) {
            if (!stack.isEmpty() && (stack.isOf(Items.TOTEM_OF_UNDYING) || stack.getItem() instanceof TotemicItem)) {
                return true;
            }
        }
        return false;
    }

    public static Item getInventoryTotemicItem(PlayerEntity player) {
        for (ItemStack stack : player.getInventory().main) {
            if (!stack.isEmpty() && (stack.isOf(Items.TOTEM_OF_UNDYING) || stack.getItem() instanceof TotemicItem)) {
                return stack.getItem();
            }
        }
        return null;
    }

    public static boolean moveInventoryStackToHand(Item item, PlayerEntity player) {
        for (int inv = 0; inv < player.getInventory().main.size(); inv++) {
            ItemStack inventoryStack = player.getInventory().getStack(inv);
            if (!inventoryStack.isEmpty() && inventoryStack.getItem().equals(item)) {
                if ((player.getMainHandStack().isEmpty()) || (player.getOffHandStack().isEmpty())) {
                ItemStack singleStack = inventoryStack.copy();
                inventoryStack.decrement(1);
                singleStack.setCount(1);
                player.setStackInHand(player.getOffHandStack().isEmpty() ? Hand.OFF_HAND : Hand.MAIN_HAND, singleStack);
                return true;
                }
            }
        }
        return false;
    }

    public static boolean shouldBuff(LivingEntity entity, LivingEntity owner) {
        return entity == owner || entity.isTeammate(owner)
                || (entity instanceof Tameable tameable && (tameable.getOwner() == owner
                || (tameable.getOwner() != null && tameable.getOwner().isTeammate(owner))))
                || (entity instanceof PlayerEntity player && (owner.getLastAttacker() != player && player.getLastAttacker() != owner));
    }

    public static boolean shouldHarm(LivingEntity entity, LivingEntity owner) {
        return entity instanceof HostileEntity || entity == owner.getLastAttacker()
                || owner == entity.getLastAttacker();
    }


}
