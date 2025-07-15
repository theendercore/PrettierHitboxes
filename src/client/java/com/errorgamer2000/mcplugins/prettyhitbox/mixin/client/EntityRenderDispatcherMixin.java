package com.errorgamer2000.mcplugins.prettyhitbox.mixin.client;

import com.errorgamer2000.mcplugins.prettyhitbox.PrettyHitboxesConfig;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedColor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.errorgamer2000.mcplugins.prettyhitbox.PrettyHitboxesModClient.CONFIG;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
    @Shadow
    private static void drawVector(MatrixStack matrices, VertexConsumer vertexConsumers, Vector3f offset, Vec3d vec, int color) {
    }

    @Unique
    private static float clampedColorValue(int colorValue) {
        return Math.min(1.0F, Math.max(0.0F, colorValue / 255.0F));
    }

    @Unique
    private static boolean isTargeted(Entity entity) {
        if (MinecraftClient.getInstance().crosshairTarget != null && MinecraftClient.getInstance().crosshairTarget.getType() == HitResult.Type.ENTITY) {
            EntityHitResult target = (EntityHitResult) MinecraftClient.getInstance().crosshairTarget;
            Entity targetEntity = target.getEntity();
            return targetEntity.getUuid() == entity.getUuid();
        }

        return false;
    }

    @Unique
    private static void drawBox(MatrixStack matrices, VertexConsumer vertices, Box box, ValidatedColor color) {
        WorldRenderer.drawBox(matrices, vertices, box, clampedColorValue(color.r()), clampedColorValue(color.g()), clampedColorValue(color.b()), clampedColorValue(color.a()));
    }

    /**
     * @author ErrorGamer2000
     * @reason We need to completely redefine how hitboxes are drawn
     */
    @Inject(at = @At("HEAD"), method = "renderHitbox", cancellable = true)
    private static void renderHitbox(MatrixStack matrices, VertexConsumer vertices, Entity entity, float tickDelta, float red, float green, float blue, CallbackInfo ci) {
        Box box = entity.getBoundingBox().offset(-entity.getX(), -entity.getY(), -entity.getZ());

        ValidatedColor bboxColor = CONFIG.boundingBoxColor;
        ValidatedColor targetColor = CONFIG.entityTargetedColor;
        if (CONFIG.showBoundingBox) {
            if (!(entity instanceof EnderDragonEntity)) {
                ValidatedColor color = entity instanceof ItemEntity ? CONFIG.itemHitboxColor : bboxColor;
                if (CONFIG.differentColorWhenTargeted && isTargeted(entity)) color = targetColor;
                if (!(entity instanceof ItemEntity && !CONFIG.showItemHitboxes) && !(entity instanceof ThrownItemEntity && !CONFIG.showThrowableItemHitboxes) && !(entity instanceof BoatEntity && !CONFIG.showBoatHitboxes) && !((entity instanceof PaintingEntity && !CONFIG.showPaintingHitboxes) || (entity instanceof ItemFrameEntity && !CONFIG.showItemFrameHitboxes)))
                    drawBox(matrices, vertices, box, color);

            } else if (!CONFIG.hideBigDragonBox) {
                EnderDragonPart[] parts = ((EnderDragonEntity) entity).getBodyParts();
                int partNum = parts.length;

                boolean targeted = false;
                for (int i = 0; i < partNum && !targeted; ++i) {
                    if (isTargeted(parts[i])) targeted = true;
                }

                ValidatedColor color = bboxColor;
                if (targeted) color = targetColor;
                drawBox(matrices, vertices, box, color);
            }
        }

        if (entity instanceof EnderDragonEntity) {
            double d = -MathHelper.lerp(tickDelta, entity.lastRenderX, entity.getX());
            double e = -MathHelper.lerp(tickDelta, entity.lastRenderY, entity.getY());
            double f = -MathHelper.lerp(tickDelta, entity.lastRenderZ, entity.getZ());
            EnderDragonPart[] partsArray = ((EnderDragonEntity) entity).getBodyParts();

            for (EnderDragonPart dragonPart : partsArray) {
                ValidatedColor color = CONFIG.dragonPartColor;
                if (CONFIG.differentColorWhenTargeted && isTargeted(dragonPart)) color = targetColor;
                matrices.push();
                double g = d + MathHelper.lerp(tickDelta, dragonPart.lastRenderX, dragonPart.getX());
                double h = e + MathHelper.lerp(tickDelta, dragonPart.lastRenderY, dragonPart.getY());
                double i = f + MathHelper.lerp(tickDelta, dragonPart.lastRenderZ, dragonPart.getZ());
                matrices.translate(g, h, i);
                if (CONFIG.showBoundingBox)
                    drawBox(matrices, vertices, dragonPart.getBoundingBox().offset(-dragonPart.getX(), -dragonPart.getY(), -dragonPart.getZ()), color);
                matrices.pop();
            }
        }

        if (entity instanceof LivingEntity && CONFIG.showEyeHeight) {
            ValidatedColor eyeHeightColor = CONFIG.eyeHeightColor;
            float j = 0.01F;
            WorldRenderer.drawBox(matrices, vertices, box.minX, entity.getStandingEyeHeight() - 0.01F, box.minZ, box.maxX, entity.getStandingEyeHeight() + 0.01F, box.maxZ, clampedColorValue(eyeHeightColor.r()), clampedColorValue(eyeHeightColor.g()), clampedColorValue(eyeHeightColor.b()), clampedColorValue(eyeHeightColor.a()));
        }

        if (CONFIG.showEntityRotationVector && !(entity instanceof ItemEntity && !CONFIG.showItemHitboxes) && !(entity instanceof ThrownItemEntity && !CONFIG.showThrowableItemHitboxes) && !(entity instanceof BoatEntity && !CONFIG.showBoatHitboxes) && !((entity instanceof PaintingEntity && !CONFIG.showPaintingHitboxes) || (entity instanceof ItemFrameEntity && !CONFIG.showItemFrameHitboxes))) {
            drawVector(matrices, vertices, new Vector3f(0.0F, entity.getStandingEyeHeight(), 0.0F), entity.getRotationVec(tickDelta).multiply(2.0F), CONFIG.entityRotationVectorColor.toInt());
        }

        ci.cancel();
    }
}
