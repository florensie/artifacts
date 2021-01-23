package artifacts.common.item.trinket;

import artifacts.Artifacts;
import artifacts.client.render.model.trinket.PanicNecklaceModel;
import artifacts.common.events.UserHurtCallback;
import artifacts.common.init.Items;
import artifacts.common.trinkets.TrinketsHelper;
import dev.emi.trinkets.api.SlotGroups;
import dev.emi.trinkets.api.Slots;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class PanicNecklaceItem extends TrinketArtifactItem {

	private static final Identifier TEXTURE = new Identifier(Artifacts.MODID, "textures/entity/trinket/panic_necklace.png");

	public PanicNecklaceItem() {
		super(new Item.Settings());
		UserHurtCallback.EVENT.register(PanicNecklaceItem::applyEffects);
	}

	private static void applyEffects(LivingEntity user, DamageSource source, float amount) {
		if (!user.world.isClient && amount >= 1 && TrinketsHelper.isEquipped(Items.PANIC_NECKLACE, user)) {
			user.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 160, 0, false, false));
		}
	}

	@Override
	protected SoundEvent getEquipSound() {
		return SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND;
	}

	@Override
	@Environment(EnvType.CLIENT)
	protected BipedEntityModel<LivingEntity> createModel() {
		return new PanicNecklaceModel();
	}

	@Override
	@Environment(EnvType.CLIENT)
	protected Identifier getTexture() {
		return TEXTURE;
	}

	@Override
	public boolean canWearInSlot(String group, String slot) {
		return group.equals(SlotGroups.CHEST) && slot.equals(Slots.NECKLACE);
	}
}
