// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, ModLoader, ScaledResolution, RenderEngine, 
//            BlockWeaponBox, GunRecipe, FontRenderer, InventoryPlayer, 
//            ItemStack, Item, Block, RenderItem, 
//            GameSettings, KeyBinding, EntityPlayerSP

public class GuiWeaponBox extends GuiScreen
{

    private InventoryPlayer inventory;
    private Minecraft mc;
    private static RenderItem itemRenderer = new RenderItem();
    private BlockWeaponBox box;
    private int page;
    private int wood;
    private int glass;
    private int gunpowder;
    private int iron;
    private int guiOriginX;
    private int guiOriginY;

    public GuiWeaponBox(InventoryPlayer inventoryplayer, BlockWeaponBox blockweaponbox)
    {
        inventory = inventoryplayer;
        mc = ModLoader.getMinecraftInstance();
        box = blockweaponbox;
        page = 0;
    }

    public void drawScreen(int i, int j, float f)
    {
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int k = scaledresolution.getScaledWidth();
        int l = scaledresolution.getScaledHeight();
        FontRenderer fontrenderer = mc.fontRenderer;
        drawDefaultBackground();
        GL11.glEnable(3042 /*GL_BLEND*/);
        GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, mc.renderEngine.getTexture("/gui/weaponBox.png"));
        int i1 = guiOriginX = k / 2 - 88;
        int j1 = guiOriginY = l / 2 - 81;
        drawTexturedModalRect(i1, j1, 0, 0, 176, 162);
        drawCenteredString(fontRenderer, (new StringBuilder()).append(box.teamName).append(" Weapon Box").toString(), k / 2, j1 + 5, 0xffffff);
        if(box.guns[page * 2] != null)
        {
            drawCenteredString(fontrenderer, box.guns[page * 2].name, i1 + 56, j1 + 83, 0xffffff);
        }
        if(box.guns[page * 2 + 1] != null)
        {
            drawCenteredString(fontrenderer, box.guns[page * 2 + 1].name, i1 + 119, j1 + 83, 0xffffff);
        }
        iron = 0;
        wood = 0;
        gunpowder = 0;
        glass = 0;
        for(int k1 = 0; k1 < inventory.getSizeInventory(); k1++)
        {
            ItemStack itemstack = inventory.getStackInSlot(k1);
            if(itemstack == null)
            {
                continue;
            }
            if(itemstack.itemID == Item.ingotIron.shiftedIndex)
            {
                iron += itemstack.stackSize;
            }
            if(itemstack.itemID == Item.gunpowder.shiftedIndex)
            {
                gunpowder += itemstack.stackSize;
            }
            if(itemstack.itemID == Block.wood.blockID)
            {
                wood += itemstack.stackSize;
            }
            if(itemstack.itemID == Block.glass.blockID)
            {
                glass += itemstack.stackSize;
            }
        }

        drawSlotInventory(new ItemStack(Item.ingotIron), i1 + 8, j1 + 20);
        fontRenderer.drawString((new StringBuilder()).append("Iron : ").append(iron).toString(), i1 + 28, j1 + 25, 0x404040);
        drawSlotInventory(new ItemStack(Item.gunpowder), i1 + 8, j1 + 38);
        fontRenderer.drawString((new StringBuilder()).append("Powder: ").append(gunpowder).toString(), i1 + 28, j1 + 43, 0x404040);
        drawSlotInventory(new ItemStack(Block.wood), i1 + 89, j1 + 20);
        fontRenderer.drawString((new StringBuilder()).append("Wood : ").append(wood).toString(), i1 + 109, j1 + 25, 0x404040);
        drawSlotInventory(new ItemStack(Block.glass), i1 + 89, j1 + 38);
        fontRenderer.drawString((new StringBuilder()).append("Glass : ").append(glass).toString(), i1 + 109, j1 + 43, 0x404040);
        if(box.guns[page * 2] != null)
        {
            drawSlotInventory(new ItemStack(box.guns[page * 2].gun), i1 + 35, j1 + 103);
            drawSlotInventory(new ItemStack(box.guns[page * 2].clip), i1 + 62, j1 + 103);
            fontRenderer.drawString((new StringBuilder()).append(box.guns[page * 2].ironNeeded).append(" ").toString(), i1 + 37, j1 + 122, 0x404040);
            fontRenderer.drawString((new StringBuilder()).append(box.guns[page * 2].gunpowderNeeded).append(" ").toString(), i1 + 37, j1 + 130, 0x404040);
            fontRenderer.drawString((new StringBuilder()).append(box.guns[page * 2].woodNeeded).append(" ").toString(), i1 + 37, j1 + 138, 0x404040);
            fontRenderer.drawString((new StringBuilder()).append(box.guns[page * 2].glassNeeded).append(" ").toString(), i1 + 37, j1 + 146, 0x404040);
            fontRenderer.drawString((new StringBuilder()).append(box.guns[page * 2].clipIronNeeded).append(" ").toString(), i1 + 64, j1 + 122, 0x404040);
            fontRenderer.drawString((new StringBuilder()).append(box.guns[page * 2].clipGunpowderNeeded).append(" ").toString(), i1 + 64, j1 + 130, 0x404040);
            fontRenderer.drawString((new StringBuilder()).append(box.guns[page * 2].clipWoodNeeded).append(" ").toString(), i1 + 64, j1 + 138, 0x404040);
            fontRenderer.drawString((new StringBuilder()).append(box.guns[page * 2].clipGlassNeeded).append(" ").toString(), i1 + 64, j1 + 146, 0x404040);
        }
        if(box.guns[page * 2 + 1] != null)
        {
            drawSlotInventory(new ItemStack(box.guns[page * 2 + 1].gun), i1 + 98, j1 + 103);
            drawSlotInventory(new ItemStack(box.guns[page * 2 + 1].clip), i1 + 125, j1 + 103);
            fontRenderer.drawString((new StringBuilder()).append(box.guns[page * 2 + 1].ironNeeded).append(" ").toString(), i1 + 100, j1 + 122, 0x404040);
            fontRenderer.drawString((new StringBuilder()).append(box.guns[page * 2 + 1].gunpowderNeeded).append(" ").toString(), i1 + 100, j1 + 130, 0x404040);
            fontRenderer.drawString((new StringBuilder()).append(box.guns[page * 2 + 1].woodNeeded).append(" ").toString(), i1 + 100, j1 + 138, 0x404040);
            fontRenderer.drawString((new StringBuilder()).append(box.guns[page * 2 + 1].glassNeeded).append(" ").toString(), i1 + 100, j1 + 146, 0x404040);
            fontRenderer.drawString((new StringBuilder()).append(box.guns[page * 2 + 1].clipIronNeeded).append(" ").toString(), i1 + 127, j1 + 122, 0x404040);
            fontRenderer.drawString((new StringBuilder()).append(box.guns[page * 2 + 1].clipGunpowderNeeded).append(" ").toString(), i1 + 127, j1 + 130, 0x404040);
            fontRenderer.drawString((new StringBuilder()).append(box.guns[page * 2 + 1].clipWoodNeeded).append(" ").toString(), i1 + 127, j1 + 138, 0x404040);
            fontRenderer.drawString((new StringBuilder()).append(box.guns[page * 2 + 1].clipGlassNeeded).append(" ").toString(), i1 + 127, j1 + 146, 0x404040);
        }
        GL11.glDisable(3042 /*GL_BLEND*/);
    }

    private void drawSlotInventory(ItemStack itemstack, int i, int j)
    {
        itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, itemstack, i, j);
        itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, itemstack, i, j);
    }

    protected void mouseClicked(int i, int j, int k)
    {
        super.mouseClicked(i, j, k);
        int l = i - guiOriginX;
        int i1 = j - guiOriginY;
        if(k == 0 || k == 1)
        {
            if(l > 4 && l < 21 && i1 > 97 && i1 < 124)
            {
                int j1 = (page - 1) % 8;
                if(j1 >= 0 && box.guns[j1 * 2] != null)
                {
                    page = j1;
                }
            }
            if(l > 154 && l < 171 && i1 > 97 && i1 < 124)
            {
                int k1 = (page + 1) % 8;
                if(k1 < 8 && box.guns[k1 * 2] != null)
                {
                    page = k1;
                }
            }
            if(box.guns[page * 2] != null && l > 34 && l < 52 && i1 > 102 && i1 < 120)
            {
                box.buyGun(page * 2, iron, gunpowder, wood, glass, inventory);
            }
            if(box.guns[page * 2] != null && l > 61 && l < 79 && i1 > 102 && i1 < 120)
            {
                box.buyClip(page * 2, iron, gunpowder, wood, glass, inventory);
            }
            if(box.guns[page * 2 + 1] != null && l > 97 && l < 115 && i1 > 102 && i1 < 120)
            {
                box.buyGun(page * 2 + 1, iron, gunpowder, wood, glass, inventory);
            }
            if(box.guns[page * 2 + 1] != null && l > 124 && l < 142 && i1 > 102 && i1 < 120)
            {
                box.buyClip(page * 2 + 1, iron, gunpowder, wood, glass, inventory);
            }
        }
    }

    protected void keyTyped(char c, int i)
    {
        if(i == 1 || i == mc.gameSettings.keyBindInventory.keyCode)
        {
            mc.thePlayer.closeScreen();
        }
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

}
