package com.gleb.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class GlebClientGui extends Screen {
    private final GlebClient client = GlebClient.instance;
    private int selectedModule = -1;
    private boolean bindingKey = false;

    public GlebClientGui() {
        super(Component.literal("Gleb Client"));
    }

    @Override
    protected void init() {
        this.clearWidgets();
        
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        this.addRenderableWidget(Button.builder(
            Component.literal("KillAura: " + (client.killAura ? "§aON" : "§cOFF")),
            btn -> { client.killAura = !client.killAura; this.init(); }
        ).bounds(centerX - 100, centerY - 100, 200, 20).build());

        this.addRenderableWidget(Button.builder(
            Component.literal("Fly: " + (client.fly ? "§aON" : "§cOFF")),
            btn -> { client.fly = !client.fly; this.init(); }
        ).bounds(centerX - 100, centerY - 75, 200, 20).build());

        this.addRenderableWidget(Button.builder(
            Component.literal("ESP: " + (client.esp ? "§aON" : "§cOFF")),
            btn -> { client.esp = !client.esp; this.init(); }
        ).bounds(centerX - 100, centerY - 50, 200, 20).build());

        this.addRenderableWidget(Button.builder(
            Component.literal("X-Ray: " + (client.xray ? "§aON" : "§cOFF")),
            btn -> { client.xray = !client.xray; this.init(); }
        ).bounds(centerX - 100, centerY - 25, 200, 20).build());

        this.addRenderableWidget(Button.builder(
            Component.literal("Speed: " + (client.speed ? "§aON" : "§cOFF")),
            btn -> { client.speed = !client.speed; this.init(); }
        ).bounds(centerX - 100, centerY, 200, 20).build());

        this.addRenderableWidget(Button.builder(
            Component.literal("Nuker: " + (client.nuker ? "§aON" : "§cOFF")),
            btn -> { client.nuker = !client.nuker; this.init(); }
        ).bounds(centerX - 100, centerY + 25, 200, 20).build());

        this.addRenderableWidget(Button.builder(
            Component.literal("KillAura Key: " + GLFW.glfwGetKeyName(client.killAuraKey, 0)),
            btn -> { selectedModule = 0; bindingKey = true; }
        ).bounds(centerX - 100, centerY + 60, 200, 20).build());

        this.addRenderableWidget(Button.builder(
            Component.literal("Fly Key: " + GLFW.glfwGetKeyName(client.flyKey, 0)),
            btn -> { selectedModule = 1; bindingKey = true; }
        ).bounds(centerX - 100, centerY + 85, 200, 20).build());

        this.addRenderableWidget(Button.builder(
            Component.literal("ESP Key: " + GLFW.glfwGetKeyName(client.espKey, 0)),
            btn -> { selectedModule = 2; bindingKey = true; }
        ).bounds(centerX - 100, centerY + 110, 200, 20).build());

        this.addRenderableWidget(Button.builder(
            Component.literal("X-Ray Key: " + GLFW.glfwGetKeyName(client.xrayKey, 0)),
            btn -> { selectedModule = 3; bindingKey = true; }
        ).bounds(centerX - 100, centerY + 135, 200, 20).build());

        this.addRenderableWidget(Button.builder(
            Component.literal("Speed Key: " + GLFW.glfwGetKeyName(client.speedKey, 0)),
            btn -> { selectedModule = 4; bindingKey = true; }
        ).bounds(centerX - 100, centerY + 160, 200, 20).build());

        this.addRenderableWidget(Button.builder(
            Component.literal("Nuker Key: " + GLFW.glfwGetKeyName(client.nukerKey, 0)),
            btn -> { selectedModule = 5; bindingKey = true; }
        ).bounds(centerX - 100, centerY + 185, 200, 20).build());

        this.addRenderableWidget(Button.builder(
            Component.literal("KillAura Range: " + client.killAuraRange),
            btn -> { client.killAuraRange = (client.killAuraRange == 4.5) ? 6.0 : 4.5; this.init(); }
        ).bounds(centerX - 100, centerY + 220, 200, 20).build());

        this.addRenderableWidget(Button.builder(
            Component.literal("Speed Multiplier: " + client.speedMultiplier),
            btn -> { client.speedMultiplier = (client.speedMultiplier == 2.0) ? 3.0 : 2.0; this.init(); }
        ).bounds(centerX - 100, centerY + 245, 200, 20).build());

        this.addRenderableWidget(Button.builder(
            Component.literal("Nuker Radius: " + client.nukerRadius),
            btn -> { client.nukerRadius = (client.nukerRadius == 3) ? 5 : 3; this.init(); }
        ).bounds(centerX - 100, centerY + 270, 200, 20).build());

        this.addRenderableWidget(Button.builder(
            Component.literal("Close"),
            btn -> this.onClose()
        ).bounds(centerX - 100, centerY + 300, 200, 20).build());
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (bindingKey) {
            switch (selectedModule) {
                case 0: client.killAuraKey = keyCode; break;
                case 1: client.flyKey = keyCode; break;
                case 2: client.espKey = keyCode; break;
                case 3: client.xrayKey = keyCode; break;
                case 4: client.speedKey = keyCode; break;
                case 5: client.nukerKey = keyCode; break;
            }
            bindingKey = false;
            selectedModule = -1;
            this.init();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        
        graphics.drawCenteredString(this.font, 
            "§c§lGleb Client §7by ruborez", 
            this.width / 2, 20, 0xFFFFFF);
        
        if (bindingKey) {
            graphics.drawCenteredString(this.font, 
                "§ePress any key...", 
                this.width / 2, this.height / 2 - 120, 0xFFFFFF);
        }
        
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
