package ru.ilyyya.serverTestModification;

import java.util.ArrayList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
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
	//private int eventTick = 0;
	private int wave = 0;
	private ArrayList<EntityPlayer> players = new ArrayList<EntityPlayer>();
	private ArrayList<EntityMob> mobs = new ArrayList<EntityMob>();
	private int trying = 0;
	private int waiting = 500;

	private int[][] waves =
		{
				{1,1,1,1}, // 1
				{2,2,2,2,2}, // 2
				{1,1,1,2,2,2}, // 3
				{1,2,1,2,1,2,1,2}, // 4
				{3,3,3,3}, // 5
				{1,2,3,1,2,3,1,2,1,2}, // 6
				{1,2,3,1,2,3,1,2,3,1,2,3}, // 7
				{4,4,4,4,4}, // 8
				{1,2,3,4,1,2,3}, // 9
				{1,2,3,4,1,2,3,4}, // 10
				{1,2,3,4,4,4,3,2,1}, // 11
				{1,2,3,4,1,2,3,4,1,2,3,4,1,2,3,4}, // 12
				{1,1,1,1,1,1,1,1,1,1,1,1}, // 13
				{2,2,2,2,2,2,2,2,2,2,2,2}, // 14
				{3,3,3,3,3,3,3,3,3,3,3,3}, // 15
				{4,4,4,4,4,4,4,4}, // 16
				{1,2,3,4,1,2,3,4,1,2,3,4,1,2,3,4,1,2,3,4,1,2,3,4,1,2,3,4,1,2,3,4}, // 17
				{5,5,5,5,5,5,5,5}, // 18
				{5,4,5,4,5,4,5,4,5}, // 19
				{2,3,2,3,2,3,2,3,2,3,2}, //20
				{1,2,3,4,5,1,2,3,4,5,1,2,3,4,5,1,2,3,4,5}, // 21
				{6,6,6,6,6,6,6,6,6,6,6}, // 22
				{1,2,3,4,5,6,6,6,6,6,6,5,4,3,2,1}, // 23
				{1,2,3,4,5,6,1,2,3,4,5,6,1,2,3,4,5,6,1,2,3,4,5,6,1,2,3,4,5,6,1,2,3,4,5,6}, // 24
				{7,7,7,7,7}, // 25
				{2,7,7,2,7,7,2,2,2,2}, // 26
				{2,3,7,2,3,7,2,3,7,2,3,7}, // 27
				{1,2,3,4,5,6,7,1,2,3,4,5,6,7}, // 28
				{7,6,7,6,7,6,7,6,7,6,7,6,7,6,7,6}, // 29
				{1,2,3,4,5,6,7,1,2,3,4,5,6,7,1,2,3,4,5,6,7,1,2,3,4,5,6,7,1,2,3,4,5,6,7,1,2,3,4,5,6,7} // 30
		};
	
	private ItemStack[] prizes =
		{
			new ItemStack(Items.POTATO), // 1
			new ItemStack(Blocks.BONE_BLOCK), // 2
			new ItemStack(Items.COOKIE), // 3
			new ItemStack(Items.MELON), // 4
			new ItemStack(Items.ARROW, 8), // 5
			new ItemStack(Items.CARROT), // 6
			new ItemStack(Items.FLINT), // 7
			new ItemStack(Blocks.OBSIDIAN, 2), // 8
			new ItemStack(Items.field_191525_da, 3), // 9
			new ItemStack(Blocks.IRON_ORE), // 10
			new ItemStack(Items.NAME_TAG), // 11
			new ItemStack(Items.BLAZE_ROD, 1), // 12
			new ItemStack(Items.field_191525_da, 6), // 13
			new ItemStack(Items.GOLD_NUGGET, 3), // 14
			new ItemStack(Items.SPECTRAL_ARROW, 2), // 15
			new ItemStack(Blocks.WEB, 3), // 16
			new ItemStack(Items.APPLE), // 17
			new ItemStack(Items.MILK_BUCKET), // 18
			new ItemStack(Blocks.DIAMOND_ORE, 1, 2), // 19
			new ItemStack(Blocks.GOLD_ORE, 2), // 20
			new ItemStack(Items.GOLDEN_APPLE), // 21
			new ItemStack(Blocks.TNT, 2), // 22
			new ItemStack(Items.ENDER_PEARL, 2), // 23
			new ItemStack(Items.RABBIT_STEW), // 24
			new ItemStack(Items.SKULL, 1, 1), // 25
			new ItemStack(Blocks.EMERALD_BLOCK, 2), // 26
			new ItemStack(Items.GOLDEN_APPLE, 1, 1), // 27
			new ItemStack(Items.ELYTRA), // 28
			new ItemStack(Items.field_190929_cY), // 29
		};
	
	public CreepiSquidyEvent(World world, BlockPos pos)
	{
		this.world = world;
		this.pos = pos;
		
		for(int i = 0; i < world.playerEntities.size(); i++)
		{
			EntityPlayer pl = world.playerEntities.get(i);
			if(pl.getDistanceSqToCenter(pos) < 40D && !pl.isCreative())
			{
				players.add(pl);
			}
		}

		MinecraftServer.theServer.sendMessageForAll("Players on event: " + players.size());
	}
	
	@Override
	public void onTickUpdate()
	{
		
	}
	
	private void CheckPlayersLiving()
	{
		for(int i = 0; i < players.size(); i++)
		{
			if(players.get(i).isDead) players.remove(i);
		}
	}
	
	private void CheckMobsIsLiving()
	{
		for(int i = 0; i < mobs.size(); i++)
		{
			if(mobs.get(i).isDead) mobs.remove(i);
		}
	}
	
	private void CheckMobsInAndOut()
	{
		for(int i = 0; i < mobs.size(); i++)
		{
			EntityMob entity = mobs.get(i);
			
			if(entity.isDead) mobs.remove(entity);
			
			if(!EntityIn(entity))
			{
				entity.setPosition(pos.getX()+0.5D, pos.getY() + 3, pos.getZ()+0.5D);
				entity.fallDistance = 0F;
			}
		}
	}
	
	private EntityMob getSpawningEntity(int id)
	{
		switch(id)
		{
			case 1: return new EntityZombie(world);
			case 2: return new EntitySkeleton(world);
			case 3: EntitySkeleton sk = new EntitySkeleton(world);
					sk.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
					return sk;
			case 4: return new EntitySpider(world);
			case 5: return new EntityCaveSpider(world);
			case 6: return new EntityCreeper(world);
			case 7: return new EntityWitherSkeleton(world);
		}
		return null;
	}
	
	private void CheckInAndOutPlayers()
	{
		for(int i = 0; i < world.playerEntities.size(); i++)
		{
			EntityPlayer pl = world.playerEntities.get(i);
			
			if(pl.isDead) players.remove(pl);
			
			if(players.contains(pl))
			{
				boolean buff = !EntityIn(pl);
				if(buff && trying <= 0)
				{
					pl.setPosition(pos.getX()+0.5D, pos.getY() + 3, pos.getZ()+0.5D);
					pl.fallDistance = 0F;
					trying = 5;
				}
				else if(buff && trying > 0)
				{
					trying--;
				}
				else
				{
					trying = 10;
				}
			}
			else
			{
				if(EntityIn(pl) && pl.onGround)
				{
					pl.attackEntityFrom(DamageSource.fall, 1F);
					pl.onGround = false;
					pl.motionX = pl.posX - pos.getX();
					pl.motionY = 1D;
					pl.motionZ = pl.posZ - pos.getZ();
				}
			}
		}
	}
	
	private boolean EntityIn(Entity entity)
	{
		if(entity.posX > pos.getX() - (ilStructures.arena[0].length/2) &&
				entity.posX < pos.getX() + (ilStructures.arena[0].length/2) &&
				entity.posZ > pos.getZ() - (ilStructures.arena[0][0].length()/2) &&
				entity.posZ < pos.getZ() + (ilStructures.arena[0][0].length()/2) &&
				entity.posY >= pos.getY() &&
						entity.posY < pos.getY() + ilStructures.arena.length)
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
			case 2: return Blocks.GLOWSTONE.getDefaultState();
			case 3: return Blocks.REDSTONE_TORCH.getDefaultState();
			case 4: return Blocks.ENDER_CHEST.getDefaultState();
			case 5: return Blocks.CHEST.getDefaultState();
		}
		
		return null;
	}
	
	@Override
	public void onTickUpdateEnd()
	{
		
	}
	
	int last = 0;

	@Override
	public void onEnemysUpdate()
	{
		CheckPlayersLiving();
		CheckInAndOutPlayers();
		CheckMobsIsLiving();
		CheckMobsInAndOut();
		
		
		if(players.size() == 0)
		{
			MinecraftServer.theServer.sendMessageForAll("Event stopped. All the players who took event died");
			TickHandler.removeHandler(this);
		}
		else if(stage == 0)
		{
			int iy = CheckAndCreatePlace();
			if(iy >= 0)
			{
				for(int i = 0; i < players.size(); i++)
				{
					if((int)players.get(i).posX != (int)(pos.getX()+0.5D) || (int)players.get(i).posY != (int)(pos.getY()+0.5D))
					{
						players.get(i).setPosition(pos.getX()+0.5D, pos.getY() + 3, pos.getZ()+0.5D);
						players.get(i).fallDistance = 0F;
					}
				}
			}
			else stage = 1;
		}
		else if(stage == 1)
		{
			CheckAndCreatePlace();
			if(wave < waves.length)
			{
				if(mobs.size() == 0)
				{
					if(waiting == 720 + wave * 45)
					{
						MinecraftServer.theServer.sendMessageForAll("Good Job!! Take a prize. " + ((720 + wave * 45)/60) + " seconds for battle");
						for(int i = 0; i < wave; i++)
						{
							EntityItem item = new EntityItem(world, pos.getX() + 0.5D, pos.getY() + 4, pos.getZ() + 0.5D, prizes[i]);
				        	world.spawnEntityInWorld(item);
						}
					}
					switch(waiting)
					{
						case 599: 
								MinecraftServer.theServer.sendMessageForAll("10 seconds to prepare for battle");
								MinecraftServer.theServer.sendMessageForAll("Next wave: " + (wave+1) + "/" + waves.length);
								MinecraftServer.theServer.sendMessageForAll("Next mobs count: " + waves[wave].length);
								break;
						case 60*2: MinecraftServer.theServer.sendMessageForAll("Ready"); break;
						case 60*1: MinecraftServer.theServer.sendMessageForAll("Steady"); break;
						case 0: MinecraftServer.theServer.sendMessageForAll("GO!!!"); break;
					}
					
					if(waiting <= 0)
					{
						for(int i = 0; i < waves[wave].length; i++)
						{
							EntityMob mob = getSpawningEntity(waves[wave][i]);
							switch(i%5)
							{
								case 0:	mob.setPosition(
										pos.getX() + 0.5D - 6D,
										pos.getY() + 3,
										pos.getZ() + 0.5D - 6D);
										break;
								case 1: mob.setPosition(
										pos.getX() + 0.5D + 6D,
										pos.getY() + 3,
										pos.getZ() + 0.5D - 6D);
										break;
								case 2: mob.setPosition(
										pos.getX() + 0.5D - 6D,
										pos.getY() + 3,
										pos.getZ() + 0.5D + 6D);
										break;
								case 3: mob.setPosition(
										pos.getX() + 0.5D + 6D,
										pos.getY() + 3,
										pos.getZ() + 0.5D + 6D);
										break;
								case 4: mob.setPosition(
										pos.getX() + 0.5D,
										pos.getY() + 3D,
										pos.getZ() + 0.5D);
										break;
							}
							world.spawnEntityInWorld(mob);
							mobs.add(mob);
						}
						wave++;
						waiting = 760 + wave * 45;
					}
					else
					{
						waiting--;
					}
				}
				else
				{
					if(last != mobs.size())
					{
						MinecraftServer.theServer.sendMessageForAll("" + mobs.size() + " mobs you need kill to took next wave");
						last = mobs.size();
					}
				}
			}
			else if(mobs.size() == 0)
			{
				MinecraftServer.theServer.sendMessageForAll("WELL PLAYED!");
				MinecraftServer.theServer.sendMessageForAll("You passed this event");
				MinecraftServer.theServer.sendMessageForAll("Take your prizes...");
				for(int i = 0; i < prizes.length; i++)
				{
					EntityItem item = new EntityItem(world, pos.getX() + 0.5D, pos.getY() + 4, pos.getZ() + 0.5D, prizes[i]);
		        	world.spawnEntityInWorld(item);
				}
				MinecraftServer.theServer.sendMessageForAll("and this structure ;)");
				world.setBlockState(pos.south(2).west().up(), Blocks.END_PORTAL_FRAME.getStateFromMeta(2));
				world.setBlockState(pos.south(2).up(), Blocks.END_PORTAL_FRAME.getStateFromMeta(2));
				world.setBlockState(pos.south(2).east().up(), Blocks.END_PORTAL_FRAME.getStateFromMeta(2));
				world.setBlockState(pos.north(2).west().up(), Blocks.END_PORTAL_FRAME.getStateFromMeta(0));
				world.setBlockState(pos.north(2).up(), Blocks.END_PORTAL_FRAME.getStateFromMeta(0));
				world.setBlockState(pos.north(2).east().up(), Blocks.END_PORTAL_FRAME.getStateFromMeta(0));
				world.setBlockState(pos.west(2).south().up(), Blocks.END_PORTAL_FRAME.getStateFromMeta(3));
				world.setBlockState(pos.west(2).up(), Blocks.END_PORTAL_FRAME.getStateFromMeta(3));
				world.setBlockState(pos.west(2).north().up(), Blocks.END_PORTAL_FRAME.getStateFromMeta(3));
				world.setBlockState(pos.east(2).south().up(), Blocks.END_PORTAL_FRAME.getStateFromMeta(1));
				world.setBlockState(pos.east(2).up(), Blocks.END_PORTAL_FRAME.getStateFromMeta(1));
				world.setBlockState(pos.east(2).north().up(), Blocks.END_PORTAL_FRAME.getStateFromMeta(1));
				TickHandler.removeHandler(this);
			}
		}
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
