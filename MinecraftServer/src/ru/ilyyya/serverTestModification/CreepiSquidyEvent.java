package ru.ilyyya.serverTestModification;

import java.util.ArrayList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CreepiSquidyEvent implements EventHandler
{
	private World world;
	private BlockPos pos;
	private int stage = 0;
	private ArrayList<EntityPlayer> players = new ArrayList<EntityPlayer>();
	
	//HELLO WORLD!!
	
	public CreepiSquidyEvent(World world, BlockPos pos)
	{
		this.world = world;
		this.pos = pos;
		
		for(int i = 0; i < world.playerEntities.size(); i++)
		{
			EntityPlayer pl = world.playerEntities.get(i);
			if(pl.getDistanceSqToCenter(pos) < 20D && !pl.isCreative())
			{
				players.add(pl);
			}
		}
	}
	
	@Override
	public void onTickUpdate()
	{
		if(stage < 10) CheckInAndOutPlayers();
		
		if(stage == 0)
		{
			int iy = CheckAndCreatePlace();
			if(iy >= 0)
			{
				for(int i = 0; i < players.size(); i++) if((int)players.get(i).posX != (int)(pos.getX()+0.5D) || (int)players.get(i).posY != (int)(pos.getY()+0.5D)) players.get(i).setPosition(pos.getX()+0.5D, pos.getY() + iy + 1, pos.getZ()+0.5D);
			}
			else stage = 1;
		}
		else if(stage == 1)
		{
			CheckAndCreatePlace();
		}
	}
	
	private void CheckInAndOutPlayers()
	{
		for(int i = 0; i < world.playerEntities.size(); i++)
		{
			EntityPlayer pl = world.playerEntities.get(i);
			if(players.contains(pl))
			{
				if(!PlayerIn(pl))
				{
					pl.setPosition(pos.getX()+0.5D, pos.getY() + 3, pos.getZ()+0.5D);
				}
			}
			else
			{
				if(PlayerIn(pl) && pl.onGround)
				{
					pl.attackEntityFrom(DamageSource.fall, 1F);
					pl.onGround = false;
					pl.motionX = pl.posX - pos.getX();
					pl.motionY = 1.5D;
					pl.motionZ = pl.posZ - pos.getZ();
				}
			}
		}
	}
	
	private boolean PlayerIn(EntityPlayer pl)
	{
		if(pl.posX > pos.getX() - (ilStructures.arena[0].length/2) &&
				pl.posX < pos.getX() + (ilStructures.arena[0].length/2) &&
				pl.posZ > pos.getZ() - (ilStructures.arena[0][0].length()/2) &&
				pl.posZ < pos.getZ() + (ilStructures.arena[0][0].length()/2) &&
				pl.posY >= pos.getY() &&
				pl.posY < pos.getY() + ilStructures.arena.length + 2)
			return true;
		else return false;
	}

	private int CheckAndCreatePlace()
	{
		for(int iy = 0; iy < ilStructures.arena.length; iy++)
		{
			for(int ix = 0; ix < ilStructures.arena[iy].length; ix++)
			{
				for(int iz = 0; iz < ilStructures.arena[iy][ix].length(); iz++)
				{
					BlockPos posBuff = pos.add(ix - ilStructures.arena[iy].length/2, iy, iz - ilStructures.arena[iy][ix].length()/2);
					IBlockState stateBuff = getBlockByStringNum(ilStructures.arena[iy][ix].substring(iz, iz+1));
					if(world.getBlockState(posBuff) != stateBuff)
					{
						if(world.getBlockState(posBuff) != Blocks.AIR.getDefaultState()) world.spawnEntityInWorld(new EntityItem(world, posBuff.getX(), posBuff.getY()+1, posBuff.getZ(), new ItemStack(world.getBlockState(posBuff).getBlock())));
						world.setBlockState(posBuff, stateBuff);
						return iy;
					}
				}
			}
		}
		return -1;
	}
	
	private IBlockState getBlockByStringNum(String str)
	{
		int n = Integer.parseInt(str);
		switch(n)
		{
			case 0: return Blocks.AIR.getDefaultState();
			case 1: return Blocks.STONEBRICK.getDefaultState();
		}
		
		return null;
	}
	
	@Override
	public void onTickUpdateEnd()
	{
		
	}

	@Override
	public void onEnemysUpdate()
	{
		
	}

	@Override
	public void onEnemysUpdateEnd()
	{
		
	}

	@Override
	public void onLightsUpdate()
	{
		
	}

	@Override
	public void onLightsUpdateEnd()
	{
		
	}

}
