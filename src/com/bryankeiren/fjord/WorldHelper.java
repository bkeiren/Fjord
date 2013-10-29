package com.bryankeiren.fjord;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_6_R3.CraftWorld;

public class WorldHelper 
{
	public static boolean setBlockTypeIdAndDataFast(CraftWorld world, int x, int y, int z, int blockId, byte data) {
	    return world.getHandle().getChunkAt(x >> 4, z >> 4).a(x & 0x0f, y, z & 0x0f, blockId, data);
	}
	
	public static boolean setBlockTypeAndDataFast(CraftWorld world, int x, int y, int z, Material type, byte data) {
	    return setBlockTypeIdAndDataFast(world, x, y, z, type.getId(), data);
	}
	
	public static boolean setBlockTypeIdFast(CraftWorld world, int x, int y, int z, int blockId) {
	    return setBlockTypeIdAndDataFast(world, x, y, z, blockId, (byte) world.getHandle().getData(x, y, z));
	}
	
	public static boolean setBlockTypeFast(CraftWorld world, int x, int y, int z, Material type) {
	    return setBlockTypeIdFast(world, x, y, z, type.getId());
	}
	
	public static boolean setBlockTypeIdAndDataFast(CraftWorld world, Location location, int blockId, byte data) {
	    return setBlockTypeIdAndDataFast(world, location.getBlockX(), location.getBlockY(), location.getBlockZ(), blockId, data);
	}
	
	public static boolean setBlockTypeAndDataFast(CraftWorld world, Location location, Material type, byte data) {
	    return setBlockTypeAndDataFast(world, location.getBlockX(), location.getBlockY(), location.getBlockZ(), type, data);
	}
	
	public static boolean setBlockTypeIdFast(CraftWorld world, Location location, int blockId) {
	    return setBlockTypeIdFast(world, location.getBlockX(), location.getBlockY(), location.getBlockZ(), blockId);
	}
	
	public static boolean setBlockTypeFast(CraftWorld world, Location location, Material type) {
	    return setBlockTypeFast(world, location.getBlockX(), location.getBlockY(), location.getBlockZ(), type);
	}
	
	public static boolean setBlockDataFast(CraftWorld world, int x, int y, int z, byte data) {
	    return setBlockTypeIdAndDataFast(world, x, y, z, world.getHandle().getTypeId(x, y, z), data);
	}
	
	public static boolean setBlockDataFast(CraftWorld world, Location location, byte data) {
	    return setBlockDataFast(world, location.getBlockX(), location.getBlockY(), location.getBlockZ(), data);
	}
}
