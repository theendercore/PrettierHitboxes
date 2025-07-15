package com.theendercore.prettier_hitbox.client.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.theendercore.prettier_hitbox.client.utils.HitboxWithAlpha;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.state.EntityHitbox;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static com.theendercore.prettier_hitbox.client.PrettierHitboxesModClient.CONFIG;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
    @WrapWithCondition(method = "renderHitboxes(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/entity/state/EntityHitboxAndView;Lnet/minecraft/client/render/VertexConsumer;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexRendering;drawVector(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lorg/joml/Vector3f;Lnet/minecraft/util/math/Vec3d;I)V"))
    private static boolean renderEntityRotationVector(MatrixStack matrices, VertexConsumer vertexConsumers, Vector3f offset, Vec3d vec, int argb) {
        return CONFIG.showEntityRotationVector;
    }

    @ModifyArg(method = "renderHitboxes(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/entity/state/EntityHitboxAndView;Lnet/minecraft/client/render/VertexConsumer;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexRendering;drawVector(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lorg/joml/Vector3f;Lnet/minecraft/util/math/Vec3d;I)V"))
    private static int setEntityRotationVectorColor(int color) {
        return CONFIG.entityRotationVectorColor.toInt();
    }

    @SuppressWarnings("ConstantValue")
    @ModifyArg(method = "renderHitbox", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexRendering;drawBox(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;DDDDDDFFFF)V"), index = 11)
    private static float modifyAlpha(float alpha, @Local(argsOnly = true) EntityHitbox hitbox) {
        return ((Object) hitbox instanceof HitboxWithAlpha box) ? box.prettier_hitboxes_getAlpha() : alpha;
    }
}
