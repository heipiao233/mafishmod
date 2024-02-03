package net.jiang.tutorialmod.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.block.SkullBlock;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

import java.util.List;


public class KeyInputHandler {
    public static final String KEY_CATEGORY_TUTORIAL = "key.category.tutorialmod.tutorial";
    public static final String KEY_THROW_ITEM = "key.tutorialmod.throw_item";

    public static KeyBinding throwingKey;

    private static float throwPower = 0.0f;
    private static boolean isCharging = false;
    private static final float maxThrowPower = 2.0f; // 最大力度上限
    private static long chargeStartTime = 0;
    private static long chargeEndTime = 0;

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (throwingKey.isPressed()) {
                if (!isCharging) {
                    // Key was just pressed, start charging
                    chargeStartTime = System.currentTimeMillis();
                    isCharging = true;
                } else {
                    // Key is still pressed, update charge power
                    chargeEndTime = System.currentTimeMillis();
                    long chargeDuration = chargeEndTime - chargeStartTime;

                    // Calculate charge power based on duration (with max limit)
                    throwPower = Math.min((float) (chargeDuration / 1000.0), maxThrowPower);

                    // Display charge power to player (you can use a game overlay or chat message)
                    client.player.sendMessage(Text.literal("Charging: " + throwPower), true);
                }
            } else if (isCharging) {
                // Key was released, throw the item with the charged power
                chargeEndTime = System.currentTimeMillis();
                long chargeDuration = chargeEndTime - chargeStartTime;

                // Calculate charge power based on duration (with max limit)
                throwPower = Math.min((float) (chargeDuration / 1000.0), maxThrowPower);

                // Throw the item with the calculated power
                throwItemWithPower(client.player, throwPower,client.world);
                client.player.sendMessage(Text.literal("DIU"), true);

                // Reset variables
                isCharging = false;
                chargeStartTime = 0;
                chargeEndTime = 0;
            }
        });
    }

    public static void register() {
        throwingKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_THROW_ITEM,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_Y,
                KEY_CATEGORY_TUTORIAL
        ));

        registerKeyInputs();
    }

    private static void throwItemWithPower(ClientPlayerEntity player, float power, World world) {
        if (power > 0.0f) {

//            ItemStack itemStackToThrow = player.getMainHandStack(); // 获取玩家手持物品
//
//            ItemEntity itemEntity = new ItemEntity(world, player.getX(), player.getEyeY(), player.getZ(), itemStackToThrow);
//
//            world.spawnEntity(itemEntity);
//
                Vec3d direction = player.getRotationVector(); // 获取玩家面朝方向
//
//            player.dropSelectedItem(false);


            // 获取玩家手上的选定物品堆栈
            ItemStack itemStackToThrow = player.getStackInHand(Hand.MAIN_HAND);

            // 发送丢弃物品的操作
            player.dropSelectedItem(false);

            // 获取丢出的物品实体
            Vec3d playerPos = player.getPos();
            Entity itemEntity = null;

            // 定义一个用于检测的区域，以玩家为中心
            Box detectionBox = new Box(player.getBlockPos().add(-5, -5, -5), player.getBlockPos().add(5, 5, 5));

            // 获取玩家周围的实体（排除玩家自己）
            List<Entity> nearbyEntities = world.getOtherEntities(player, detectionBox);

            for (Entity entity : nearbyEntities) {
                if (entity instanceof LivingEntity) {
                    LivingEntity possibleItemEntity = (LivingEntity) entity;
                    Vec3d entityPos = possibleItemEntity.getPos();
                    itemEntity = possibleItemEntity;
//                    // 检查是否是刚刚丢出的物品实体
//                    if (entityPos.distanceTo(playerPos) < 1.0) {
//                        itemEntity = possibleItemEntity;
//                        break;
//                    }
                }
            }

            // 在这里可以操作刚刚丢出的物品实体 (itemEntity)
            if (itemEntity != null) {

//                itemEntity.move(MovementType.SELF,direction.multiply(power));
                itemEntity.addVelocity(direction.multiply(power)); // 根据力度设置速度向量
                player.sendMessage(Text.literal((String.valueOf(itemEntity))),false);
            } else {
                player.sendMessage(Text.literal((String.valueOf(123))),false);
                // 物品实体未生成，处理错误或等待更长时间
            }

            // 恢复初始物品堆栈
//            player.setStackInHand(Hand.MAIN_HAND, itemStackToThrow);


//            if (itemEntity2 != null) {
//                itemEntity2.addVelocity(direction.multiply(power)); // 根据力度设置速度向量
//            }
//
//            player.sendMessage(Text.literal((String.valueOf(itemEntity2))),false);

//            itemStackToThrow.decrement(1);


            // 重置蓄力力度
            throwPower = 0.0f;
        }
    }
}
