package net.minecraft.src;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

class ThreadLoginVerifier extends Thread
{
    final Packet1Login loginPacket;
    final NetLoginHandler loginHandler;

    ThreadLoginVerifier(NetLoginHandler netloginhandler, Packet1Login packet1login)
    {
        loginHandler = netloginhandler;
        loginPacket = packet1login;
    }

    public void run()
    {
        try
        {
            String s = NetLoginHandler.getServerId(loginHandler);
            URL url = new URL((new StringBuilder()).append("http://session.minecraft.net/game/checkserver.jsp?user=").append(URLEncoder.encode(loginPacket.username, "UTF-8")).append("&serverId=").append(URLEncoder.encode(s, "UTF-8")).toString());
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(url.openStream()));
            String s1 = bufferedreader.readLine();
            bufferedreader.close();
            if (s1.equals("YES"))
            {
                NetLoginHandler.setLoginPacket(loginHandler, loginPacket);
            }
            else
            {
                loginHandler.kickUser("Failed to verify username!");
            }
        }
        catch (Exception exception)
        {
            loginHandler.kickUser((new StringBuilder()).append("Failed to verify username! [internal error ").append(exception).append("]").toString());
            exception.printStackTrace();
        }
    }
}
