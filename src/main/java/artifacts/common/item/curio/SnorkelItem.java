package artifacts.common.item.curio;

import artifacts.Artifacts;
import artifacts.client.render.model.curio.SnorkelModel;
import artifacts.common.item.Curio;
import artifacts.common.item.RenderableCurio;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import top.theillusivec4.curios.api.type.component.ICurio;
import top.theillusivec4.curios.api.type.component.IRenderableCurio;

public class SnorkelItem extends CurioArtifactItem {

	private static final Identifier TEXTURE = new Identifier(Artifacts.MODID, "textures/entity/curio/snorkel.png");

	public SnorkelItem() {
		super(new Item.Settings());
	}

	@Override
	public ICurio attachCurio(ItemStack stack) {
		return new Curio(this) {
			@Override
			public StatusEffectInstance getPermanentEffect() {
				return new StatusEffectInstance(StatusEffects.WATER_BREATHING, 20, 0, true, false);
			}
		};
	}

	@Override
	public IRenderableCurio attachRenderableCurio(ItemStack stack) {
		return new RenderableCurio() {
			private Object model;

			@Override
			@Environment(EnvType.CLIENT)
			protected SnorkelModel getModel() {
				if (model == null) {
					model = new SnorkelModel();
				}
				return (SnorkelModel) model;
			}

			@Override
			@Environment(EnvType.CLIENT)
			protected Identifier getTexture() {
				return TEXTURE;
			}
		};
	}
}