package com.bryankeiren.fjord;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.Random;

public class FjordTreePopulator extends BlockPopulator {
	private float minDensity = 0.0f;
	private float maxDensity = 0.1f;
	private float redwoodVsTallredwoodDistribution = 0.5f;
	private SimplexOctaveGenerator gen1 = null;

	@Override
	public void populate(World world, Random random, Chunk chunk) {
		if (gen1 == null) {
			int numOctaves = 2;
			gen1 = new SimplexOctaveGenerator(world, numOctaves);
			gen1.setScale(1 / 128.0);
		}

		double density = minDensity + ((float) ((gen1.noise(chunk.getZ() * 16 + 8192, -chunk.getX() * 16 - 8192, 0.2,
				0.7) + 1.0) * 0.5) * (maxDensity - minDensity));

		if (density <= 0.0) {
			return;    // Skip this chunk.
		}

		int realZ = chunk.getZ() * 16;
		for (int z = 0; z < 16; ++z, ++realZ) {
			int realX = chunk.getX() * 16;
			for (int x = 0; x < 16; ++x, ++realX) {
				float rf = random.nextFloat();

				// Do a random-check with the density parameter to see if we should place a tree here.

				//if (rf <= density)
				if (rf <= density) {
					Block highestBlock = world.getHighestBlockAt(realX, realZ);

					// If the block is a free block...
					if (highestBlock.getType() == Material.AIR ||
							highestBlock.getType() == Material.SNOW) {
						Block highestBlockBelow = highestBlock.getRelative(BlockFace.DOWN);

						// ...and we have grass below us.
						if (highestBlockBelow.getType() == Material.GRASS) {
							world.generateTree(highestBlock.getLocation(),
									random.nextFloat() < redwoodVsTallredwoodDistribution ? TreeType.REDWOOD : TreeType.TALL_REDWOOD);
						}
					}
				}
			}
		}
	}
}
