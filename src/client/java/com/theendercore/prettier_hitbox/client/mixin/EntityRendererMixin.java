package com.theendercore.prettier_hitbox.client.mixin;

import com.google.common.collect.ImmutableList;
import com.theendercore.prettier_hitbox.client.utils.PHHelpers;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityHitbox;
import net.minecraft.client.render.entity.state.EntityHitboxAndView;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity> {
    @Shadow
    protected abstract void appendHitboxes(T entity, ImmutableList.Builder<EntityHitbox> builder, float tickProgress);

    @Inject(method = "createHitbox", at = @At("HEAD"), cancellable = true)
    private void run(T entity, float tickProgress, boolean green, CallbackInfoReturnable<EntityHitboxAndView> cir) {
        ImmutableList.Builder<EntityHitbox> builder = PHHelpers.createBuilder(entity);

        this.appendHitboxes(entity, builder, tickProgress);
        Vec3d vec3d2 = entity.getRotationVec(tickProgress);
        cir.setReturnValue(new EntityHitboxAndView(vec3d2.x, vec3d2.y, vec3d2.z, builder.build()));
    }
}