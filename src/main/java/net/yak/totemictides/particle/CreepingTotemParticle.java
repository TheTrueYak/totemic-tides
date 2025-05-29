package net.yak.totemictides.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.AnimatedParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;

@Environment(EnvType.CLIENT)
public class CreepingTotemParticle extends AnimatedParticle {
    public CreepingTotemParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider, 1.25F);
        this.velocityMultiplier = 0.6F;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        this.scale *= 0.75F;
        this.maxAge = 60 + this.random.nextInt(12);
        this.setSpriteForAge(spriteProvider);
        switch (this.random.nextInt(10)) { // random color (pulled from texture)
            case 0,1,2: this.setColor(0.42f, 0.61f, 0.39f); break;
            case 3,4: this.setColor(0.32f, 0.52f, 0.36f); break;
            case 5,6: this.setColor(0.19f, 0.30f, 0.24f); break;
            case 7,8: this.setColor(0.26f, 0.43f, 0.33f); break;
            default: this.setColor(0.11f, 0.19f, 0.17f);
        }

    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new CreepingTotemParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
        }
    }
}

