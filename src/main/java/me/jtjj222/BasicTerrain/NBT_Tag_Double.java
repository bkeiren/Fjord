package me.jtjj222.BasicTerrain;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBT_Tag_Double extends NBT_Tag{
	
	public double payload;

	public NBT_Tag_Double(String name){
		super(6, name);
	}
	
	public NBT_Tag_Double(String name, double payload){
		super(8, name);
		this.payload = payload;
	}

	@Override
	public void readTagPayload(DataInput in) throws IOException {
		this.payload = in.readDouble();		
	}

	public void writeTag(DataOutput out) throws IOException {
		out.write(this.id);
		out.writeUTF(this.name);
		this.writePayload(out);
	}
	public void writePayload(DataOutput out) throws IOException {
		out.writeDouble(this.payload);
	}
}
