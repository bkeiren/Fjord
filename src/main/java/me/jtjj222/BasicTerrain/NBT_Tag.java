package me.jtjj222.BasicTerrain;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public abstract class NBT_Tag {
	
	public static NBT_Tag getNewTag(int id,String name) {
		switch (id) {
		case 0 : return new NBT_Tag_End("");
		case 1 : return new NBT_Tag_Byte(name);
		case 2 : return new NBT_Tag_Short(name);
		case 3 : return new NBT_Tag_Int(name);
		case 4 : return new NBT_Tag_Long(name);
		case 5 : return new NBT_Tag_Float(name);
		case 6 : return new NBT_Tag_Double(name);
		case 7 : return new NBT_Tag_Byte_Array(name);
		case 8 : return new NBT_Tag_String(name);
		case 9 : return new NBT_Tag_List(name);
		case 10 : return new NBT_Tag_Compound(name);
		case 11 : return new NBT_Tag_Int_Array(name);
		default : return null;
		}
	}
	public static NBT_Tag readTag(DataInput in) throws IOException {
		NBT_Tag tag;
		byte tag_id = in.readByte();
		if (tag_id == 0) return new NBT_Tag_End("");
		String tag_name = in.readUTF();
		tag = getNewTag(tag_id,tag_name);
		tag.readTagPayload(in);
		return tag;
	}
	
	//First byte of the tag is the type id
	byte id;
	
	//Then the name of the tag in UTF-8
	String name;
	
	//Then the tag-specific payload...
	
	public NBT_Tag(String name) {
		this.id = 0; //overwritten
		this.name = name;
	}
	protected NBT_Tag(int id, String name) {
		this.id = (byte) id;
		this.name = name;
	}
	
	//Read the tag's payload (assumes that the id,
	//name_length and name have already been read
	//in order to identfy the tag
	public abstract void readTagPayload(DataInput in) throws IOException;
	
	public abstract void writeTag(DataOutput out) throws IOException;
	
	public abstract void writePayload(DataOutput out) throws IOException;
}
