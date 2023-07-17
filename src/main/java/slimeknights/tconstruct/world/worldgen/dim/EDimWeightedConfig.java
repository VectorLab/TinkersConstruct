package slimeknights.tconstruct.world.worldgen.dim;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import slimeknights.tconstruct.world.worldgen.type.ISlimeIslandType;
import slimeknights.tconstruct.world.worldgen.type.RSlimeIslandType;

public class EDimWeightedConfig implements IDimConfig{
	public int[] appliedDimension;
	
	public WeightedIslandType[] weightList;
	public WeightedIslandType[] weightPage;
	
	public int baseRateBound;

	public static class WeightedIslandType{
		public int weight;
		
		public String name;
		public ISlimeIslandType instance;
		
		public ISlimeIslandType getInstance() {
			if(null==this.instance) {
				this.instance=RSlimeIslandType.getByName(this.name);
			}
			return this.instance;
		}
		
		public boolean fromJson(JsonObject o) {
			this.name=o.get("name").getAsString();
			this.weight=o.get("weight").getAsInt();
			return true;
		}
		
		public JsonObject toJson() {
			JsonObject o=new JsonObject();
			this.getInstance();
			if(null==this.instance) {
				o.addProperty("name",this.name);
			}else {
				o.addProperty("name",this.instance.getName());
			}
			o.addProperty("weight",this.weight);
			return o;
		}
	}

	public void setWeight(List<WeightedIslandType> p1) {
		this.weightList=p1.toArray(new WeightedIslandType[0]);
		{
			int v1=0;
			for(WeightedIslandType v2:this.weightList) {
				v1+=v2.weight;
			}
			this.weightPage=new WeightedIslandType[v1];
			v1=0;
			for(WeightedIslandType v2:this.weightList) {
				Arrays.fill(this.weightPage,v1,v1+v2.weight,v2);
				v1+=v2.weight;
			}
		}
	}

	@Override
	public int getBaseRateBound() {
		return this.baseRateBound;
	}

	@Override
	public ISlimeIslandType selectIslandForSpawn(Random i) {
		int i1=i.nextInt(this.weightPage.length);
		return this.weightPage[i1].getInstance();
	}

	@Override
	public int[] getAppliedDimension() {
		return this.appliedDimension;
	}

	@Override
	public JsonObject toJson() {
		JsonObject root=new JsonObject();
		root.addProperty("baseRateBound",this.baseRateBound);
		{
			JsonArray o=new JsonArray();
			for(int i:this.appliedDimension) {
				o.add(i);
			}
			root.add("appliedDimension",o);
		}
		{
			JsonArray o=new JsonArray();
			for(WeightedIslandType i:this.weightList) {
				o.add(i.toJson());
			}
			root.add("weight",o);
		}
		return root;
	}

	@Override
	public boolean fromJson(JsonObject o) {
		this.baseRateBound=o.get("baseRateBound").getAsInt();
		{
			JsonArray o1=o.get("weight").getAsJsonArray();
			List<WeightedIslandType> i1=new Vector<WeightedIslandType>();
			for(JsonElement o2:o1) {
				WeightedIslandType i2=new WeightedIslandType();
				boolean o3=i2.fromJson(o2.getAsJsonObject());
				if(!o3) {
					return false;
				}
				i1.add(i2);
			}
			this.setWeight(i1);
		}
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
