package artifacts.common.item.trinket;

import artifacts.Artifacts;
import artifacts.client.render.model.trinket.KittySlippersModel;
import artifacts.common.events.PlayHurtSoundCallback;
import artifacts.common.init.Items;
import artifacts.common.trinkets.Slots;
import artifacts.common.trinkets.TrinketsHelper;
import dev.emi.trinkets.api.SlotGroups;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class KittySlippersItem extends TrinketArtifactItem {

	private static final Identifier TEXTURE = new Identifier(Artifacts.MODID, "textures/entity/trinket/kitty_slippers.png");

	public KittySlippersItem() {
		super(new Item.Settings());
	}

	@Override
	protected SoundEvent getExtraHurtSound() {
		return SoundEvents.ENTITY_CAT_HURT;
	}

	@Override
	@Environment(EnvType.CLIENT)
	protected BipedEntityModel<LivingEntity> createModel() {
		return new KittySlippersModel();
	}

	@Override
	@Environment(EnvType.CLIENT)
	protected Identifier getTexture() {
		return TEXTURE;
	}

	@Override
	public boolean canWearInSlot(String group, String slot) {
		return group.equals(SlotGroups.FEET) && slot.equals(Slots.SHOES);
	}

	@Override
	protected SoundEvent getEquipSound() {
		return SoundEvents.ENTITY_CAT_AMBIENT;
	}
}
