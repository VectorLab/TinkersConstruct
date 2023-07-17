package slimeknights.tconstruct.world;

import net.minecraft.entity.EnumCreatureType;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.world.worldgen.SlimeIslandUtilities;
import slimeknights.tconstruct.world.worldgen.SlimeIslandData.SlimeIslandDataEntry;
import slimeknights.tconstruct.world.worldgen.type.ISlimeIslandType;

public class WorldEvents {
/*
  // Custom slime spawning on slime islands
  Biome.SpawnListEntry magmaSlimeSpawn = new Biome.SpawnListEntry(EntityMagmaCube.class, 150, 4, 6);
  Biome.SpawnListEntry blueSlimeSpawn = new Biome.SpawnListEntry(EntityBlueSlime.class, 15, 2, 4);
*/
  @SubscribeEvent
  public void extraSlimeSpawn(WorldEvent.PotentialSpawns event) {
    if(event.getType() != EnumCreatureType.MONSTER) {
    return;	
    }
    SlimeIslandDataEntry v1=SlimeIslandUtilities.isSlimeIslandAt(event.getWorld(), event.getPos().down(3));
    if(null==v1) {
    	return;
    }
    ISlimeIslandType v2=v1.getIslandType();
    if(null==v2) {
    	return;
    }
    v2.patchSpawnEntityForEvent(event.getList());
/*
      // inside a magma slime island?
      if(MagmaSlimeIslandGenerator.INSTANCE.isSlimeIslandAt(event.getWorld(), event.getPos().down(3))) {
        // spawn magma slime, pig zombies have weight 100
        event.getList().clear();
        event.getList().add(magmaSlimeSpawn);
      }
      // inside a slime island?
      if(SlimeIslandGenerator.INSTANCE.isSlimeIslandAt(event.getWorld(), event.getPos().down(3))) {
        // spawn blue slime, most regular mobs have weight 10
        event.getList().clear();
        event.getList().add(blueSlimeSpawn);
      }
*/
  }
}
