package artifacts.mixin.mixins.item.everlastingfood;

import artifacts.Artifacts;
import artifacts.item.EverlastingFoodItem;
import artifacts.mixin.extensions.AnimalEntityExtensions;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnimalEntity.class)
public abstract class AnimalEntityMixin implements AnimalEntityExtensions {

	@Shadow
	public abstract boolean isBreedingItem(ItemStack stack);

	@Redirect(method = "interactMob", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/AnimalEntity;isBreedingItem(Lnet/minecraft/item/ItemStack;)Z"))
	private boolean cooldownBreedingItem(AnimalEntity animalEntity, ItemStack stack, PlayerEntity player) {
		return this.artifacts$isBreedingItemWithCooldown(stack, player);
	}

	@Inject(method = "eat", at = @At("HEAD"), cancellable = true)
	private void cancelEat(PlayerEntity player, Hand hand, ItemStack stack, CallbackInfo info) {
		Item item = stack.getItem();

		if (item instanceof EverlastingFoodItem) {
			player.getItemCooldownManager().set(item, Artifacts.CONFIG.general.everlastingFoodCooldown);
			info.cancel();
		}
	}

	@Override
	@Unique
	public boolean artifacts$isBreedingItemWithCooldown(ItemStack stack, PlayerEntity player) {
		Item item = stack.getItem();
		boolean original = this.isBreedingItem(stack);

		if (original && item instanceof EverlastingFoodItem) {
			return !player.getItemCooldownManager().isCoolingDown(item);
		}

		return original;
	}
}
