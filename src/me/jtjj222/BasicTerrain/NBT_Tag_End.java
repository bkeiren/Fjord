package me.jtjj222.BasicTerrain;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

//This marks the end of compound tags.
//It has no name, and is only ever a single byte
public class NBT_Tag_End extends NBT_Tag{
	
	public NBT_Tag_End(String name){
		super(0, "");
	}
	
	public NBT_Tag_End(String name, int payload){
		super(8, name);
	}

	@Override
	public void readTagPayload(DataInput in) throws IOException {
		System.out.println("An error has occoured. An named binary tree tag 'end' has had it's payload read. It doesn't have a payload. Fix your code :D");	
	}

	@Override
	public void writeTag(DataOutput out) throws IOException {
		out.write(this.id);		
	}

	public void writePayload(DataOutput out) throws IOException {}
	
}
