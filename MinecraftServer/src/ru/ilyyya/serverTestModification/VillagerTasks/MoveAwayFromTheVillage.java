package ru.ilyyya.serverTestModification.VillagerTasks;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class MoveAwayFromTheVillage extends EntityAIBase
{
	private EntityVillager theVillager;
    private double movePosX;
    private double movePosY;
    private double movePosZ;
    
	public MoveAwayFromTheVillage(EntityVillager villager)
	{
		theVillager = villager;
		this.setMutexBits(1);
	}
	@Override
	public boolean shouldExecute()
	{
		if(theVillager.villageObj != null && theVillager.getDistanceSqToCenter(theVillager.villageObj.getCenter()) < theVillager.villageObj.villageRadius*theVillager.villageObj.villageRadius)
		{
			BlockPos blockpos = theVillager.villageObj.getCenter();
			
	        boolean isitX = Math.random() < 0.5;
	        double anotherCoord = Math.random() * theVillager.villageObj.villageRadius;
	        
	        movePosX = isitX ? blockpos.getX() + anotherCoord : blockpos.getX() + theVillager.villageObj.villageRadius;
	        movePosZ = isitX ? blockpos.getZ() + theVillager.villageObj.villageRadius : blockpos.getZ() + anotherCoord;
	        
	        movePosY = theVillager.posY;
	        
			return true;
		}
		return false;
	}
	
	@Override
	public boolean continueExecuting()
    {
        return !this.theVillager.getNavigator().noPath();
    }
	
	@Override
	public void startExecuting()
    {
        this.theVillager.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, 0.7D);
        MinecraftServer.theServer.sendMessageForAll("Trying to move to x " + this.movePosX + " y " + this.movePosY + " z " + this.movePosZ);
    }
}
