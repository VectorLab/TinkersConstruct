package slimeknights.tconstruct.world.worldgen;

import com.google.gson.JsonObject;

public interface JsonSerializable {
	default boolean isJson() {
		return true;
	}
	JsonObject toJson();
	boolean fromJson(JsonObject o);
}
