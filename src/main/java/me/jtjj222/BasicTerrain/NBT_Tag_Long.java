package me.jtjj222.BasicTerrain;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBT_Tag_Long extends NBT_Tag{
	
	public long payload;

	public NBT_Tag_Long(String name){
		super(4, name);
	}
	
	public NBT_Tag_Long(String name, Long payload){
		super(8, name);
		this.payload = payload;
	}

	@Override
	public void readTagPayload(DataInput in) throws IOException {
		this.payload = in.readLong();		
	}
	
	public void writeTag(DataOutput out) throws IOException {
		out.write(this.id);
		out.writeUTF(this.name);
		this.writePayload(out);
	}
	
	public void writePayload(DataOutput out) throws IOException {
		out.writeLong(this.payload);
	}

}
