package me.jtjj222.BasicTerrain;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;

public class NBT_Tag_Compound extends NBT_Tag{
	
	public HashMap<String,NBT_Tag> payload;

	public NBT_Tag_Compound(String name){
		super(10, name);
	}
	public NBT_Tag_Compound(String name, HashMap<String,NBT_Tag> payload){
		super(8, name);
		this.payload = payload;
	}

	@Override
	public void readTagPayload(DataInput in) throws IOException {
		payload = new HashMap<String,NBT_Tag>();
		NBT_Tag tag;
		//while it isn't Tag_End
		while ((tag = NBT_Tag.readTag(in)).id != 0) {
			this.payload.put(tag.name,tag);
		}
		this.payload.put("__end",new NBT_Tag_End("__end"));
	}
	
	public void writeTag(DataOutput out) throws IOException {
		out.write(this.id);
		out.writeUTF(this.name);
		this.writePayload(out);
	}
	
	public void writePayload(DataOutput out) throws IOException {
		for(String key : payload.keySet()) {
			NBT_Tag tag = payload.get(key);
			tag.writeTag(out);
		}
	}
}
