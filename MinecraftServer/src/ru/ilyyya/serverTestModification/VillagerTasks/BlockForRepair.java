package ru.ilyyya.serverTestModification.VillagerTasks;

import net.minecraft.util.math.BlockPos;

public class BlockForRepair
{
	public int posX = 0;
	public int posY = 0;
	public int posZ = 0;
	
	public BlockPos blockPos;
	
	public int blockId = 0;
	
	public int meta = 0;
	
	public BlockForRepair(int x, int y, int z, int id)
	{
		posX = x; posY = y; posZ = z; blockId = id;
		blockPos = new BlockPos(x,y,z);
	}
	public BlockForRepair(BlockPos pos, int id)
	{
		posX = pos.getX(); posY = pos.getY(); posZ = pos.getZ(); blockId = id;
		blockPos = new BlockPos(posX,posY,posZ);
	}
	public String toString()
	{
		return "BlockForRepair{" +blockId + "@id{x:" + posX + " y:" + posY + " z: " + posZ + "}"; 
	}
}