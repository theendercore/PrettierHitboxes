package com.theendercore.prettier_hitbox.client.utils;

import com.google.common.collect.ImmutableList;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedColor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.state.EntityHitbox;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.entity.vehicle.AbstractBoatEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import static com.theendercore.prettier_hitbox.client.PrettierHitboxesModClient.CONFIG;

public interface PHHelpers {
    static float clampColor(int colorValue) {
        return Math.min(1.0F, Math.max(0.0F, colorValue / 255.0F));
    }

    static boolean isTargeted(Entity entity) {
        if (!CONFIG.differentColorWhenTargeted) return false;
        if (MinecraftClient.getInstance().targetedEntity != null) {
            Entity targetEntity = MinecraftClient.getInstance().targetedEntity;
            return targetEntity.getUuid() == entity.getUuid();
        }
        return false;
    }

    static <T extends Entity> ImmutableList.Builder<EntityHitbox> createBuilder(T entity) {
        ImmutableList.Builder<EntityHitbox> builder = new ImmutableList.Builder<>();
        if (!CONFIG.showBoundingBox) return builder;
        if (!CONFIG.showItemHitboxes && entity instanceof ItemEntity) return builder;
        if (!CONFIG.showItemFrameHitboxes && entity instanceof ItemFrameEntity) return builder;
        if (!CONFIG.showPaintingHitboxes && entity instanceof PaintingEntity) return builder;
        if (!CONFIG.showBoatHitboxes && entity instanceof AbstractBoatEntity) return builder;
        if (!CONFIG.showThrowableItemHitboxes && entity instanceof ThrownEntity) return builder;
        var box = getEntityHitbox(entity);
        if (box != null) builder.add(box);
        Entity entity2 = entity.getVehicle();
        if (entity2 != null) {
            float f = Math.min(entity2.getWidth(), entity.getWidth()) / 2.0F;
            float g = 0.0625F;
            Vec3d vec3d = entity2.getPassengerRidingPos(entity).subtract(entity.getPos());
            EntityHitbox entityHitbox2 = new EntityHitbox(vec3d.x - f, vec3d.y, vec3d.z - f, vec3d.x + f, vec3d.y + 0.0625, vec3d.z + f, 1.0F, 1.0F, 0.0F);
            builder.add(entityHitbox2);
        }

        return builder;
    }

    @SuppressWarnings("ConstantValue")
    static <T extends Entity> EntityHitbox getEntityHitbox(T entity) {
        if (entity instanceof EnderDragonEntity && CONFIG.hideBigDragonBox) return null;
        Box box = entity.getBoundingBox();
        ValidatedColor color;
        if (isTargeted(entity)) {
            color = CONFIG.entityTargetedColor;
        } else {
            color = (entity instanceof ItemEntity) ? CONFIG.itemHitboxColor : CONFIG.boundingBoxColor;
        }

        var hitBox = new EntityHitbox(box.minX - entity.getX(), box.minY - entity.getY(), box.minZ - entity.getZ(), box.maxX - entity.getX(), box.maxY - entity.getY(), box.maxZ - entity.getZ(), clampColor(color.r()), clampColor(color.g()), clampColor(color.b()));
        if ((Object) hitBox instanceof HitboxWithAlpha alphaBox) {
            alphaBox.prettier_hitboxes_setAlpha(clampColor(color.a()));
        }
        return hitBox;
    }
}
