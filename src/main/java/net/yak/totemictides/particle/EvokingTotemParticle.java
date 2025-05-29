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
public class EvokingTotemParticle extends AnimatedParticle {
    public EvokingTotemParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider, 1.25F);
        this.velocityMultiplier = 0.6F;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        this.scale *= 0.75F;
        this.maxAge = 60 + this.random.nextInt(12);
        this.setSpriteForAge(spriteProvider);
        switch (this.random.nextInt(12)) { // random color (pulled from texture)
            case 0,1,2: this.setColor(0.24f, 0.23f, 0.21f); break;
            case 3,4: this.setColor(0.48f, 0.46f, 0.44f); break;
            case 5,6: this.setColor(0.35f, 0.34f, 0.32f); break;
            case 7,8: this.setColor(0.24f, 0.23f, 0.21f); break;
            case 9,10: this.setColor(0.16f, 0.15f, 0.14f); break;
            default: this.setColor(0.99f, 0.80f, 0.25f);
        }
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new EvokingTotemParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
        }
    }
}

