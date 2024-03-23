package net.jiang.tutorialmod.particle.custom;

import net.blf02.vrapi.api.IVRAPI;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.jiang.tutorialmod.item.ModItems;
import net.jiang.tutorialmod.item.vrcustom.VrRubberItem;
import net.jiang.tutorialmod.networking.ModMessages;
import net.jiang.tutorialmod.networking.packet.ParticleColorC2SPacket;
import net.jiang.tutorialmod.particle.ParticleStorage;
import net.jiang.tutorialmod.vr.VRPlugin;
import net.jiang.tutorialmod.vr.VRPluginVerify;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EntityList;

import java.awt.*;
import java.util.List;

public class CitrineParticle extends SpriteBillboardParticle {
    protected CitrineParticle(ClientWorld level, double xCoord, double yCoord, double zCoord,
                              SpriteProvider spriteSet, double xd, double yd, double zd) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);

        this.velocityMultiplier = 0F;
        this.red = (float) xd;
        this.green = (float) yd;
        this.blue = (float) zd;
        this.scale *= 1F;
        this.maxAge = 999999999;
        this.setSpriteForAge(spriteSet);

        this.setColor(red, green, blue); // 设置颜色
    }
    @Override
    public float getSize(float tickDelta) {
        return 0.013F;
    }

    @Override
    public void setColor(float red, float green, float blue) {
        super.setColor(red, green, blue);
    }

    @Override
    public void tick() {
        if(world.isClient) {
            PlayerEntity player = world.getClosestPlayer(x,y,z,10,false);
            if (player!=null) {
                if (VrRubberItem.isErasing && VrRubberItem.userbox!=null) {// 检查粒子是否在笔刷碰撞箱内
                    if (isParticleInsideBox(new Vec3d(this.x, this.y, this.z), VrRubberItem.userbox)) {
                        this.markDead();
                        ParticleStorage.getOrCreateForWorld().removeParticleAtPosition(new Vec3d(this.x, this.y, this.z));
                    }
                }
                if(VrRubberItem.isErasing && player.getOffHandStack().getItem()== ModItems.RUBY){//大范围清屏
                    this.markDead();
                    ParticleStorage.getOrCreateForWorld().clearAll();
                }
            }
        }
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider sprites;

        public Factory(SpriteProvider spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(DefaultParticleType particleType, ClientWorld level, double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new CitrineParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }
    private boolean printXYZ(){
        System.out.println(new Vec3d(x, y, z));
        return true;
    }
    private static boolean isParticleInsideBox(Vec3d position, Box collisionBox) {
        double posX = position.x;
        double posY = position.y;
        double posZ = position.z;
        double boxMinX = collisionBox.minX;
        double boxMinY = collisionBox.minY;
        double boxMinZ = collisionBox.minZ;
        double boxMaxX = collisionBox.maxX;
        double boxMaxY = collisionBox.maxY;
        double boxMaxZ = collisionBox.maxZ;

        return posX >= boxMinX && posX <= boxMaxX
                && posY >= boxMinY && posY <= boxMaxY
                && posZ >= boxMinZ && posZ <= boxMaxZ;
    }

}