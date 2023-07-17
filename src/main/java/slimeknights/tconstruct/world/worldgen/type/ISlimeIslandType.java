package slimeknights.tconstruct.world.worldgen.type;

import java.util.List;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import slimeknights.tconstruct.world.worldgen.JsonSerializable;
import slimeknights.tconstruct.world.worldgen.SlimeLakeGenerator;
import slimeknights.tconstruct.world.worldgen.SlimePlantGenerator;
import slimeknights.tconstruct.world.worldgen.SlimeTreeGenerator;

public interface ISlimeIslandType extends JsonSerializable {
	// Slime Spawn Event
	void patchSpawnEntityForEvent(List<SpawnListEntry> ls);

	// -1 -> do not spawn, 0~255 valid
	int locateFindBaseY(World world, Random rand, int x, int z);

	String getName();

	IBlockState getDirtState();
	IBlockState getGrassState();
	IBlockState getVine();
	SlimeLakeGenerator getLakeGen();
	SlimeTreeGenerator getTreeGen();
	SlimePlantGenerator getPlantGen();
}
