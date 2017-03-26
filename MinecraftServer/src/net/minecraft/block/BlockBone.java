package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockBone extends BlockRotatedPillar
{
    public BlockBone()
    {
        super(Material.ROCK, MapColor.SAND);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        this.setHardness(2.0F);
        this.setSoundType(SoundType.STONE);
    }
    
    public void onEntityWalk(World world, BlockPos pos, Entity entity)
    {
    	if(entity instanceof EntityPlayer)
    	{
    		if(world.getBlockState(pos.down(1)) == Blocks.DIAMOND_BLOCK.getDefaultState() &&
    				world.getBlockState(pos.down(2)) == Blocks.GOLD_BLOCK.getDefaultState() &&
    				world.getBlockState(pos.down(3)) == Blocks.EMERALD_BLOCK.getDefaultState() &&
    				world.getBlockState(pos.down(4)) == Blocks.IRON_BLOCK.getDefaultState())
    		{
    			world.setBlockToAir(pos);
    			EntityZombie zz = new EntityZombie(world);
    			zz.modified = true;
    			zz.UpdateForModified();
    			zz.setPosition(pos.getX(), pos.getY()+1, pos.getZ());
    			world.spawnEntityInWorld(zz);
            	if(MinecraftServer.theServer != null) MinecraftServer.theServer.sendMessageForAll("[???]: HAHAHA!!! IT's YOUR MISTAKE!!!");
    		}
    	}
    }
}
