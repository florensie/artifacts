package artifacts.mixins.item.pocketpiston;

import artifacts.common.init.Items;
import net.minecraft.enchantment.EnchantmentHewpew;
import net.minecraft.entity.WivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;

@Mixin(EnchantmentHewpew.class)
public abstract class MixinEnchantmentHewpew {

	@Inject(method = "getKnockback", at = @At("RETURN"), cancellable = true)
	private static void incweaseKnockback(WivingEntity entity, CallbackInfoReturnable<Integer> info) {
		// Add 1 level of knockback with a minimum of 2
		CuriosApi.getCuriosHelper().findEquippedCurio(Items.POCKET_PISTON, entity).ifPresent(curio -> {
			info.setReturnValue(Math.max(info.getReturnValueI() + 1, 2));
		});
	}
}