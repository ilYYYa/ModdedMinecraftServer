package ru.ilyyya.serverTestModification.VillagerTasks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockStairs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import ru.ilyyya.serverTestModification.ilStructures;

public class VillageStructure
{
	public int structureId = 0;
	public int structureType = 0;
	public int[] doorPosInStructure = {0,0,0};
	
	public int facing = 0;
	public boolean mirrored = false;
	
	public BlockPos doorPos;
	public double percent = 0.0D;
	
	public int posX1 = 0;
	public int posY1 = 0;
	public int posZ1 = 0;
	public BlockPos pos1;
	
	public int posX2 = 0;
	public int posY2 = 0;
	public int posZ2 = 0;
	public BlockPos pos2;
	
	public VillageStructure(){}
	
	public VillageStructure(BlockPos doorPos, int face, boolean mirror, int structureType, int structureId, int[] doorCoordsInStructure)
	{
		this.doorPos = doorPos;
		this.facing = face;
		this.mirrored = mirror;
		this.structureType = structureType;
		this.structureId = structureId;
		this.doorPosInStructure = doorCoordsInStructure;
		
		this.setPos1();
		this.setPos2();
		this.updatePoses();
	}
	
	public VillageStructure(int x1, int y1, int z1, int x2, int y2, int z2)
	{
		posX1 = x1; posY1 = y1; posZ1 = z1;
		posX2 = x2; posY2 = y2; posZ2 = z2;
		updatePoses();
	}
	
	public VillageStructure(BlockPos p1, BlockPos p2)
	{
		this(p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ());
	}
	
	public BlockForRepair getBlockForRepair(World world, BlockForRepair last)
	{
		int[][][] struc = getStructureByFacingAndMirroring();
		for(int iy = 0; iy < struc.length; iy++)
		{
			for(int ix = 0; ix < struc[iy].length; ix++)
			{
				for(int iz = 0; iz < struc[iy][ix].length; iz++)
				{
					BlockPos b_pos = new BlockPos(posX1+ix, posY1+iy, posZ1+iz);
					if(!Village.checkBlockPosAndBlockId(world, b_pos, struc[iy][ix][iz]))
					{
						if(last != null && last.blockPos != b_pos || last == null)
						{
							BlockForRepair bfr = new  BlockForRepair(b_pos, struc[iy][ix][iz]);
							
							if(struc[iy][ix][iz] == 5)
							{
								if((ix == struc[iy].length - 1 || struc[iy][ix+1][iz] == 0) && (ix == 0 || struc[iy][ix-1][iz] != 0)) bfr.meta = 1;
								else if((ix == struc[iy].length - 1 || struc[iy][ix+1][iz] != 0) && (ix == 0 || struc[iy][ix-1][iz] == 0)) bfr.meta = 0;
								else if((iz == struc[iy][ix].length - 1 || struc[iy][ix][iz+1] == 0) && (iz == 0 || struc[iy][ix][iz-1] != 0)) bfr.meta = 3;
								else if((iz == struc[iy][ix].length - 1 || struc[iy][ix][iz+1] != 0) && (iz == 0 || struc[iy][ix][iz-1] == 0)) bfr.meta = 2;
							}
							
							return bfr;
						}
					}
				}
			}
		}
		
		return last;
	}
	
	public void checkStructure(World world)
	{
		int[][][] struc = getStructureByFacingAndMirroring();
		
		double trues = 0.0D;
		double trys = 0.0D;
		
		for(int iy = 0; iy < struc.length; iy++)
		{
			for(int ix = 0; ix < struc[iy].length; ix++)
			{
				for(int iz = 0; iz < struc[iy][ix].length; iz++)
				{
					BlockPos pos = new BlockPos(posX1 + ix, posY1 + iy, posZ1 + iz);
					if(Village.checkBlockPosAndBlockId(world, pos, struc[iy][ix][iz])) trues++;
					
					trys++;
				}
			}
		}
		
		percent = trues/trys;
	}
	
	public void setPos1()
	{
		if(facing == 0 && !mirrored) pos1 = doorPos.down(doorPosInStructure[1]).west(doorPosInStructure[0]).north(doorPosInStructure[2]);
		if(facing == 1 && !mirrored) pos1 = doorPos.down(doorPosInStructure[1]).south(doorPosInStructure[0]).west(doorPosInStructure[2]);
		if(facing == 2 && !mirrored) pos1 = doorPos.down(doorPosInStructure[1]).east(doorPosInStructure[0]).south(doorPosInStructure[2]);
		if(facing == 3 && !mirrored) pos1 = doorPos.down(doorPosInStructure[1]).north(doorPosInStructure[0]).east(doorPosInStructure[2]);
		
		if(facing == 0 && mirrored) pos1 = doorPos.down(doorPosInStructure[1]).west(doorPosInStructure[0]).south(doorPosInStructure[2]);
		if(facing == 1 && mirrored) pos1 = doorPos.down(doorPosInStructure[1]).south(doorPosInStructure[0]).east(doorPosInStructure[2]);
		if(facing == 2 && mirrored) pos1 = doorPos.down(doorPosInStructure[1]).east(doorPosInStructure[0]).north(doorPosInStructure[2]);
		if(facing == 3 && mirrored) pos1 = doorPos.down(doorPosInStructure[1]).north(doorPosInStructure[0]).west(doorPosInStructure[2]);

		posX1 = pos1.getX(); posY1 = pos1.getY(); posZ1 = pos1.getZ();
	}
	
	public void setPos2()
	{
		if(facing == 0 && !mirrored) pos2 = pos1.up(getStructure().length-1).east(getStructure()[0].length-1).south(getStructure()[0][0].length-1);
		if(facing == 1 && !mirrored) pos2 = pos1.up(getStructure().length-1).north(getStructure()[0].length-1).east(getStructure()[0][0].length-1);
		if(facing == 2 && !mirrored) pos2 = pos1.up(getStructure().length-1).west(getStructure()[0].length-1).north(getStructure()[0][0].length-1);
		if(facing == 3 && !mirrored) pos2 = pos1.up(getStructure().length-1).south(getStructure()[0].length-1).west(getStructure()[0][0].length-1);
		
		if(facing == 0 && mirrored) pos2 = pos1.up(getStructure().length-1).east(getStructure()[0].length-1).north(getStructure()[0][0].length-1);
		if(facing == 1 && mirrored) pos2 = pos1.up(getStructure().length-1).north(getStructure()[0].length-1).west(getStructure()[0][0].length-1);
		if(facing == 2 && mirrored) pos2 = pos1.up(getStructure().length-1).west(getStructure()[0].length-1).south(getStructure()[0][0].length-1);
		if(facing == 3 && mirrored) pos2 = pos1.up(getStructure().length-1).south(getStructure()[0].length-1).east(getStructure()[0][0].length-1);

		posX2 = pos2.getX(); posY2 = pos2.getY(); posZ2 = pos2.getZ();
	}
	
	public void updatePoses()
	{
		int buff = 0;
		if(posX1 > posX2)
		{
			buff = posX1;
			posX1 = posX2;
			posX2 = buff;
		}
		if(posY1 > posY2)
		{
			buff = posY1;
			posY1 = posY2;
			posY2 = buff;
		}
		if(posZ1 > posZ2)
		{
			buff = posZ1;
			posZ1 = posZ2;
			posZ2 = buff;
		}
		
		pos1 = new BlockPos(posX1, posY1, posZ1);
		pos2 = new BlockPos(posX2, posY2, posZ2);
	}
	
	public int[][][] getStructure()
	{
		switch(structureType)
		{
			case 0: return ilStructures.Houses[structureId];
		}
		return new int[][][] {{{}}};
	}
	
	public int[][][] getStructureByFacingAndMirroring()
	{
		int[][][] struc = getStructure();
		
		if(facing == 0 && !mirrored) return struc;
		
		int[][][] buff;
		
		if(facing == 1 || facing == 3) buff = new int[struc.length][struc[0][0].length][struc[0].length];
		else buff = new int[struc.length][struc[0].length][struc[0][0].length];
		
		for(int iy = 0; iy < struc.length; iy++)
		{
			for(int ix = 0; ix < struc[iy].length; ix++)
			{
				for(int iz = 0; iz < struc[iy][ix].length; iz++)
				{
					int[] buffer = getXandZbyFacing(ix, iz, struc[iy].length, struc[iy][ix].length);
					if(facing == 1 || facing == 3) buffer = getXandZbyMirroring(buffer[0], buffer[1], struc[iy][ix].length, struc[iy].length);
					else buffer = getXandZbyMirroring(buffer[0], buffer[1], struc[iy].length, struc[iy][ix].length);
					
					buff[iy][buffer[0]][buffer[1]] = struc[iy][ix][iz];
				}
			}
		}
		
		return buff;
	}
	
	public int[] getXandZbyFacing(int x, int z, int maxX, int maxZ)
	{
		if(facing == 0) return new int[]{x,z};
		else if(facing == 1) return new int[]{z,maxX-x-1};
		else if(facing == 2) return new int[]{maxX-x-1,maxZ-z-1};
		else return new int[]{maxZ-z-1,x};
	}
	public int[] getXandZbyMirroring(int x, int z, int maxX, int maxZ)
	{
		if(!mirrored) return new int[]{x,z};
		else
		{
			if(facing == 1 || facing == 3) return new int[]{maxX - x - 1, z};
			else return new int[]{x, maxZ - z - 1};
		}
	}
	
	public String toString()
	{
		return "VillageStructure{Type: " + structureType + ", ID: " + structureId + ", Facing: " + facing + ", Mirrored: " + mirrored + ", " +
				(int)(percent*100D) + "%, POS1: " + pos1.toString() + ", POS2: " + pos2.toString() + ", DoorPOS: " + doorPos.toString() +"}";
	}
}



























