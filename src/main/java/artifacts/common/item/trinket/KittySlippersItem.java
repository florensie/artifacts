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
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class KittySlippersItem extends TrinketArtifactItem {

	private static final Identifier TEXTURE = new Identifier(Artifacts.MODID, "textures/entity/trinket/kitty_slippers.png");
	private Object model;

	public KittySlippersItem() {
		super(new Item.Settings());
		PlayHurtSoundCallback.EVENT.register(KittySlippersItem::onPlayHurtSound);
	}

	private static void onPlayHurtSound(LivingEntity entity, float volume, float pitch) {
		if (TrinketsHelper.isEquipped(Items.KITTY_SLIPPERS, entity)) {
			entity.playSound(SoundEvents.ENTITY_CAT_HURT, volume, pitch);
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	protected KittySlippersModel getModel() {
		if (model == null) {
			model = new KittySlippersModel();
		}
		return (KittySlippersModel) model;
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
