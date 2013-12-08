package com.bryankeiren.fjord;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.generator.ChunkGenerator;

public final class Fjord extends JavaPlugin
{
	@Override
	public void onEnable()
	{
		getCommand("fjord").setExecutor(new FjordCommandExecutor(this));
		
		//CopySchematics();
	}
	
	@Override
	public void onDisable()
	{
		
	}
	
	public ChunkGenerator getDefaultWorldGenerator( String worldName, String id ) 
	{
        return new FjordChunkGenerator(this);
    }
	
	private void CopySchematics()
	{
		CopyFromJarToFolder("/schematics/fjordPine0.schematic", getDataFolder() + "/fjordPine0.schematic");
		CopyFromJarToFolder("/schematics/fjordPine1.schematic", getDataFolder() + "/fjordPine1.schematic");
		CopyFromJarToFolder("/schematics/fjordSpruce0.schematic", getDataFolder() + "/fjordSpruce0.schematic");
		CopyFromJarToFolder("/schematics/fjordSpruce1.schematic", getDataFolder() + "/fjordSpruce1.schematic");
	}
	
	private void CopyFromJarToFolder( String _FileInsideJar, String _FileOutsideJar )
	{
		InputStream stream = Fjord.class.getResourceAsStream(_FileInsideJar);
	    if (stream == null) 
	    {
	        //send your exception or warning
	    	this.getLogger().log(Level.SEVERE, "Failed to open resource stream to file in .jar-file: " + _FileInsideJar);
	    }
	    OutputStream resStreamOut;
	    int readBytes;
	    byte[] buffer = new byte[4096];
	    try 
	    {
	    	File file = new File(_FileOutsideJar);
	    	if (!file.exists())
	    	{
		    	file.createNewFile();
	    	}
	        resStreamOut = new FileOutputStream(file);
	        while ((readBytes = stream.read(buffer)) > 0) 
	        {
	            resStreamOut.write(buffer, 0, readBytes);
	        }
	    } 
	    catch (IOException e1) 
	    {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
	    }
	}
}
