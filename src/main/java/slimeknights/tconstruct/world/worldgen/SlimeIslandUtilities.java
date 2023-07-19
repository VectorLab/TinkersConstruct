package slimeknights.tconstruct.world.worldgen;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.ref.WeakReference;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import gnu.trove.map.hash.TIntObjectHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.world.TinkerWorld;
import slimeknights.tconstruct.world.worldgen.SlimeIslandData.SlimeIslandDataEntry;
import slimeknights.tconstruct.world.worldgen.dim.IDimConfig;
import slimeknights.tconstruct.world.worldgen.dim.RDimConfig;
import slimeknights.tconstruct.world.worldgen.type.RSlimeIslandType;

public final class SlimeIslandUtilities {
	public static final Gson json = new Gson();

	public static SlimeIslandDataEntry isSlimeIslandAt(World world, BlockPos pos) {
		SlimeIslandData v1=getIslandData(world);
		if(null==v1) {
			return null;
		}
		return v1.getIslandByBlockPos(pos);
	}

	public static final SlimeIslandGenerator instance_SlimeIslandGenerator = new SlimeIslandGenerator();

	public static SlimeIslandData getIslandData(World world) {
		int dimensionId = world.provider.getDimension();
		SlimeIslandData data = null;
		{
			WeakReference<SlimeIslandData> i = islandData.get(dimensionId);
			if (null != i) {
				if (!i.isEnqueued()) {
					data = i.get();
					if (null != data) {
						return data;
					}
				}
			}
		}

		IDimConfig dimcfg = RDimConfig.getConfigByDimension(dimensionId);
		if (null == dimcfg) {
			return null;
		}

		data = (SlimeIslandData) world.getPerWorldStorage().getOrLoadData(SlimeIslandData.class,
				SlimeIslandData.islandDataRegName);
		if (data == null) {
			data = new SlimeIslandData(SlimeIslandData.islandDataRegName);
			world.getPerWorldStorage().setData(SlimeIslandData.islandDataRegName, data);
		}
		data.dimConfig = dimcfg;
		islandData.put(dimensionId, new WeakReference<>(data));
		return data;
	}

// Serialize
	// block <-> JSON converter, slow & safe
	public static IBlockState deserializeJsonToBlock(JsonObject json) {
		if (null == json || (!json.has("Name"))) {
			return null;
		}
		String residstr = json.get("Name").getAsString();
		Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(residstr));
		if (null == b) {
			return null;
		}
		if (json.has("Meta")) {
			int meta = json.get("Meta").getAsInt();
			@SuppressWarnings("deprecation")
			IBlockState bs = b.getStateFromMeta(meta);
			return bs;
		} else {
			return b.getDefaultState();
		}
	}

	public static JsonObject serializeBlockToJson(IBlockState block) {
		if (null == block) {
			return null;
		}
		JsonObject root = new JsonObject();
		Block b = block.getBlock();
		root.addProperty("Name", b.getRegistryName().toString());
		root.addProperty("Meta", b.getMetaFromState(block));
		return root;
	}

	public static void postInit() {
		File conf = new File(new File(Loader.instance().getConfigDir(), Util.MODID), "TinkerWorldGen.json");
		try {
			if (conf.isFile()) {
				JsonObject cfgdata = null;
				try {
					FileReader reader=new FileReader(conf);
					cfgdata = json.fromJson(reader, JsonObject.class);
					reader.close();
				} catch (JsonSyntaxException | JsonIOException | IOException e) {
					TinkerWorld.log.error("parse config {} failed", conf.getAbsolutePath());
					e.printStackTrace();
					cfgdata = null;
				}
				if (null != cfgdata) {
					if (fromJson(cfgdata)) {
						return;
					}
					TinkerWorld.log.error("load config {} failed", conf.getAbsolutePath());
				}
				TinkerWorld.log.warn("unable to load config {} , reset to default", conf.getAbsolutePath());
				{
					File confbak = new File(new File(Loader.instance().getConfigDir(), Util.MODID),
							"TinkerWorldGen.json." + Long.toHexString(System.currentTimeMillis()) + ".bak");
					if (confbak.exists()) {
						confbak.delete();
					}
					boolean a = conf.renameTo(confbak);
					if (!a) {
						conf.delete();
					}
				}
			} else {
				if (conf.exists()) {
					conf.delete();
				}
			}
			TinkerWorld.log.warn("loading default config...");
			loadConfigFromDefault();
		} finally {
			TinkerWorld.log.warn("save config...");
			JsonObject cfgdata = toJson();
			try {
				FileWriter writer=new FileWriter(conf);
				json.toJson(cfgdata, writer);
				writer.flush();
				writer.close();
			} catch (JsonIOException | IOException e) {
				TinkerWorld.log.warn("save failed");
				e.printStackTrace();
			}
		}
	}
	/*
	 * ================================================================ fake public
	 * from private private:
	 */
	public static void loadConfigFromDefault() {
		RSlimeIslandType.loadDefault();
		RDimConfig.loadDefault();
	}

	public static boolean fromJson(JsonObject o) {
		boolean r = true;
		if (o.has("islands")) {
			r &= RSlimeIslandType.fromJson(o.get("islands").getAsJsonArray());
		}
		if (o.has("dimensions")) {
			r &= RDimConfig.fromJson(o.get("dimensions").getAsJsonArray());
		}
		return r;
	}

	public static JsonObject toJson() {
		JsonObject o = new JsonObject();
		o.add("islands", RSlimeIslandType.toJson());
		o.add("dimensions", RDimConfig.toJson());
		return o;
	}

	public static TIntObjectHashMap<WeakReference<SlimeIslandData>> islandData = new TIntObjectHashMap<>();
}
