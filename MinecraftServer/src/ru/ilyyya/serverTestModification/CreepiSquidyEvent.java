package ru.ilyyya.serverTestModification;

import java.util.ArrayList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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
			if(pl.getDistanceSqToCenter(pos) < 10D && !pl.isCreative())
			{
				players.add(pl);
			}
		}
	}
	
	@Override
	public void onTickUpdate()
	{
		int yy = CheckAndCreatePlace();
		if(yy >= 0) for(int i = 0; i < players.size(); i++) players.get(i).setPosition(pos.getX(), yy+1, pos.getZ());
	}

	private int CheckAndCreatePlace()
	{
		for(int iy = 0; iy < ilStructures.arena.length; iy++)
		{
			for(int ix = 0; ix < ilStructures.arena[iy].length; ix++)
			{
				for(int iz = 0; iz < ilStructures.arena[iy][ix].length(); iz++)
				{
					if(world.getBlockState(pos.add(ix - ilStructures.arena[iy].length/2, iy, iz - ilStructures.arena[iy][ix].length()/2)) != getBlockByStringNum(ilStructures.arena[iy][ix].substring(iz, iz+1)))
					{
						world.setBlockState(pos.add(ix - ilStructures.arena[iy].length/2, iy, iz - ilStructures.arena[iy][ix].length()/2), getBlockByStringNum(ilStructures.arena[iy][ix].substring(iz, iz+1)));
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
			case 1: return Blocks.BRICK_BLOCK.getDefaultState();
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
