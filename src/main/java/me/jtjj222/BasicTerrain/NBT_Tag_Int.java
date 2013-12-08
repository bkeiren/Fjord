package me.jtjj222.BasicTerrain;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBT_Tag_Int extends NBT_Tag{
	
	public int payload;

	public NBT_Tag_Int(String name){
		super(3, name);
	}

	@Override
	public void readTagPayload(DataInput in) throws IOException {
		this.payload = in.readInt();		
	}
	
	public NBT_Tag_Int(String name, int payload){
		super(8, name);
		this.payload = payload;
	}
	
	public void writeTag(DataOutput out) throws IOException {
		out.write(this.id);
		out.writeUTF(this.name);
		this.writePayload(out);
	}

	public void writePayload(DataOutput out) throws IOException {
		out.writeInt(this.payload);
	}
	
}
