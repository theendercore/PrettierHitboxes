package com.theendercore.prettier_hitbox.client.mixin;

import com.theendercore.prettier_hitbox.client.utils.HitboxWithAlpha;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedColor;
import net.minecraft.client.render.entity.state.EntityHitbox;
import org.spongepowered.asm.mixin.*;

import static com.theendercore.prettier_hitbox.client.utils.PHHelpers.clampColor;

@Mixin(EntityHitbox.class)
public class EntityHitboxMixin implements HitboxWithAlpha {
    @Mutable @Shadow @Final private float red;
    @Mutable @Shadow @Final private float green;
    @Mutable @Shadow @Final private float blue;
    @Unique private float prettier_hitboxes_alpha = 1.0f;


    @Override
    public void prettier_hitboxes_setColor(ValidatedColor value) {
        red = clampColor(value.r());
        green = clampColor(value.g());
        blue = clampColor(value.b());
        prettier_hitboxes_setAlpha(clampColor(value.a()));
    }

    @Override
    public float prettier_hitboxes_getAlpha() {
        return prettier_hitboxes_alpha;
    }

    @Override
    public void prettier_hitboxes_setAlpha(float value) {
        prettier_hitboxes_alpha = value;
    }
}
