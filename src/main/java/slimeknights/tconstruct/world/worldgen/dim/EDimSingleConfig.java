package slimeknights.tconstruct.world.worldgen.dim;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import slimeknights.tconstruct.world.worldgen.type.ISlimeIslandType;
import slimeknights.tconstruct.world.worldgen.type.RSlimeIslandType;

public class EDimSingleConfig implements IDimConfig {

	public String typeName;
	public ISlimeIslandType instance;
	
	public int[] appliedDimension;
	public int baseRateBound;
	
	@Override
	public int getBaseRateBound() {
		return this.baseRateBound;
	}

	@Override
	public ISlimeIslandType selectIslandForSpawn(Random i) {
		if(null==this.instance) {
			this.instance=RSlimeIslandType.getByName(this.typeName);
		}
		return this.instance;
	}

	@Override
	public int[] getAppliedDimension() {
		return this.appliedDimension;
	}

	@Override
	public JsonObject toJson() {
		JsonObject root=new JsonObject();
		if(null==this.instance) {
			this.instance=RSlimeIslandType.getByName(this.typeName);
		}
		if(null==this.instance) {
			root.addProperty("name",this.typeName);
		}else {
			root.addProperty("name",this.instance.getName());
		}
		root.addProperty("baseRateBound",this.baseRateBound);
		{
			JsonArray o=new JsonArray();
			for(int i:this.appliedDimension) {
				o.add(i);
			}
			root.add("appliedDimension",o);
		}
		return root;
	}

	@Override
	public boolean fromJson(JsonObject o) {
		this.baseRateBound=o.get("baseRateBound").getAsInt();
		this.typeName=o.get("name").getAsString();
		{
			JsonArray o1=o.get("appliedDimension").getAsJsonArray();
			Set<Integer> i1=new HashSet<Integer>();
			for(JsonElement o2:o1) {
				i1.add(o2.getAsInt());
			}
			int[] i2=new int[i1.size()];
			int i3=0;
			for(Integer i4:i1) {
				if(null==i4) {
					continue;
				}
				i2[i3]=i4.intValue();
				++i3;
			}
			this.appliedDimension=i2;
		}
		return true;
	}

}
