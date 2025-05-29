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
public class BreezingTotemParticle extends AnimatedParticle {
    public BreezingTotemParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider, 1.25F);
        this.velocityMultiplier = 0.6F;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        this.scale *= 0.75F;
        this.maxAge = 60 + this.random.nextInt(12);
        this.setSpriteForAge(spriteProvider);
        switch (this.random.nextInt(12)) { // random color (pulled from texture)
            case 0,1,2: this.setColor(0.74f, 0.79f, 1.0f); break;
            case 3,4: this.setColor(0.83f, 0.86f, 1.0f); break;
            case 5,6: this.setColor(0.92f, 0.94f, 1.0f); break;
            case 7,8: this.setColor(0.99f, 0.60f, 0.51f); break;
            case 9,10: this.setColor(0.91f, 0.47f, 0.34f); break;
            default: this.setColor(0.61f, 0.27f, 0.16f);
        }
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new BreezingTotemParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
        }
    }
}

