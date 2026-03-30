package com.xyrth.shrunken.event.potions;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.world.World;

import com.xyrth.shrunken.event.GenericPotionEvent;

public class PotionPrinter extends GenericPotionEvent {

    public PotionPrinter(World world, int x, int y, int z, EntityLivingBase targetEntity) {
        super(world, x, y, z, targetEntity);

        for (int id = 0; id < Potion.potionTypes.length; id++) {
            Potion potion = Potion.potionTypes[id];

            if (potion != null) {
                System.out.println("ID: " + id + " Name: " + potion.getName());
            }
        }
    }
}
