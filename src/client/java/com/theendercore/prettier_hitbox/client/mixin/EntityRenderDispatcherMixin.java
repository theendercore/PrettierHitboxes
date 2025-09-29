package com.theendercore.prettier_hitbox.client.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.theendercore.prettier_hitbox.client.utils.HitboxWithAlpha;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.state.HitboxRenderState;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static com.theendercore.prettier_hitbox.client.PrettierHitboxesModClient.CONFIG;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
    @WrapWithCondition(method = "renderHitboxesAndViewVector", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ShapeRenderer;renderVector(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lorg/joml/Vector3f;Lnet/minecraft/world/phys/Vec3;I)V"))
    private static boolean renderEntityRotationVector(PoseStack matrices, VertexConsumer vertexConsumers, Vector3f offset, Vec3 vec, int argb) {
        return CONFIG.showEntityRotationVector;
    }

    @ModifyArg(method = "renderHitboxesAndViewVector", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ShapeRenderer;renderVector(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lorg/joml/Vector3f;Lnet/minecraft/world/phys/Vec3;I)V"))
    private static int setEntityRotationVectorColor(int color) {
        return CONFIG.entityRotationVectorColor.toInt();
    }

    @SuppressWarnings("ConstantValue")
    @ModifyArg(method = "renderHitbox", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ShapeRenderer;renderLineBox(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;DDDDDDFFFF)V"), index = 11)
    private static float modifyAlpha(float alpha, @Local(argsOnly = true) HitboxRenderState hitbox) {
        return ((Object) hitbox instanceof HitboxWithAlpha box) ? box.prettier_hitboxes_getAlpha() : alpha;
    }
}
