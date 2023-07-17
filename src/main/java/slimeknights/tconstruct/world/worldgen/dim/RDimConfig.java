package slimeknights.tconstruct.world.worldgen.dim;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.netty.util.collection.IntObjectHashMap;

public class RDimConfig {
	public static Map<Integer,IDimConfig> REGISTRY_dim=new IntObjectHashMap<>();
	
	public static Set<IDimConfig> REGISTRY_uniq=new HashSet<>();
	
	public static IDimConfig getConfigByDimension(int id) {
		return REGISTRY_dim.get(id);
	}

	public static void registerReset() {
		REGISTRY_dim=new IntObjectHashMap<>();
		REGISTRY_uniq=new HashSet<>();
	}
	
	public static boolean registerAdd(IDimConfig c) {
		if(REGISTRY_uniq.contains(c)) {
			return false;
		}
		int[] dim=c.getAppliedDimension();
		for(int ii:dim) {
			if(REGISTRY_dim.containsKey(ii)) {
				return false;
			}
		}
		
		REGISTRY_uniq.add(c);
		for(int ii:dim) {
			REGISTRY_dim.put(ii,c);
		}
		return true;
	}
	
	public static boolean fromJson(JsonArray json) {
		registerReset();
		
		for(JsonElement o1:json) {
			JsonObject o2=o1.getAsJsonObject();
			IDimConfig i1=null;
			{
				JsonObject o3=o2.get("__proto__").getAsJsonObject();
				String className=o3.get("class").getAsString();
				
				try {
					i1=(IDimConfig) Class.forName(className).newInstance();
				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
					e.printStackTrace();
					return false;
				}
			}
			i1.fromJson(o2);
			registerAdd(i1);
		}
		return true;
	}
	
	public static JsonArray toJson() {
		JsonArray root=new JsonArray();		
		for(IDimConfig i:REGISTRY_uniq) {
			if(!i.isJson()) {
				continue;
			}
			JsonObject o2=i.toJson();
			{
				JsonObject o3=new JsonObject();
				o3.addProperty("class",i.getClass().getName());
				o2.add("__proto__",o3);
			}
			root.add(o2);
		}
		return root;
	}
	
	public static void loadDefault(){
		registerReset();
		List<IDimConfig> v1=PDimConfig.generateAll();
		for(IDimConfig v2:v1) {
			registerAdd(v2);
		}
	}

}
