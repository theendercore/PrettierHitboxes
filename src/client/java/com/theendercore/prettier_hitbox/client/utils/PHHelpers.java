package com.theendercore.prettier_hitbox.client.utils;

import com.google.common.collect.ImmutableList;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.state.HitboxRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.entity.vehicle.AbstractBoat;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import static com.theendercore.prettier_hitbox.client.PrettierHitboxesModClient.CONFIG;

public interface PHHelpers {
    static float clampColor(int colorValue) {
        return Math.min(1.0F, Math.max(0.0F, colorValue / 255.0F));
    }

    static boolean isTargeted(Entity entity) {
        if (!CONFIG.differentColorWhenTargeted) return false;
        if (Minecraft.getInstance().crosshairPickEntity != null) {
            Entity targetEntity = Minecraft.getInstance().crosshairPickEntity;
            return targetEntity.getUUID() == entity.getUUID();
        }
        return false;
    }

    static <T extends Entity> ImmutableList.Builder<HitboxRenderState> createBuilder(T entity) {
        ImmutableList.Builder<HitboxRenderState> builder = new ImmutableList.Builder<>();
        if (!CONFIG.showBoundingBox) return builder;
        if (!CONFIG.showItemHitboxes && entity instanceof ItemEntity) return builder;
        if (!CONFIG.showItemFrameHitboxes && entity instanceof ItemFrame) return builder;
        if (!CONFIG.showPaintingHitboxes && entity instanceof Painting) return builder;
        if (!CONFIG.showBoatHitboxes && entity instanceof AbstractBoat) return builder;
        if (!CONFIG.showThrowableItemHitboxes && entity instanceof ThrowableProjectile) return builder;
        var box = getEntityHitbox(entity);
        if (box != null) builder.add(box);
        Entity entity2 = entity.getVehicle();
        if (entity2 != null) {
            float f = Math.min(entity2.getBbWidth(), entity.getBbWidth()) / 2.0F;
            Vec3 vec3d = entity2.getPassengerRidingPosition(entity).subtract(entity.position());
            HitboxRenderState entityHitbox2 = new HitboxRenderState(vec3d.x - f, vec3d.y, vec3d.z - f, vec3d.x + f, vec3d.y + 0.0625, vec3d.z + f, 1.0F, 1.0F, 0.0F);
            builder.add(entityHitbox2);
        }

        return builder;
    }

    @SuppressWarnings("ConstantValue")
    static <T extends Entity> HitboxRenderState getEntityHitbox(T entity) {
        if (entity instanceof EnderDragon && CONFIG.hideBigDragonBox) return null;
        AABB box = entity.getBoundingBox();
        ValidatedColor color;
        if (isTargeted(entity)) {
            color = CONFIG.entityTargetedColor;
        } else {
            color = (entity instanceof ItemEntity) ? CONFIG.itemHitboxColor : CONFIG.boundingBoxColor;
        }

        var hitBox = new HitboxRenderState(box.minX - entity.getX(), box.minY - entity.getY(), box.minZ - entity.getZ(), box.maxX - entity.getX(), box.maxY - entity.getY(), box.maxZ - entity.getZ(), clampColor(color.r()), clampColor(color.g()), clampColor(color.b()));
        if ((Object) hitBox instanceof HitboxWithAlpha alphaBox) {
            alphaBox.prettier_hitboxes_setAlpha(clampColor(color.a()));
        }
        return hitBox;
    }
}
