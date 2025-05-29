package net.yak.totemictides.init;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.yak.totemictides.TotemicTides;
import net.yak.totemictides.entity.AvatarOfRegeneratingEntity;
import net.yak.totemictides.entity.ElderGuardianSpikeEntity;
import net.yak.totemictides.entity.InkCloudEntity;

public class TotemicEntityTypes {

    public static final EntityType<ElderGuardianSpikeEntity> ELDER_GUARDIAN_SPIKE_ENTITY = Registry.register(Registries.ENTITY_TYPE,
            TotemicTides.id("elder_guardian_spike_entity"), EntityType.Builder.<ElderGuardianSpikeEntity>create(ElderGuardianSpikeEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5f, 0.5f).build());

    public static final EntityType<InkCloudEntity> INK_CLOUD_ENTITY = Registry.register(Registries.ENTITY_TYPE,
            TotemicTides.id("ink_cloud_entity"), EntityType.Builder.<InkCloudEntity>create(InkCloudEntity::new, SpawnGroup.MISC)
                    .dimensions(5f, 5f).build());

    public static final EntityType<AvatarOfRegeneratingEntity> AVATAR_OF_REGENERATING_ENTITY = Registry.register(Registries.ENTITY_TYPE,
            TotemicTides.id("avatar_of_regenerating"), EntityType.Builder.<AvatarOfRegeneratingEntity>create(AvatarOfRegeneratingEntity::new, SpawnGroup.MISC)
                    .dimensions(5f, 5f).build());


    public static void init() {

    }

}
