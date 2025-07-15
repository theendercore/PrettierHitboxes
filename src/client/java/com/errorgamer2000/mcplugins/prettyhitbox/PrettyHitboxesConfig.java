package com.errorgamer2000.mcplugins.prettyhitbox;

import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedColor;
import net.minecraft.util.Identifier;

import static com.errorgamer2000.mcplugins.prettyhitbox.PrettyHitboxesModClient.MODID;

public class PrettyHitboxesConfig extends Config {
    public PrettyHitboxesConfig() {
        super(Identifier.of(MODID, MODID));
    }

    @SuppressWarnings("unused")
    public ConfigGroup features = new ConfigGroup("features");
    public boolean hitboxesEnabledByDefault = false;
    public boolean hideBigDragonBox = false;
    public boolean showBoundingBox = true;
    public boolean showEyeHeight = true;
    public boolean showEntityRotationVector = true;
    public boolean differentColorWhenTargeted = false;
    public boolean showItemHitboxes = true;
    public boolean showItemFrameHitboxes = true;
    public boolean showPaintingHitboxes = true;
    public boolean showBoatHitboxes = true;
    @ConfigGroup.Pop
    public boolean showThrowableItemHitboxes = true;

    @SuppressWarnings("unused")
    public ConfigGroup colors = new ConfigGroup("colors");
    public ValidatedColor boundingBoxColor = new ValidatedColor(true);
    public ValidatedColor dragonPartColor = new ValidatedColor(0, 255, 0, 255);
    public ValidatedColor eyeHeightColor = new ValidatedColor(255, 0, 0, 255);
    public ValidatedColor entityRotationVectorColor = new ValidatedColor(0, 0, 255, 255);
    public ValidatedColor entityTargetedColor = new ValidatedColor(100, 100, 100);
    @ConfigGroup.Pop
    public ValidatedColor itemHitboxColor = new ValidatedColor(true);
}
