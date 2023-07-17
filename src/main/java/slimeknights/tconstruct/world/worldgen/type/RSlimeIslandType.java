package slimeknights.tconstruct.world.worldgen.type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class RSlimeIslandType {
	
	public static Map<String,ISlimeIslandType> REGISTRY=new HashMap<>();
	
	public static ISlimeIslandType getByName(String n) {
		if(null==n||n.isEmpty()) {
			return null;
		}
		return REGISTRY.get(n);
	}

	public static JsonArray toJson() {
		JsonArray root=new JsonArray();		
		for(Entry<String, ISlimeIslandType> i1:REGISTRY.entrySet()) {
			ISlimeIslandType i2 = i1.getValue();
			if(!i2.isJson()) {
				continue;
			}
			JsonObject o2=i2.toJson();
			{
				JsonObject o3=new JsonObject();
				o3.addProperty("class",i2.getClass().getName());
				o2.add("__proto__",o3);
			}
			root.add(o2);
		}
		return root;
	}
	
	public static boolean fromJson(JsonArray json) {
		REGISTRY=new HashMap<>();
		
		for(JsonElement i1:json) {
			JsonObject o1=i1.getAsJsonObject();
			ISlimeIslandType i2;
			{
				JsonObject o2=o1.get("__proto__").getAsJsonObject();
				String o3=o2.get("class").getAsString();
				
				try {
					i2=(ISlimeIslandType) Class.forName(o3).newInstance();
				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
					e.printStackTrace();
					return false;
				}
			}
			i2.fromJson(o1);
			REGISTRY.put(i2.getName(), i2);
		}
		return true;
	}
	
	public static void loadDefault(){
		REGISTRY=new HashMap<>();
		List<ESlimeIslandType> v1=PSlimeIslandType.generateAll();
		for(ESlimeIslandType v2:v1) {
			REGISTRY.put(v2.getName(), v2);
		}
	}
}
