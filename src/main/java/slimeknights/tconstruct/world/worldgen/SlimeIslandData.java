package slimeknights.tconstruct.world.worldgen;

import java.util.Map;

import javax.annotation.Nonnull;

import io.netty.util.collection.IntObjectHashMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.INBTSerializable;
import slimeknights.tconstruct.world.worldgen.dim.IDimConfig;
import slimeknights.tconstruct.world.worldgen.type.ISlimeIslandType;
import slimeknights.tconstruct.world.worldgen.type.RSlimeIslandType;

public class SlimeIslandData extends WorldSavedData{
	public IDimConfig dimConfig;
	public static final String islandDataRegName="SlimeIslands";
	  public SlimeIslandData(String name) {
		    super(name);
		  }
	
	public static enum SlimeIslandGenStatus{
		SCHEDULED,
		COMPLETED,
		CANCELLED;
	}
	public static class SlimeIslandDataEntry implements INBTSerializable<NBTTagCompound>{
		public SlimeIslandDataEntry(SlimeIslandData owner) {
			this.owner = owner;
		}

		public StructureBoundingBox range=null;
		
		public int chunkX;
		public int chunkZ;
		public ChunkPos chunkInstance;
		
		public String islandType;
		public ISlimeIslandType islandTypeInstance;
		
		public SlimeIslandGenStatus status=SlimeIslandGenStatus.SCHEDULED;
		public long seed=0;
		
		public final SlimeIslandData owner;

		public void setChunkPos(int x,int z) {
			this.chunkX=x;
			this.chunkZ=z;
			this.chunkInstance=null;
		}

		public ChunkPos getChunkPos() {
			if(null==this.chunkInstance) {
				this.chunkInstance=new ChunkPos(this.chunkX,this.chunkZ);
			}
			return this.chunkInstance;
		}
		
		public void setIslandType(ISlimeIslandType i) {
			this.islandType=i.getName();
			this.islandTypeInstance=i;
		}

		public void setIslandType(String i) {
			this.islandType=i;
			this.islandTypeInstance=null;
		}
		
		public ISlimeIslandType getIslandType() {
			if(null==this.islandTypeInstance) {
				this.islandTypeInstance=RSlimeIslandType.getByName(this.islandType);
			}
			return this.islandTypeInstance;
		}

		@Override
		public NBTTagCompound serializeNBT() {
			NBTTagCompound tag=new NBTTagCompound();
			tag.setInteger("chunkX", this.chunkX);
			tag.setInteger("chunkZ", this.chunkZ);
			tag.setLong("seed", this.seed);
			if (null!=this.islandType) {
				if(null==this.islandTypeInstance) {
					this.islandTypeInstance=RSlimeIslandType.getByName(this.islandType);
				}
				if(null==this.islandTypeInstance) {
					tag.setString("islandType", this.islandType);
				}else{
					tag.setString("islandType", this.islandTypeInstance.getName());
				}
			}
			tag.setString("status",this.status.name());
			if(null!=this.range) {
				tag.setTag("range",this.range.toNBTTagIntArray());
			}
			return tag;
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			{
				int x=nbt.getInteger("chunkX");
				int z=nbt.getInteger("chunkZ");
				this.setChunkPos(x, z);
			}
			this.seed=nbt.getLong("seed");
			if(nbt.hasKey("islandType")) {
				this.setIslandType(nbt.getString("islandType"));
			}
			this.status=SlimeIslandGenStatus.valueOf(nbt.getString("status"));
			if(nbt.hasKey("range")) {
				this.range=new StructureBoundingBox(nbt.getIntArray("range"));
			}
		}
	};

	// entries
	private final Map<Integer, SlimeIslandDataEntry> entries = new IntObjectHashMap<>();

	  public static int hashChunkPos(int x,int z) {
		    int i = 1664525 * x + 1013904223;
		    int j = 1664525 * (z ^ 0xDEADBEEF) + 1013904223;
		    return i ^ j;
		  }
  
  public SlimeIslandDataEntry getIslandByBlockPos(BlockPos pos) {
	  int cxi=pos.getX()>>4;
	  int czi=pos.getZ()>>4;
	  int cp;
	  SlimeIslandDataEntry ce;
	  
	  cp=hashChunkPos(cxi,czi);
	  ce=this.entries.get(cp);
	  if(null!=ce&&null!=ce.range&&ce.range.isVecInside(pos)){
		  return ce;
	  }
	  
	  cp=hashChunkPos(cxi+1,czi);
	  ce=this.entries.get(cp);
	  if(null!=ce&&null!=ce.range&&ce.range.isVecInside(pos)){
		  return ce;
	  }
	  
	  cp=hashChunkPos(cxi-1,czi);
	  ce=this.entries.get(cp);
	  if(null!=ce&&null!=ce.range&&ce.range.isVecInside(pos)){
		  return ce;
	  }
	  
	  cp=hashChunkPos(cxi,czi+1);
	  ce=this.entries.get(cp);
	  if(null!=ce&&null!=ce.range&&ce.range.isVecInside(pos)){
		  return ce;
	  }
	  
	  cp=hashChunkPos(cxi,czi-1);
	  ce=this.entries.get(cp);
	  if(null!=ce&&null!=ce.range&&ce.range.isVecInside(pos)){
		  return ce;
	  }
	  
	  cp=hashChunkPos(cxi+1,czi-1);
	  ce=this.entries.get(cp);
	  if(null!=ce&&null!=ce.range&&ce.range.isVecInside(pos)){
		  return ce;
	  }
	  
	  cp=hashChunkPos(cxi-1,czi+1);
	  ce=this.entries.get(cp);
	  if(null!=ce&&null!=ce.range&&ce.range.isVecInside(pos)){
		  return ce;
	  }
	  
	  cp=hashChunkPos(cxi+1,czi+1);
	  ce=this.entries.get(cp);
	  if(null!=ce&&null!=ce.range&&ce.range.isVecInside(pos)){
		  return ce;
	  }
	  
	  cp=hashChunkPos(cxi-1,czi-1);
	  ce=this.entries.get(cp);
	  if(null!=ce&&null!=ce.range&&ce.range.isVecInside(pos)){
		  return ce;
	  }
	  
	return null;
  }

  public SlimeIslandDataEntry createOrGetEntry(int chunkX, int chunkZ) {
	  int v1=hashChunkPos(chunkX,chunkZ);
	  SlimeIslandDataEntry v2=entries.get(v1);
	  if(null!=v2) {
		  return v2;
	  }
	  v2=new SlimeIslandDataEntry(this);
	  v2.setChunkPos(chunkX,chunkZ);
	  entries.put(hashChunkPos(v2.chunkX,v2.chunkZ), v2);
	  this.markDirty();
	  v2.status=SlimeIslandGenStatus.SCHEDULED;
	  return v2;
  }
  
  public SlimeIslandDataEntry getEntry(int chunkX, int chunkZ) {
		  return entries.get(hashChunkPos(chunkX,chunkZ));
  }
  
  @Override
  public void readFromNBT(@Nonnull NBTTagCompound nbt) {
	  entries.clear();
    NBTTagList tagList = nbt.getTagList("slimeislands", 10);
    for(int i = 0; i < tagList.tagCount(); i++) {
    	SlimeIslandDataEntry e=new SlimeIslandDataEntry(this);
    	e.deserializeNBT(tagList.getCompoundTagAt(i));
    	entries.put(hashChunkPos(e.chunkX,e.chunkZ),e);
    }
  }

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound nbt) {
    NBTTagList tagList = new NBTTagList();
   for(SlimeIslandDataEntry o:this.entries.values()){
      tagList.appendTag(o.serializeNBT());
    }
    nbt.setTag("slimeislands", tagList);
    return nbt;
  }
/* ================================================================
  private final List<StructureBoundingBox> islands = Lists.newArrayList();
  // I honestly don't know if we need a concurrent hashset, but can't be too sure for compatibility
//  private final Map<ChunkPos, Long> chunksToGenerate = new ConcurrentHashMap<>();

  public void markChunkForGeneration(int chunkX, int chunkZ, long seed) {
    chunksToGenerate.put(new ChunkPos(chunkX, chunkZ), seed);
  }

  public Optional<Long> getSeedForChunkToGenerate(int chunkX, int chunkZ) {
    return Optional.ofNullable(chunksToGenerate.get(new ChunkPos(chunkX, chunkZ)));
  }

  public boolean markChunkAsGenerated(int chunkX, int chunkZ) {
    return chunksToGenerate.remove(new ChunkPos(chunkX, chunkZ)) != null;
  }

  public List<StructureBoundingBox> getIslands() {
    return islands;
  }

  @Override
  public void readFromNBT(@Nonnull NBTTagCompound nbt) {
    islands.clear();

    NBTTagList tagList = nbt.getTagList("slimeislands", 11);
    for(int i = 0; i < tagList.tagCount(); i++) {
      islands.add(new StructureBoundingBox(tagList.getIntArrayAt(i)));
    }
  }

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound nbt) {
    NBTTagList tagList = new NBTTagList();
    for(StructureBoundingBox sbb : islands) {
      tagList.appendTag(sbb.toNBTTagIntArray());
    }

    nbt.setTag("slimeislands", tagList);

    return nbt;
  }
*/
}
