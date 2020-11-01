package artifacts.common.item.trinket;

import artifacts.Artifacts;
import artifacts.client.render.model.trinket.ClawsModel;
import dev.emi.trinkets.api.SlotGroups;
import dev.emi.trinkets.api.Slots;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class DiggingClawsItem extends GloveItem {

	public static final int NEW_BASE_MINING_LEVEL = 2;
	public static final float MINING_SPEED_INCREASE = 4;

	private static final Identifier TEXTURE_DEFAULT = new Identifier(Artifacts.MODID, "textures/entity/trinket/digging_claws_default.png");
	private static final Identifier TEXTURE_SLIM = new Identifier(Artifacts.MODID, "textures/entity/trinket/digging_claws_default.png");

	public DiggingClawsItem() {
		super(new Item.Settings());
	}

	@Override
	@Environment(EnvType.CLIENT)
	protected Identifier getTexture() {
		return TEXTURE_DEFAULT;
	}

	@Override
	@Environment(EnvType.CLIENT)
	protected Identifier getSlimTexture() {
		return TEXTURE_SLIM;
	}

	@Environment(EnvType.CLIENT)
	protected ClawsModel getSlimModel() {
		if (modelSlim == null) {
			modelSlim = new ClawsModel(true);
		}
		return (ClawsModel) modelSlim;
	}

	@Override
	@Environment(EnvType.CLIENT)
	protected ClawsModel getModel() {
		if (modelDefault == null) {
			modelDefault = new ClawsModel(false);
		}
		return (ClawsModel) modelDefault;
	}
}