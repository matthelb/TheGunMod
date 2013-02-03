package com.heuristix.guns.helper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import net.minecraft.client.Minecraft;

import com.heuristix.guns.util.Log;

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
	    File minecraftDir = Minecraft.getMinecraftDir();
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
	        return folder;
	    }
	    return null;
	}

	public static File getTempFile(String name, String format, byte[] value) {
	    FileOutputStream out = null;
	    try {
	        File file = File.createTempFile(name, format);
	        out = new FileOutputStream(file);
	        out.write(value);
	        file.deleteOnExit();
	        return file;
	    } catch (IOException e) {
	        Log.getLogger().throwing(IOHelper.class.getName(), "getTempFile(String name, String format, byte[] value)", e);
	    } finally {
	        if (out != null) {
	            try {
	                out.close();
	            } catch (IOException e) {
	                Log.getLogger().throwing(IOHelper.class.getName(), "getTempFile(String name, String format, byte[] value)", e);
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
	
}