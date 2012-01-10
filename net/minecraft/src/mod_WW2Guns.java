// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import java.io.PrintStream;
import java.util.Map;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            BaseModMp, ModLoader, EntityBullet2, ModLoaderMp, 
//            EntityAntiTank, BlockWeaponBox, ItemStack, Item, 
//            Packet230ModLoader, RenderBullet2, World, WorldInfo, 
//            ScaledResolution, RenderEngine, Tessellator, EntityPlayerSP, 
//            InventoryPlayer, ItemGun, EntityRenderer, Block

public class mod_WW2Guns extends BaseModMp
{

    public static int germanBoxId;
    public static int britishBoxId;
    public static int americanBoxId;
    public static Block weaponBoxGerman;
    public static Block weaponBoxBritish;
    public static Block weaponBoxAmerican;
    public static Item mp40;
    public static Item mp40Clip;
    public static Item mp44;
    public static Item mp44Clip;
    public static Item kar98k;
    public static Item kar98kSniper;
    public static Item kar98kClip;
    public static Item luger;
    public static Item lugerClip;
    public static Item panzerschreck;
    public static Item panzerschreckClip;
    public static Item sten;
    public static Item stenClip;
    public static Item leeEnfield;
    public static Item leeEnfieldSniper;
    public static Item leeEnfieldClip;
    public static Item webley;
    public static Item webleyClip;
    public static Item m1carbine;
    public static Item m1carbineClip;
    public static Item m1garand;
    public static Item m1garandClip;
    public static Item thompson;
    public static Item thompsonClip;
    public static Item BAR;
    public static Item BARClip;
    public static Item springfield;
    public static Item springfieldClip;
    public static Item colt;
    public static Item coltClip;
    public static Item bazooka;
    public static Item bazookaClip;
    public static int shootTime;
    public static float playerRecoil;
    public static float antiRecoil;
    public static float playerZoom = 1.0F;
    public static float newZoom = 1.0F;
    public static float lastPlayerZoom;
    public static boolean zoomOverlay = false;
    private long lastTime;
    private static mod_WW2Guns instance;

    public mod_WW2Guns()
    {
        ModLoader.SetInGameHook(this, true, false);
        initBlocks();
        initGuns();
        addRecipes();
        ModLoaderMp.RegisterNetClientHandlerEntity(net.minecraft.src.EntityBullet2.class, 40);
        ModLoaderMp.RegisterNetClientHandlerEntity(net.minecraft.src.EntityAntiTank.class, 41);
        instance = this;
    }

    public String getVersion()
    {
        return "1.0.0v3";
    }

    public void load()
    {
    }

    private void initBlocks()
    {
        ModLoader.RegisterBlock(weaponBoxGerman);
        ModLoader.AddName(weaponBoxGerman, "German Weapon Box");
        ((BlockWeaponBox)weaponBoxGerman).addGun("MP 40", mp40, 10, 0, 0, 0, mp40Clip, 3, 1, 0, 0);
        ((BlockWeaponBox)weaponBoxGerman).addGun("MP 44", mp44, 20, 0, 4, 0, mp44Clip, 4, 1, 0, 0);
        ((BlockWeaponBox)weaponBoxGerman).addGun("Kar98k", kar98k, 8, 0, 15, 0, kar98kClip, 3, 1, 0, 0);
        ((BlockWeaponBox)weaponBoxGerman).addGun("K98k Sniper", kar98kSniper, 12, 0, 15, 4, kar98kClip, 3, 1, 0, 0);
        ((BlockWeaponBox)weaponBoxGerman).addGun("Luger", luger, 5, 0, 5, 0, lugerClip, 2, 1, 0, 0);
        ((BlockWeaponBox)weaponBoxGerman).addGun("Panzerschreck", panzerschreck, 25, 0, 6, 0, panzerschreckClip, 5, 3, 0, 0);
        ModLoader.RegisterBlock(weaponBoxBritish);
        ModLoader.AddName(weaponBoxBritish, "British Weapon Box");
        ((BlockWeaponBox)weaponBoxBritish).addGun("Sten", sten, 8, 0, 0, 0, stenClip, 2, 1, 0, 0);
        ((BlockWeaponBox)weaponBoxBritish).addGun("Lee Enfield", leeEnfield, 8, 0, 15, 0, leeEnfieldClip, 5, 2, 0, 0);
        ((BlockWeaponBox)weaponBoxBritish).addGun("L.E. Sniper", leeEnfieldSniper, 10, 0, 15, 4, leeEnfieldClip, 5, 2, 0, 0);
        ((BlockWeaponBox)weaponBoxBritish).addGun("Webley", webley, 6, 0, 4, 0, webleyClip, 2, 1, 0, 0);
        ((BlockWeaponBox)weaponBoxBritish).addGun("Bazooka", bazooka, 24, 0, 8, 0, bazookaClip, 6, 3, 0, 0);
        ModLoader.RegisterBlock(weaponBoxAmerican);
        ModLoader.AddName(weaponBoxAmerican, "American Weapon Box");
        ((BlockWeaponBox)weaponBoxAmerican).addGun("M1 Carbine", m1carbine, 8, 0, 12, 0, m1carbineClip, 3, 1, 0, 0);
        ((BlockWeaponBox)weaponBoxAmerican).addGun("M1 Garand", m1garand, 10, 0, 15, 0, m1garandClip, 2, 1, 0, 0);
        ((BlockWeaponBox)weaponBoxAmerican).addGun("Thompson", thompson, 6, 0, 8, 0, thompsonClip, 4, 1, 0, 0);
        ((BlockWeaponBox)weaponBoxAmerican).addGun("BAR", BAR, 15, 0, 10, 0, BARClip, 4, 1, 0, 0);
        ((BlockWeaponBox)weaponBoxAmerican).addGun("Springfield", springfield, 12, 0, 20, 4, springfieldClip, 3, 1, 0, 0);
        ((BlockWeaponBox)weaponBoxAmerican).addGun("Colt", colt, 5, 0, 6, 0, coltClip, 2, 1, 0, 0);
        ((BlockWeaponBox)weaponBoxAmerican).addGun("Bazooka", bazooka, 24, 0, 8, 0, bazookaClip, 6, 3, 0, 0);
    }

    private void initGuns()
    {
        ModLoader.AddName(mp40, "MP 40");
        ModLoader.AddName(mp40Clip, "MP 40 Clip");
        ModLoader.AddName(mp44, "MP 44");
        ModLoader.AddName(mp44Clip, "MP 44 Clip");
        ModLoader.AddName(kar98k, "Kar98k");
        ModLoader.AddName(kar98kSniper, "Kar98k Sniper");
        ModLoader.AddName(kar98kClip, "Kar98k Clip");
        ModLoader.AddName(luger, "Luger");
        ModLoader.AddName(lugerClip, "Luger Clip");
        ModLoader.AddName(panzerschreck, "Panzerschreck");
        ModLoader.AddName(panzerschreckClip, "Panzerschreck Rocket");
        ModLoader.AddName(sten, "Sten");
        ModLoader.AddName(stenClip, "Sten Clip");
        ModLoader.AddName(leeEnfield, "Lee Enfield");
        ModLoader.AddName(leeEnfieldSniper, "Lee Enfield Sniper");
        ModLoader.AddName(leeEnfieldClip, "Lee Enfield Clip");
        ModLoader.AddName(webley, "Webley Revolver");
        ModLoader.AddName(webleyClip, "Webley Clip");
        ModLoader.AddName(m1garand, "M1 Garand");
        ModLoader.AddName(m1garandClip, "M1 Garand Clip");
        ModLoader.AddName(m1carbine, "M1 Carbine");
        ModLoader.AddName(m1carbineClip, "M1 Carbine Clip");
        ModLoader.AddName(thompson, "Thompson");
        ModLoader.AddName(thompsonClip, "Thompson Clip");
        ModLoader.AddName(BAR, "BAR");
        ModLoader.AddName(BARClip, "BAR Clip");
        ModLoader.AddName(springfield, "Springfield");
        ModLoader.AddName(springfieldClip, "Springfield Clip");
        ModLoader.AddName(colt, "Colt M1911");
        ModLoader.AddName(coltClip, "Colt Clip");
        ModLoader.AddName(bazooka, "Bazooka");
        ModLoader.AddName(bazookaClip, "Bazooka Rocket");
    }

    private void addRecipes()
    {
        ModLoader.AddRecipe(new ItemStack(weaponBoxAmerican), new Object[] {
            "XXX", "BRR", "XXX", Character.valueOf('X'), Item.ingotIron, Character.valueOf('R'), new ItemStack(Item.dyePowder, 1, 1), Character.valueOf('B'), new ItemStack(Item.dyePowder, 1, 4)
        });
        ModLoader.AddRecipe(new ItemStack(weaponBoxGerman), new Object[] {
            "XXX", "RBR", "XXX", Character.valueOf('X'), Item.ingotIron, Character.valueOf('R'), new ItemStack(Item.dyePowder, 1, 1), Character.valueOf('B'), new ItemStack(Item.dyePowder, 1, 0)
        });
        ModLoader.AddRecipe(new ItemStack(weaponBoxBritish), new Object[] {
            "XXX", "BRB", "XXX", Character.valueOf('X'), Item.ingotIron, Character.valueOf('R'), new ItemStack(Item.dyePowder, 1, 1), Character.valueOf('B'), new ItemStack(Item.dyePowder, 1, 4)
        });
    }

    public static void buyGun(BlockWeaponBox blockweaponbox, int i)
    {
        Packet230ModLoader packet230modloader = new Packet230ModLoader();
        int ai[] = new int[2];
        ai[0] = i;
        if(blockweaponbox == weaponBoxGerman)
        {
            ai[1] = 0;
        }
        if(blockweaponbox == weaponBoxBritish)
        {
            ai[1] = 1;
        }
        if(blockweaponbox == weaponBoxAmerican)
        {
            ai[1] = 2;
        }
        packet230modloader.packetType = 0;
        packet230modloader.dataInt = ai;
        ModLoaderMp.SendPacket(instance, packet230modloader);
    }

    public static void buyClip(BlockWeaponBox blockweaponbox, int i)
    {
        Packet230ModLoader packet230modloader = new Packet230ModLoader();
        int ai[] = new int[2];
        ai[0] = i;
        if(blockweaponbox == weaponBoxGerman)
        {
            ai[1] = 0;
        }
        if(blockweaponbox == weaponBoxBritish)
        {
            ai[1] = 1;
        }
        if(blockweaponbox == weaponBoxAmerican)
        {
            ai[1] = 2;
        }
        packet230modloader.packetType = 1;
        packet230modloader.dataInt = ai;
        ModLoaderMp.SendPacket(instance, packet230modloader);
    }

    public static void shoot()
    {
        Packet230ModLoader packet230modloader = new Packet230ModLoader();
        packet230modloader.packetType = 2;
        ModLoaderMp.SendPacket(instance, packet230modloader);
    }

    public void AddRenderer(Map map)
    {
        map.put(net.minecraft.src.EntityBullet2.class, new RenderBullet2());
        map.put(net.minecraft.src.EntityAntiTank.class, new RenderBullet2(5F));
    }

    public boolean OnTickInGame(float f, Minecraft minecraft)
    {
        long l = minecraft.theWorld.worldInfo.getWorldTime();
        if(l > lastTime)
        {
            tick(minecraft);
            lastTime = l;
        }
        if(zoomOverlay)
        {
            ScaledResolution scaledresolution = new ScaledResolution(minecraft.gameSettings, minecraft.displayWidth, minecraft.displayHeight);
            int i = scaledresolution.getScaledWidth();
            int j = scaledresolution.getScaledHeight();
            GL11.glEnable(3042 /*GL_BLEND*/);
            GL11.glDisable(2929 /*GL_DEPTH_TEST*/);
            GL11.glDepthMask(false);
            GL11.glBlendFunc(770, 771);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
            GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, minecraft.renderEngine.getTexture("/gui/scope.png"));
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(i / 2 - 2 * j, j, -90D, 0.0D, 1.0D);
            tessellator.addVertexWithUV(i / 2 + 2 * j, j, -90D, 1.0D, 1.0D);
            tessellator.addVertexWithUV(i / 2 + 2 * j, 0.0D, -90D, 1.0D, 0.0D);
            tessellator.addVertexWithUV(i / 2 - 2 * j, 0.0D, -90D, 0.0D, 0.0D);
            tessellator.draw();
            GL11.glDepthMask(true);
            GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
            GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
        return true;
    }

    private void tick(Minecraft minecraft)
    {
        if(shootTime > 0)
        {
            shootTime--;
        }
        if(playerRecoil > 0.0F)
        {
            playerRecoil *= 0.8F;
        }
        minecraft.thePlayer.rotationPitch -= playerRecoil;
        antiRecoil += playerRecoil;
        minecraft.thePlayer.rotationPitch += antiRecoil * 0.2F;
        antiRecoil *= 0.8F;
        Item item = null;
        ItemStack itemstack = minecraft.thePlayer.inventory.getCurrentItem();
        if(itemstack != null)
        {
            item = itemstack.getItem();
        }
        if(item != null && (!(item instanceof ItemGun) || !((ItemGun)item).type.equals("Sniper")))
        {
            newZoom = 1.0F;
        }
        float f = newZoom - playerZoom;
        playerZoom += f / 3F;
        if(playerZoom < 1.1F)
        {
            playerZoom = 1.0F;
            zoomOverlay = false;
        }
        if(Math.abs(playerZoom - lastPlayerZoom) > 0.015625F)
        {
            try
            {
                ModLoader.setPrivateValue(net.minecraft.src.EntityRenderer.class, minecraft.entityRenderer, "cameraZoom", Float.valueOf(playerZoom));
            }
            catch(NoSuchFieldException nosuchfieldexception)
            {
                try
                {
                    ModLoader.setPrivateValue(net.minecraft.src.EntityRenderer.class, minecraft.entityRenderer, "V", Float.valueOf(playerZoom));
                }
                catch(NoSuchFieldException nosuchfieldexception1)
                {
                    System.out.println("I forgot to update obfuscated reflection D:");
                    throw new RuntimeException(nosuchfieldexception1);
                }
            }
        }
        lastPlayerZoom = playerZoom;
    }

    static 
    {
        germanBoxId = 150;
        britishBoxId = 151;
        americanBoxId = 152;
        int i = ModLoader.addOverride("/terrain.png", "/guns/BoxTop.png");
        weaponBoxGerman = (new BlockWeaponBox(germanBoxId, i, ModLoader.addOverride("/terrain.png", "/guns/GermanBox.png"), "German")).setHardness(5F).setResistance(10F).setStepSound(Block.soundMetalFootstep).setBlockName("weaponBoxGerman");
        weaponBoxBritish = (new BlockWeaponBox(britishBoxId, i, ModLoader.addOverride("/terrain.png", "/guns/BritishBox.png"), "British")).setHardness(5F).setResistance(10F).setStepSound(Block.soundMetalFootstep).setBlockName("weaponBoxBritish");
        weaponBoxAmerican = (new BlockWeaponBox(americanBoxId, i, ModLoader.addOverride("/terrain.png", "/guns/AmericanBox.png"), "American")).setHardness(5F).setResistance(10F).setStepSound(Block.soundMetalFootstep).setBlockName("weaponBoxAmerican");
        mp40Clip = (new Item(23300)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/guns/mp40Clip.png")).setItemName("mp40Clip").setMaxStackSize(1).setMaxDamage(32);
        mp40 = (new ItemGun(23301, 50, 3, 4, 5, 2, mp40Clip, "mp40Shoot", "mp40Reload", "SMG")).setIconIndex(ModLoader.addOverride("/gui/items.png", "/guns/mp40.png")).setItemName("mp40");
        mp44Clip = (new Item(23302)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/guns/mp44Clip.png")).setItemName("mp44Clip").setMaxStackSize(1).setMaxDamage(30);
        mp44 = (new ItemGun(23303, 50, 2, 5, 6, 2, mp44Clip, "mp44Shoot", "mp44Reload", "SMG")).setIconIndex(ModLoader.addOverride("/gui/items.png", "/guns/mp44.png")).setItemName("mp44");
        kar98kClip = (new Item(23304)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/guns/kar98kClip.png")).setItemName("kar98kClip").setMaxStackSize(1).setMaxDamage(5);
        kar98k = (new ItemGun(23305, 60, 10, 10, 3, 30, kar98kClip, "kar98kShoot", "kar98kReload", "Rifle")).setIconIndex(ModLoader.addOverride("/gui/items.png", "/guns/kar98k.png")).setItemName("kar98k");
        kar98kSniper = (new ItemGun(23306, 60, 20, 15, 2, 30, kar98kClip, "kar98kSniperShoot", "kar98kSniperReload", "Sniper")).setIconIndex(ModLoader.addOverride("/gui/items.png", "/guns/kar98kSniper.png")).setItemName("kar98kSniper");
        lugerClip = (new Item(23307)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/guns/lugerClip.png")).setItemName("lugerClip").setMaxStackSize(1).setMaxDamage(8);
        luger = (new ItemGun(23308, 40, 4, 4, 5, 10, lugerClip, "lugerShoot", "lugerReload", "Rifle")).setIconIndex(ModLoader.addOverride("/gui/items.png", "/guns/luger.png")).setItemName("luger");
        panzerschreckClip = (new Item(23309)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/guns/panzerschreckClip.png")).setItemName("panzerschreckClip").setMaxStackSize(1).setMaxDamage(1);
        panzerschreck = (new ItemGun(23310, 80, 4, 250, 12, 5, panzerschreckClip, "bazookaShoot", "bazookaReload", "AntiTank")).setIconIndex(ModLoader.addOverride("/gui/items.png", "/guns/panzerschreck.png")).setItemName("panzerschreck");
        stenClip = (new Item(23330)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/guns/stenClip.png")).setItemName("stenClip").setMaxStackSize(1).setMaxDamage(32);
        sten = (new ItemGun(23331, 40, 1, 2, 4, 2, stenClip, "stenShoot", "stenReload", "SMG")).setIconIndex(ModLoader.addOverride("/gui/items.png", "/guns/sten.png")).setItemName("sten");
        leeEnfieldClip = (new Item(23332)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/guns/leeEnfieldClip.png")).setItemName("leeEnfieldClip").setMaxStackSize(1).setMaxDamage(10);
        leeEnfield = (new ItemGun(23333, 80, 8, 9, 4, 16, leeEnfieldClip, "leeEnfieldShoot", "leeEnfieldReload", "Rifle")).setIconIndex(ModLoader.addOverride("/gui/items.png", "/guns/leeEnfield.png")).setItemName("leeEnfield");
        leeEnfieldSniper = (new ItemGun(23334, 80, 18, 12, 1, 16, leeEnfieldClip, "leeEnfieldShoot", "leeEnfieldReload", "Sniper")).setIconIndex(ModLoader.addOverride("/gui/items.png", "/guns/leeEnfieldSniper.png")).setItemName("leeEnfieldSniper");
        webleyClip = (new Item(23335)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/guns/webleyClip.png")).setItemName("webleyClip").setMaxStackSize(1).setMaxDamage(6);
        webley = (new ItemGun(23336, 80, 5, 4, 4, 9, webleyClip, "webleyShoot", "webleyReload", "Rifle")).setIconIndex(ModLoader.addOverride("/gui/items.png", "/guns/webley.png")).setItemName("webley");
        m1carbineClip = (new Item(23360)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/guns/m1carbineClip.png")).setItemName("m1carbineClip").setMaxStackSize(1).setMaxDamage(15);
        m1carbine = (new ItemGun(23361, 60, 4, 6, 3, 5, m1carbineClip, "m1carbineShoot", "m1carbineReload", "Rifle")).setIconIndex(ModLoader.addOverride("/gui/items.png", "/guns/m1carbine.png")).setItemName("m1carbine");
        m1garandClip = (new Item(23362)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/guns/m1garandClip.png")).setItemName("m1garandClip").setMaxStackSize(1).setMaxDamage(8);
        m1garand = (new ItemGun(23363, 30, 5, 7, 3, 5, m1garandClip, "m1garandShoot", "m1garandReload", "Rifle")).setIconIndex(ModLoader.addOverride("/gui/items.png", "/guns/m1garand.png")).setItemName("m1garand");
        thompsonClip = (new Item(23364)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/guns/thompsonClip.png")).setItemName("thompsonClip").setMaxStackSize(1).setMaxDamage(30);
        thompson = (new ItemGun(23365, 40, 3, 5, 5, 2, thompsonClip, "thompsonShoot", "thompsonReload", "SMG")).setIconIndex(ModLoader.addOverride("/gui/items.png", "/guns/thompson.png")).setItemName("thompson");
        BARClip = (new Item(23366)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/guns/BARClip.png")).setItemName("BARClip").setMaxStackSize(1).setMaxDamage(20);
        BAR = (new ItemGun(23367, 50, 4, 8, 4, 2, BARClip, "BARShoot", "BARReload", "SMG")).setIconIndex(ModLoader.addOverride("/gui/items.png", "/guns/BAR.png")).setItemName("BAR");
        springfieldClip = (new Item(23368)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/guns/springfieldClip.png")).setItemName("springfieldClip").setMaxStackSize(1).setMaxDamage(5);
        springfield = (new ItemGun(23369, 80, 20, 18, 3, 20, springfieldClip, "springfieldShoot", "springfieldReload", "Sniper")).setIconIndex(ModLoader.addOverride("/gui/items.png", "/guns/springfield.png")).setItemName("springfield");
        coltClip = (new Item(23370)).setIconIndex(lugerClip.iconIndex).setItemName("coltClip").setMaxStackSize(1).setMaxDamage(7);
        colt = (new ItemGun(23371, 40, 6, 3, 3, 6, coltClip, "coltShoot", "coltReload", "Rifle")).setIconIndex(ModLoader.addOverride("/gui/items.png", "/guns/colt.png")).setItemName("colt");
        bazookaClip = (new Item(23372)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/guns/bazookaClip.png")).setItemName("bazookaClip").setMaxStackSize(1).setMaxDamage(1);
        bazooka = (new ItemGun(23373, 80, 5, 200, 10, 5, bazookaClip, "bazookaShoot", "bazookaReload", "AntiTank")).setIconIndex(ModLoader.addOverride("/gui/items.png", "/guns/bazooka.png")).setItemName("bazooka");
    }
}
