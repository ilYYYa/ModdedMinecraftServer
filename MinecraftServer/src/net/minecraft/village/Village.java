package net.minecraft.village;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import ru.ilyyya.serverTestModification.ilStructures;

public class Village
{
    private World worldObj;
    private final List<VillageDoorInfo> villageDoorInfoList = Lists.<VillageDoorInfo>newArrayList();

    /**
     * This is the sum of all door coordinates and used to calculate the actual village center by dividing by the number
     * of doors.
     */
    private BlockPos centerHelper = BlockPos.ORIGIN;

    /** This is the actual village center. */
    private BlockPos center = BlockPos.ORIGIN;
    public int villageRadius;
    private int lastAddDoorTimestamp;
    private int tickCounter;
    private int numVillagers;

    /** Timestamp of tick count when villager last bred */
    private int noBreedTicks;
    private final Map<String, Integer> playerReputation = Maps.<String, Integer>newHashMap();
    private final List<Village.VillageAggressor> villageAgressors = Lists.<Village.VillageAggressor>newArrayList();
    private int numIronGolems;

    public Village()
    {
    }

    public Village(World worldIn)
    {
        this.worldObj = worldIn;
    }

    public void setWorld(World worldIn)
    {
        this.worldObj = worldIn;
    }

    public void getHouseByDoorInfo(VillageDoorInfo di)
    {
    	BlockPos pos = di.getDoorBlockPos();
    }
    
    public void checkAllDoors()
    {
    	for(int i = 0; i < villageDoorInfoList.size(); i++)
    	{
    		double[] buff = checkAllStructuresForDoor(villageDoorInfoList.get(i).getDoorBlockPos());
    		MinecraftServer.theServer.sendMessageForAll("Door at " + villageDoorInfoList.get(i).getDoorBlockPos().toString() + " with structure N" + buff[3] + " have " + buff[0] + "% with " + buff[1] + " facing and " + (buff[2] != 0) + " mirror");
    	}
    }
    
    public double[] checkAllStructuresForDoor(BlockPos doorPos)
    {
    	double pc = 0D;
    	double buff = 0D;
    	
    	double fc = 0D;
    	double mr = 0D;
    	double index = 0D;
    	
    	for(int i = 0; i < ilStructures.Houses.length; i++)
    	{
    		for(int j = 0; j < 8; j++)
    		{
        		buff = checkStructureByFacingAndMirroring(ilStructures.Houses[i], findDoorInStructure(ilStructures.Houses[i]), doorPos, j%4, (int) (j/4) != 0);
        		if(buff > pc)
        		{
        			pc = buff;
        			fc = j%4;
        			mr = (int) (j/4);
        			index = i;
        		}
    		}
    	}
    	
    	return new double[] { pc, fc, mr, index };
    }

    public int[] findDoorInStructure(int[][][] structure)
    {
    	for(int iy = 0; iy < structure.length; iy++)
    	{
    		for(int ix = 0; ix < structure[iy].length; ix++)
        	{
    			for(int iz = 0; iz < structure[iy][ix].length; iz++)
            	{
            		if(structure[iy][ix][iz] == 3) return new int[] {ix, iy, iz};
            	}
        	}
    	}
    	return null;
    }
    
    public double checkStructureByFacingAndMirroring(int[][][] structure, int[] doorCoords, BlockPos doorPos, int facing, boolean mirror)
    {
    	int trys = 0;
    	int trues = 0;
    	
		BlockPos pos = null;
		if(facing == 0 && !mirror) pos = doorPos.down(doorCoords[1]).west(doorCoords[0]).north(doorCoords[2]);
		if(facing == 1 && !mirror) pos = doorPos.down(doorCoords[1]).north(doorCoords[0]).east(doorCoords[2]);
		if(facing == 2 && !mirror) pos = doorPos.down(doorCoords[1]).east(doorCoords[0]).south(doorCoords[2]);
		if(facing == 3 && !mirror) pos = doorPos.down(doorCoords[1]).south(doorCoords[0]).west(doorCoords[2]);
		
		if(facing == 0 && mirror) pos = doorPos.down(doorCoords[1]).west(doorCoords[0]).south(doorCoords[2]);
		if(facing == 1 && mirror) pos = doorPos.down(doorCoords[1]).north(doorCoords[0]).west(doorCoords[2]);
		if(facing == 2 && mirror) pos = doorPos.down(doorCoords[1]).east(doorCoords[0]).north(doorCoords[2]);
		if(facing == 3 && mirror) pos = doorPos.down(doorCoords[1]).south(doorCoords[0]).east(doorCoords[2]);
		
    	if(doorCoords == null || pos == null) return 0;
    	
    	for(int iy = 0; iy < structure.length; iy++)
    	{
    		for(int ix = 0; ix < structure[iy].length; ix++)
        	{
    			for(int iz = 0; iz < structure[iy][ix].length; iz++)
            	{
    				trys++;

    				if(facing == 0 && !mirror && checkBlockPosAndBlockId(pos.up(iy).east(ix).south(iz), structure[iy][ix][iz])) trues++;
    				if(facing == 1 && !mirror && checkBlockPosAndBlockId(pos.up(iy).south(ix).west(iz), structure[iy][ix][iz])) trues++;
    				if(facing == 2 && !mirror && checkBlockPosAndBlockId(pos.up(iy).west(ix).north(iz), structure[iy][ix][iz])) trues++;
    				if(facing == 3 && !mirror && checkBlockPosAndBlockId(pos.up(iy).north(ix).east(iz), structure[iy][ix][iz])) trues++;
    				
    				if(facing == 0 && mirror && checkBlockPosAndBlockId(pos.up(iy).east(ix).north(iz), structure[iy][ix][iz])) trues++;
    				if(facing == 1 && mirror && checkBlockPosAndBlockId(pos.up(iy).south(ix).east(iz), structure[iy][ix][iz])) trues++;
    				if(facing == 2 && mirror && checkBlockPosAndBlockId(pos.up(iy).west(ix).south(iz), structure[iy][ix][iz])) trues++;
    				if(facing == 3 && mirror && checkBlockPosAndBlockId(pos.up(iy).north(ix).west(iz), structure[iy][ix][iz])) trues++;
            	}
        	}
    	}
    	
    	return trues/trys;
    }
    
    public boolean checkBlockPosAndBlockId(BlockPos pos, int id)
    {
    	boolean ret = false;
    	if(id == 0) ret = true;
    	if(id == 1 && worldObj.getBlockState(pos).isFullBlock() && worldObj.getBlockState(pos).isFullCube()	 && !worldObj.getBlockState(pos).isOpaqueCube()) ret = true;
    	if(id == 3 && worldObj.getBlockState(pos).getBlock() instanceof BlockDoor) ret = true;
    	if(id == 5 && worldObj.getBlockState(pos).getBlock() instanceof BlockStairs) ret = true;
    	if(id == 6 && worldObj.getBlockState(pos).getBlock() instanceof BlockPane) ret = true;
    	
    	if(ret) System.out.println(pos.toString() + " id{" + id + "}");
    	
    	return ret;
    }
    
    int z = 0;
    /**
     * Called periodically by VillageCollection
     */
    public void tick(int tickCounterIn)
    {
        this.tickCounter = tickCounterIn;
        this.removeDeadAndOutOfRangeDoors();
        this.removeDeadAndOldAgressors();

        if (tickCounterIn % 20 == 0)
        {
            this.updateNumVillagers();
        }

        if (tickCounterIn % 30 == 0)
        {
            this.updateNumIronGolems();
        }

        int i = this.numVillagers / 10;

        if (this.numIronGolems < i && this.villageDoorInfoList.size() > 20 && this.worldObj.rand.nextInt(7000) == 0)
        {
            Vec3d vec3d = this.findRandomSpawnPos(this.center, 2, 4, 2);

            if (vec3d != null)
            {
                EntityIronGolem entityirongolem = new EntityIronGolem(this.worldObj);
                entityirongolem.setPosition(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord);
                this.worldObj.spawnEntityInWorld(entityirongolem);
                ++this.numIronGolems;
            }
        }

        if (tickCounterIn % 120 == 0 && z < 1)
        {
        	MinecraftServer.theServer.sendMessageForAll("Doors in village count: " + villageDoorInfoList.size());
        	checkAllDoors();
        	z++;
        }
    }

    private Vec3d findRandomSpawnPos(BlockPos pos, int x, int y, int z)
    {
        for (int i = 0; i < 10; ++i)
        {
            BlockPos blockpos = pos.add(this.worldObj.rand.nextInt(16) - 8, this.worldObj.rand.nextInt(6) - 3, this.worldObj.rand.nextInt(16) - 8);

            if (this.isBlockPosWithinSqVillageRadius(blockpos) && this.isAreaClearAround(new BlockPos(x, y, z), blockpos))
            {
                return new Vec3d((double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ());
            }
        }

        return null;
    }

    /**
     * Checks to see if the volume around blockLocation is clear and able to fit blockSize
     */
    private boolean isAreaClearAround(BlockPos blockSize, BlockPos blockLocation)
    {
        if (!this.worldObj.getBlockState(blockLocation.down()).isFullyOpaque())
        {
            return false;
        }
        else
        {
            int i = blockLocation.getX() - blockSize.getX() / 2;
            int j = blockLocation.getZ() - blockSize.getZ() / 2;

            for (int k = i; k < i + blockSize.getX(); ++k)
            {
                for (int l = blockLocation.getY(); l < blockLocation.getY() + blockSize.getY(); ++l)
                {
                    for (int i1 = j; i1 < j + blockSize.getZ(); ++i1)
                    {
                        if (this.worldObj.getBlockState(new BlockPos(k, l, i1)).isNormalCube())
                        {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }

    private void updateNumIronGolems()
    {
        List<EntityIronGolem> list = this.worldObj.<EntityIronGolem>getEntitiesWithinAABB(EntityIronGolem.class, new AxisAlignedBB((double)(this.center.getX() - this.villageRadius), (double)(this.center.getY() - 4), (double)(this.center.getZ() - this.villageRadius), (double)(this.center.getX() + this.villageRadius), (double)(this.center.getY() + 4), (double)(this.center.getZ() + this.villageRadius)));
        this.numIronGolems = list.size();
    }

    private void updateNumVillagers()
    {
        List<EntityVillager> list = this.worldObj.<EntityVillager>getEntitiesWithinAABB(EntityVillager.class, new AxisAlignedBB((double)(this.center.getX() - this.villageRadius), (double)(this.center.getY() - 4), (double)(this.center.getZ() - this.villageRadius), (double)(this.center.getX() + this.villageRadius), (double)(this.center.getY() + 4), (double)(this.center.getZ() + this.villageRadius)));
        this.numVillagers = list.size();

        if (this.numVillagers == 0)
        {
            this.playerReputation.clear();
        }
    }

    public BlockPos getCenter()
    {
        return this.center;
    }

    public int getVillageRadius()
    {
        return this.villageRadius;
    }

    /**
     * Actually get num village door info entries, but that boils down to number of doors. Called by
     * EntityAIVillagerMate and VillageSiege
     */
    public int getNumVillageDoors()
    {
        return this.villageDoorInfoList.size();
    }

    public int getTicksSinceLastDoorAdding()
    {
        return this.tickCounter - this.lastAddDoorTimestamp;
    }

    public int getNumVillagers()
    {
        return this.numVillagers;
    }

    /**
     * Checks to see if the distance squared between this BlockPos and the center of this Village is less than this
     * Village's villageRadius squared
     */
    public boolean isBlockPosWithinSqVillageRadius(BlockPos pos)
    {
        return this.center.distanceSq(pos) < (double)(this.villageRadius * this.villageRadius);
    }

    public List<VillageDoorInfo> getVillageDoorInfoList()
    {
        return this.villageDoorInfoList;
    }

    public VillageDoorInfo getNearestDoor(BlockPos pos)
    {
        VillageDoorInfo villagedoorinfo = null;
        int i = Integer.MAX_VALUE;

        for (VillageDoorInfo villagedoorinfo1 : this.villageDoorInfoList)
        {
            int j = villagedoorinfo1.getDistanceToDoorBlockSq(pos);

            if (j < i)
            {
                villagedoorinfo = villagedoorinfo1;
                i = j;
            }
        }

        return villagedoorinfo;
    }

    /**
     * Returns {@link net.minecraft.village.VillageDoorInfo VillageDoorInfo} from given block position
     */
    public VillageDoorInfo getDoorInfo(BlockPos pos)
    {
        VillageDoorInfo villagedoorinfo = null;
        int i = Integer.MAX_VALUE;

        for (VillageDoorInfo villagedoorinfo1 : this.villageDoorInfoList)
        {
            int j = villagedoorinfo1.getDistanceToDoorBlockSq(pos);

            if (j > 256)
            {
                j = j * 1000;
            }
            else
            {
                j = villagedoorinfo1.getDoorOpeningRestrictionCounter();
            }

            if (j < i)
            {
                BlockPos blockpos = villagedoorinfo1.getDoorBlockPos();
                EnumFacing enumfacing = villagedoorinfo1.getInsideDirection();

                if (this.worldObj.getBlockState(blockpos.offset(enumfacing, 1)).getBlock().isPassable(this.worldObj, blockpos.offset(enumfacing, 1)) && this.worldObj.getBlockState(blockpos.offset(enumfacing, -1)).getBlock().isPassable(this.worldObj, blockpos.offset(enumfacing, -1)) && this.worldObj.getBlockState(blockpos.up().offset(enumfacing, 1)).getBlock().isPassable(this.worldObj, blockpos.up().offset(enumfacing, 1)) && this.worldObj.getBlockState(blockpos.up().offset(enumfacing, -1)).getBlock().isPassable(this.worldObj, blockpos.up().offset(enumfacing, -1)))
                {
                    villagedoorinfo = villagedoorinfo1;
                    i = j;
                }
            }
        }

        return villagedoorinfo;
    }

    @Nullable

    /**
     * if door not existed in this village, null will be returned
     */
    public VillageDoorInfo getExistedDoor(BlockPos doorBlock)
    {
        if (this.center.distanceSq(doorBlock) > (double)(this.villageRadius * this.villageRadius))
        {
            return null;
        }
        else
        {
            for (VillageDoorInfo villagedoorinfo : this.villageDoorInfoList)
            {
                if (villagedoorinfo.getDoorBlockPos().getX() == doorBlock.getX() && villagedoorinfo.getDoorBlockPos().getZ() == doorBlock.getZ() && Math.abs(villagedoorinfo.getDoorBlockPos().getY() - doorBlock.getY()) <= 1)
                {
                    return villagedoorinfo;
                }
            }

            return null;
        }
    }

    public void addVillageDoorInfo(VillageDoorInfo doorInfo)
    {
        this.villageDoorInfoList.add(doorInfo);
        this.centerHelper = this.centerHelper.add(doorInfo.getDoorBlockPos());
        this.updateVillageRadiusAndCenter();
        this.lastAddDoorTimestamp = doorInfo.getInsidePosY();
    }

    /**
     * Returns true, if there is not a single village door left. Called by VillageCollection
     */
    public boolean isAnnihilated()
    {
        return this.villageDoorInfoList.isEmpty();
    }

    public void addOrRenewAgressor(EntityLivingBase entitylivingbaseIn)
    {
        for (Village.VillageAggressor village$villageaggressor : this.villageAgressors)
        {
            if (village$villageaggressor.agressor == entitylivingbaseIn)
            {
                village$villageaggressor.agressionTime = this.tickCounter;
                return;
            }
        }

        this.villageAgressors.add(new Village.VillageAggressor(entitylivingbaseIn, this.tickCounter));
    }

    @Nullable
    public EntityLivingBase findNearestVillageAggressor(EntityLivingBase entitylivingbaseIn)
    {
        double d0 = Double.MAX_VALUE;
        Village.VillageAggressor village$villageaggressor = null;

        for (int i = 0; i < this.villageAgressors.size(); ++i)
        {
            Village.VillageAggressor village$villageaggressor1 = (Village.VillageAggressor)this.villageAgressors.get(i);
            double d1 = village$villageaggressor1.agressor.getDistanceSqToEntity(entitylivingbaseIn);

            if (d1 <= d0)
            {
                village$villageaggressor = village$villageaggressor1;
                d0 = d1;
            }
        }

        return village$villageaggressor == null ? null : village$villageaggressor.agressor;
    }

    public EntityPlayer getNearestTargetPlayer(EntityLivingBase villageDefender)
    {
        double d0 = Double.MAX_VALUE;
        EntityPlayer entityplayer = null;

        for (String s : this.playerReputation.keySet())
        {
            if (this.isPlayerReputationTooLow(s))
            {
                EntityPlayer entityplayer1 = this.worldObj.getPlayerEntityByName(s);

                if (entityplayer1 != null)
                {
                    double d1 = entityplayer1.getDistanceSqToEntity(villageDefender);

                    if (d1 <= d0)
                    {
                        entityplayer = entityplayer1;
                        d0 = d1;
                    }
                }
            }
        }

        return entityplayer;
    }

    private void removeDeadAndOldAgressors()
    {
        Iterator<Village.VillageAggressor> iterator = this.villageAgressors.iterator();

        while (iterator.hasNext())
        {
            Village.VillageAggressor village$villageaggressor = (Village.VillageAggressor)iterator.next();

            if (!village$villageaggressor.agressor.isEntityAlive() || Math.abs(this.tickCounter - village$villageaggressor.agressionTime) > 300)
            {
                iterator.remove();
            }
        }
    }

    private void removeDeadAndOutOfRangeDoors()
    {
        boolean flag = false;
        boolean flag1 = this.worldObj.rand.nextInt(50) == 0;
        Iterator<VillageDoorInfo> iterator = this.villageDoorInfoList.iterator();

        while (iterator.hasNext())
        {
            VillageDoorInfo villagedoorinfo = (VillageDoorInfo)iterator.next();

            if (flag1)
            {
                villagedoorinfo.resetDoorOpeningRestrictionCounter();
            }

            if (!this.isWoodDoor(villagedoorinfo.getDoorBlockPos()) || Math.abs(this.tickCounter - villagedoorinfo.getInsidePosY()) > 1200)
            {
                this.centerHelper = this.centerHelper.subtract(villagedoorinfo.getDoorBlockPos());
                flag = true;
                villagedoorinfo.setIsDetachedFromVillageFlag(true);
                iterator.remove();
            }
        }

        if (flag)
        {
            this.updateVillageRadiusAndCenter();
        }
    }

    private boolean isWoodDoor(BlockPos pos)
    {
        IBlockState iblockstate = this.worldObj.getBlockState(pos);
        Block block = iblockstate.getBlock();
        return block instanceof BlockDoor ? iblockstate.getMaterial() == Material.WOOD : false;
    }

    private void updateVillageRadiusAndCenter()
    {
        int i = this.villageDoorInfoList.size();

        if (i == 0)
        {
            this.center = BlockPos.ORIGIN;
            this.villageRadius = 0;
        }
        else
        {
            this.center = new BlockPos(this.centerHelper.getX() / i, this.centerHelper.getY() / i, this.centerHelper.getZ() / i);
            int j = 0;

            for (VillageDoorInfo villagedoorinfo : this.villageDoorInfoList)
            {
                j = Math.max(villagedoorinfo.getDistanceToDoorBlockSq(this.center), j);
            }

            this.villageRadius = Math.max(32, (int)Math.sqrt((double)j) + 1);
        }
    }

    /**
     * Return the village reputation for a player
     */
    public int getPlayerReputation(String playerName)
    {
        Integer integer = (Integer)this.playerReputation.get(playerName);
        return integer == null ? 0 : integer.intValue();
    }

    /**
     * Modify a players reputation in the village. Use positive values to increase reputation and negative values to
     * decrease. <br>Note that a players reputation is clamped between -30 and 10
     */
    public int modifyPlayerReputation(String playerName, int reputation)
    {
        int i = this.getPlayerReputation(playerName);
        int j = MathHelper.clamp(i + reputation, -30, 10);
        this.playerReputation.put(playerName, Integer.valueOf(j));
        return j;
    }

    /**
     * Return whether this player has a too low reputation with this village.
     */
    public boolean isPlayerReputationTooLow(String playerName)
    {
        return this.getPlayerReputation(playerName) <= -15;
    }

    /**
     * Read this village's data from NBT.
     */
    public void readVillageDataFromNBT(NBTTagCompound compound)
    {
        this.numVillagers = compound.getInteger("PopSize");
        this.villageRadius = compound.getInteger("Radius");
        this.numIronGolems = compound.getInteger("Golems");
        this.lastAddDoorTimestamp = compound.getInteger("Stable");
        this.tickCounter = compound.getInteger("Tick");
        this.noBreedTicks = compound.getInteger("MTick");
        this.center = new BlockPos(compound.getInteger("CX"), compound.getInteger("CY"), compound.getInteger("CZ"));
        this.centerHelper = new BlockPos(compound.getInteger("ACX"), compound.getInteger("ACY"), compound.getInteger("ACZ"));
        NBTTagList nbttaglist = compound.getTagList("Doors", 10);

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            VillageDoorInfo villagedoorinfo = new VillageDoorInfo(new BlockPos(nbttagcompound.getInteger("X"), nbttagcompound.getInteger("Y"), nbttagcompound.getInteger("Z")), nbttagcompound.getInteger("IDX"), nbttagcompound.getInteger("IDZ"), nbttagcompound.getInteger("TS"));
            this.villageDoorInfoList.add(villagedoorinfo);
        }

        NBTTagList nbttaglist1 = compound.getTagList("Players", 10);

        for (int j = 0; j < nbttaglist1.tagCount(); ++j)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist1.getCompoundTagAt(j);

            if (nbttagcompound1.hasKey("UUID") && this.worldObj != null && this.worldObj.getMinecraftServer() != null)
            {
                PlayerProfileCache playerprofilecache = this.worldObj.getMinecraftServer().getPlayerProfileCache();
                GameProfile gameprofile = playerprofilecache.getProfileByUUID(UUID.fromString(nbttagcompound1.getString("UUID")));

                if (gameprofile != null)
                {
                    this.playerReputation.put(gameprofile.getName(), Integer.valueOf(nbttagcompound1.getInteger("S")));
                }
            }
            else
            {
                this.playerReputation.put(nbttagcompound1.getString("Name"), Integer.valueOf(nbttagcompound1.getInteger("S")));
            }
        }
    }

    /**
     * Write this village's data to NBT.
     */
    public void writeVillageDataToNBT(NBTTagCompound compound)
    {
        compound.setInteger("PopSize", this.numVillagers);
        compound.setInteger("Radius", this.villageRadius);
        compound.setInteger("Golems", this.numIronGolems);
        compound.setInteger("Stable", this.lastAddDoorTimestamp);
        compound.setInteger("Tick", this.tickCounter);
        compound.setInteger("MTick", this.noBreedTicks);
        compound.setInteger("CX", this.center.getX());
        compound.setInteger("CY", this.center.getY());
        compound.setInteger("CZ", this.center.getZ());
        compound.setInteger("ACX", this.centerHelper.getX());
        compound.setInteger("ACY", this.centerHelper.getY());
        compound.setInteger("ACZ", this.centerHelper.getZ());
        NBTTagList nbttaglist = new NBTTagList();

        for (VillageDoorInfo villagedoorinfo : this.villageDoorInfoList)
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setInteger("X", villagedoorinfo.getDoorBlockPos().getX());
            nbttagcompound.setInteger("Y", villagedoorinfo.getDoorBlockPos().getY());
            nbttagcompound.setInteger("Z", villagedoorinfo.getDoorBlockPos().getZ());
            nbttagcompound.setInteger("IDX", villagedoorinfo.getInsideOffsetX());
            nbttagcompound.setInteger("IDZ", villagedoorinfo.getInsideOffsetZ());
            nbttagcompound.setInteger("TS", villagedoorinfo.getInsidePosY());
            nbttaglist.appendTag(nbttagcompound);
        }

        compound.setTag("Doors", nbttaglist);
        NBTTagList nbttaglist1 = new NBTTagList();

        for (String s : this.playerReputation.keySet())
        {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            PlayerProfileCache playerprofilecache = this.worldObj.getMinecraftServer().getPlayerProfileCache();

            try
            {
                GameProfile gameprofile = playerprofilecache.getGameProfileForUsername(s);

                if (gameprofile != null)
                {
                    nbttagcompound1.setString("UUID", gameprofile.getId().toString());
                    nbttagcompound1.setInteger("S", ((Integer)this.playerReputation.get(s)).intValue());
                    nbttaglist1.appendTag(nbttagcompound1);
                }
            }
            catch (RuntimeException var9)
            {
                ;
            }
        }

        compound.setTag("Players", nbttaglist1);
    }

    /**
     * Prevent villager breeding for a fixed interval of time
     */
    public void endMatingSeason()
    {
        this.noBreedTicks = this.tickCounter;
    }

    /**
     * Return whether villagers mating refractory period has passed
     */
    public boolean isMatingSeason()
    {
        return this.noBreedTicks == 0 || this.tickCounter - this.noBreedTicks >= 3600;
    }

    public void setDefaultPlayerReputation(int defaultReputation)
    {
        for (String s : this.playerReputation.keySet())
        {
            this.modifyPlayerReputation(s, defaultReputation);
        }
    }

    class VillageAggressor
    {
        public EntityLivingBase agressor;
        public int agressionTime;

        VillageAggressor(EntityLivingBase agressorIn, int agressionTimeIn)
        {
            this.agressor = agressorIn;
            this.agressionTime = agressionTimeIn;
        }
    }
}
