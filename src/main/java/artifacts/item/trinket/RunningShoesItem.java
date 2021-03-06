package artifacts.item.trinket;

import artifacts.Artifacts;
import artifacts.client.render.model.trinket.ShoesModel;
import artifacts.trinkets.Slots;
import dev.emi.stepheightentityattribute.StepHeightEntityAttributeMain;
import dev.emi.trinkets.api.SlotGroups;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class RunningShoesItem extends TrinketArtifactItem {

	private static final Identifier TEXTURE = Artifacts.id("textures/entity/trinket/running_shoes.png");

	public static final EntityAttributeModifier SPEED_BOOST_MODIFIER = new EntityAttributeModifier(UUID.fromString("ac7ab816-2b08-46b6-879d-e5dea34ff305"),
			"artifacts:running_shoes_movement_speed", 0.4, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
	public static final EntityAttributeModifier STEP_HEIGHT_MODIFIER = new EntityAttributeModifier(UUID.fromString("7e97cede-a343-411f-b465-14cdf6df3666"),
			"artifacts:running_shoes_step_height", .5, EntityAttributeModifier.Operation.ADDITION);

	@Override
	@SuppressWarnings("ConstantConditions")
	public void onUnequip(PlayerEntity player, ItemStack stack) {
		EntityAttributeInstance movementSpeed = player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
		EntityAttributeInstance stepHeight = player.getAttributeInstance(StepHeightEntityAttributeMain.STEP_HEIGHT);

		removeModifier(movementSpeed, SPEED_BOOST_MODIFIER);
		removeModifier(stepHeight, STEP_HEIGHT_MODIFIER);
	}

	@Override
	@Environment(EnvType.CLIENT)
	protected BipedEntityModel<LivingEntity> createModel() {
		return new ShoesModel(0.5F);
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
}
