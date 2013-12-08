package me.jtjj222.BasicTerrain;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBT_Tag_String extends NBT_Tag{
	
	public String payload;

	public NBT_Tag_String(String name){
		super(8, name);
	}
	public NBT_Tag_String(String name, String payload){
		super(8, name);
		this.payload = payload;
	}

	@Override
	public void readTagPayload(DataInput in) throws IOException {
		this.payload = in.readUTF();
	}
	
	public void writeTag(DataOutput out) throws IOException {
		out.write(this.id);
		out.writeUTF(this.name);
		this.writePayload(out);
	}
	
	public void writePayload(DataOutput out) throws IOException {
		out.writeUTF(this.payload);
	}
}
