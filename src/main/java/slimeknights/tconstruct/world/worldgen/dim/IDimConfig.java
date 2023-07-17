package slimeknights.tconstruct.world.worldgen.dim;

import java.util.Random;

import slimeknights.tconstruct.world.worldgen.JsonSerializable;
import slimeknights.tconstruct.world.worldgen.type.ISlimeIslandType;

public interface IDimConfig extends JsonSerializable{
	
	int getBaseRateBound();
	ISlimeIslandType selectIslandForSpawn(Random rand);
	int[] getAppliedDimension();

}
