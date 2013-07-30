package me.jtjj222.BasicTerrain;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;

public class NBT_Tag_List extends NBT_Tag{
	
	public byte tag_type;
	public int size;
	public ArrayList<NBT_Tag> payload;

	public NBT_Tag_List(String name){
		super(9, name);
	}
	
	public NBT_Tag_List(String name, ArrayList<NBT_Tag> payload){
		super(8, name);
		this.payload = payload;
	}

	@Override
	public void readTagPayload(DataInput in) throws IOException {
		this.tag_type = in.readByte();
		int size = in.readInt();
		this.size = size;
		this.payload = new ArrayList<NBT_Tag>();
		
		for (int i = 0; i < size; i++) {
			NBT_Tag tag = NBT_Tag.getNewTag(this.tag_type, "");
			tag.readTagPayload(in);
			this.payload.add(tag);
		}
	}
	
	public void writeTag(DataOutput out) throws IOException {
		out.write(this.id);
		out.writeUTF(this.name);
		out.writeInt(this.size);
		
		this.writePayload(out);
	}
	
	public void writePayload(DataOutput out) throws IOException {
		for (NBT_Tag tag : this.payload) {
			tag.writePayload(out);
		}
	}
}
