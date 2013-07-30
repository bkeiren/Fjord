package com.bryankeiren.fjord;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

public class FjordSeaPopulator extends BlockPopulator 
{
	private int seaLevel = 64; 	
	
	@Override
	public void populate( World world, Random random, Chunk chunk )
	{
		/*for (int z = 0; z < 16; ++z)
		{
			for (int x = 0; x < 16; ++x)
			{
				int realX = x + chunk.getX() * 16;
				int realZ = z + chunk.getZ() * 16;
				int highestY = world.getHighestBlockYAt(realX, realZ);
				for (int y = highestY; y < seaLevel; ++y)
				{
					chunk.getBlock(x,  y,  z).setType(Material.WATER);
				}
			}
		}*/
	}
}
