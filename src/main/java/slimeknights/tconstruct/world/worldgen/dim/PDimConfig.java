package slimeknights.tconstruct.world.worldgen.dim;

import java.util.List;
import java.util.Vector;

import net.minecraft.world.DimensionType;
import slimeknights.tconstruct.world.worldgen.dim.EDimWeightedConfig.WeightedIslandType;

public class PDimConfig {

	public static List<IDimConfig> generateAll() {
		List<IDimConfig> o=new Vector<>();
		{
			EDimWeightedConfig o1=new EDimWeightedConfig();
			{
				List<WeightedIslandType> o2=new Vector<>();
				{
					WeightedIslandType o3=new WeightedIslandType();
					o3.name="purple";
					o3.weight=1;
					o2.add(o3);
				}
				{
					WeightedIslandType o3=new WeightedIslandType();
					o3.name="green";
					o3.weight=2;
					o2.add(o3);
				}
				{
					WeightedIslandType o3=new WeightedIslandType();
					o3.name="blue";
					o3.weight=2;
					o2.add(o3);
				}
				o1.setWeight(o2);
			}
			o1.appliedDimension=new int[] {DimensionType.OVERWORLD.getId()};
			o1.baseRateBound=730;
			o.add(o1);
		}
		{
			EDimSingleConfig o1=new EDimSingleConfig();
			o1.appliedDimension=new int[] {DimensionType.NETHER.getId()};
			o1.baseRateBound=100;
			o1.typeName="magma";
			o.add(o1);
		}
		return o;
	}
}
