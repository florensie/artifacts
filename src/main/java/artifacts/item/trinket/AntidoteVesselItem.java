package artifacts.item.trinket;

import artifacts.Artifacts;
import artifacts.client.render.model.trinket.AntidoteVesselModel;
import artifacts.mixin.extensions.StatusEffectInstanceExtensions;
import artifacts.mixin.mixins.accessors.StatusEffectAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class AntidoteVesselItem extends TrinketArtifactItem {

	private static final Identifier TEXTURE = Artifacts.id("textures/entity/trinket/antidote_vessel.png");

	@Override
	protected void effectTick(PlayerEntity player, ItemStack stack) {
		// Reduce duration of all negative status effects to 80
		player.getActiveStatusEffects().forEach((effect, instance) -> {
			if (!effect.isInstant() && ((StatusEffectAccessor) effect).getType() != StatusEffectType.BENEFICIAL && instance.getDuration() > 80) {
				((StatusEffectInstanceExtensions) instance).artifacts$setDuration(80);
			}
		});
	}

	@Override
	@Environment(EnvType.CLIENT)
	protected BipedEntityModel<LivingEntity> createModel() {
		return new AntidoteVesselModel();
	}

	@Override
	@Environment(EnvType.CLIENT)
	protected Identifier getTexture() {
		return TEXTURE;
	}

	@Override
	public SoundInfo getEquipSound() {
		return new SoundInfo(SoundEvents.ITEM_BOTTLE_FILL);
	}

	@Override
	public boolean canWearInSlot(String group, String slot) {
		return group.equals("legs") && slot.equals("belt");
	}
}
