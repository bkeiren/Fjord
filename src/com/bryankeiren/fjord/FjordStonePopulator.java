package com.bryankeiren.fjord;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

public class FjordStonePopulator extends BlockPopulator 
{	
	@Override
	public void populate( World world, Random random, Chunk chunk )
	{
		/*for (int z = 0; z < 16; ++z)
		{
			for (int x = 0; x < 16; ++x)
			{
				for (int y = 1; y <	50; ++y)	// Can skip y == 0 because we filled that level with bedrock.
				{
					Block block = chunk.getBlock(x,  y,  z);
					if (block.getType() == Material.AIR)
					{
						block.setType(Material.STONE);
					}
				}
			}
		}*/
	}
}
