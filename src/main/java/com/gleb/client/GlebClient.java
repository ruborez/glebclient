package com.gleb.client;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.lwjgl.glfw.GLFW;

@Mod("glebclient")
public class GlebClient {
    public static GlebClient instance;
    
    public boolean killAura = false;
    public boolean fly = false;
    public boolean esp = false;
    public boolean xray = false;
    public boolean speed = false;
    public boolean nuker = false;
    
    public int killAuraKey = GLFW.GLFW_KEY_R;
    public int flyKey = GLFW.GLFW_KEY_F;
    public int espKey = GLFW.GLFW_KEY_G;
    public int xrayKey = GLFW.GLFW_KEY_X;
    public int speedKey = GLFW.GLFW_KEY_V;
    public int nukerKey = GLFW.GLFW_KEY_N;
    
    public double killAuraRange = 4.5;
    public double speedMultiplier = 2.0;
    public int nukerRadius = 3;
    
    private boolean keyPressed = false;

    public GlebClient() {
        instance = this;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;

        if (GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS 
            && mc.screen == null) {
            mc.setScreen(new GlebClientGui());
        }

        checkModuleKey(killAuraKey, () -> killAura = !killAura);
        checkModuleKey(flyKey, () -> fly = !fly);
        checkModuleKey(espKey, () -> esp = !esp);
        checkModuleKey(xrayKey, () -> xray = !xray);
        checkModuleKey(speedKey, () -> speed = !speed);
        checkModuleKey(nukerKey, () -> nuker = !nuker);

        if (killAura) doKillAura(player);
        if (fly) doFly(player);
        if (speed) doSpeed(player);
        if (nuker) doNuker(player);
    }

    private void checkModuleKey(int key, Runnable action) {
        Minecraft mc = Minecraft.getInstance();
        if (GLFW.glfwGetKey(mc.getWindow().getWindow(), key) == GLFW.GLFW_PRESS) {
            if (!keyPressed) {
                action.run();
                keyPressed = true;
            }
        } else {
            keyPressed = false;
        }
    }

    private void doKillAura(LocalPlayer player) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        AABB range = player.getBoundingBox().inflate(killAuraRange);
        for (Entity entity : mc.level.getEntities(player, range)) {
            if (entity instanceof Player target && target != player) {
                player.lookAt(target.getEyePosition(1.0f));
                mc.gameMode.attack(player, target);
                player.swing(player.getUsedItemHand());
            }
        }
    }

    private void doFly(LocalPlayer player) {
        player.getAbilities().flying = true;
        player.getAbilities().setFlyingSpeed(0.05f);
        Minecraft mc = Minecraft.getInstance();
        if (GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS) {
            player.setDeltaMovement(player.getDeltaMovement().add(0, 0.5, 0));
        }
        if (GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS) {
            player.setDeltaMovement(player.getDeltaMovement().add(0, -0.5, 0));
        }
    }

    private void doSpeed(LocalPlayer player) {
        if (player.onGround()) {
            player.setDeltaMovement(
                player.getDeltaMovement().multiply(speedMultiplier, 1, speedMultiplier)
            );
        }
    }

    private void doNuker(LocalPlayer player) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        BlockPos center = player.blockPosition();
        for (int x = -nukerRadius; x <= nukerRadius; x++) {
            for (int y = -nukerRadius; y <= nukerRadius; y++) {
                for (int z = -nukerRadius; z <= nukerRadius; z++) {
                    BlockPos pos = center.offset(x, y, z);
                    BlockState state = mc.level.getBlockState(pos);
                    if (!state.isAir()) {
                        mc.level.destroyBlock(pos, true);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderGui(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        var font = mc.font;
        int y = 2;

        font.drawStringWithShadow("§c§lGleb Client §7by ruborez", 2, y, 0xFFFFFF);
        y += 12;

        if (killAura) {
            font.drawStringWithShadow("§aKillAura §7[ON]", 2, y, 0xFFFFFF);
            y += 10;
        }
        if (fly) {
            font.drawStringWithShadow("§aFly §7[ON]", 2, y, 0xFFFFFF);
            y += 10;
        }
        if (esp) {
            font.drawStringWithShadow("§aESP §7[ON]", 2, y, 0xFFFFFF);
            y += 10;
        }
        if (xray) {
            font.drawStringWithShadow("§aX-Ray §7[ON]", 2, y, 0xFFFFFF);
            y += 10;
        }
        if (speed) {
            font.drawStringWithShadow("§aSpeed §7[ON]", 2, y, 0xFFFFFF);
            y += 10;
        }
        if (nuker) {
            font.drawStringWithShadow("§aNuker §7[ON]", 2, y, 0xFFFFFF);
        }
    }
}
