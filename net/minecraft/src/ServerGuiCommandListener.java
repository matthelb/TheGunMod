package net.minecraft.src;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import net.minecraft.server.MinecraftServer;

class ServerGuiCommandListener
    implements ActionListener
{
    final JTextField textField;
    final ServerGUI mcServerGui;

    ServerGuiCommandListener(ServerGUI servergui, JTextField jtextfield)
    {
        mcServerGui = servergui;
        textField = jtextfield;
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        String s = textField.getText().trim();
        if (s.length() > 0)
        {
            ServerGUI.getMinecraftServer(mcServerGui).addCommand(s, mcServerGui);
        }
        textField.setText("");
    }
}
