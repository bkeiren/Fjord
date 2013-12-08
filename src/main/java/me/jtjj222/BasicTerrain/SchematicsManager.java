package me.jtjj222.BasicTerrain;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class SchematicsManager {
	
	private byte[] blocks,metadata;
	private short width,height,length;
	
	public void loadUncompressedSchematic(DataInput in) throws IOException{
		NBT_Tag compound = NBT_Tag.readTag(in);
		NBT_Tag_Compound tag = (NBT_Tag_Compound) compound;
		
		this.width = ((NBT_Tag_Short)tag.payload.get("Width")).payload;///x axis 
		this.height = ((NBT_Tag_Short)tag.payload.get("Height")).payload;//y axis
		this.length = ((NBT_Tag_Short)tag.payload.get("Length")).payload;//z axis
		
		this.blocks = ((NBT_Tag_Byte_Array)tag.payload.get("Blocks")).payload;
		this.metadata = ((NBT_Tag_Byte_Array)tag.payload.get("Data")).payload;
	}
	
	public void writeUncompressedSchematic(DataOutput out) throws IOException {
		NBT_Tag_Compound root = new NBT_Tag_Compound("Schematic");
		root.payload.put("Width", new NBT_Tag_Short("Width",this.width));
		root.payload.put("Height", new NBT_Tag_Short("Height",this.height));
		root.payload.put("Length", new NBT_Tag_Short("Length",this.length));
		
		root.payload.put("Blocks", new NBT_Tag_Byte_Array("Blocks",this.blocks));
		root.payload.put("Data", new NBT_Tag_Byte_Array("Data",this.metadata));
		root.writeTag(out);
	}
	
	public void writeUncompressedSchematic(File f) throws IOException {
		DataOutputStream out = new DataOutputStream(new FileOutputStream(f));
		writeUncompressedSchematic(out);
		out.close();
	}
	
	public void writeGzipedSchematic(File f) throws IOException {
		DataOutputStream out = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(f)));
		writeUncompressedSchematic(out);
		out.close();
	}
	
	//Most schematics are gzipped
	public void loadGzipedSchematic(File f) throws IOException {
		DataInputStream in = new DataInputStream(new GZIPInputStream(new FileInputStream(f)));
		loadUncompressedSchematic(in);
		in.close();
	}
	
	public void loadUncompressedSchematic(File f) throws IOException {
		DataInputStream in = new DataInputStream(new FileInputStream(f));
		loadUncompressedSchematic(in);
		in.close();
	}
	
	private int getBlockOffset(int x, int y, int z) {
		return y * width * length + z * width + x;
	}
	
	public byte getBlockIdAt(int x, int y, int z) {
		int offset = getBlockOffset(x, y, z);
		if (offset < this.blocks.length && offset >= 0) return this.blocks[offset];
		else return -1;
	}
	
	public void setBlockIdAt(int x, int y, int z, byte id) {
		int offset = getBlockOffset(x, y, z);
		if (offset < this.blocks.length && offset >= 0) this.blocks[offset] = id;
	}
	
	public byte getMetadataAt(int x, int y, int z) {
		int offset = getBlockOffset(x, y, z);
		if (offset < this.metadata.length && offset >= 0) return this.metadata[offset];
		else return 0; 
	}
	
	public void setMetadataIdAt(int x, int y, int z, byte data) {
		int offset = getBlockOffset(x, y, z);
		if (offset < this.metadata.length && offset >= 0) this.metadata[offset] = data;
	}


	public short getWidth() {
		return width;
	}

	public void setWidth(short width) {
		this.width = width;
	}

	public short getHeight() {
		return height;
	}

	public void setHeight(short height) {
		this.height = height;
	}

	public short getLength() {
		return length;
	}

	public void setLength(short length) {
		this.length = length;
	}

	public byte[] getBlocks() {
		return blocks;
	}

	public void setBlocks(byte[] blocks) {
		this.blocks = blocks;
	}

	public byte[] getMetadata() {
		return metadata;
	}

	public void setMetadata(byte[] metadata) {
		this.metadata = metadata;
	}
}
