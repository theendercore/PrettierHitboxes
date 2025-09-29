package com.theendercore.prettier_hitbox.client.mixin;

import com.google.common.collect.ImmutableList;
import com.theendercore.prettier_hitbox.client.utils.PHHelpers;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.HitboxRenderState;
import net.minecraft.client.renderer.entity.state.HitboxesRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity> {


    @Shadow
    protected abstract void extractAdditionalHitboxes(T entity, ImmutableList.Builder<HitboxRenderState> builder, float f);

    @Inject(method = "extractHitboxes(Lnet/minecraft/world/entity/Entity;FZ)Lnet/minecraft/client/renderer/entity/state/HitboxesRenderState;", at = @At("HEAD"), cancellable = true)
    private void run(T entity, float tickProgress, boolean green, CallbackInfoReturnable<HitboxesRenderState> cir) {
        ImmutableList.Builder<HitboxRenderState> builder = PHHelpers.createBuilder(entity);

        extractAdditionalHitboxes(entity, builder, tickProgress);
        Vec3 vec3d2 = entity.getViewVector(tickProgress);
        cir.setReturnValue(new HitboxesRenderState(vec3d2.x, vec3d2.y, vec3d2.z, builder.build()));
    }
}