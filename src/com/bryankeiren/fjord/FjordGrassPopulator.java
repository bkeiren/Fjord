package com.bryankeiren.fjord;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.craftbukkit.v1_6_R2.CraftWorld;

public class FjordGrassPopulator extends BlockPopulator 
{	
	private float density = 0.35f;
	private float grassVsFernDistribution = 0.6f;
	
	private Fjord plugin = null;
	
	public FjordGrassPopulator( Fjord _plugin )
	{
		plugin = _plugin;
	}
	
	@Override
	public void populate( World world, Random random, Chunk chunk )
	{
		int realZ = chunk.getZ() * 16;
		for (int z = 0; z < 16; ++z, ++realZ)
		{
			int realX = chunk.getX() * 16;			
			for (int x = 0; x < 16; ++x, ++realX)
			{
				float rf = random.nextFloat();
				
				// Do a random-check with the density parameter to see if we should place grass here.
				if (rf <= density)
				{					
					Block highestBlock = world.getHighestBlockAt(realX, realZ);
					
					// If the block is a free block...
					if (highestBlock.getType() == Material.AIR)
					{						
						Block highestBlockBelow = world.getBlockAt(realX, highestBlock.getY() - 1, realZ);
						
						// ...and we have grass below us.
						if (highestBlockBelow.getType() == Material.GRASS)
						{		
							WorldHelper.setBlockTypeAndDataFast((CraftWorld)world, highestBlock.getX(), highestBlock.getY(), highestBlock.getZ(), Material.LONG_GRASS, (byte)((random.nextFloat() < grassVsFernDistribution) ? 0x1 : 0x2));
							//highestBlock.setType(Material.LONG_GRASS);
							//highestBlock.setData((byte)((random.nextFloat() < grassVsFernDistribution) ? 0x1 : 0x2));
						}
					}
				}
			}
		}
	}
}
