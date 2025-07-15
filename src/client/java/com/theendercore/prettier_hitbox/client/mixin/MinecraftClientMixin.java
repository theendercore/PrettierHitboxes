package com.theendercore.prettier_hitbox.client.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.theendercore.prettier_hitbox.client.PrettierHitboxesModClient.CONFIG;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
	@Inject(at = @At("HEAD"), method = "run")
	private void run(CallbackInfo info) {
		MinecraftClient.getInstance().getEntityRenderDispatcher().setRenderHitboxes(CONFIG.hitboxesEnabledByDefault);
	}
}