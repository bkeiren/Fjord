package me.jtjj222.BasicTerrain;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBT_Tag_Float extends NBT_Tag{
	
	public float payload;

	public NBT_Tag_Float(String name){
		super(5, name);
	}
	public NBT_Tag_Float(String name, float payload){
		super(8, name);
		this.payload = payload;
	}

	@Override
	public void readTagPayload(DataInput in) throws IOException {
		this.payload = in.readFloat();		
	}

	public void writeTag(DataOutput out) throws IOException {
		out.write(this.id);
		out.writeUTF(this.name);
		this.writePayload(out);
	}
	
	public void writePayload(DataOutput out) throws IOException {
		out.writeFloat(this.payload);
	}
	
}
