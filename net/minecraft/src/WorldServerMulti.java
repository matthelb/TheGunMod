package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class WorldServerMulti extends WorldServer
{
    public WorldServerMulti(MinecraftServer minecraftserver, ISaveHandler isavehandler, String s, int i, WorldSettings worldsettings, WorldServer worldserver)
    {
        super(minecraftserver, isavehandler, s, i, worldsettings);
        mapStorage = worldserver.mapStorage;
    }
}
