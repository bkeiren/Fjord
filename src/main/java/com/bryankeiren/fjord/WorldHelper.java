package com.bryankeiren.fjord;

import com.darkblade12.utils.ReflectionUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class WorldHelper 
{
	public static boolean setBlockTypeIdAndDataFast(World w, int x, int y, int z, int blockId, byte data) {
		try {
			Object world = ReflectionUtil.invokeMethod("getHandle", w.getClass(), w);
			Object chunk = ReflectionUtil.invokeMethod("getChunkAt", world.getClass(), world, x >> 4, z >> 4);
			return (Boolean) ReflectionUtil.invokeMethod("a", chunk.getClass(), chunk, x & 15, y,
					z & 15, blockId, (int) data);
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean setBlockTypeAndDataFast(World world, int x, int y, int z, Material type, byte data) {
	    return setBlockTypeIdAndDataFast(world, x, y, z, type.getId(), data);
	}
	
	public static boolean setBlockTypeIdFast(World world, int x, int y, int z, int blockId) {
	    return setBlockTypeIdAndDataFast(world, x, y, z, blockId, getBlockData(world, x, y, z));
	}

	public static byte getBlockData(World w, int x, int y, int z) {
		try {
			Object world = ReflectionUtil.invokeMethod("getHandle", w.getClass(), w);
			return (Byte) ReflectionUtil.invokeMethod("getData", world.getClass(), world, x, y, z);
		} catch (Exception e) {
			return 0;
		}
	}

	public static int getBlockId(World w, int x, int y, int z) {
		try {
			Object world = ReflectionUtil.invokeMethod("getHandle", w.getClass(), w);
			return (Byte) ReflectionUtil.invokeMethod("getTypeId", world.getClass(), world, x, y, z);
		} catch (Exception e) {
			return 0;
		}
	}
	
	public static boolean setBlockTypeFast(World world, int x, int y, int z, Material type) {
	    return setBlockTypeIdFast(world, x, y, z, type.getId());
	}
	
	public static boolean setBlockTypeIdAndDataFast(World world, Location location, int blockId, byte data) {
	    return setBlockTypeIdAndDataFast(world, location.getBlockX(), location.getBlockY(), location.getBlockZ(), blockId, data);
	}
	
	public static boolean setBlockTypeAndDataFast(World world, Location location, Material type, byte data) {
	    return setBlockTypeAndDataFast(world, location.getBlockX(), location.getBlockY(), location.getBlockZ(), type, data);
	}
	
	public static boolean setBlockTypeIdFast(World world, Location location, int blockId) {
	    return setBlockTypeIdFast(world, location.getBlockX(), location.getBlockY(), location.getBlockZ(), blockId);
	}
	
	public static boolean setBlockTypeFast(World world, Location location, Material type) {
	    return setBlockTypeFast(world, location.getBlockX(), location.getBlockY(), location.getBlockZ(), type);
	}
	
	public static boolean setBlockDataFast(World world, int x, int y, int z, byte data) {
	    return setBlockTypeIdAndDataFast(world, x, y, z, getBlockId(world, x, y, z), data);
	}
	
	public static boolean setBlockDataFast(World world, Location location, byte data) {
	    return setBlockDataFast(world, location.getBlockX(), location.getBlockY(), location.getBlockZ(), data);
	}
}
