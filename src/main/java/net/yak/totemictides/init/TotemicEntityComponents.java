package net.yak.totemictides.init;

import net.minecraft.entity.LivingEntity;
import net.yak.totemictides.TotemicTides;
import net.yak.totemictides.component.BlazingComponent;
import net.yak.totemictides.component.BreezingComponent;
import net.yak.totemictides.component.InkedComponent;
import net.yak.totemictides.component.RematchingComponent;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

public class TotemicEntityComponents implements EntityComponentInitializer {

    public static final ComponentKey<BlazingComponent> BLAZING = ComponentRegistry.getOrCreate(TotemicTides.id("blazing"), BlazingComponent.class);
    public static final ComponentKey<InkedComponent> INKED = ComponentRegistry.getOrCreate(TotemicTides.id("inked"), InkedComponent.class);
    public static final ComponentKey<BreezingComponent> BREEZING = ComponentRegistry.getOrCreate(TotemicTides.id("breezing"), BreezingComponent.class);
    public static final ComponentKey<RematchingComponent> REMATCHING = ComponentRegistry.getOrCreate(TotemicTides.id("rematching"), RematchingComponent.class);


    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry entityComponentFactoryRegistry) {
        entityComponentFactoryRegistry.registerFor(LivingEntity.class, BLAZING, BlazingComponent::new);
        entityComponentFactoryRegistry.registerFor(LivingEntity.class, INKED, InkedComponent::new);
        entityComponentFactoryRegistry.registerFor(LivingEntity.class, BREEZING, BreezingComponent::new);
        entityComponentFactoryRegistry.registerForPlayers(REMATCHING, RematchingComponent::new, RespawnCopyStrategy.ALWAYS_COPY);



    }
}
