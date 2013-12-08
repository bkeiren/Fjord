package me.jtjj222.BasicTerrain;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBT_Tag_Int_Array extends NBT_Tag{
	
	public int size;
	public int[] payload;

	public NBT_Tag_Int_Array(String name){
		super(11, name);
	}
	
	public NBT_Tag_Int_Array(String name, int[] payload){
		super(8, name);
		this.payload = payload;
	}

	@Override
	public void readTagPayload(DataInput in) throws IOException {
		int size = in.readInt();
		this.size = size;
		this.payload = new int[size];
		
		for (int i = 0; i < size; i++) {
			this.payload[i] = in.readInt();
		}
	}
	
	public void writeTag(DataOutput out) throws IOException {
		out.write(this.id);
		out.writeUTF(this.name);
		out.writeInt(this.size);
		this.writePayload(out);
	}
	
	public void writePayload(DataOutput out) throws IOException {
		for (int i: this.payload) {
			out.writeInt(i);
		}
	}
}
