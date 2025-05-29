package net.yak.totemictides.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.yak.totemictides.TotemicUtil;
import net.yak.totemictides.init.TotemicEntityComponents;
import net.yak.totemictides.init.TotemicItems;
import net.yak.totemictides.init.TotemicStatusEffects;
import net.yak.totemictides.item.TotemicItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

	@Shadow public abstract ItemStack getStackInHand(Hand hand);

	@Shadow public abstract boolean hasStatusEffect(RegistryEntry<StatusEffect> effect);

	@Shadow public abstract boolean addStatusEffect(StatusEffectInstance effect);

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@WrapOperation(method = "tryUseTotem", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
	private boolean totemicTides$allowModdedTotems(ItemStack instance, Item item, Operation<Boolean> original) {
		if (instance.getItem() instanceof TotemicItem) {
			return true;
		}
		return original.call(instance, item);
	}

	@Definition(id = "itemStack", local = @Local(type = ItemStack.class, ordinal = 0))
	@Expression("itemStack != null")
	@WrapOperation(method = "tryUseTotem", at = @At("MIXINEXTRAS:EXPRESSION"))
	private boolean totemicTides$totemLogic(Object stack, Object nothing, Operation<Boolean> original, DamageSource source) {
		LivingEntity livingEntity = (LivingEntity) (Object) this;
		ItemStack totemStack = (ItemStack) stack;
		if (livingEntity.getHealth() >= 1.0f) {
			return original.call(stack, nothing); // health of 1.0f or greater means that the totem pop has already happened
		}
		if (totemStack != null && totemStack.getItem() instanceof TotemicItem) {
			if (livingEntity instanceof ServerPlayerEntity serverPlayerEntity) {
				serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(totemStack.getItem()));
				Criteria.USED_TOTEM.trigger(serverPlayerEntity, totemStack); // TODO: advancements? maybe just rename current description?
				this.emitGameEvent(GameEvent.ITEM_INTERACT_FINISH);
			}
            livingEntity.setHealth(1.0f);
			livingEntity.clearStatusEffects();
			TotemicUtil.getTotemBehavior(totemStack, livingEntity);
			return false; // returns false, that the itemStack is null, preventing the code block that follows
		}
		return original.call(stack, nothing);
	}

	@WrapOperation(method = "tryUseTotem", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/damage/DamageSource;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
	private boolean totemicTides$totemOfShulkingVoidLogic(DamageSource instance, TagKey<DamageType> tag, Operation<Boolean> original) {
		if (instance.isOf(DamageTypes.OUT_OF_WORLD)) {
			for (Hand hand : Hand.values()) {
				ItemStack stack = this.getStackInHand(hand);
				if (stack.isOf(TotemicItems.TOTEM_OF_SHULKING)) { // totem of shulking protects from void damage
					return false; // returns that the damage source is NOT in the tag and thus the method can continue past this damage source check
				}
			}
		}
		return original.call(instance, tag);
	}

	@WrapOperation(method = "tryUseTotem", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getStackInHand(Lnet/minecraft/util/Hand;)Lnet/minecraft/item/ItemStack;"))
	private ItemStack totemicTides$totemOfShulkingPriorityWithVoidDamage(LivingEntity instance, Hand hand, Operation<ItemStack> original, DamageSource source) {
		if (source.isOf(DamageTypes.OUT_OF_WORLD)) { // handling for using a shulking totem in the offhand and a different totem in the mainhand
			LivingEntity livingEntity = (LivingEntity) (Object) this;
			ItemStack mainHandStack = livingEntity.getMainHandStack();
			ItemStack offHandStack = livingEntity.getOffHandStack();
			if (offHandStack.isOf(TotemicItems.TOTEM_OF_SHULKING) && !mainHandStack.isOf(TotemicItems.TOTEM_OF_SHULKING)) {
				return offHandStack;
			}
		}
		return original.call(instance, hand);
	}

	@WrapMethod(method = "tryUseTotem")
	private boolean totemicTides$atonementPreventsTotemUseGracePreventsLethalDamage(DamageSource source, Operation<Boolean> original) {
		if (this.hasStatusEffect(TotemicStatusEffects.GRACE)) { // prevents lethal damage once by setting the health to 1.0 if death is reached then removing the effect, returns true to skip totem checks
			LivingEntity livingEntity = (LivingEntity) (Object) this;
			livingEntity.setHealth(1.0f);
			livingEntity.removeStatusEffect(TotemicStatusEffects.GRACE);
			return true;
		} // grace takes priority over atonement :p
		else if (this.hasStatusEffect(TotemicStatusEffects.ATONEMENT)) { // completely skips tryUseTotem, fuck you head inject cancels
			return false;
		}
		return original.call(source);
	}

	@ModifyReturnValue(method = "isInvulnerableTo", at = @At("RETURN"))
	private boolean totemicTides$windSailingPreventsFallDamage(boolean original, DamageSource source) {
		if (source.isOf(DamageTypes.FALL) && TotemicEntityComponents.BREEZING.get(this).isWindSailing()) {
			return true;
		}
		return original;
	}

	@WrapOperation(method = "tryUseTotem", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;)Z"))
	private boolean totemicTides$addGraceToVanillaTotemOfUndying(LivingEntity instance, StatusEffectInstance effect, Operation<Boolean> original, @Local(ordinal = 0) ItemStack totemStack) {
		if (totemStack != null && totemStack.isOf(Items.TOTEM_OF_UNDYING) && effect.getEffectType() == StatusEffects.ABSORPTION) {
			this.addStatusEffect(new StatusEffectInstance(TotemicStatusEffects.GRACE, 200));
			return original.call(instance, new StatusEffectInstance(StatusEffects.ABSORPTION, effect.getDuration(), effect.getAmplifier() + 1)); // buffs vanilla totem's absorption too
		}
		return original.call(instance, effect);
	}

	@Inject(method = "damage", at = @At("RETURN"))
	private void totemicTides$inkedQuickDecaysAfterTakingDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		if (amount > 0.0f && cir.getReturnValue()) { // if damage is > 0 and damage is actually being done
			TotemicEntityComponents.INKED.maybeGet(this).ifPresent((inkedComponent -> {
				if (!inkedComponent.shouldDecayQuickly()) {
					inkedComponent.setDecayQuickly(true); // also set inked ticks to min of remaining ticks and whenever the splotches begin to turn transparent
				}
			}));
		}
	}

}