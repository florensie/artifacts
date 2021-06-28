package artifacts.compat;

import artifacts.item.UmbrellaItem;
import artifacts.mixin.mixins.compat.origins.ConditionFactoryAccessor;
import io.github.apace100.origins.Origins;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import java.util.function.BiFunction;

public class OriginsCompat implements Runnable {

	@Override
	public void run() {
		RegistryEntryAddedCallback.event(ApoliRegistries.ENTITY_CONDITION).register(OriginsCompat::playerConditionAdded);
	}

	private static void playerConditionAdded(int rawId, Identifier id, ConditionFactory<LivingEntity> conditionFactory) {
		// Held-up umbrella blocks origins:exposed_to_sun condition
		if (conditionFactory.getSerializerId().equals(new Identifier(Origins.MODID, "exposed_to_sun"))) {
			//noinspection unchecked
			ConditionFactoryAccessor<LivingEntity> conditionAccess = (ConditionFactoryAccessor<LivingEntity>) conditionFactory;
			BiFunction<SerializableData.Instance, LivingEntity, Boolean> origCondition = conditionAccess.getCondition();

			// Wrapper around original condition
			conditionAccess.setCondition((instance, entity) -> origCondition.apply(instance, entity)
					&& !UmbrellaItem.isHeldUpInEitherHand(entity));
		}
	}
}
