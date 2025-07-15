package com.errorgamer2000.mcplugins.prettyhitbox;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;

public class PrettyHitboxesModClient implements ClientModInitializer {
	public static final String MODID = "pretty-hitboxes";
	public static PrettyHitboxesConfig CONFIG = ConfigApiJava.registerAndLoadConfig(PrettyHitboxesConfig::new, RegisterType.CLIENT);
	@Override
	public void onInitializeClient() {
	}
}