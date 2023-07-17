package slimeknights.tconstruct.world.worldgen;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

import com.google.gson.JsonObject;

import slimeknights.tconstruct.world.TinkerWorld;
import slimeknights.tconstruct.world.block.BlockTallSlimeGrass;
import slimeknights.tconstruct.world.block.BlockSlimeGrass.FoliageType;

public class SlimePlantGenerator implements IWorldGenerator,JsonSerializable {

  private FoliageType foliageType;
  private boolean clumped;

  public SlimePlantGenerator(FoliageType foliageType, boolean clumped) {
    this.foliageType = foliageType;
    this.clumped = clumped;
  }

  public SlimePlantGenerator() {}

// this function hidden for common generator
  @Override
  public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {}

  public void generatePlants(Random random, World world, BlockPos from, BlockPos to, int attempts) {
    int xd = to.getX() - from.getX();
    int yd = to.getY() - from.getY();
    int zd = to.getZ() - from.getZ();

    IBlockState state = TinkerWorld.slimeGrassTall.getDefaultState().withProperty(BlockTallSlimeGrass.FOLIAGE, foliageType);

    for(int i = 0; i < attempts; i++) {
      BlockPos pos = from.add(random.nextInt(xd), 0, random.nextInt(zd));
      if(clumped) {
        pos = pos.add(-random.nextInt(xd), 0, -random.nextInt(zd));
      }

      for(int j = 0; j < yd && world.isAirBlock(pos.down()); j++) {
        pos = pos.down();
      }

      state = state.cycleProperty(BlockTallSlimeGrass.TYPE);

      // suitable position?
      if(world.isAirBlock(pos) && TinkerWorld.slimeGrassTall.canBlockStay(world, pos, state)) {
        world.setBlockState(pos, state, 2);
      }
    }
  }

@Override
public JsonObject toJson() {
	JsonObject root=new JsonObject();
	root.addProperty("foliageType",this.foliageType.name());
	root.addProperty("clumped",this.clumped);
	return root;
}

@Override
public boolean fromJson(JsonObject o) {
	this.foliageType=FoliageType.valueOf(o.get("foliageType").getAsString());
	this.clumped=o.get("clumped").getAsBoolean();
	return false;
}
}
