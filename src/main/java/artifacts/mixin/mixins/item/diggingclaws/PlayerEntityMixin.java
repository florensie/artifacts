package artifacts.mixin.mixins.item.diggingclaws;

import artifacts.init.Items;
import artifacts.item.trinket.glove.DiggingClawsItem;
import artifacts.trinkets.TrinketsHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

	@Shadow
	@Final
	public PlayerInventory inventory;

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	/**
	 * Sets the holder on the itemstack to check
	 */
	@Inject(method = "canHarvest", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isSuitableFor(Lnet/minecraft/block/BlockState;)Z"))
	private void setItemStackHolder(BlockState block, CallbackInfoReturnable<Boolean> info) {
		this.inventory.getMainHandStack().setHolder(this);
	}

	/**
	 * This is identical to the forge version but might not be ideal
	 * It adds the speed after the vanilla method does all the calculations on the base modifier such as haste and underwater
	 */
	// TODO: identical artifacts-forge behaviour but could do this on the speed mutliplier instead of end result
	@Inject(method = "getBlockBreakingSpeed", at = @At("RETURN"), cancellable = true)
	private void increaseMiningSpeed(BlockState block, CallbackInfoReturnable<Float> info) {
		if (TrinketsHelper.isEquipped(Items.DIGGING_CLAWS, this)) {
			info.setReturnValue(info.getReturnValueF() + DiggingClawsItem.MINING_SPEED_INCREASE);
		}
	}
}
