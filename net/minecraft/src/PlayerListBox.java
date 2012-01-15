package net.minecraft.src;

import java.util.List;
import java.util.Vector;
import javax.swing.JList;
import net.minecraft.server.MinecraftServer;

public class PlayerListBox extends JList
    implements IUpdatePlayerListBox
{
    private MinecraftServer mcServer;
    private int updateCounter;

    public PlayerListBox(MinecraftServer minecraftserver)
    {
        updateCounter = 0;
        mcServer = minecraftserver;
        minecraftserver.addToOnlinePlayerList(this);
    }

    public void update()
    {
        if (updateCounter++ % 20 == 0)
        {
            Vector vector = new Vector();
            for (int i = 0; i < mcServer.configManager.playerEntities.size(); i++)
            {
                vector.add(((EntityPlayerMP)mcServer.configManager.playerEntities.get(i)).username);
            }

            setListData(vector);
        }
    }
}
