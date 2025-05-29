package net.yak.totemictides.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.yak.totemictides.TotemicTides;
import net.yak.totemictides.init.TotemicEntityComponents;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

public class BlazingAuraFeatureRenderer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {

    private static final Identifier BLAZING_AURA = TotemicTides.id("textures/entity/blazing_aura.png");
    private final Quaternionf rotation;

    public BlazingAuraFeatureRenderer(FeatureRendererContext<T, M> context) {
        super(context);
        this.rotation = new Quaternionf().rotationX((float) Math.toRadians(90));
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        TotemicEntityComponents.BLAZING.maybeGet(entity).ifPresent((blazingComponent -> {
            //if (blazingComponent.getBlazingTicks() > 0) {
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(BLAZING_AURA));
            int overlay = LivingEntityRenderer.getOverlay(entity, 0.0F);

            matrices.push();
            //matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
            //matrices.scale(2f, 2f, 2f);
            Vec3d cameraPos = MinecraftClient.getInstance().getCameraEntity() != null ? MinecraftClient.getInstance().getCameraEntity().getCameraPosVec(tickDelta) : entity.getCameraPosVec(tickDelta);
            //Vec3d cameraPos = entity.getCameraPosVec(tickDelta);
            float f = (float) (MathHelper.lerp(tickDelta, entity.prevX, entity.getX()) - cameraPos.getX());
            float g = (float) (MathHelper.lerp(tickDelta, entity.prevY, entity.getY()) - cameraPos.getY());
            float h = (float) (MathHelper.lerp(tickDelta, entity.prevZ, entity.getZ()) - cameraPos.getZ());
            Vector3f[] vertices = new Vector3f[]{
                    new Vector3f(-1.0F, -1.0F, 0.0F),
                    new Vector3f(-1.0F, 1.0F, 0.0F),
                    new Vector3f(1.0F, 1.0F, 0.0F),
                    new Vector3f(1.0F, -1.0F, 0.0F)
            };
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            RenderSystem.depthMask(false);
            Quaternionf dynamicRotation = new Quaternionf();
            dynamicRotation.mul(this.rotation);
            for (int i = 0; i < 4; i++) {
                Vector3f vertex = vertices[i];
                vertex.rotate(dynamicRotation);
                //vertex.mul(particleSize);
                vertex.add(f, g, h);
            }
            this.vertex(vertexConsumer, vertices[0], 1.0f, 1.0f, light, overlay);
            this.vertex(vertexConsumer, vertices[1], 1.0f, 0.0f, light, overlay);
            this.vertex(vertexConsumer, vertices[2], 0.0f, 0.0f, light, overlay);
            this.vertex(vertexConsumer, vertices[3], 0.0f, 1.0f, light, overlay);
            this.vertex(vertexConsumer, vertices[0], 1.0f, 1.0f, light, overlay);
            this.vertex(vertexConsumer, vertices[3], 0.0f, 1.0f, light, overlay);
            this.vertex(vertexConsumer, vertices[2], 0.0f, 0.0f, light, overlay);
            this.vertex(vertexConsumer, vertices[1], 1.0f, 0.0f, light, overlay);
            RenderSystem.depthMask(true);
            RenderSystem.defaultBlendFunc();
            //vertexConsumer.vertex((float) entity.getX(),(float) entity.getY(),(float) entity.getZ(), 0xFFFFFF, 0, 1, overlay, light, (float) entity.getX(),(float) entity.getY(),(float) entity.getZ());
            //vertexConsumer.light(light);
            //this.getContextModel().render(matrices, vertexConsumer, light, overlay);
            //((PlayerEntityModel)this.getContextModel()).renderEars(matrices, vertexConsumer, light, m);
            matrices.pop();
        }));
    }

    private void vertex(VertexConsumer vertexConsumer, Vector3f position, float u, float v, int light, int overlay) {
        vertexConsumer.vertex(position.x(), position.y() + 0.05f, position.z())
                .texture(u, v).color(1.0f, 1.0f, 1.0f, 1.0f).light(light)
                .overlay(overlay).normal(position.x(), position.y() + 0.05f, position.z());
    }
}
