package net.minecraft.src;

import java.io.*;

public class SaveFormatOld
    implements ISaveFormat
{
    protected final File savesDirectory;

    public SaveFormatOld(File file)
    {
        if (!file.exists())
        {
            file.mkdirs();
        }
        savesDirectory = file;
    }

    public WorldInfo getWorldInfo(String s)
    {
        File file = new File(savesDirectory, s);
        if (!file.exists())
        {
            return null;
        }
        File file1 = new File(file, "level.dat");
        if (file1.exists())
        {
            try
            {
                NBTTagCompound nbttagcompound = CompressedStreamTools.loadGzippedCompoundFromOutputStream(new FileInputStream(file1));
                NBTTagCompound nbttagcompound2 = nbttagcompound.getCompoundTag("Data");
                return new WorldInfo(nbttagcompound2);
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }
        }
        file1 = new File(file, "level.dat_old");
        if (file1.exists())
        {
            try
            {
                NBTTagCompound nbttagcompound1 = CompressedStreamTools.loadGzippedCompoundFromOutputStream(new FileInputStream(file1));
                NBTTagCompound nbttagcompound3 = nbttagcompound1.getCompoundTag("Data");
                return new WorldInfo(nbttagcompound3);
            }
            catch (Exception exception1)
            {
                exception1.printStackTrace();
            }
        }
        return null;
    }

    protected static void deleteFiles(File afile[])
    {
        for (int i = 0; i < afile.length; i++)
        {
            if (afile[i].isDirectory())
            {
                System.out.println((new StringBuilder()).append("Deleting ").append(afile[i]).toString());
                deleteFiles(afile[i].listFiles());
            }
            afile[i].delete();
        }
    }

    public ISaveHandler getSaveLoader(String s, boolean flag)
    {
        return new PlayerNBTManager(savesDirectory, s, flag);
    }

    public boolean isOldSaveType(String s)
    {
        return false;
    }

    public boolean convertMapFormat(String s, IProgressUpdate iprogressupdate)
    {
        return false;
    }
}
