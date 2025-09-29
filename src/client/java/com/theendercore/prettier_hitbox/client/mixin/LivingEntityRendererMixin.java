package com.theendercore.prettier_hitbox.client.mixin;

import com.google.common.collect.ImmutableList;
import com.theendercore.prettier_hitbox.client.utils.HitboxWithAlpha;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.HitboxRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.theendercore.prettier_hitbox.client.PrettierHitboxesModClient.CONFIG;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {
    @Inject(method = "extractAdditionalHitboxes(Lnet/minecraft/world/entity/LivingEntity;Lcom/google/common/collect/ImmutableList$Builder;F)V", at = @At("HEAD"), cancellable = true)
    void boxDisabler(LivingEntity enderDragonEntity, ImmutableList.Builder<HitboxRenderState> builder, float f, CallbackInfo ci) {
        if (!CONFIG.showEyeHeight) ci.cancel();
    }

    @ModifyArg(method = "extractAdditionalHitboxes(Lnet/minecraft/world/entity/LivingEntity;Lcom/google/common/collect/ImmutableList$Builder;F)V", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableList$Builder;add(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList$Builder;", remap = false))
    private <E> E run(E element) {
        if (element instanceof HitboxWithAlpha box) {
            box.prettier_hitboxes_setColor(CONFIG.eyeHeightColor);
        }
        return element;
    }
}