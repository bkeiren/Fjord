package com.bryankeiren.fjord;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

public class FjordWaterStreamPopulator extends BlockPopulator {
	private float occurrence = 0.1f;

	@Override
	public void populate(World world, Random random, Chunk chunk) {
		if (random.nextFloat() <= occurrence) {
			int x = random.nextInt(16);
			int z = random.nextInt(16);

			int realX = x + chunk.getX() * 16;
			int realZ = z + chunk.getZ() * 16;

			Block highest = world.getHighestBlockAt(realX, realZ);

			cutStream(highest.getRelative(BlockFace.DOWN), 1 + random.nextInt(3));
		}
	}

	private void cutStream(Block origin, int streamWidth) {
		if (origin == null) {
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
		for (BlockFace blockFace : blockFaces) {
			Block neighbour = origin.getRelative(blockFace);
			if (neighbour.getType() == Material.AIR &&
					neighbour.getRelative(BlockFace.DOWN).getType() == Material.AIR) {
				dX = origin.getX() - neighbour.getX();
				dZ = origin.getZ() - neighbour.getZ();
				break;
			}
		}

		Block centerBlock = origin;
		int halfStreamWidth = streamWidth >> 1;
		boolean doloop = true;
		while (doloop) {
			centerBlock.setType(Material.WATER);

			Block nextCenterBlock = null;

			for (int i = -halfStreamWidth; i < halfStreamWidth; ++i) {
				Block b = centerBlock.getRelative(dX * i, 0, dZ * i);
				if (b.getType() != Material.AIR) {
					b.setType(Material.WATER);
				}
				if (i == 0) {
					while (b.getType() == Material.AIR) {
						b = b.getRelative(0, -1, 0);
						b.setType(Material.WATER);
					}
					nextCenterBlock = b;

					if (b.getRelative(0, 1, 0).getType() != Material.AIR) {
						doloop = false;
					}
				}
			}
			centerBlock = nextCenterBlock;
		}
	}
}
