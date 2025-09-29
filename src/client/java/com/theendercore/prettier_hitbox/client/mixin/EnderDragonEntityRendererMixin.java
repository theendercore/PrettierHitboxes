package com.theendercore.prettier_hitbox.client.mixin;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.sugar.Local;
import com.theendercore.prettier_hitbox.client.utils.HitboxWithAlpha;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.entity.state.HitboxRenderState;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.theendercore.prettier_hitbox.client.PrettierHitboxesModClient.CONFIG;
import static com.theendercore.prettier_hitbox.client.utils.PHHelpers.isTargeted;

@Mixin(EnderDragonRenderer.class)
public abstract class EnderDragonEntityRendererMixin {


    @Inject(method = "extractAdditionalHitboxes(Lnet/minecraft/world/entity/boss/enderdragon/EnderDragon;Lcom/google/common/collect/ImmutableList$Builder;F)V", at = @At("HEAD"), cancellable = true)
    void boxDisabler(EnderDragon enderDragonEntity, ImmutableList.Builder<HitboxRenderState> builder, float f, CallbackInfo ci) {
        if (!CONFIG.showBoundingBox) ci.cancel();
    }

    @ModifyArg(method = "extractAdditionalHitboxes(Lnet/minecraft/world/entity/boss/enderdragon/EnderDragon;Lcom/google/common/collect/ImmutableList$Builder;F)V", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableList$Builder;add(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList$Builder;", remap = false))
    private <E> E run(E element, @Local EnderDragonPart part) {
        if (element instanceof HitboxWithAlpha box) {
            box.prettier_hitboxes_setColor((isTargeted(part)) ? CONFIG.entityTargetedColor : CONFIG.dragonPartColor);
        }
        return element;
    }
}