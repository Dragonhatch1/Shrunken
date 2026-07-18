package com.xyrth.shrunken.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

import java.util.Arrays;

public class CommandBreakroom extends CommandBase {

    @Override
    public String getCommandName(){
        return "breakroom";
    }

    @Override
    public String getCommandUsage(ICommandSender sender){
        return "breakroom <pos1|pos2|info>";
    }

    @Override
    public int getRequiredPermissionLevel(){
        return 2;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args){
        if (!(sender instanceof EntityPlayer)) {
            sender.addChatMessage(new ChatComponentText("Must be run by a player."));
            return;
        }
        EntityPlayer player = (EntityPlayer) sender;

        if (args.length < 1) {
            sender.addChatMessage(new ChatComponentText("Usage:" + getCommandUsage(sender)));
            return;
        }

        switch (args[0]) {

            case "pos1":
                double[] pos1 = new double[3];
                pos1[0] = player.posX;
                pos1[1] = player.posY;
                pos1[2] = player.posZ;
                sender.addChatMessage(new ChatComponentText("Corner 1 set to " + pos1[0] + ", " + pos1[1] + ", " + pos1[2] + ", "));
                break;

            case "pos2":
                double[] pos2 = new double[3];
                pos2[0] = player.posX;
                pos2[1] = player.posY;
                pos2[2] = player.posZ;
                sender.addChatMessage(new ChatComponentText("Corner 1 set to " + pos2[0] + ", " + pos2[1] + ", " + pos2[2] + ", "));
                break;

            case "info":
                sender.addChatMessage(new ChatComponentText("Set your corners using pos1 or pos2."));
                break;

            default:
                sender.addChatMessage(new ChatComponentText("Unknown Subcommmand."));
        }
    }
}
