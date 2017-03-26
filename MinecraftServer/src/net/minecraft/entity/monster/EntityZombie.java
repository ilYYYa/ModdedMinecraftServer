package net.minecraft.entity.monster;

import java.awt.Component;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBreakDoor;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIZombieAttack;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityZombie extends EntityMob
{
    /**
     * The attribute which determines the chance that this mob will spawn reinforcements
     */
    protected static final IAttribute SPAWN_REINFORCEMENTS_CHANCE = (new RangedAttribute((IAttribute)null, "zombie.spawnReinforcements", 0.0D, 0.0D, 1.0D)).setDescription("Spawn Reinforcements Chance");
    private static final UUID BABY_SPEED_BOOST_ID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
    private static final AttributeModifier BABY_SPEED_BOOST = new AttributeModifier(BABY_SPEED_BOOST_ID, "Baby speed boost", 0.5D, 1);
    private static final DataParameter<Boolean> IS_CHILD = EntityDataManager.<Boolean>createKey(EntityZombie.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> VILLAGER_TYPE = EntityDataManager.<Integer>createKey(EntityZombie.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> ARMS_RAISED = EntityDataManager.<Boolean>createKey(EntityZombie.class, DataSerializers.BOOLEAN);
    private final EntityAIBreakDoor breakDoor = new EntityAIBreakDoor(this);
    private boolean isBreakDoorsTaskSet;

    /** The width of the entity */
    private float zombieWidth = -1.0F;

    /** The height of the the entity. */
    private float zombieHeight;
    
    public boolean modified = Math.random() < 0.0075;
    private int babyZ = 0;
    private int babyZTimer = 0;
    private int lightZ = 0;
    private int upZ = 0;
    
    public EntityZombie(World worldIn)
    {
        super(worldIn);
        this.setSize(0.6F, 1.95F);
        if(modified) UpdateForModified();
    }
    
    public void UpdateForModified()
    {
    	this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(Math.random() * 40 + 60);
    	this.setHealth((float)this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue());
    }

    protected void initEntityAI()
    {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIZombieAttack(this, 1.0D, false));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.applyEntityAI();
    }

    protected void applyEntityAI()
    {
        this.tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0D, false));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] {EntityPigZombie.class}));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityVillager.class, false));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
        this.getAttributeMap().registerAttribute(SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(this.rand.nextDouble() * 0.10000000149011612D);
    }

    protected void entityInit()
    {
        super.entityInit();
        this.getDataManager().register(IS_CHILD, Boolean.valueOf(false));
        this.getDataManager().register(VILLAGER_TYPE, Integer.valueOf(0));
        this.getDataManager().register(ARMS_RAISED, Boolean.valueOf(false));
    }

    public void setArmsRaised(boolean armsRaised)
    {
        this.getDataManager().set(ARMS_RAISED, Boolean.valueOf(armsRaised));
    }

    public boolean isBreakDoorsTaskSet()
    {
        return this.isBreakDoorsTaskSet;
    }

    /**
     * Sets or removes EntityAIBreakDoor task
     */
    public void setBreakDoorsAItask(boolean enabled)
    {
        if (this.isBreakDoorsTaskSet != enabled)
        {
            this.isBreakDoorsTaskSet = enabled;
            ((PathNavigateGround)this.getNavigator()).setBreakDoors(enabled);

            if (enabled)
            {
                this.tasks.addTask(1, this.breakDoor);
            }
            else
            {
                this.tasks.removeTask(this.breakDoor);
            }
        }
    }

    /**
     * If Animal, checks if the age timer is negative
     */
    public boolean isChild()
    {
        return ((Boolean)this.getDataManager().get(IS_CHILD)).booleanValue();
    }

    /**
     * Get the experience points the entity currently has.
     */
    protected int getExperiencePoints(EntityPlayer player)
    {
        if (this.isChild())
        {
            this.experienceValue = (int)((float)this.experienceValue * 2.5F);
        }

        return super.getExperiencePoints(player);
    }

    /**
     * Set whether this zombie is a child.
     */
    public void setChild(boolean childZombie)
    {
        this.getDataManager().set(IS_CHILD, Boolean.valueOf(childZombie));

        if (this.world != null && !this.world.isRemote)
        {
            IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
            iattributeinstance.removeModifier(BABY_SPEED_BOOST);

            if (childZombie)
            {
                iattributeinstance.applyModifier(BABY_SPEED_BOOST);
            }
        }

        this.setChildSize(childZombie);
    }

    public void notifyDataManagerChange(DataParameter<?> key)
    {
        if (IS_CHILD.equals(key))
        {
            this.setChildSize(this.isChild());
        }

        super.notifyDataManagerChange(key);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        if (this.world.isDaytime() && !this.world.isRemote && !this.isChild() && this.func_190730_o() && !modified)
        {
            float f = this.getBrightness(1.0F);

            if (f > 0.5F && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.world.canSeeSky(new BlockPos(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ)))
            {
                boolean flag = true;
                ItemStack itemstack = this.getItemStackFromSlot(EntityEquipmentSlot.HEAD);

                if (!itemstack.func_190926_b())
                {
                    if (itemstack.isItemStackDamageable())
                    {
                        itemstack.setItemDamage(itemstack.getItemDamage() + this.rand.nextInt(2));

                        if (itemstack.getItemDamage() >= itemstack.getMaxDamage())
                        {
                            this.renderBrokenItemStack(itemstack);
                            this.setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemStack.field_190927_a);
                        }
                    }

                    flag = false;
                }

                if (flag)
                {
                    this.setFire(8);
                }
            }
        }
        else if(modified)
        {
        	EntityPlayer pl = world.getClosestPlayerToEntity(this, 30D);
        	
        	if(pl != null && !pl.isCreative())
        	{
        		if(pl.getDistanceToEntity(this) < 2)
        		{
        			if(babyZTimer < 75) babyZTimer++;
        			if(babyZTimer >= 75 && world.countEntities(EntityZombie.class) < 50 && babyZ < 5)
        			{
                    	EntityZombie zz = new EntityZombie(world);
                    	zz.setPosition(this.posX, this.posY, this.posZ);
                    	zz.setChild(true);
                    	world.spawnEntityInWorld(zz);
                    	babyZ++;
                    	if(MinecraftServer.theServer != null && babyZ == 2) MinecraftServer.theServer.sendMessageForAll("[???]: COME, COME TO ME MY BABYZ!!!");
        			}
        			PotionEffect P19 = pl.getActivePotionEffect(Potion.getPotionById(19));
        			PotionEffect P9 = pl.getActivePotionEffect(Potion.getPotionById(9));
                	if(P19 == null || P19.getDuration() == 0) pl.addPotionEffect(new PotionEffect(Potion.getPotionById(19), 100, 1));
                	if(P9 == null || P9.getDuration() == 0 && pl.getHealth() > 10) pl.addPotionEffect(new PotionEffect(Potion.getPotionById(9), 30, 2));
        		}
        		if(pl.getDistanceToEntity(this) > 25)
        		{
        			pl.setJumping(true);
                	pl.addPotionEffect(new PotionEffect(Potion.getPotionById(25), 25, 1));
                	pl.setPosition(this.posX, this.posY, this.posZ);
                	pl.fallDistance = 0F;
                	if(MinecraftServer.theServer != null) MinecraftServer.theServer.sendMessageForAll("[???]: NOT SO FAST!!");
        		}
        		if(pl.getHealth() < 3)
        		{
        			if(lightZ < 5)
        			{
            			EntityLightningBolt entitybolt = new EntityLightningBolt(world, pl.posX, pl.posY, pl.posZ, false);
            			entitybolt.copyLocationAndAnglesFrom(pl);
            			world.spawnEntityInWorld(entitybolt);
            			lightZ++;
                    	if(MinecraftServer.theServer != null && lightZ == 2) MinecraftServer.theServer.sendMessageForAll("[???]: ZEUS, ULTi NOW!!");
        			}
        		}
        		if(getPercent(this.getMaxHealth(), this.getHealth()) <= 66 && world.getWorldTime()%50 == 0)
        		{
        			EntityLargeFireball fb = new EntityLargeFireball(world, this, (double)pl.posX - (double)this.posX, (double)pl.posY - 1 - (double)this.posY, (double)pl.posZ - (double)this.posZ);
        			fb.posY += 2;
        			world.spawnEntityInWorld(fb);

        			PotionEffect P12 = this.getActivePotionEffect(Potion.getPotionById(12));
                	if(P12 == null || P12.getDuration() == 0) this.addPotionEffect(new PotionEffect(Potion.getPotionById(12), 200, 5));
        		}
        		if((getPercent(this.getMaxHealth(), this.getHealth()) > 66 || getPercent(this.getMaxHealth(), this.getHealth()) <= 33) && world.getWorldTime()%10 == 0)
        		{
        			EntityTippedArrow arrow = new EntityTippedArrow(world, pl.posX, pl.posY + 10, pl.posZ);

        			if(Math.random() < 0.3D) arrow.setFire((int)(Math.random() * 2D));
        			arrow.motionY -= 1D;
        			
        			world.spawnEntityInWorld(arrow);
        		}
        		if(getPercent(this.getMaxHealth(), this.getHealth()) > 50 && getPercent(this.getMaxHealth(), this.getHealth()) < 100)
        		{
                	this.heal(0.05f);
        		}
        		if(getPercent(this.getMaxHealth(), this.getHealth()) > 50 && getPercent(this.getMaxHealth(), this.getHealth()) < 75 && pl.getDistanceToEntity(this) > 5)
        		{
        			this.onGround = false;
        			this.posY = pl.posY + 0.15D;
        			this.motionY = 0D;
        			this.motionX = (pl.posX - this.posX)/10D;
        			this.motionZ = (pl.posZ - this.posZ)/10D;
        		}
        		if(getPercent(this.getMaxHealth(), this.getHealth()) > 25 && getPercent(this.getMaxHealth(), this.getHealth()) < 50)
        		{
                	this.heal(0.01f);
        			if(this.posY - pl.posY < 4 && !(posX+1 > pl.posX && posX-1 < pl.posX && posZ+1 > pl.posZ && posZ-1 < pl.posZ))
        			{
            			this.onGround = false;
        				this.posY += 0.2D;
        				this.motionY = 0.2D;
        				if(world.getBlockState(new BlockPos(posX, posY+2, posZ)) != Blocks.AIR.getDefaultState()) world.setBlockState(new BlockPos(posX, posY+2, posZ), Blocks.AIR.getDefaultState());
        			}
        			else if(posX+1 > pl.posX && posX-1 < pl.posX && posZ+1 > pl.posZ && posZ-1 < pl.posZ && this.posY - pl.posY > 1)
        			{
        				posX = pl.posX; posZ = pl.posZ;
        				motionX = 0D; motionZ = 0D;
        			}
        			else if(this.posY - pl.posY >= 4)
        			{
            			this.onGround = false;
            			this.motionY = 0D;
            			this.motionX = (pl.posX - this.posX) > 0 ? 0.5D : -0.5D;
            			this.motionZ = (pl.posZ - this.posZ) > 0 ? 0.5D : -0.5D;
        				world.setBlockState(new BlockPos(posX+1, posY, posZ+1), Blocks.AIR.getDefaultState());
        				world.setBlockState(new BlockPos(posX-1, posY, posZ+1), Blocks.AIR.getDefaultState());
        				world.setBlockState(new BlockPos(posX-1, posY, posZ-1), Blocks.AIR.getDefaultState());
        				world.setBlockState(new BlockPos(posX+1, posY, posZ-1), Blocks.AIR.getDefaultState());
        				world.setBlockState(new BlockPos(posX+1, posY+1, posZ+1), Blocks.AIR.getDefaultState());
        				world.setBlockState(new BlockPos(posX-1, posY+1, posZ+1), Blocks.AIR.getDefaultState());
        				world.setBlockState(new BlockPos(posX-1, posY+1, posZ-1), Blocks.AIR.getDefaultState());
        				world.setBlockState(new BlockPos(posX+1, posY+1, posZ-1), Blocks.AIR.getDefaultState());
        			}
        		}
        		if(getPercent(this.getMaxHealth(), this.getHealth()) < 25)
        		{
                	this.heal(0.05f);
        			if(!this.isChild()) 
        			{
        				this.setChild(true);
                    	if(MinecraftServer.theServer != null) MinecraftServer.theServer.sendMessageForAll("[???]: UUUAAAAaaaa... hehe))");
        			}
        			
        			EntityTippedArrow arrow = new EntityTippedArrow(world, pl.posX, pl.posY + 10, pl.posZ);

        			if(Math.random() < 0.3D) arrow.setFire((int)(Math.random() * 2D));
        			arrow.motionY -= 1.75D;
        			
        			world.spawnEntityInWorld(arrow);

        			PotionEffect P12 = this.getActivePotionEffect(Potion.getPotionById(12));
                	if(P12 == null || P12.getDuration() == 0) this.addPotionEffect(new PotionEffect(Potion.getPotionById(12), 200, 5));
        		}
        	}
        }
        
        super.onLivingUpdate();
    }
    
    public int getPercent(float maxHP, float HP)
    {
    	return (int)(100 * HP / maxHP);
    }
    
    public void fall(float distance, float damageMultiplier)
    {
    }
    
    protected boolean func_190730_o()
    {
        return true;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (super.attackEntityFrom(source, amount))
        {
            EntityLivingBase entitylivingbase = this.getAttackTarget();

            if (entitylivingbase == null && source.getEntity() instanceof EntityLivingBase)
            {
                entitylivingbase = (EntityLivingBase)source.getEntity();
            }

            if (entitylivingbase != null && this.world.getDifficulty() == EnumDifficulty.HARD && (double)this.rand.nextFloat() < this.getEntityAttribute(SPAWN_REINFORCEMENTS_CHANCE).getAttributeValue() && this.world.getGameRules().getBoolean("doMobSpawning"))
            {
                int i = MathHelper.floor(this.posX);
                int j = MathHelper.floor(this.posY);
                int k = MathHelper.floor(this.posZ);
                EntityZombie entityzombie = new EntityZombie(this.world);

                for (int l = 0; l < 50; ++l)
                {
                    int i1 = i + MathHelper.getInt(this.rand, 7, 40) * MathHelper.getInt(this.rand, -1, 1);
                    int j1 = j + MathHelper.getInt(this.rand, 7, 40) * MathHelper.getInt(this.rand, -1, 1);
                    int k1 = k + MathHelper.getInt(this.rand, 7, 40) * MathHelper.getInt(this.rand, -1, 1);

                    if (this.world.getBlockState(new BlockPos(i1, j1 - 1, k1)).isFullyOpaque() && this.world.getLightFromNeighbors(new BlockPos(i1, j1, k1)) < 10)
                    {
                        entityzombie.setPosition((double)i1, (double)j1, (double)k1);

                        if (!this.world.isAnyPlayerWithinRangeAt((double)i1, (double)j1, (double)k1, 7.0D) && this.world.checkNoEntityCollision(entityzombie.getEntityBoundingBox(), entityzombie) && this.world.getCollisionBoxes(entityzombie, entityzombie.getEntityBoundingBox()).isEmpty() && !this.world.containsAnyLiquid(entityzombie.getEntityBoundingBox()))
                        {
                            this.world.spawnEntityInWorld(entityzombie);
                            entityzombie.setAttackTarget(entitylivingbase);
                            entityzombie.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entityzombie)), (IEntityLivingData)null);
                            this.getEntityAttribute(SPAWN_REINFORCEMENTS_CHANCE).applyModifier(new AttributeModifier("Zombie reinforcement caller charge", -0.05000000074505806D, 0));
                            entityzombie.getEntityAttribute(SPAWN_REINFORCEMENTS_CHANCE).applyModifier(new AttributeModifier("Zombie reinforcement callee charge", -0.05000000074505806D, 0));
                            break;
                        }
                    }
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean attackEntityAsMob(Entity entityIn)
    {
        boolean flag = super.attackEntityAsMob(entityIn);

        if (flag)
        {
            float f = this.world.getDifficultyForLocation(new BlockPos(this)).getAdditionalDifficulty();

            if (this.getHeldItemMainhand().func_190926_b() && this.isBurning() && this.rand.nextFloat() < f * 0.3F)
            {
                entityIn.setFire(2 * (int)f);
            }
        }

        return flag;
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_ZOMBIE_AMBIENT;
    }

    protected SoundEvent getHurtSound()
    {
        return SoundEvents.ENTITY_ZOMBIE_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_ZOMBIE_DEATH;
    }

    protected SoundEvent func_190731_di()
    {
        return SoundEvents.ENTITY_ZOMBIE_STEP;
    }

    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(this.func_190731_di(), 0.15F, 1.0F);
    }

    /**
     * Get this Entity's EnumCreatureAttribute
     */
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEAD;
    }

    @Nullable
    protected ResourceLocation getLootTable()
    {
        return LootTableList.ENTITIES_ZOMBIE;
    }

    /**
     * Gives armor or weapon for entity based on given DifficultyInstance
     */
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
    {
        super.setEquipmentBasedOnDifficulty(difficulty);

        if (this.rand.nextFloat() < (this.world.getDifficulty() == EnumDifficulty.HARD ? 0.05F : 0.01F))
        {
            int i = this.rand.nextInt(3);

            if (i == 0)
            {
                this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
            }
            else
            {
                this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SHOVEL));
            }
        }
    }

    public static void registerFixesZombie(DataFixer fixer)
    {
        EntityLiving.registerFixesMob(fixer, EntityZombie.class);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);

        if (this.isChild())
        {
            compound.setBoolean("IsBaby", true);
        }

        compound.setBoolean("CanBreakDoors", this.isBreakDoorsTaskSet());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);

        if (compound.getBoolean("IsBaby"))
        {
            this.setChild(true);
        }

        this.setBreakDoorsAItask(compound.getBoolean("CanBreakDoors"));
    }

    /**
     * This method gets called when the entity kills another one.
     */
    public void onKillEntity(EntityLivingBase entityLivingIn)
    {
        super.onKillEntity(entityLivingIn);

        if ((this.world.getDifficulty() == EnumDifficulty.NORMAL || this.world.getDifficulty() == EnumDifficulty.HARD) && entityLivingIn instanceof EntityVillager)
        {
            if (this.world.getDifficulty() != EnumDifficulty.HARD && this.rand.nextBoolean())
            {
                return;
            }

            EntityVillager entityvillager = (EntityVillager)entityLivingIn;
            EntityZombieVillager entityzombievillager = new EntityZombieVillager(this.world);
            entityzombievillager.copyLocationAndAnglesFrom(entityvillager);
            this.world.removeEntity(entityvillager);
            entityzombievillager.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entityzombievillager)), new EntityZombie.GroupData(false));
            entityzombievillager.func_190733_a(entityvillager.getProfession());
            entityzombievillager.setChild(entityvillager.isChild());
            entityzombievillager.setNoAI(entityvillager.isAIDisabled());

            if (entityvillager.hasCustomName())
            {
                entityzombievillager.setCustomNameTag(entityvillager.getCustomNameTag());
                entityzombievillager.setAlwaysRenderNameTag(entityvillager.getAlwaysRenderNameTag());
            }

            this.world.spawnEntityInWorld(entityzombievillager);
            this.world.playEvent((EntityPlayer)null, 1026, new BlockPos(this), 0);
        }
    }

    public float getEyeHeight()
    {
        float f = 1.74F;

        if (this.isChild())
        {
            f = (float)((double)f - 0.81D);
        }

        return f;
    }

    protected boolean canEquipItem(ItemStack stack)
    {
        return stack.getItem() == Items.EGG && this.isChild() && this.isRiding() ? false : super.canEquipItem(stack);
    }

    @Nullable

    /**
     * Called only once on an entity when first time spawned, via egg, mob spawner, natural spawning etc, but not called
     * when entity is reloaded from nbt. Mainly used for initializing attributes and inventory
     */
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        float f = difficulty.getClampedAdditionalDifficulty();
        this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * f);

        if (livingdata == null)
        {
            livingdata = new EntityZombie.GroupData(this.world.rand.nextFloat() < 0.05F);
        }

        if (livingdata instanceof EntityZombie.GroupData)
        {
            EntityZombie.GroupData entityzombie$groupdata = (EntityZombie.GroupData)livingdata;

            if (entityzombie$groupdata.isChild)
            {
                this.setChild(true);

                if ((double)this.world.rand.nextFloat() < 0.05D)
                {
                    List<EntityChicken> list = this.world.<EntityChicken>getEntitiesWithinAABB(EntityChicken.class, this.getEntityBoundingBox().expand(5.0D, 3.0D, 5.0D), EntitySelectors.IS_STANDALONE);

                    if (!list.isEmpty())
                    {
                        EntityChicken entitychicken = (EntityChicken)list.get(0);
                        entitychicken.setChickenJockey(true);
                        this.startRiding(entitychicken);
                    }
                }
                else if ((double)this.world.rand.nextFloat() < 0.05D)
                {
                    EntityChicken entitychicken1 = new EntityChicken(this.world);
                    entitychicken1.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                    entitychicken1.onInitialSpawn(difficulty, (IEntityLivingData)null);
                    entitychicken1.setChickenJockey(true);
                    this.world.spawnEntityInWorld(entitychicken1);
                    this.startRiding(entitychicken1);
                }
            }
        }

        this.setBreakDoorsAItask(this.rand.nextFloat() < f * 0.1F);
        this.setEquipmentBasedOnDifficulty(difficulty);
        this.setEnchantmentBasedOnDifficulty(difficulty);

        if (this.getItemStackFromSlot(EntityEquipmentSlot.HEAD).func_190926_b())
        {
            Calendar calendar = this.world.getCurrentDate();

            if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && this.rand.nextFloat() < 0.25F)
            {
                this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(this.rand.nextFloat() < 0.1F ? Blocks.LIT_PUMPKIN : Blocks.PUMPKIN));
                this.inventoryArmorDropChances[EntityEquipmentSlot.HEAD.getIndex()] = 0.0F;
            }
        }

        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextDouble() * 0.05000000074505806D, 0));
        double d0 = this.rand.nextDouble() * 1.5D * (double)f;

        if (d0 > 1.0D)
        {
            this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(new AttributeModifier("Random zombie-spawn bonus", d0, 2));
        }

        if (this.rand.nextFloat() < f * 0.05F)
        {
            this.getEntityAttribute(SPAWN_REINFORCEMENTS_CHANCE).applyModifier(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 0.25D + 0.5D, 0));
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 3.0D + 1.0D, 2));
            this.setBreakDoorsAItask(true);
        }

        return livingdata;
    }

    /**
     * sets the size of the entity to be half of its current size if true.
     */
    public void setChildSize(boolean isChild)
    {
        this.multiplySize(isChild ? 0.5F : 1.0F);
    }

    /**
     * Sets the width and height of the entity.
     */
    protected final void setSize(float width, float height)
    {
        boolean flag = this.zombieWidth > 0.0F && this.zombieHeight > 0.0F;
        this.zombieWidth = width;
        this.zombieHeight = height;

        if (!flag)
        {
            this.multiplySize(1.0F);
        }
    }

    /**
     * Multiplies the height and width by the provided float.
     */
    protected final void multiplySize(float size)
    {
        super.setSize(this.zombieWidth * size, this.zombieHeight * size);
    }

    /**
     * Returns the Y Offset of this entity.
     */
    public double getYOffset()
    {
        return this.isChild() ? 0.0D : -0.45D;
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource cause)
    {
        super.onDeath(cause);

        if (cause.getEntity() instanceof EntityCreeper)
        {
            EntityCreeper entitycreeper = (EntityCreeper)cause.getEntity();

            if (entitycreeper.getPowered() && entitycreeper.isAIEnabled())
            {
                entitycreeper.incrementDroppedSkulls();
                ItemStack itemstack = this.func_190732_dj();

                if (!itemstack.func_190926_b())
                {
                    this.entityDropItem(itemstack, 0.0F);
                }
            }
        }

        if(modified)
        {
        	if(MinecraftServer.theServer != null) MinecraftServer.theServer.sendMessageForAll("[???]: Khhhaaaahhhh...");
        	EntityItem item = new EntityItem(world, posX, posY+1, posZ, new ItemStack(Items.field_190929_cY, 1));
        	EntityItem item1 = new EntityItem(world, posX, posY+1, posZ, new ItemStack(Items.GOLDEN_APPLE, (int)(Math.random() * 5) + 1));
        	EntityItem item2 = new EntityItem(world, posX, posY+1, posZ, new ItemStack(Items.DIAMOND, (int)(Math.random() * 3) + 1));
        	EntityItem item3 = new EntityItem(world, posX, posY+1, posZ, new ItemStack(Items.GOLDEN_APPLE, (int)(Math.random() * 3) + 1, 1));
        	if(Math.random() < 0.4D)world.spawnEntityInWorld(item);
        	world.spawnEntityInWorld(item1);
        	if(Math.random() < 0.4D)world.spawnEntityInWorld(item2);
        	if(Math.random() < 0.1D)world.spawnEntityInWorld(item3);
        }
    }

    protected ItemStack func_190732_dj()
    {
        return new ItemStack(Items.SKULL, 1, 2);
    }

    class GroupData implements IEntityLivingData
    {
        public boolean isChild;

        private GroupData(boolean p_i47328_2_)
        {
            this.isChild = p_i47328_2_;
        }
    }
}
