package net.yak.totemictides.init;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Rarity;
import net.yak.totemictides.TotemicTides;
import net.yak.totemictides.item.BottledVexItem;
import net.yak.totemictides.item.ElderGuardianSpikeItem;
import net.yak.totemictides.item.TotemicItem;

public class TotemicItems {

    public static final Item TOTEM_OF_CREEPING = registerItem("totem_of_creeping", new TotemicItem(new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON)));
    public static final Item TOTEM_OF_BLAZING = registerItem("totem_of_blazing", new TotemicItem(new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON)));
    public static final Item TOTEM_OF_INKING = registerItem("totem_of_inking", new TotemicItem(new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON)));
    public static final Item TOTEM_OF_BREEZING = registerItem("totem_of_breezing", new TotemicItem(new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON)));
    public static final Item TOTEM_OF_REMATCHING = registerItem("totem_of_rematching", new TotemicItem(new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON)));
    public static final Item TOTEM_OF_REGENERATING = registerItem("totem_of_regenerating", new TotemicItem(new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON)));
    public static final Item TOTEM_OF_EVOKING = registerItem("totem_of_evoking", new TotemicItem(new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON)));
    public static final Item TOTEM_OF_SHULKING = registerItem("totem_of_shulking", new TotemicItem(new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON)));
    public static final Item BOTTLED_VEX = registerItem("bottled_vex", new BottledVexItem(new Item.Settings().maxCount(16)));
    public static final Item ELDER_GUARDIAN_SPIKE = registerItem("elder_guardian_spike", new ElderGuardianSpikeItem(new Item.Settings().maxCount(16)));


    public static void init() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.addAfter(Items.TOTEM_OF_UNDYING, TOTEM_OF_CREEPING);
            entries.addAfter(TOTEM_OF_CREEPING, TOTEM_OF_BLAZING);
            entries.addAfter(TOTEM_OF_BLAZING, TOTEM_OF_INKING);
            entries.addAfter(TOTEM_OF_INKING, TOTEM_OF_BREEZING);
            entries.addAfter(TOTEM_OF_BREEZING, TOTEM_OF_REMATCHING);
            entries.addAfter(TOTEM_OF_REMATCHING, TOTEM_OF_REGENERATING);
            entries.addAfter(TOTEM_OF_REGENERATING, TOTEM_OF_EVOKING);
            entries.addAfter(TOTEM_OF_EVOKING, TOTEM_OF_SHULKING);
        });
    }

    public static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, TotemicTides.id(name), item);
    }
}
