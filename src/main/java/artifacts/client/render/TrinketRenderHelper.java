package artifacts.client.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;

/**
 * Some helper methods for rendering trinket.
 * This code is from <a href="https://github.com/TheIllusiveC4/Curios/blob/1.16.x-fabric/src/main/java/top/theillusivec4/curios/api/type/component/IRenderableCurio.java#L78">Curios api</a>
 */
public final class TrinketRenderHelper {

	/**
	 * Rotates the rendering for the models based on the entity's poses and movements. This will do
	 * nothing if the entity render object does not implement {@link LivingEntityRenderer} or if the
	 * model does not implement {@link BipedEntityModel}).
	 *
	 * @param livingEntity The wearer of the trinket
	 * @param model       The model to align to the body movement
	 */
	public static void followBodyRotations(final LivingEntity livingEntity,
										   final BipedEntityModel<LivingEntity> model) {

		EntityRenderer<? super LivingEntity> render = MinecraftClient.getInstance()
				.getEntityRenderDispatcher().getRenderer(livingEntity);

		if (render instanceof LivingEntityRenderer) {
			//noinspection unchecked
			LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>> livingRenderer =
					(LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>>) render;
			EntityModel<LivingEntity> entityModel = livingRenderer.getModel();

			if (entityModel instanceof BipedEntityModel) {
				BipedEntityModel<LivingEntity> bipedModel = (BipedEntityModel<LivingEntity>) entityModel;
				bipedModel.setAttributes(model);
			}
		}
	}
}
