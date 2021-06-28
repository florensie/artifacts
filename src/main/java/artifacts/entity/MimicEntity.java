package artifacts.entity;

import artifacts.init.LootTables;
import artifacts.init.SoundEvents;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

import java.util.EnumSet;

public class MimicEntity extends MobEntity implements Monster {

	public int ticksInAir;
	public int attackCooldown;
	public boolean isDormant;

	public MimicEntity(EntityType<? extends MimicEntity> type, World world) {
		super(type, world);
		moveControl = new MimicMovementController(this);
		experiencePoints = 10;
	}

	public static DefaultAttributeContainer.Builder createMobAttributes() {
		return MobEntity.createMobAttributes()
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 60)
				.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16)
				.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.8)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.8)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5);
	}

	@Override
	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, NbtCompound entityTag) {
		if (getMoveControl() instanceof MimicMovementController) {
			((MimicMovementController) moveControl).setDirection(random.nextInt(4) * 90, false);
		}
		return super.initialize(world, difficulty, spawnReason, entityData, entityTag);
	}

	public SoundCategory getSoundCategory() {
		return SoundCategory.HOSTILE;
	}

	@Override
	public boolean canImmediatelyDespawn(double distance) {
		return false;
	}

	@Override
	protected void initGoals() {
		super.initGoals();
		goalSelector.add(1, new FloatGoal(this));
		goalSelector.add(2, new AttackGoal(this));
		goalSelector.add(3, new FaceRandomGoal(this));
		goalSelector.add(5, new HopGoal(this));
		//noinspection ConstantConditions
		targetSelector.add(1, new FollowTargetGoal<>(this, PlayerEntity.class, 1, true, false, (entity) -> !isDormant || distanceTo(entity) < getAttributeInstance(EntityAttributes.GENERIC_FOLLOW_RANGE).getValue() / 2.5));
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound compound) {
		super.writeCustomDataToNbt(compound);
		compound.putInt("ticksInAir", ticksInAir);
		compound.putBoolean("isDormant", isDormant);
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound compound) {
		super.readCustomDataFromNbt(compound);
		ticksInAir = compound.getInt("ticksInAir");
		isDormant = compound.getBoolean("isDormant");
	}

	@Override
	public void tick() {
		super.tick();

		if (isTouchingWater()) {
			ticksInAir = 0;
			if (isDormant) {
				isDormant = false;
			}
		} else if (!onGround) {
			ticksInAir++;
		} else if (ticksInAir > 0) {
			playSound(getLandingSound(), getSoundVolume(), getSoundPitch());
			ticksInAir = 0;
		}

		if (attackCooldown > 0) {
			attackCooldown--;
		}
	}

	@Override
	public void onPlayerCollision(PlayerEntity player) {
		super.onPlayerCollision(player);
		// noinspection ConstantConditions
		if (attackCooldown <= 0 && player.getEntityWorld().getDifficulty() != Difficulty.PEACEFUL && canSee(player)
				&& squaredDistanceTo(player.getBoundingBox().getCenter().subtract(0, getBoundingBox().getYLength() / 2, 0)) < 1
				&& player.damage(DamageSource.mob(this), (float) getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).getValue())) {
			attackCooldown = 20;
			applyDamageEffects(this, player);
		}
	}

	@Override
	public void setTarget(LivingEntity entity) {
		isDormant = false;
		super.setTarget(entity);
	}

	@Override
	public boolean damage(DamageSource source, float amount) {
		if (source.getAttacker() instanceof PlayerEntity) {
			setTarget((LivingEntity) source.getAttacker());
		}

		if (ticksInAir <= 0 && source.isProjectile() && !source.isUnblockable()) {
			playSound(SoundEvents.MIMIC_HURT, getSoundVolume(), getSoundPitch());
			return false;
		}

		if (isOnGround() && getRandom().nextBoolean() && getMoveControl() instanceof MimicMovementController) {
			((MimicMovementController) getMoveControl()).setDirection(getRandom().nextInt(4) * 90, true);
		}

		return super.damage(source, amount);
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource) {
		return SoundEvents.MIMIC_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.MIMIC_DEATH;
	}

	protected SoundEvent getJumpingSound() {
		return SoundEvents.MIMIC_OPEN;
	}

	protected SoundEvent getLandingSound() {
		return SoundEvents.MIMIC_CLOSE;
	}

	@Override
	protected Identifier getLootTableId() {
		return LootTables.MIMIC;
	}

	public void setDormant() {
		isDormant = true;
	}

	protected static class AttackGoal extends Goal {

		private final MimicEntity mimic;
		private int timeRemaining;

		public AttackGoal(MimicEntity mimic) {
			this.mimic = mimic;
			setControls(EnumSet.of(Goal.Control.LOOK));
		}

		@Override
		public boolean canStart() {
			LivingEntity target = mimic.getTarget();

			return target instanceof PlayerEntity
					&& target.isAlive()
					&& target.getEntityWorld().getDifficulty() != Difficulty.PEACEFUL
					&& !((PlayerEntity) target).getAbilities().invulnerable;
		}

		@Override
		public void start() {
			timeRemaining = 300;
			super.start();
		}

		@Override
		public boolean shouldContinue() {
			LivingEntity target = mimic.getTarget();

			return target instanceof PlayerEntity
					&& target.isAlive()
					&& target.getEntityWorld().getDifficulty() != Difficulty.PEACEFUL
					&& !((PlayerEntity) target).getAbilities().invulnerable
					&& --timeRemaining > 0;
		}

		@Override
		public void tick() {
			super.tick();
			if (mimic.getTarget() != null && mimic.getMoveControl() instanceof MimicMovementController) {
				mimic.lookAtEntity(mimic.getTarget(), 10, 10);
				((MimicMovementController) mimic.getMoveControl()).setDirection(mimic.getYaw(), true);
			}
		}
	}

	protected static class FaceRandomGoal extends Goal {

		private final MimicEntity mimic;
		private int chosenDegrees;
		private int nextRandomizeTime;

		public FaceRandomGoal(MimicEntity mimic) {
			this.mimic = mimic;
			setControls(EnumSet.of(Goal.Control.LOOK));
		}

		@Override
		public boolean canStart() {
			return mimic.getTarget() == null && (mimic.onGround || mimic.isTouchingWater() || mimic.isInLava() || mimic.hasStatusEffect(StatusEffects.LEVITATION));
		}

		@Override
		public void tick() {
			if (--nextRandomizeTime <= 0) {
				nextRandomizeTime = 480 + mimic.getRandom().nextInt(320);
				if (mimic.isDormant) {
					chosenDegrees = Math.round(mimic.getYaw() / 90) * 90;
				} else {
					chosenDegrees = mimic.getRandom().nextInt(4) * 90;
				}
			}
			if (mimic.getMoveControl() instanceof MimicMovementController) {
				((MimicMovementController) mimic.getMoveControl()).setDirection(chosenDegrees, false);
			}
		}
	}

	protected static class FloatGoal extends Goal {

		private final MimicEntity mimic;

		public FloatGoal(MimicEntity mimic) {
			this.mimic = mimic;
			setControls(EnumSet.of(Goal.Control.JUMP, Goal.Control.MOVE));
			mimic.getNavigation().setCanSwim(true);
		}

		@Override
		public boolean canStart() {
			return mimic.isTouchingWater() || mimic.isInLava();
		}

		@Override
		public void tick() {
			if (mimic.getRandom().nextFloat() < 0.8F) {
				mimic.jumpControl.setActive();
			}
			if (mimic.getMoveControl() instanceof MimicMovementController) {
				((MimicMovementController) mimic.getMoveControl()).setSpeed(1.2);
			}
		}
	}

	protected static class HopGoal extends Goal {

		private final MimicEntity mimic;

		public HopGoal(MimicEntity mimic) {
			this.mimic = mimic;
			setControls(EnumSet.of(Goal.Control.JUMP, Goal.Control.MOVE));
		}

		@Override
		public boolean canStart() {
			return !mimic.isDormant && !mimic.hasVehicle();
		}

		@Override
		public void tick() {
			if (mimic.getMoveControl() instanceof MimicMovementController) {
				((MimicMovementController) mimic.getMoveControl()).setSpeed(1);
			}
		}
	}

	protected static class MimicMovementController extends MoveControl {

		private final MimicEntity mimic;
		private float rotationDegrees;
		private int jumpDelay;

		public MimicMovementController(MimicEntity mimic) {
			super(mimic);
			this.mimic = mimic;
			rotationDegrees = 180 * mimic.getYaw() / (float) Math.PI;
			jumpDelay = mimic.random.nextInt(320) + 640;
		}

		public void setDirection(float rotation, boolean shouldJump) {
			this.rotationDegrees = rotation;
			if (shouldJump && jumpDelay > 10) {
				jumpDelay = 10;
			}
		}

		public void setSpeed(double speed) {
			this.speed = speed;
			state = State.MOVE_TO;
		}

		@Override
		public void tick() {
			float newYaw = wrapDegrees(mimic.getYaw(), rotationDegrees, 90);
			mimic.headYaw = mimic.bodyYaw = newYaw;
			mimic.setYaw(newYaw);

			if (state != State.MOVE_TO) {
				mimic.setForwardSpeed(0);
			} else {
				state = State.WAIT;
				if (mimic.onGround) {
					// noinspection ConstantConditions
					mimic.setMovementSpeed((float) (speed * mimic.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).getValue()));
					if (jumpDelay-- > 0) {
						mimic.sidewaysSpeed = mimic.forwardSpeed = 0;
						mimic.setMovementSpeed(0);
					} else {
						jumpDelay = mimic.random.nextInt(320) + 640;

						mimic.jumpControl.setActive();
						mimic.playSound(mimic.getJumpingSound(), mimic.getSoundVolume(), mimic.getSoundPitch());
					}
				} else {
					// noinspection ConstantConditions
					mimic.setMovementSpeed((float) (speed * mimic.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).getValue()));
				}
			}
		}
	}
}
