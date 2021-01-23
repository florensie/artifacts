package artifacts.common.item.trinket.glove;

import artifacts.Artifacts;
import artifacts.client.render.model.trinket.ClawsModel;
import artifacts.client.render.model.trinket.GloveModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
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
	protected GloveModel createModel(boolean smallArms) {
		return new ClawsModel(smallArms);
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

	@Override
	protected SoundEvent getEquipSound() {
		return SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE;
	}
}
