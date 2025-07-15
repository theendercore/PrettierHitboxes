package com.theendercore.prettier_hitbox.client.mixin;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.sugar.Local;
import com.theendercore.prettier_hitbox.client.utils.HitboxWithAlpha;
import net.minecraft.client.render.entity.EnderDragonEntityRenderer;
import net.minecraft.client.render.entity.state.EntityHitbox;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.theendercore.prettier_hitbox.client.PrettierHitboxesModClient.CONFIG;
import static com.theendercore.prettier_hitbox.client.utils.PHHelpers.isTargeted;

@Mixin(EnderDragonEntityRenderer.class)
public abstract class EnderDragonEntityRendererMixin<T extends LivingEntity> {


    @Inject(method = "appendHitboxes(Lnet/minecraft/entity/boss/dragon/EnderDragonEntity;Lcom/google/common/collect/ImmutableList$Builder;F)V", at = @At("HEAD"), cancellable = true)
    void boxDisabler(EnderDragonEntity enderDragonEntity, ImmutableList.Builder<EntityHitbox> builder, float f, CallbackInfo ci) {
        if (!CONFIG.showBoundingBox) ci.cancel();
    }

    @ModifyArg(method = "appendHitboxes(Lnet/minecraft/entity/boss/dragon/EnderDragonEntity;Lcom/google/common/collect/ImmutableList$Builder;F)V", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableList$Builder;add(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList$Builder;", remap = false))
    private <E> E run(E element, @Local EnderDragonPart part) {
        if (element instanceof HitboxWithAlpha box) {
            box.prettier_hitboxes_setColor((isTargeted(part)) ? CONFIG.entityTargetedColor : CONFIG.dragonPartColor);
        }
        return element;
    }
}