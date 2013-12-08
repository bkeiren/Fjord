package com.bryankeiren.fjord;

import java.util.Random;


import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;

public class FjordWaterStreamPopulator extends BlockPopulator 
{	
	private float occurrence = 0.1f;
	
	private Fjord plugin = null;
	public FjordWaterStreamPopulator( Fjord _plugin )
	{
		plugin = _plugin;
	}
	
	@Override
	public void populate( World world, Random random, Chunk chunk )
	{
		if (random.nextFloat() <= occurrence)
		{
			int x = random.nextInt(16);
			int z = random.nextInt(16);
			
			int realX = x + chunk.getX() * 16;
			int realZ = z + chunk.getZ() * 16;
			
			Block highest = world.getHighestBlockAt(realX, realZ);
			
			//ExpandToNeighbour(0, 2 + random.nextInt(3), highest.getRelative(BlockFace.DOWN));
			
			CutStream(highest.getRelative(BlockFace.DOWN), 1 + random.nextInt(3));
		}
	}
	
	private void CutStream( Block origin, int streamWidth )
	{
		if (origin == null)
		{
			return;
		}
		
		// Check NORTH, EAST, WEST and SOUTH. First one to go down is the direction we will take.
		//	NOTE: 'going down' means that the adjacent block is air and the block below that is also air.
		//
		// Start going in the found direction, cutting out 1 block for every block that we are from the
		// center of the stream.
		
		int dX = 0;
		int dZ = 0;
		
		BlockFace[] blockFaces = {BlockFace.NORTH, BlockFace.EAST, BlockFace.WEST, BlockFace.SOUTH};
		for (int i = 0; i < blockFaces.length; ++i)
		{
			Block neighbour = origin.getRelative(blockFaces[i]);
			if (neighbour.getType() == Material.AIR &&
				neighbour.getRelative(BlockFace.DOWN).getType() == Material.AIR)
			{
				dX = origin.getX() - neighbour.getX();
				dZ = origin.getZ() - neighbour.getZ();
				break;
			}
		}
		
		Block centerBlock = origin;
		int halfStreamWidth = streamWidth >> 1;
		boolean doloop = true;
		while (doloop)
		{
			centerBlock.setType(Material.WATER);
			
			Block nextCenterBlock = null;
			
			for (int i = -halfStreamWidth; i < halfStreamWidth; ++i)
			{				
				Block b = centerBlock.getRelative(dX * i, 0, dZ * i);
				if (b.getType() != Material.AIR)
				{
					b.setType(Material.WATER);
				}
				if (i == 0)
				{
					while (b.getType() == Material.AIR)
					{
						b = b.getRelative(0, -1, 0);
						b.setType(Material.WATER);
					}
					nextCenterBlock = b;

					if (b.getRelative(0, 1, 0).getType() != Material.AIR)
					{
						doloop = false;
					}
				}
			}
			centerBlock = nextCenterBlock;
		}
	}
	
	private void ExpandToNeighbour( int iteration, int maxIterations, Block block )
	{
		if (block != null &&
			block.getType() != Material.AIR &&
			block.getType() != Material.WATER)
		{
			if (iteration < maxIterations)
			{
				block.setType(Material.WATER);
				
				Block up = block.getRelative(BlockFace.UP);
				if (up.getType() != Material.WATER)
				{
					up.setType(Material.AIR);
				}
				
				ExpandToNeighbour(iteration + 1, maxIterations, block.getRelative(BlockFace.NORTH));
				ExpandToNeighbour(iteration + 1, maxIterations, block.getRelative(BlockFace.EAST));
				ExpandToNeighbour(iteration + 1, maxIterations, block.getRelative(BlockFace.WEST));
				ExpandToNeighbour(iteration + 1, maxIterations, block.getRelative(BlockFace.SOUTH));
			}
			else
			{
				BlockFace[] blockFaces = {BlockFace.NORTH, BlockFace.EAST, BlockFace.WEST, BlockFace.SOUTH};
				
				for (int i = 0; i < blockFaces.length; ++i)
				{
					Block neighbour = block.getRelative(blockFaces[i]);
					if (neighbour.getType() == Material.AIR)
					{
						neighbour.setType(Material.DIRT);
						neighbour.getRelative(BlockFace.DOWN).setType(Material.DIRT);
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
