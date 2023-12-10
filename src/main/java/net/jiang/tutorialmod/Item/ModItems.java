package net.jiang.tutorialmod.Item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.jiang.tutorialmod.TutorialMod;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item RUBY = registerItem("ruby",new Item(new FabricItemSettings()));

    private static void addItemsToIngredientItemGroup(FabricItemGroupEntries entries){
        entries.add(RUBY);
    }

    public static Item registerItem(String name,Item item){
        return Registry.register(Registries.ITEM,new Identifier(TutorialMod.MOD_ID,name),item);
    }

    public static void registerModItems(){
        TutorialMod.LOGGER.info("注册一个MOD物品"+TutorialMod.MOD_ID);
//233
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientItemGroup);
    }
}
