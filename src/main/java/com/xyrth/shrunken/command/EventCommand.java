package com.xyrth.shrunken.command;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import com.xyrth.shrunken.reference.PotionEvent;
import com.xyrth.shrunken.util.LogUtil;

public class EventCommand extends GenericCommand {

    @Override
    public String getCommandName() {
        return "shrunken";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Run a shrunken event manually. Available are: " + Arrays.toString(PotionEvent.values());
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        // EntityPlayerMP player = getCommandSenderAsPlayer(sender);
        EntityPlayerMP player = MinecraftServer.getServer()
            .getConfigurationManager()
            .func_152612_a("Dragonhatch1");
        World world = player.worldObj;

        // Args is an array of strings split by the command input on spaces, and sliced after the command name

        if (args.length == 4) {

            if (PotionEvent.isValidEnum(args[0].toUpperCase())) {

                PotionEvent event = PotionEvent.valueOf(args[0].toUpperCase());

                try {
                    event.genericEventClass
                        .getDeclaredConstructor(World.class, int.class, int.class, int.class, EntityLivingBase.class)
                        .newInstance(
                            world,
                            Integer.parseInt(args[1]),
                            Integer.parseInt(args[2]),
                            Integer.parseInt(args[3]),
                            player);
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException
                    | InvocationTargetException e) {
                    sendChatToSender(sender, EnumChatFormatting.RED + "Failed to trigger event.");
                    LogUtil.error(e);
                    LogUtil.error(e.getMessage());
                }
            } else {
                sendChatToSender(
                    sender,
                    String.format(EnumChatFormatting.YELLOW + "Event '%s' does not exist.", args[0]));
            }
        } else {
            sendChatToSender(sender, EnumChatFormatting.YELLOW + "Missing an event type.");
        }
    }

    // We only want ops to be able to run this command
    @Override
    protected boolean isAdminOnly() {
        return true;
    }
}
