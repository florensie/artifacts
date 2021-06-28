package artifacts.item.trinket;

import artifacts.Artifacts;
import artifacts.client.render.model.trinket.UniversalAttractorModel;
import artifacts.init.Components;
import artifacts.mixin.mixins.accessors.ItemEntityAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class UniversalAttractorItem extends TrinketArtifactItem {

	private static final Identifier TEXTURE = Artifacts.id("textures/entity/trinket/universal_attractor.png");

	@Override
	// magnet logic from Botania, see https://github.com/Vazkii/Botania
	protected void effectTick(PlayerEntity player, ItemStack stack) {
		Vec3d playerPos = player.getPos().add(0, 0.75, 0);

		int range = 5;
		List<ItemEntity> items = player.world.getNonSpectatingEntities(ItemEntity.class, new Box(playerPos.x - range, playerPos.y - range, playerPos.z - range, playerPos.x + range, playerPos.y + range, playerPos.z + range));
		int pulled = 0;
		for (ItemEntity item : items) {
			boolean attractable = Components.DROPPED_ITEM_ENTITY.maybeGet(item).isPresent()
					&& (!Components.DROPPED_ITEM_ENTITY.get(item).get() || ((ItemEntityAccessor) item).getItemAge() > 100);
			if (attractable && item.isAlive() && !item.cannotPickup()) {
				if (pulled++ > 200) {
					break;
				}

				Vec3d motion = playerPos.subtract(item.getPos().add(0, item.getHeight() / 2, 0));
				if (Math.sqrt(motion.x * motion.x + motion.y * motion.y + motion.z * motion.z) > 1) {
					motion = motion.normalize();
				}
				item.setVelocity(motion.multiply(0.6));
			}
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	protected BipedEntityModel<LivingEntity> createModel() {
		return new UniversalAttractorModel();
	}

	@Override
	@Environment(EnvType.CLIENT)
	protected Identifier getTexture() {
		return TEXTURE;
	}

	@Override
	public boolean canWearInSlot(String group, String slot) {
		return group.equals("legs") && slot.equals("belt");
	}
}
