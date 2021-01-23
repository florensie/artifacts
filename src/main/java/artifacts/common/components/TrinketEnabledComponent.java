package artifacts.common.components;

import artifacts.common.trinkets.TrinketsHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

public class TrinketEnabledComponent extends BooleanComponent {

	private final ItemStack stack;

	public TrinketEnabledComponent(ItemStack stack) {
		super("isEnabled");
		this.stack = stack;
		this.set(true);
	}

	@Override
	public boolean shouldSyncWith(ServerPlayerEntity player) {
		// Only sync if stack is in inventory or trinkets inventory
		return player.inventory.contains(stack)
				|| TrinketsHelper.isEquipped((ItemStack equippedStack) -> equippedStack.equals(stack), player);
	}
}
