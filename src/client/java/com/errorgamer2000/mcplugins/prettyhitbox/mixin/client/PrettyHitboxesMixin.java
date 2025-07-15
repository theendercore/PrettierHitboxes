package com.errorgamer2000.mcplugins.prettyhitbox.mixin.client;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.errorgamer2000.mcplugins.prettyhitbox.PrettyHitboxesModClient.CONFIG;

@Mixin(MinecraftClient.class)
public class PrettyHitboxesMixin {
	@Inject(at = @At("HEAD"), method = "run")
	private void run(CallbackInfo info) {
		MinecraftClient.getInstance().getEntityRenderDispatcher().setRenderHitboxes(CONFIG.hitboxesEnabledByDefault);
	}
}