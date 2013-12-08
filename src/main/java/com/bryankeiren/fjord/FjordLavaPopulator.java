package com.bryankeiren.fjord;

import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

public class FjordLavaPopulator extends BlockPopulator {
	private int m_MaxUndergroundLavaLakeY = 10;

	@Override
	public void populate(World world, Random random, Chunk chunk) {
		for (int z = 0; z < 16; ++z) {
			for (int x = 0; x < 16; ++x) {
				for (int y = 1; y < m_MaxUndergroundLavaLakeY; ++y) {
					if (chunk.getBlock(x, y, z).getType() == Material.AIR) {
						//chunk.getBlock(x, y, z).setType(Material.LAVA);
						WorldHelper.setBlockTypeFast(world, chunk.getX() * 16 + x, y, chunk.getZ() * 16 + z, Material.LAVA);
					}
				}
			}
		}
	}
}
