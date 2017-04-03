package ru.ilyyya.serverTestModification.VillagerTasks;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityVillager;

public class RepairHouseAI extends EntityAIBase
{
	private EntityVillager theVillager;
	private VillageStructure structure = null;
	private BlockForRepair repairInfo = null;
	
	private int coolDown = 0;
	private int stayALongAtOncePositionTimer = 0;
	
	private int salaoptX = 0;
	private int salaoptZ = 0;
	
	public RepairHouseAI(EntityVillager villager)
	{
        this.setMutexBits(1);
        theVillager = villager;
	}

	@Override
	public boolean shouldExecute()
	{
		if(theVillager.villageObj != null && theVillager.villageObj.VillageStructures.size() > 0 && (structure == null || structure.percent >= 1.0D))
		{
			structure = theVillager.villageObj.getDamagedHouse();
		}
		if(structure != null && structure.percent < 1.0D)
		{
			repairInfo = structure.getBlockForRepair(theVillager.world, repairInfo);
		}
		return structure != null && structure.percent < 1.0D && repairInfo != null && theVillager.villageObj != null && theVillager.villageObj.canThisVillagerRepairThisBlock(repairInfo, theVillager);
	}
	
	@Override
	public boolean continueExecuting()
	{
		return structure != null && structure.percent < 1.0D && repairInfo != null && theVillager.villageObj.canThisVillagerRepairThisBlock(repairInfo, theVillager);
	}
	
	@Override
	public void updateTask()
	{
		if(repairInfo != null && theVillager.getDistance(repairInfo.posX, theVillager.posY, repairInfo.posZ) > 3D && theVillager.getNavigator().noPath())
		{
			int i = 0;
			while(!theVillager.getNavigator().tryMoveToXYZ(repairInfo.posX + i%7 - 3, theVillager.posY, repairInfo.posZ + i/7, 0.7D) && i <= 49) {i++;}
			if(i == 50) repairInfo = null;
		}
		else if(coolDown <= 0 && repairInfo != null && theVillager.getDistance(repairInfo.posX, theVillager.posY, repairInfo.posZ) <= 3D && theVillager.villageObj != null)
		{
			theVillager.villageObj.repairThisBlock(repairInfo, theVillager);
			repairInfo = null;
			coolDown = 15;
		}
		
		if(salaoptX == (int)theVillager.posX && salaoptZ == (int)theVillager.posZ) stayALongAtOncePositionTimer++;
		else
		{
			stayALongAtOncePositionTimer = 0;
			salaoptX = (int)theVillager.posX;
			salaoptZ = (int)theVillager.posZ;
		}
		
		if(coolDown <= 0 && repairInfo != null && stayALongAtOncePositionTimer > 300)
		{
			theVillager.villageObj.repairThisBlock(repairInfo, theVillager);
			repairInfo = null;
			structure = null;
			coolDown = 15;
		}
		
		if(coolDown > 0) coolDown--;
	}
}
