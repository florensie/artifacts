package artifacts.mixin.mixins.client.render;

import artifacts.Artifacts;
import artifacts.item.trinket.glove.GloveItem;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin {

	@Inject(method = "renderLeftArm", at = @At("TAIL"))
	private void renderLeftGlove(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, CallbackInfo info) {
		GloveItem equippedGlove = getGloveToRenderForHand(player, Hand.OFF_HAND);
		if (equippedGlove != null) {
			equippedGlove.renderArm(matrices, vertexConsumers, light, player, Arm.LEFT, false);
		}
	}

	@Inject(method = "renderRightArm", at = @At("TAIL"))
	private void renderRightGlove(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, CallbackInfo info) {
		GloveItem equippedGlove = getGloveToRenderForHand(player, Hand.MAIN_HAND);
		if (equippedGlove != null) {
			equippedGlove.renderArm(matrices, vertexConsumers, light, player, Arm.RIGHT, false);
		}
	}

	@Unique
	private static GloveItem getGloveToRenderForHand(PlayerEntity player, Hand hand) {
		if (!Artifacts.CONFIG.general.showFirstPersonGloves) {
			return null;
		}

		String slotGroup = hand == Hand.MAIN_HAND ? "hand" : "offhand";
		Optional<TrinketComponent> maybeComponent = TrinketsApi.getTrinketComponent(player);

		if (maybeComponent.isPresent()) {
			ItemStack item = maybeComponent.get().getInventory().get(slotGroup).get("glove").getStack(0);

			if (item.getItem() instanceof GloveItem) {
				return (GloveItem) item.getItem();
			}
		}
		return null;
	}
}
