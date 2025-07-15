package com.theendercore.prettier_hitbox.client;

import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedColor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;

public interface PHHelpers {
    static float clampedColorValue(int colorValue) {
        return Math.min(1.0F, Math.max(0.0F, colorValue / 255.0F));
    }

    static boolean isTargeted(Entity entity) {
        if (MinecraftClient.getInstance().crosshairTarget != null && MinecraftClient.getInstance().crosshairTarget.getType() == HitResult.Type.ENTITY) {
            EntityHitResult target = (EntityHitResult) MinecraftClient.getInstance().crosshairTarget;
            Entity targetEntity = target.getEntity();
            return targetEntity.getUuid() == entity.getUuid();
        }

        return false;
    }

    static void drawBox(MatrixStack matrices, VertexConsumer vertices, Box box, ValidatedColor color) {
        WorldRenderer.drawBox(matrices, vertices, box, clampedColorValue(color.r()), clampedColorValue(color.g()), clampedColorValue(color.b()), clampedColorValue(color.a()));
    }
}
