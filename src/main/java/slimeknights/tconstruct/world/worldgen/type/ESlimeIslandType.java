package slimeknights.tconstruct.world.worldgen.type;

import java.util.List;
import java.util.Random;
import java.util.Vector;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import slimeknights.tconstruct.world.TinkerWorld;
import slimeknights.tconstruct.world.worldgen.JsonSerializable;
import slimeknights.tconstruct.world.worldgen.SlimeIslandUtilities;
import slimeknights.tconstruct.world.worldgen.SlimeLakeGenerator;
import slimeknights.tconstruct.world.worldgen.SlimePlantGenerator;
import slimeknights.tconstruct.world.worldgen.SlimeTreeGenerator;

public class ESlimeIslandType implements ISlimeIslandType {

	public static class BlockMatcher implements JsonSerializable{

		public Block block=null;
		public int meta=-1;

		@Override
		public JsonObject toJson() {
			JsonObject root=new JsonObject();
			if(null!=this.block) {
				root.addProperty("Name",this.block.getRegistryName().toString());
			}
			root.addProperty("Meta",this.meta);
			return root;
		}

		@Override
		public boolean fromJson(JsonObject o) {
			this.block=null;
			this.meta=-1;
			if(null==o) {
				return true;
			}
			
			if(o.has("Name")) {
				String residstr=o.get("Name").getAsString();
				if(null==residstr||"".equals(residstr)) {}else {
					this.block= ForgeRegistries.BLOCKS.getValue(new ResourceLocation(residstr));
				}
			}
			if(o.has("Meta")) {
				this.meta=o.get("Meta").getAsInt();
			}
			return true;
		}
		
		public boolean empty() {
			return null==this.block&&-1==this.meta;
		}
		
		public boolean match(IBlockState bs) {
			if(null==bs) {
				return null==this.block&&-1==this.meta;
			}
			if(null!=this.block) {
				if(this.block!=bs.getBlock()) {
					return false;
				}
			}
			if(-1!=this.meta) {
				if(this.meta!=bs.getBlock().getMetaFromState(bs)) {
					return false;
				}
			}
			return true;
		}
		
	}
	/*
	 *  0: not set
	 *  1: relative from ground
	 *  2: fixed from 0
	 */
	public int locate_type=0;
	public int[] locate_args=null;

	public BlockMatcher locate_blockUp=new BlockMatcher();
	public BlockMatcher locate_blockMiddle=new BlockMatcher();
	public BlockMatcher locate_blockDown=new BlockMatcher();

	public String name;

	public SpawnListEntry spawnEntry = null;

	public IBlockState dirtState = null;

	public IBlockState grassState = null;

	public IBlockState vine = null;

	public SlimeLakeGenerator lakeGen = null;

	public SlimeTreeGenerator treeGen = null;

	public SlimePlantGenerator plantGen = null;

	@Override
	public void patchSpawnEntityForEvent(List<SpawnListEntry> ls) {
		if (null != spawnEntry) {
			ls.clear();
			ls.add(spawnEntry);
		}
	}

	public boolean fromJson(JsonObject json) {
		this.name = json.getAsJsonPrimitive("name").getAsString();
		if(json.has("spawnEntry")&&json.get("spawnEntry").isJsonObject()){
			JsonObject o = json.getAsJsonObject("spawnEntry");
			Class<? extends EntityLiving> o1 = null;
			try {
				Class<?> o2 = null;
				o2 = Class.forName(o.getAsJsonPrimitive("entityClass").getAsString());
				if (EntityLiving.class.isAssignableFrom(o2)) {
					o1 = (Class<? extends EntityLiving>) o2;
				}
			} catch (ClassCastException | ClassNotFoundException e) {
				return false;
			}
			if (null == o1) {
				return false;
			}
			spawnEntry = new SpawnListEntry(o1, o.getAsJsonPrimitive("itemWeight").getAsInt(),
					o.getAsJsonPrimitive("minGroupCount").getAsInt(), o.getAsJsonPrimitive("maxGroupCount").getAsInt());
		}

		if (json.has("dirtState")) {
			this.dirtState = SlimeIslandUtilities.deserializeJsonToBlock(json.get("dirtState").getAsJsonObject());
		}
		if (json.has("grassState")) {
			this.grassState = SlimeIslandUtilities.deserializeJsonToBlock(json.get("grassState").getAsJsonObject());
		}
		if (json.has("vine")) {
			this.vine = SlimeIslandUtilities.deserializeJsonToBlock(json.get("vine").getAsJsonObject());
		}
		if (json.has("lakeGen")) {
			this.lakeGen = new SlimeLakeGenerator();
			this.lakeGen.fromJson(json.get("lakeGen").getAsJsonObject());
		} else {
			this.lakeGen = null;
		}
		if (json.has("treeGen")) {
			this.treeGen = new SlimeTreeGenerator();
			this.treeGen.fromJson(json.get("treeGen").getAsJsonObject());
		} else {
			this.treeGen = null;
		}
		if (json.has("plantGen")) {
			this.plantGen = new SlimePlantGenerator();
			this.plantGen.fromJson(json.get("plantGen").getAsJsonObject());
		} else {
			this.plantGen = null;
		}
		
		if (json.has("locate")){
			JsonObject o = json.getAsJsonObject("locate");
			this.locate_type=o.get("type").getAsInt();
			this.locate_blockDown.fromJson(o.get("blockDown").getAsJsonObject());
			this.locate_blockMiddle.fromJson(o.get("blockMiddle").getAsJsonObject());
			this.locate_blockUp.fromJson(o.get("blockUp").getAsJsonObject());
			this.locate_args=null;
			if (o.has("args")){
				JsonArray o1=o.get("args").getAsJsonArray();
				List<Integer> i1=new Vector<>();
				for(JsonElement o2:o1) {
					int o3=o2.getAsInt();
					i1.add(o3);
				}
				this.locate_args=new int[i1.size()];
				for(int j=0;j<i1.size();++j) {
					this.locate_args[j]=i1.get(j);
				}
			}
		}

		return true;
	}

	public JsonObject toJson() {
		JsonObject root = new JsonObject();
		root.addProperty("name", name);
		if(null!=this.spawnEntry){
			JsonObject o = new JsonObject();
			o.addProperty("entityClass", this.spawnEntry.entityClass.getTypeName());
			o.addProperty("itemWeight", this.spawnEntry.itemWeight);
			o.addProperty("minGroupCount", this.spawnEntry.minGroupCount);
			o.addProperty("maxGroupCount", this.spawnEntry.maxGroupCount);
			root.add("spawnEntry", o);
		}

		if (null != this.dirtState) {
			root.add("dirtState", SlimeIslandUtilities.serializeBlockToJson(this.dirtState));
		}
		if (null != this.grassState) {
			root.add("grassState", SlimeIslandUtilities.serializeBlockToJson(this.grassState));
		}
		if (null != this.vine) {
			root.add("vine", SlimeIslandUtilities.serializeBlockToJson(this.vine));
		}
		if (null != this.lakeGen) {
			root.add("lakeGen", this.lakeGen.toJson());
		}
		if (null != this.treeGen) {
			root.add("treeGen", this.treeGen.toJson());
		}
		if (null != this.plantGen) {
			root.add("plantGen", this.plantGen.toJson());
		}
		
		{
			JsonObject o = new JsonObject();
			o.addProperty("type",this.locate_type);
			o.add("blockDown",this.locate_blockDown.toJson());
			o.add("blockMiddle",this.locate_blockMiddle.toJson());
			o.add("blockUp",this.locate_blockUp.toJson());
			if(null!=this.locate_args){
				JsonArray o1=new JsonArray();
				for(int i1:this.locate_args) {
					o1.add(i1);
				}
				o.add("args", o1);
			}
			root.add("locate", o);
		}
		return root;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int locateFindBaseY(World world, Random rand, int x, int z) {
		switch(this.locate_type) {
		case 1:
		{
			int yG=world.getHeight(new BlockPos(x, 0, z)).getY();
			int yS=255-yG-this.locate_args[0];
			if(yS<0) {
				// no enough space
				return -1;
			}
			int yR1=Math.min(yS,this.locate_args[1]);
			int yR2=0;
			if(yR1>0) {
				yR2=rand.nextInt(yR1);
			}
			int yF=yG+this.locate_args[0]+yR2;
			if(yF>254) {
				yF=255;
			}
			if(yF<1) {
				yF=0;
			}
			if(yF!=255&&(!this.locate_blockUp.empty())) {
				if(!this.locate_blockUp.match(world.getBlockState(new BlockPos(x,yF+1,z)))) {
					return -1;
				}
			}
			if(yF!=0&&(!this.locate_blockDown.empty())) {
				if(!this.locate_blockDown.match(world.getBlockState(new BlockPos(x,yF-1,z)))) {
					return -1;
				}
			}
			if(!this.locate_blockMiddle.empty()) {
				if(!this.locate_blockMiddle.match(world.getBlockState(new BlockPos(x+1,yF,z)))) {
					return -1;
				}
				if(!this.locate_blockMiddle.match(world.getBlockState(new BlockPos(x-1,yF,z)))) {
					return -1;
				}
				if(!this.locate_blockMiddle.match(world.getBlockState(new BlockPos(x,yF,z+1)))) {
					return -1;
				}
				if(!this.locate_blockMiddle.match(world.getBlockState(new BlockPos(x,yF,z-1)))) {
					return -1;
				}
			}
			return yF;
		}
		case 2:
		{
			int yF=this.locate_args[0];
			if(yF!=255&&(!this.locate_blockUp.empty())) {
				if(!this.locate_blockUp.match(world.getBlockState(new BlockPos(x,yF+1,z)))) {
					return -1;
				}
			}
			if(yF!=0&&(!this.locate_blockDown.empty())) {
				if(!this.locate_blockDown.match(world.getBlockState(new BlockPos(x,yF-1,z)))) {
					return -1;
				}
			}
			if(!this.locate_blockMiddle.empty()) {
				if(!this.locate_blockMiddle.match(world.getBlockState(new BlockPos(x+1,yF,z)))) {
					return -1;
				}
				if(!this.locate_blockMiddle.match(world.getBlockState(new BlockPos(x-1,yF,z)))) {
					return -1;
				}
				if(!this.locate_blockMiddle.match(world.getBlockState(new BlockPos(x,yF,z+1)))) {
					return -1;
				}
				if(!this.locate_blockMiddle.match(world.getBlockState(new BlockPos(x,yF,z-1)))) {
					return -1;
				}
			}
			return yF+this.locate_args[1];
		}
		
		case 0:
		default:
		{
			TinkerWorld.log.error("invalid location type for IslandType {} value: {}",this.name,this.locate_type);
			return -1;
		}
		
		}
	}

	@Override
	public IBlockState getDirtState() {
		return this.dirtState;
	}

	@Override
	public IBlockState getGrassState() {
		return this.grassState;
	}

	@Override
	public IBlockState getVine() {
		return this.vine;
	}

	@Override
	public SlimeLakeGenerator getLakeGen() {
		return this.lakeGen;
	}

	@Override
	public SlimeTreeGenerator getTreeGen() {
		return this.treeGen;
	}

	@Override
	public SlimePlantGenerator getPlantGen() {
		return this.plantGen;
	}

}
