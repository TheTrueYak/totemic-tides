package net.yak.totemictides.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.yak.totemictides.init.TotemicItems;

import java.util.List;

public class BottledVexItem extends Item {

    public BottledVexItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (clickType == ClickType.RIGHT && stack.isOf(Items.TOTEM_OF_UNDYING) && otherStack.isOf(TotemicItems.BOTTLED_VEX)) {
            stack.decrement(1);
            slot.setStack(new ItemStack(TotemicItems.TOTEM_OF_EVOKING, stack.getCount()));
            if (player.getWorld().isClient()) {
                player.playSound(SoundEvents.AMBIENT_UNDERWATER_ENTER, 0.7f, 1.4f);
                player.playSound(SoundEvents.ENTITY_VEX_DEATH, 0.4f, 1.0f);
            }
            return true;
        }
        return super.onStackClicked(stack, slot, clickType, player);
    }

    @Override
    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        if (clickType == ClickType.RIGHT && slot.getStack().isOf(Items.TOTEM_OF_UNDYING) && stack.isOf(TotemicItems.BOTTLED_VEX)) {
            stack.decrement(1);
            slot.setStack(new ItemStack(TotemicItems.TOTEM_OF_EVOKING, slot.getStack().getCount()));
            if (player.getWorld().isClient()) {
                player.playSound(SoundEvents.AMBIENT_UNDERWATER_ENTER, 0.7f, 1.4f);
                player.playSound(SoundEvents.ENTITY_VEX_DEATH, 0.4f, 1.0f);
            }
            return true;
        }
        return super.onStackClicked(stack, slot, clickType, player);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.totemictides.can_infuse").setStyle(Style.EMPTY.withColor(0x517D9E)));
    }

}
