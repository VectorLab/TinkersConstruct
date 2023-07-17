package slimeknights.tconstruct.world.worldgen.type;

import java.util.List;
import java.util.Vector;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import slimeknights.tconstruct.shared.TinkerCommons;
import slimeknights.tconstruct.shared.TinkerFluids;
import slimeknights.tconstruct.shared.block.BlockSlime;
import slimeknights.tconstruct.world.TinkerWorld;
import slimeknights.tconstruct.world.block.BlockSlimeDirt;
import slimeknights.tconstruct.world.block.BlockSlimeGrass;
import slimeknights.tconstruct.world.entity.EntityBlueSlime;
import slimeknights.tconstruct.world.worldgen.SlimeLakeGenerator;
import slimeknights.tconstruct.world.worldgen.SlimePlantGenerator;
import slimeknights.tconstruct.world.worldgen.SlimeTreeGenerator;

public class PSlimeIslandType {

	private static void generateIslandInChunkSufecae(int rnr, boolean hell, ESlimeIslandType pf) {
		// member variable
		SlimeLakeGenerator lakeGenGreen = null;
		SlimeLakeGenerator lakeGenBlue = null;
		SlimeLakeGenerator lakeGenPurple = null;

		SlimePlantGenerator plantGenBlue = null;
		SlimePlantGenerator plantGenPurple = null;

		SlimeTreeGenerator treeGenBlue = null;
		SlimeTreeGenerator treeGenPurple = null;
// SlimeIslandGenerator()
		IBlockState slimeGreen = TinkerCommons.blockSlimeCongealed.getDefaultState().withProperty(BlockSlime.TYPE,
				BlockSlime.SlimeType.GREEN);
		IBlockState slimeBlue = TinkerCommons.blockSlimeCongealed.getDefaultState().withProperty(BlockSlime.TYPE,
				BlockSlime.SlimeType.BLUE);
		IBlockState slimePurple = TinkerCommons.blockSlimeCongealed.getDefaultState().withProperty(BlockSlime.TYPE,
				BlockSlime.SlimeType.PURPLE);

		IBlockState leaves = TinkerWorld.slimeLeaves.getDefaultState();

		IBlockState slimeFLuidBlue = Blocks.WATER.getDefaultState();
		IBlockState slimeFLuidPurple = Blocks.WATER.getDefaultState();
		if (TinkerFluids.blueslime != null) {
			slimeFLuidBlue = TinkerFluids.blueslime.getBlock().getDefaultState();
			slimeFLuidPurple = slimeFLuidBlue; // just in case, will never be used with how the mod is set up
		}
		if (TinkerFluids.purpleSlime != null) {
			slimeFLuidPurple = TinkerFluids.purpleSlime.getBlock().getDefaultState();
		}

		lakeGenGreen = new SlimeLakeGenerator(slimeFLuidBlue, slimeGreen, slimeGreen, slimeBlue);
		lakeGenBlue = new SlimeLakeGenerator(slimeFLuidBlue, slimeBlue, slimeGreen, slimeBlue);
		lakeGenPurple = new SlimeLakeGenerator(slimeFLuidPurple, slimePurple, slimePurple);

		treeGenBlue = new SlimeTreeGenerator(5, 4, slimeGreen,
				leaves.withProperty(BlockSlimeGrass.FOLIAGE, BlockSlimeGrass.FoliageType.BLUE),
				TinkerWorld.slimeVineBlue2.getDefaultState());
		treeGenPurple = new SlimeTreeGenerator(5, 4, slimeGreen,
				leaves.withProperty(BlockSlimeGrass.FOLIAGE, BlockSlimeGrass.FoliageType.PURPLE),
				TinkerWorld.slimeVinePurple2.getDefaultState());

		plantGenBlue = new SlimePlantGenerator(BlockSlimeGrass.FoliageType.BLUE, false);
		plantGenPurple = new SlimePlantGenerator(BlockSlimeGrass.FoliageType.PURPLE, false);

		// MagmaSlimeIslandGenerator()
		SlimeLakeGenerator lakeGenMagma;
		SlimePlantGenerator plantGenMagma;
		SlimeTreeGenerator treeGenMagma;
		IBlockState dirtMagma;
		IBlockState grassMagma;
		if (hell) {
			IBlockState slimeMagma = TinkerCommons.blockSlimeCongealed.getDefaultState().withProperty(BlockSlime.TYPE,
					BlockSlime.SlimeType.MAGMA);
			IBlockState slimeBlood = TinkerCommons.blockSlimeCongealed.getDefaultState().withProperty(BlockSlime.TYPE,
					BlockSlime.SlimeType.BLOOD);

			dirtMagma = TinkerWorld.slimeDirt.getDefaultState().withProperty(BlockSlimeDirt.TYPE,
					BlockSlimeDirt.DirtType.MAGMA);
			grassMagma = TinkerWorld.slimeGrass.getStateFromDirt(dirtMagma).withProperty(BlockSlimeGrass.FOLIAGE,
					BlockSlimeGrass.FoliageType.ORANGE);

			lakeGenMagma = new SlimeLakeGenerator(Blocks.LAVA.getDefaultState(), slimeMagma, slimeMagma, slimeMagma,
					slimeMagma, slimeMagma, slimeBlood);
			treeGenMagma = new SlimeTreeGenerator(5, 4, slimeMagma, TinkerWorld.slimeLeaves.getDefaultState()
					.withProperty(BlockSlimeGrass.FOLIAGE, BlockSlimeGrass.FoliageType.ORANGE), null);
			plantGenMagma = new SlimePlantGenerator(BlockSlimeGrass.FoliageType.ORANGE, false);

			// generateIslandInChunk(long seed, World world, int chunkX, int chunkZ)

			postConfigFromBuilder(pf, dirtMagma, grassMagma, null, lakeGenMagma, treeGenMagma, plantGenMagma);
			return;
		} else {
			// determine parameters of the slime island!
			// default is a blue island
			// 6 7 8 9
			BlockSlimeGrass.FoliageType grass = BlockSlimeGrass.FoliageType.BLUE;
			BlockSlimeDirt.DirtType dirt = BlockSlimeDirt.DirtType.BLUE;
			SlimeLakeGenerator lakeGen = lakeGenBlue;
			SlimePlantGenerator plantGen = plantGenPurple;
			SlimeTreeGenerator treeGen = treeGenPurple; // purple trees on blue/green islands
			IBlockState vine = TinkerWorld.slimeVineBlue1.getDefaultState();

			// int rnr = random.nextInt(10);
			// purple island.. rare!
			if (rnr <= 1) {// 0 1
				grass = BlockSlimeGrass.FoliageType.PURPLE;
				dirt = BlockSlimeDirt.DirtType.PURPLE;
				lakeGen = lakeGenPurple;
				treeGen = treeGenBlue; // blue trees on purple grass. yay
				plantGen = plantGenBlue;
				vine = TinkerWorld.slimeVinePurple1.getDefaultState();
			}
			// green island.. not so rare
			else if (rnr < 6) {// 2 3 4 5
				dirt = BlockSlimeDirt.DirtType.GREEN;
				lakeGen = lakeGenGreen;
			}

			IBlockState dirtState = TinkerWorld.slimeDirt.getDefaultState().withProperty(BlockSlimeDirt.TYPE, dirt);
			IBlockState grassState = TinkerWorld.slimeGrass.getStateFromDirt(dirtState)
					.withProperty(BlockSlimeGrass.FOLIAGE, grass);

			postConfigFromBuilder(pf, dirtState, grassState, vine, lakeGen, treeGen, plantGen);
			return;
		}

	}

	private static void postConfigFromBuilder(ESlimeIslandType a, IBlockState dirtState, IBlockState grassState,
			IBlockState vine, SlimeLakeGenerator lakeGen, SlimeTreeGenerator treeGen, SlimePlantGenerator plantGen) {
		a.dirtState = dirtState;
		a.grassState = grassState;
		a.vine = vine;
		a.lakeGen = lakeGen;
		a.treeGen = treeGen;
		a.plantGen = plantGen;
	}
	
	private static class MatcherBuilder{
		
		Block block=null;
		IBlockState blockstate=null;
		int meta=-2;
		
		public MatcherBuilder b(Block b) {
			this.block=b;
			return this;
		}
		
		public MatcherBuilder bs(IBlockState bs) {
			this.blockstate=bs;
			return this;
		}
		
		public MatcherBuilder m(int m) {
			this.meta=m;
			return this;
		}
		
		public ESlimeIslandType.BlockMatcher build(){
			ESlimeIslandType.BlockMatcher o=new ESlimeIslandType.BlockMatcher();
			if(null!=this.blockstate) {
				o.block=this.blockstate.getBlock();
				o.meta=this.blockstate.getBlock().getMetaFromState(this.blockstate);
			}
			if(null!=this.block) {
				o.block=this.block;
			}
			if(-2!=this.meta) {
				o.meta=this.meta;
			}
			return o;
		}
		
	}

	private static void makeDefaultSurface(ESlimeIslandType o) {
		o.locate_blockDown=new MatcherBuilder().build();
		o.locate_blockMiddle=new MatcherBuilder().build();
		o.locate_blockUp=new MatcherBuilder().build();
		o.locate_args=new int[] {50+11,50};
		o.locate_type=1;
		o.spawnEntry = new SpawnListEntry(EntityBlueSlime.class, 15, 2, 4);
	}
	
	public static List<ESlimeIslandType> generateAll() {
		List<ESlimeIslandType> o = new Vector<>();

		{
			ESlimeIslandType a = new ESlimeIslandType();
			a.spawnEntry = new SpawnListEntry(EntityMagmaCube.class, 150, 4, 6);
			a.locate_blockDown=new MatcherBuilder().build();
			a.locate_blockMiddle=new MatcherBuilder().b(Blocks.LAVA).build();
			a.locate_blockUp=new MatcherBuilder().b(Blocks.AIR).build();
			a.locate_args=new int[] {31,1};
			a.locate_type=2;
			a.name = "magma";
			generateIslandInChunkSufecae(0, true, a);
			o.add(a);
		}

		{
			ESlimeIslandType a = new ESlimeIslandType();
			makeDefaultSurface(a);
			a.name = "purple";
			generateIslandInChunkSufecae(0, false, a);
			o.add(a);
		}

		{
			ESlimeIslandType a = new ESlimeIslandType();
			makeDefaultSurface(a);
			a.name = "green";
			generateIslandInChunkSufecae(4, false, a);
			o.add(a);
		}

		{
			ESlimeIslandType a = new ESlimeIslandType();
			makeDefaultSurface(a);
			a.name = "blue";
			generateIslandInChunkSufecae(8, false, a);
			o.add(a);
		}

		return o;
	}

}
