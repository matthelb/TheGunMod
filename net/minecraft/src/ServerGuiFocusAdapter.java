package net.minecraft.src;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

class ServerGuiFocusAdapter extends FocusAdapter
{
    final ServerGUI mcServerGui;

    ServerGuiFocusAdapter(ServerGUI servergui)
    {
        mcServerGui = servergui;
    }

    public void focusGained(FocusEvent focusevent)
    {
    }
}
