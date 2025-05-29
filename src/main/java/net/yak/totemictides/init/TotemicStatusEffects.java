package net.yak.totemictides.init;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.yak.totemictides.TotemicTides;
import net.yak.totemictides.status_effect.TotemicStatusEffect;

public class TotemicStatusEffects {

    public static final RegistryEntry<StatusEffect> ATONEMENT = register("atonement", new TotemicStatusEffect(StatusEffectCategory.HARMFUL, 0xFC0851));
    public static final RegistryEntry<StatusEffect> GRACE = register("grace", new TotemicStatusEffect(StatusEffectCategory.BENEFICIAL, 0xDE9D2C));
    public static final RegistryEntry<StatusEffect> BLAZING_EMPOWERMENT = register("blazing_empowerment", new TotemicStatusEffect(StatusEffectCategory.BENEFICIAL, 0xD17123));
    public static final RegistryEntry<StatusEffect> RADIANCE = register("radiance", new TotemicStatusEffect(StatusEffectCategory.BENEFICIAL, 0xBF2424));

    public static void init() {

    }

    private static RegistryEntry<StatusEffect> register(String id, StatusEffect statusEffect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, TotemicTides.id(id), statusEffect);
    }
}
