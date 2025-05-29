package net.yak.totemictides;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.util.Identifier;
import net.yak.totemictides.init.TotemicEntityTypes;
import net.yak.totemictides.init.TotemicItems;
import net.yak.totemictides.init.TotemicParticles;
import net.yak.totemictides.init.TotemicStatusEffects;
import net.yak.totemictides.networking.TotemicPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TotemicTides implements ModInitializer {
	public static final String MOD_ID = "totemictides";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		LOGGER.info("fear the tides of the storm");

		TotemicItems.init();
		TotemicStatusEffects.init();
		TotemicParticles.init();
		TotemicEntityTypes.init();

		// C2S packets
		PayloadTypeRegistry.playS2C().register(TotemicPayload.ID, TotemicPayload.CODEC);

		
		/*
		atonement: effect that blocks usage of totems
		wind sail: effect that increases midair movement speed
		totem of creeping: explosion at location of totem pop - design is leaf based (changes with biome?), with dark black eyes
		totem of blazing: player has swirling flame particles and sets on fire any enemy in radius, any allies in radius are powered up with increased damage and fire resistance (has visual indicator, kinda like being on fire) - design is nether brick based with glowing fire eyes (emissive?)
		totem of inking: players around the popped totem have their hud covered in splotches of ink + a black vignette, entities cannot pathfind anywhere, affected entities are slowed and potentially glow in glow ink colors - design is clay-based with glowing cyan eyes (emissive?)
		totem of breezing: player goes flying into the air, has increased midair speed, and is immune to fall damage when landing - design is copper based with a swirl of wind around it (animated)
		totem of rematching: on totem pop, returns you to your spawn point for 30 seconds and then brings you back to the location of the totem pop - design
		totem of regenerating: creates 10x10 aoe field of powerful regeneration that heals the user and their allies, along with buffing their damage resistance at low health - design is oak log based with a glowing red heart (emissive? pulsates?)
		totem of evoking: is weaker than a normal totem. on totem pop, searches your inventory for other totems, prioritizing other totems of evoking, and replaces the recently used totem in your offhand with the one grabbed from your inventory, goes on cooldown - design resembles an evoker fang swirling with totem particles
		totem of shulking: works in the void. gives levitation, slow falling, and damages you in water

		// built in resource pack that converts the unique designs to the vanilla totem color palette
		// compatibility with totem particle mod thingy if it's not already innate
		*/
	}

	public static Identifier id(String name) {
		return Identifier.of(MOD_ID, name);
	}
}