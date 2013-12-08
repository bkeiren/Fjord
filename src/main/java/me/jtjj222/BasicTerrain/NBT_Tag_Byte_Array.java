package me.jtjj222.BasicTerrain;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBT_Tag_Byte_Array extends NBT_Tag{
	
	public int size;
	public byte[] payload;

	public NBT_Tag_Byte_Array(String name){
		super(7, name);
	}
	
	public NBT_Tag_Byte_Array(String name, byte[] payload){
		super(8, name);
		this.payload = payload;
	}

	@Override
	public void readTagPayload(DataInput in) throws IOException {
		int size = in.readInt();
		this.size = size;
		this.payload = new byte[size];
		
		in.readFully(this.payload);
	}
	
	public void writeTag(DataOutput out) throws IOException {
		out.write(this.id);
		out.writeUTF(this.name);
		out.writeInt(this.size);
		this.writePayload(out);
	}
	
	public void writePayload(DataOutput out) throws IOException {
		for (byte i: this.payload) {
			out.writeByte(i);
		}
	}
}
