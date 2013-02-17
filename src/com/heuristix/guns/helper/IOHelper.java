package com.heuristix.guns.helper;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

import com.heuristix.guns.util.Log;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class IOHelper {
	
	public static File HOME_DIRECTORY;

	private IOHelper() { }

	public static byte[] read(URL url) {
	    InputStream in = null;
	    try {
	        in = url.openStream();
	        return IOHelper.read(in);
	    } catch (IOException e) {
	        Log.getLogger().throwing(IOHelper.class.getName(), "read(URL url)", e);
	        return null;
	    } finally {
	        if (in != null) {
	            try {
	                in.close();
	            } catch (IOException e) {
	                Log.getLogger().throwing(IOHelper.class.getName(), "read(URL url)", e);
	            }
	        }
	    }
	}

	public static byte[] read(File file) {
	    FileInputStream fis = null;
	    try {
	        fis = new FileInputStream(file);
	        return IOHelper.read(fis);
	    } catch (FileNotFoundException e) {
	        Log.getLogger().throwing(IOHelper.class.getName(), "read(File file)", e);
	        return null;
	    } finally {
	        if (fis != null) {
	            try {
	                fis.close();
	            } catch (IOException e) {
	                Log.getLogger().throwing(IOHelper.class.getName(), "read(File file)", e);
	            }
	        }
	    }
	}

	public static byte[] read(InputStream is) {
	    ByteArrayOutputStream os = new ByteArrayOutputStream();
	    byte[] buffer = new byte[4096];
	    int n;
	    try {
	        while ((n = is.read(buffer)) != -1) {
	            os.write(buffer, 0, n);
	        }
	    } catch (IOException e) {
	        Log.getLogger().throwing(IOHelper.class.getName(), "read(InputStream is)", e);
	        return null;
	    } catch (ArrayIndexOutOfBoundsException e) {
	        Log.getLogger().throwing(IOHelper.class.getName(), "read(InputStream is)", e);
	        return null;
	    }
	    return os.toByteArray();
	}

	public static File getMinecraftDir(String dir) {
		File minecraftDir;
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			minecraftDir = Minecraft.getMinecraftDir();
		} else {
			minecraftDir = new File(System.getProperty("user.dir"));
		}
	    if (minecraftDir != null) {
	        return new File(minecraftDir.getAbsolutePath() + File.separator + dir);
	    }
	    return null;
	}

	public static File getHeuristixFile(String path, String name) {
	    File folder = IOHelper.getHeuristixDir(path);
	    if(folder != null) {
	        return new File(folder.getAbsolutePath() + File.separator + name);
	    }
	    return null;
	}

	public static File getHeuristixDir(String name) {
	    File folder = getMinecraftDir("heuristix");
	    if (folder != null) {
	        folder = new File(folder.getAbsolutePath() + File.separator + name);
	        if (!folder.exists()) {
	            folder.mkdirs();
	        }
	    }
	    return folder;
	}

	public static File getSysTempFile(String name, String format, byte[] value) {
		try {
			return getTempFile(File.createTempFile(name, format), value);
		} catch (IOException e) {
			Log.getLogger().throwing(IOHelper.class.getName(), "getSysTempFile(String name, String format, byte[] value)", e);
		}
		return null;
	}
	
	public static File getHeuristixTempFile(String folder, String name, byte[] value) {
		return getTempFile(getHeuristixFile(folder, "tmp" + File.separator + name), value);
	}
	
	public static File getHeuristixTempFile(String folder, String name) {
		return getTempFile(getHeuristixFile(folder, "tmp" + File.separator + name));
	}
	
	public static File getTempFile(File file) {
		file.deleteOnExit();
		return file;
	}
	
	public static File getTempFile(File file, byte[] value) {
		FileOutputStream out = null;
		try {
			File parent = file.getParentFile();
			if (!parent.exists()) {
				parent.mkdirs();
			}
	        out = new FileOutputStream(file);
	        out.write(value);
	        return getTempFile(file);
	    } catch (IOException e) {
	        Log.getLogger().throwing(IOHelper.class.getName(), "getTempFile(File file, byte[] value)", e);
	    } finally {
	        if (out != null) {
	            try {
	                out.close();
	            } catch (IOException e) {
	                Log.getLogger().throwing(IOHelper.class.getName(), "getTempFile(File file, byte[] value)", e);
	            }
	        }
	    }
		return null;
	}

	public static File getFile(String name, File dir) {
	    File f = new File(dir.getAbsolutePath() + File.separator + name);
	    if (f.exists()) {
	    	return f;
	    }
	    return null;
	}

	public static File getHomeDirectory() {
	    if (HOME_DIRECTORY == null) {
	        HOME_DIRECTORY = new File(System.getProperty("user.home"));
	    }
	    return HOME_DIRECTORY;
	}

	public static byte[] writeIntsToByteArray(int... data) throws IOException {
	    ByteArrayOutputStream bOut = new ByteArrayOutputStream();
	    DataOutputStream out = new DataOutputStream(bOut);
	    for (int i : data) {
	        out.writeInt(i);
	    }
	    return bOut.toByteArray();
	}
	
}