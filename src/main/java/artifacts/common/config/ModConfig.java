package artifacts.common.config;

import artifacts.Artifacts;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.serializer.PartitioningSerializer;

@Config(name = Artifacts.MODID)
@Config.Gui.Background("minecraft:textures/block/mossy_cobblestone.png")
public final class ModConfig extends PartitioningSerializer.GlobalData {
	@ConfigEntry.Category("general")
	@ConfigEntry.Gui.TransitiveObject
	public General general = new General();

	@ConfigEntry.Category("worldgen")
	@ConfigEntry.Gui.TransitiveObject
	public WorldGen worldgen = new WorldGen();

	private ModConfig() { }

	@Config(name = "general")
	public static final class General implements ConfigData {
		@SuppressWarnings("unused")
		@ConfigEntry.Gui.Excluded
		public int configVersion = 0;
		@ConfigEntry.Gui.Tooltip(count = 2)
		public int everlastingFoodCooldown = 300;

		private General() { }
	}

	@Config(name = "worldgen")
	public static final class WorldGen implements ConfigData {
		@ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
		public Campsite campsite = new Campsite();

		private WorldGen() { }

		public static final class Campsite {
			@ConfigEntry.Gui.RequiresRestart
			@ConfigEntry.Gui.Tooltip(count = 2)
			@ConfigEntry.BoundedDiscrete(max = 100)
			public int genChance = 8;

			@ConfigEntry.Gui.Tooltip(count = 2)
			@ConfigEntry.BoundedDiscrete(max = 100)
			public int mimicChance = 30;

			@ConfigEntry.Gui.Tooltip(count = 2)
			@ConfigEntry.BoundedDiscrete(max = 100)
			public int oreChance = 25;

			// TODO: this should probably be higher so we don't break any bedrock
			@ConfigEntry.Gui.Tooltip
			@ConfigEntry.BoundedDiscrete(max = 256)
			public int minY = 0;

			@ConfigEntry.Gui.Tooltip
			@ConfigEntry.BoundedDiscrete(max = 256)
			public int maxY = 45;

			private Campsite() { }
		}
	}
}
