package com.theendercore.prettier_hitbox.client;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import net.fabricmc.api.ClientModInitializer;

public class PrettierHitboxesModClient implements ClientModInitializer {
	public static final String MODID = "prettier_hitboxes";
	public static PrettierHitboxesConfig CONFIG = ConfigApiJava.registerAndLoadConfig(PrettierHitboxesConfig::new, RegisterType.CLIENT);
	@Override
	public void onInitializeClient() {
	}
}