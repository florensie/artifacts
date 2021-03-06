package artifacts.mixin.mixins.item.umbrella.client;

import artifacts.Artifacts;
import artifacts.init.Items;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

// Priority is higher so we can inject into canvas' renderItem overwrite
// TODO: rewrite this using FabricBakedModel if/when RenderContext gets the transform mode
@Mixin(value = ItemRenderer.class, priority = 1500)
public abstract class ItemRendererMixin {

	@Shadow @Final private ItemModels models;
	@Unique
	private static final ModelIdentifier UMBRELLA_HELD_MODEL = new ModelIdentifier(Artifacts.id("umbrella_in_hand"), "inventory");
	@Unique
	private static final ModelIdentifier UMBRELLA_ICON_MODEL = new ModelIdentifier(Artifacts.id("umbrella"), "inventory");

	@ModifyVariable(method = "getHeldItemModel", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/render/item/ItemModels;getModel(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/client/render/model/BakedModel;"))
	private BakedModel setUmbrellaHeldModel(BakedModel model, ItemStack stack) {
		return stack.getItem() == Items.UMBRELLA ? this.models.getModelManager().getModel(UMBRELLA_HELD_MODEL) : model;
	}

	@ModifyVariable(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
			argsOnly = true, at = @At(value = "HEAD"))
	private BakedModel setUmbrellaIconModel(BakedModel model, ItemStack stack, ModelTransformation.Mode renderMode) {
		boolean shouldUseIcon = renderMode == ModelTransformation.Mode.GUI ||
								renderMode == ModelTransformation.Mode.GROUND ||
								renderMode == ModelTransformation.Mode.FIXED;

		return stack.getItem() == Items.UMBRELLA && shouldUseIcon
				? this.models.getModelManager().getModel(UMBRELLA_ICON_MODEL) : model;
	}
}
