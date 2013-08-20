package com.bryankeiren.fjord;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

public class FjordTreePopulator extends BlockPopulator 
{	
	private float minDensity = 0.0f;
	private float maxDensity = 0.1f;
	private float redwoodVsTallredwoodDistribution = 0.5f;
	
	private SimplexOctaveGenerator gen1 = null;
	
	private String[] treeSchematics = {	"/schematics/fjordPine0.schematic",
										"/schematics/fjordPine1.schematic",
										"/schematics/fjordSpruce0.schematic",
										"/schematics/fjordSpruce1.schematic"};
	
	private Fjord plugin = null;
	public FjordTreePopulator( Fjord _plugin )
	{
		plugin = _plugin;
	}
	
	@Override
	public void populate( World world, Random random, Chunk chunk )
	{
		//if (true) return;
		
		if (gen1 == null)
		{
			int numOctaves = 2;
			gen1 = new SimplexOctaveGenerator(world, numOctaves);
			gen1.setScale(1/128.0);
		}
		
		//float density = minDensity + ((float)((gen1.noise(chunk.getZ() * 16 + 8192, -chunk.getX() * 16 - 8192, 0.2, 0.7) + 1.0) * 0.5) * (maxDensity - minDensity));
		double density = minDensity + ((float)((gen1.noise(chunk.getZ() * 16 + 8192, -chunk.getX() * 16 - 8192, 0.2, 0.7) + 1.0) * 0.5) * (maxDensity - minDensity));
		
		if (density <= 0.0) 
		{
			return;	// Skip this chunk.
		}
		
		int realZ = chunk.getZ() * 16;
		for (int z = 0; z < 16; ++z, ++realZ)
		{
			int realX = chunk.getX() * 16;			
			for (int x = 0; x < 16; ++x, ++realX)
			{
				float rf = random.nextFloat();
				
				// Do a random-check with the density parameter to see if we should place a tree here.
				
				//if (rf <= density)
				if (rf <= density)
				{
					Block highestBlock = world.getHighestBlockAt(realX, realZ);
					
					// If the block is a free block...
					if (highestBlock.getType() == Material.AIR || 
						highestBlock.getType() == Material.SNOW)
					{
						Block highestBlockBelow = highestBlock.getRelative(BlockFace.DOWN);
						
						// ...and we have grass below us.
						if (highestBlockBelow.getType() == Material.GRASS)
						{							
							world.generateTree(	highestBlock.getLocation(), 
												random.nextFloat() < redwoodVsTallredwoodDistribution ? TreeType.REDWOOD : TreeType.TALL_REDWOOD);
							
							//placeTreeIfPossibleRandom(world, random, chunk, realX, highestBlock.getY(), realZ);
						}
					}
				}
			}
		}
	}
	
	/*
	private void placeTreeIfPossibleRandom( World world, Random random, Chunk chunk, int wX, int wY, int wZ )
	{
		String filepath = plugin.getDataFolder() + treeSchematics[random.nextInt(treeSchematics.length)]; 
		
		plugin.getLogger().info("Placing tree from path: " + filepath);
		
		//if (chunk.getX() % 3 == 0 && chunk.getZ() % 3 == 0) 
		{		 
			try 
			{
				File file = new File(filepath);
				SchematicsManager man = new SchematicsManager();
				man.loadGzipedSchematic(file);
				 
				int width = man.getWidth();
				int height = man.getHeight();
				int length = man.getLength();
				 
				int starty = wY;
				int endy = starty + height;
				
				for (int x = 0; x < width; x++) 
				{
					for (int z = 0; z < length; z++) 
					{
						//int realX = x + chunk.getX() * 16;
						//int realZ = z + chunk.getZ() * 16;
						int realX = x + wX;
						int realZ = z + wZ;
						 
						for (int y = starty; y<=endy && y <255; y++) 
						{
							int rely = y - starty;
							int id = man.getBlockIdAt(x, rely, z);
							byte data = man.getMetadataAt(x, rely, z);
							 
							if (id != -1) world.getBlockAt(realX, y, realZ).setTypeId(id);
							if (id != -1) world.getBlockAt(realX, y, realZ).setData(data);
						}
					}
				}			 
			} 
			catch (IOException e) 
			{
				System.out.println("Could not read the schematic file");
				e.printStackTrace();
			}
		}
	}*/
}
