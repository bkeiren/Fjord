package com.bryankeiren.fjord;

import me.simplex.nordic.populators.Nordic_LakePopulator;

import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.*;
import org.bukkit.util.noise.*;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import libnoiseforjava.NoiseGen.NoiseQuality;
import libnoiseforjava.exception.ExceptionInvalidParam;
import libnoiseforjava.module.RidgedMulti;

import org.bukkit.craftbukkit.v1_6_R2.generator.NormalChunkGenerator;

public class FjordChunkGenerator extends ChunkGenerator
{	
	private Fjord plugin = null;
	
	public FjordChunkGenerator( Fjord _plugin )
	{
		plugin = _plugin;
	}
	
    //This is where you stick your populators - these will be covered in another tutorial
    @Override
    public List<BlockPopulator> getDefaultPopulators( World world ) 
    {
    	/*NormalChunkGenerator t = new NormalChunkGenerator(world, world.getSeed());
    	List<BlockPopulator> list = t.getDefaultPopulators(world);
    	for (int i = 0; i < t.getDefaultPopulators(world).size(); ++i)
    	{
    		plugin.getLogger().info(list.get(i).toString());
    	}*/
    	
    	ArrayList<BlockPopulator> arrayList = new ArrayList<BlockPopulator>();
    	 
    	arrayList.add(new FjordStonePopulator());
    	arrayList.add(new FjordSeaPopulator());
    	//arrayList.add(new FjordWaterStreamPopulator(plugin));
    	//arrayList.add(new Nordic_LakePopulator());
    	arrayList.add(new FjordTreePopulator(plugin));
    	arrayList.add(new FjordGrassPopulator(plugin));
    	arrayList.add(new FjordOrePopulator());
    	 
    	return arrayList;
    }
    
    @Override
    public boolean canSpawn( World world, int x, int z ) 
    {
    	Block highestBlock = world.getHighestBlockAt(x, z).getRelative(BlockFace.DOWN);
    	
    	Material highestBlockType = highestBlock.getType();
    	if (highestBlockType == Material.SAND ||
    		highestBlockType == Material.GRAVEL ||
    		highestBlockType == Material.GRASS ||
    		highestBlockType == Material.STONE)
    	{
    		return true;
    	}
    	
    	return false;
    }
    
    //This converts relative chunk locations to bytes that can be written to the chunk
    public int xyzToByte(int x, int y, int z) 
    {
    	return (x * 16 + z) * 128 + y;
    }
    
    @Deprecated
    public byte[] generate( World world, Random rand, int chunkx, int chunkz ) 
    {
    	throw new UnsupportedOperationException("Custom generator is missing required methods: generate(), generateBlockSections() and generateExtBlockSections()");
    }
    
    // Converts a y-position to a section ID.
    private int getSectionID( int y )
    {
    	return y >> 4;
    }
    
    // Set a block in a chunk. If the section in which the coordinates exist is not allocated,
    // we allocate it here.
    void setBlock( short[][] result, int x, int y, int z, short blockID ) 
    {
    	int sectionID = getSectionID(y);
    	if (result[sectionID] == null) 
    	{
    		result[sectionID] = new short[4096];
    	}
    	result[sectionID][((y & 0xF) << 8) | (z << 4) | x] = blockID;
    }
    
    short getBlock( short[][] result, int x, int y, int z ) 
    {
    	int sectionID = getSectionID(y);
    	if (result[sectionID] == null) 
    	{
			return (short)0;
		}
		return result[sectionID][((y & 0xF) << 8) | (z << 4) | x];
    }
    
    // Compared to generateBlockSections, this function has extended block ID support. That means that the block ID
    // range is [0..4095] (note the fact that the functions returns a 2D array of short instead of byte).
    public short[][] generateExtBlockSections( World world, Random random, int x, int z, BiomeGrid biomes ) 
    {
    	int SimplexOctaves = 8;
    	SimplexOctaveGenerator gen1 = new SimplexOctaveGenerator(world, SimplexOctaves);
    	gen1.setScale(1/128.0); // Roughly: The distance between peaks.
    	
    	int PerlinOctaves = 5;
    	PerlinOctaveGenerator gen2 = new PerlinOctaveGenerator(world, PerlinOctaves);
    	gen2.setScale(1/64.0);
    	
    	int PerlinOctaves2 = 5;
    	PerlinOctaveGenerator gen3 = new PerlinOctaveGenerator(world, PerlinOctaves2);
    	gen3.setScale(1/32.0);
    	
    	int SimplexOctaves2 = 8;
    	SimplexOctaveGenerator gen4 = new SimplexOctaveGenerator(world, SimplexOctaves2);
    	gen4.setScale(1/24.0);
    	
    	int SimplexOctaves3 = 4;
    	SimplexOctaveGenerator gen5 = new SimplexOctaveGenerator(world, SimplexOctaves3);
    	gen5.setScale(1/8.0);
    	
    	/*RidgedMulti ridgedMultiGen = new RidgedMulti();
    	ridgedMultiGen.setFrequency(2);
    	try {	// Try-catch because else Eclipse whines.
			ridgedMultiGen.setOctaveCount(1);
		} catch (ExceptionInvalidParam e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	ridgedMultiGen.setNoiseQuality(NoiseQuality.QUALITY_FAST);
    	ridgedMultiGen.setSeed((int)world.getSeed());*/
    	
    	int worldMaxHeight = world.getMaxHeight();
    	int numSections = worldMaxHeight / 16;
    	short[][] result = new short[numSections][];
    	
    	for (int bZ = 0; bZ < 16; ++bZ)
    	{
	    	for (int bX = 0; bX < 16; ++bX)
	    	{
	    		DoLayer_Bedrock(result, random, bX, bZ);
	    		
	    		int realX = bX + x * 16;	// x == X of the chunk.
	    		int realZ = bZ + z * 16;	// z == Z of the chunk.
	    		double frequency = 0.15;
	    		double amplitude = 0.4;
	    		int multitude = 42;
	    		int sealevel = 64;
	    		double noiseValue = 0.0;
	    		double plainsValue = (gen1.noise(realX * 0.43, realZ * -0.43, 0.1, 0.3) + 1.0) * 0.5;	    		
	    		noiseValue += gen1.noise(realX, realZ, frequency * plainsValue, amplitude) * multitude + sealevel;
	    		noiseValue += gen2.noise(realX, realZ, 0.8, 0.3) * 16;
	    		noiseValue += gen3.noise(realX, realZ, 1.7, 0.45) * 8; 
	    		
	    		for (int bY = 1; bY < noiseValue && bY < worldMaxHeight; ++bY) 
	    		{
	    			double freq = 1.5;
	    			double ampl = 0.6;
	    			
	    			double f0 = (((bY / noiseValue) - 0.8) / 0.2);
	    			if (f0 < 0.0) f0 = 0.0;
	    			if (f0 > 1.0) f0 = 1.0;
	    			ampl *= 1.0 - f0;
	    			
	    			double v = gen4.noise(realX * 0.5, bY * 1.5, realZ, freq, ampl);
	    			double bandHalfWidth = 0.75;
	    			if (v > 0.0 - bandHalfWidth && v < bandHalfWidth)
	    			{
	    				setBlock(result, bX, bY, bZ, (short)Material.STONE.getId());
	    			}
	    		}
	    		
	    		// Set a prelimenary biome based on terrain height. May chance in the code that follows.
	    		int taigaBorder = 95;
	    		biomes.setBiome(bX, bZ, noiseValue > taigaBorder ? Biome.TAIGA_HILLS : Biome.EXTREME_HILLS);
	    		
	    		int actualSealevel = (int)(sealevel * 0.75f);
	    		
	    		DoLayer_Shore(result, world, random, bX, bZ, actualSealevel);
	    		
	    		DoLayer_SeaBed(result, world, random, bX, bZ, actualSealevel);

	    		DoLayer_Ore(result, world, random, bX, bZ, realX, realZ, gen5);
	    		
	    		DoLayer_Sea(result, world, random, bX, bZ, actualSealevel);
	    		
	    		DoLayer_GrassAndDirt(result, world, random, bX, bZ);
	    		
	    		DoLayer_Snow(result, world, random, bX, bZ, taigaBorder);
	    	}
    	}
    	
    	return result;    	
        //return null; // Default - returns null, which drives call to generateBlockSections()
    }
    
    private int findHighestBlockY( short[][] result, World world, int bX, int bZ )
    {
    	int worldMaxHeight = world.getMaxHeight();
    	for (int bY = worldMaxHeight - 1; bY > 0; --bY)
    	{
    		short blockType = getBlock(result, bX, bY, bZ);
    		if (blockType != (short)Material.AIR.getId())
    		{
    			// If the current block is NOT air, we've found the highest one.
    			// TODO MAYBE: Check for overhangs and stuff?
    			return bY;
    		}
    	}
    	return 0;
    }
    
    private void DoLayer_Bedrock( short[][] result, Random random, int bX, int bZ )
    {
    	setBlock(result, bX, 0, bZ, (short)Material.BEDROCK.getId());	// One layer of bedrock.
		
		float gaussianThresholdFactor = 0.1f;
		for (int bY = 1; bY < 5; ++bY)
		{
    		double gaussianDouble = random.nextGaussian();
    		float guassianThreshold = gaussianThresholdFactor * (5 - bY);
    		if (gaussianDouble > (0.0 - guassianThreshold) && 
    			gaussianDouble < guassianThreshold)
    		{
	    		setBlock(result, bX, bY, bZ, (short)Material.BEDROCK.getId());
    		}
		}
    }
    
    private void DoLayer_Shore( short[][] result, World world, Random random, int bX, int bZ, int sealevel )
    {
    	int worldMaxHeight = world.getMaxHeight();
    	
    	int shoreMinLimitDry = 2;	// Minimum number of blocks in the vertical direction (y-axis)
    								// that the shore may extend above sealevel.
    	int shoreMaxLimitDry = 4;	// Maximum number of blocks in the vertical direction (y-axis)
									// that the shore may extend above sealevel.
    	int shoreLimitMinWet = 5;	// Minimum number of blocks in the vertical direction (y-axis)
									// that the shore may extend below sealevel.
    	int shoreLimitMaxWet = 9;	// Maximum number of blocks in the vertical direction (y-axis)
									// that the shore may extend below sealevel.
    	int shoreMinDepth = 2;		// Min shore depth.
    	int shoreMaxDepth = 5;		// Max shore depth.
    	
    	// Find the highest block.
    	int highestBlockY = findHighestBlockY(result, world, bX, bZ);
    	
    	
    	int shoreLimitWet = shoreLimitMinWet + (int)(random.nextFloat() * (shoreLimitMaxWet - shoreLimitMinWet));
    	int shoreLimitDry = shoreMinLimitDry + (int)(random.nextFloat() * (shoreMaxLimitDry - shoreMinLimitDry));
    	if (highestBlockY > sealevel - shoreLimitWet &&
    		highestBlockY < sealevel + shoreLimitDry)
    	{
    		int shoreDepth = shoreMinDepth + (int)(random.nextFloat() * (shoreMaxDepth - shoreMinDepth));
    		for (int bY = highestBlockY; bY > highestBlockY - shoreDepth && bY > 0; --bY)
    		{
    			setBlock(result, bX, bY, bZ, (short)Material.GRAVEL.getId());
    		}
    	}
    	
    	// TODO MAYBE: Place gravel below it up until the next solid block so that it doesn't fall down?
    }
    
    private void DoLayer_SeaBed( short[][] result, World world, Random random, int bX, int bZ, int sealevel )
    {
    	int highestBlockY = findHighestBlockY(result, world, bX, bZ);
    	
    	if (highestBlockY <= sealevel)
    	{
	    	for (int i = highestBlockY; i > highestBlockY - 5 && i > 1; --i)
	    	{
	    		short currBlockMat = getBlock(result, bX, i, bZ);
	    		if (currBlockMat != (short)Material.AIR.getId() &&
	    			currBlockMat != (short)Material.GRAVEL.getId())
	    		{
		    		double nextGaussian = random.nextGaussian();
		    		Material mat = (nextGaussian > -0.2 && nextGaussian < 0.2) ? (Material.CLAY) : (Material.DIRT); 
		    		setBlock(result, bX, i, bZ, (short)mat.getId());   	
	    		}
	    	}
    	}
    }
    
    private void DoLayer_Sea( short[][] result, World world, Random random, int bX, int bZ, int sealevel )
    {
    	int worldMaxHeight = world.getMaxHeight();
    	
    	// Find the highest block.
    	int highestBlockY = findHighestBlockY(result, world, bX, bZ);
    	
    	for (int bY = highestBlockY + 1; bY < sealevel && bY < worldMaxHeight; ++bY)
    	{
    		// Replace air blocks with water.
    		if (getBlock(result, bX, bY, bZ) == (short)Material.AIR.getId())
    		{
    			setBlock(result, bX, bY, bZ, (short)Material.WATER.getId());
    		}
    	}
    }
    
    private void DoLayer_GrassAndDirt( short[][] result, World world, Random random, int bX, int bZ )
    {
    	// Overlay grass, dirt below it.
    	
    	int highestBlockY = findHighestBlockY(result, world, bX, bZ);
    	
    	short highestBlockType = getBlock(result, bX, highestBlockY, bZ);
    	
    	if (highestBlockType != (short)Material.STONE.getId())
    	{
    		return;
    	}
    	
    	int dirtMinDepth = 2;
    	int dirtMaxDepth = 7;
    	
    	int dirtDepth = dirtMinDepth + (int)(random.nextGaussian() * (dirtMaxDepth - dirtMinDepth));
    	for (int bY = highestBlockY - 1; bY > highestBlockY - dirtDepth && bY > 0; --bY)
    	{
    		if (getBlock(result, bY, bY, bZ) != (short)Material.AIR.getId())
    		{
    			setBlock(result, bX, bY, bZ, (short)Material.DIRT.getId());
    		}
    	}
    	setBlock(result, bX, highestBlockY, bZ, (short)Material.GRASS.getId());
    }
    
    private void DoLayer_Snow( short[][] result, World world, Random random, int bX, int bZ, int taigaBorder )
    {
    	int highestBlockY = findHighestBlockY(result, world, bX, bZ);
    	
    	int snowMinTransitionHeight = -2;
    	int snowMaxTransitionHeight = 2;
    	
    	int snowTransitionHeight = snowMinTransitionHeight + (int)(random.nextGaussian() * (snowMaxTransitionHeight - snowMinTransitionHeight));
    	taigaBorder += snowTransitionHeight;
    	
    	if (highestBlockY < taigaBorder)
    	{
    		return;
    	}
    	
    	short blockType = getBlock(result, bX, highestBlockY, bZ);
    	if (blockType == (short)Material.WATER.getId() ||
    		blockType == (short)Material.LAVA.getId() ||
    		blockType == (short)Material.AIR.getId())
    	{
    		return;
    	}
    	
    	if (highestBlockY + 1 < world.getMaxHeight())
    	{
    		setBlock(result, bX, highestBlockY + 1, bZ, (short)Material.SNOW.getId());
    	}
    }
    
    private void DoLayer_Ore( short[][] result, World world, Random random, int bX, int bZ, int realX, int realZ, SimplexOctaveGenerator gen )
    {
    	int highestBlockY = findHighestBlockY(result, world, bX, bZ);
    	
		for (int bY = 1; bY < highestBlockY; ++bY)	// Can skip y == 0 because we filled that level with bedrock.
		{
			double noise = gen.noise(realX, bY, realZ, 5, 0.005, 0.1);
			
			if (noise > 0.75)
			{
				if (getBlock(result, bX, bY, bZ) != (short)Material.AIR.getId())
				{
					short MaterialID = 0;
					
					double gaussianDouble = Math.abs(random.nextGaussian()) / 3.0;	// Workable range [0..1].
					
					
					// Ores that we want, ordered by chance of occurence: (The gaussian distribution
					// of the random double will help us properly distribute)
					// Coal
					// Iron
					// Gold
					// Lapis Lazuli
					// Redstone
					// Diamond
					short[] ores = {	(short)Material.COAL_ORE.getId(),
										(short)Material.IRON_ORE.getId(),
										(short)Material.GOLD_ORE.getId(),
										(short)Material.LAPIS_ORE.getId(),
										(short)Material.REDSTONE_ORE.getId(),
										(short)Material.DIAMOND_ORE.getId()	};
					
					int[] oreLayersMin = {	5,
											5,
											5,
											14,
											5,
											5	};
					int[] oreLayersMax = {	132,
											68,
											34,
											34,
											16,
											16	};
					
					double d = 1.0 / ores.length;
					for (int i = 0; i < ores.length; ++i)
					{
						double threshold = i * d;
						if (gaussianDouble > threshold &&
							gaussianDouble < threshold + d &&
							bY >= oreLayersMin[i] &&
							bY <= oreLayersMax[i])
						{
							setBlock(result, bX, bY, bZ, ores[i]);
							break;
						}
					}
				}
			}
		}
    }
    
    public byte[][] generateBlockSections( World world, Random random, int x, int z, BiomeGrid biomes ) 
    {
        return null; // Default - returns null, which drives call to generate()
    }
}