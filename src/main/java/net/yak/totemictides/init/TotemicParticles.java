package net.yak.totemictides.init;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.particle.Particle;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.yak.totemictides.TotemicTides;
import net.yak.totemictides.particle.*;

public class TotemicParticles {

    public static final SimpleParticleType TOTEM_OF_CREEPING_PARTICLE = registerParticle("totem_of_creeping", true, FabricParticleTypes.simple());
    public static final SimpleParticleType TOTEM_OF_BLAZING_PARTICLE = registerParticle("totem_of_blazing", true, FabricParticleTypes.simple());
    public static final SimpleParticleType TOTEM_OF_INKING_PARTICLE = registerParticle("totem_of_inking", true, FabricParticleTypes.simple());
    public static final SimpleParticleType TOTEM_OF_BREEZING_PARTICLE = registerParticle("totem_of_breezing", true, FabricParticleTypes.simple());
    public static final SimpleParticleType TOTEM_OF_REMATCHING_PARTICLE = registerParticle("totem_of_rematching", true, FabricParticleTypes.simple());
    public static final SimpleParticleType TOTEM_OF_REGENERATING_PARTICLE = registerParticle("totem_of_regenerating", true, FabricParticleTypes.simple());
    public static final SimpleParticleType TOTEM_OF_EVOKING_PARTICLE = registerParticle("totem_of_evoking", true, FabricParticleTypes.simple());
    public static final SimpleParticleType TOTEM_OF_SHULKING_PARTICLE = registerParticle("totem_of_shulking", true, FabricParticleTypes.simple());
    public static final SimpleParticleType INK_CLOUD_PARTICLE = registerParticle("ink_cloud", true, FabricParticleTypes.simple());


    public static void init() {

    }

    public static void initClient() {
        ParticleFactoryRegistry.getInstance().register(TOTEM_OF_CREEPING_PARTICLE, CreepingTotemParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(TOTEM_OF_BLAZING_PARTICLE, BlazingTotemParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(TOTEM_OF_INKING_PARTICLE, InkingTotemParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(TOTEM_OF_BREEZING_PARTICLE, BreezingTotemParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(TOTEM_OF_REMATCHING_PARTICLE, RematchingTotemParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(TOTEM_OF_REGENERATING_PARTICLE, RegeneratingTotemParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(TOTEM_OF_EVOKING_PARTICLE, EvokingTotemParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(TOTEM_OF_SHULKING_PARTICLE, ShulkingTotemParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(INK_CLOUD_PARTICLE, InkCloudParticle.Factory::new);

    }

    private static SimpleParticleType registerParticle(String name, boolean alwaysShow, SimpleParticleType particleType) {
        return Registry.register(Registries.PARTICLE_TYPE, TotemicTides.id(name), particleType);
    }
}
