package com.bryankeiren.fjord;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.noise.SimplexNoiseGenerator;;

public class FjordOrePopulator extends BlockPopulator 
{	
	private SimplexNoiseGenerator gen1 = null;
	
	public FjordOrePopulator()
	{
		
	}
	
	@Override
	public void populate( World world, Random random, Chunk chunk )
	{
		
	}
}
