package net.jiang.tutorialmod.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class NormalCursedEnchantment extends Enchantment {
    protected NormalCursedEnchantment(Rarity rarity, EnchantmentTarget target, EquipmentSlot[] slotTypes) {
        super(rarity, target, slotTypes);
    }


    public int getMaxLevel() {
        return 1;
    }

    public boolean isCursed() {
        return true;
    }
}
