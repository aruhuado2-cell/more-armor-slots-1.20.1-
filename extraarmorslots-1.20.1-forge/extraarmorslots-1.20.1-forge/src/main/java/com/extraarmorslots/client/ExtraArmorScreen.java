package com.extraarmorslots.client;

import com.extraarmorslots.menu.ExtraArmorMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class ExtraArmorScreen extends AbstractContainerScreen<ExtraArmorMenu> {

    public ExtraArmorScreen(ExtraArmorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 18 + menu.getRows() * 18 + 14 + 76 + 8;
        this.inventoryLabelY = this.imageHeight - 94;
        this.titleLabelY = 6;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = this.leftPos;
        int y = this.topPos;
        int w = this.imageWidth;
        int h = this.imageHeight;

        guiGraphics.fill(x, y, x + w, y + h, 0xFFC6C6C6);
        guiGraphics.fill(x, y, x + w - 1, y + 2, 0xFFFFFFFF);
        guiGraphics.fill(x, y, x + 2, y + h - 1, 0xFFFFFFFF);
        guiGraphics.fill(x + 1, y + h - 2, x + w, y + h, 0xFF555555);
        guiGraphics.fill(x + w - 2, y + 1, x + w, y + h, 0xFF555555);
        guiGraphics.renderOutline(x - 1, y - 1, w + 2, h + 2, 0xFF000000);

        for (Slot slot : this.menu.slots) {
            int sx = x + slot.x - 1;
            int sy = y + slot.y - 1;
            guiGraphics.fill(sx, sy, sx + 18, sy + 18, 0xFF8B8B8B);
            guiGraphics.fill(sx, sy, sx + 17, sy + 1, 0xFF373737);
            guiGraphics.fill(sx, sy, sx + 1, sy + 17, 0xFF373737);
            guiGraphics.fill(sx + 1, sy + 17, sx + 18, sy + 18, 0xFFFFFFFF);
            guiGraphics.fill(sx + 17, sy + 1, sx + 18, sy + 18, 0xFFFFFFFF);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
