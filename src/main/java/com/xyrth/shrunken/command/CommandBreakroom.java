package com.xyrth.shrunken.command;

import com.xyrth.shrunken.event.BreakroomHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

import java.util.Arrays;

public class CommandBreakroom extends CommandBase {

    public double[] pos1 = {BreakroomHandler.BreakroomConfig.MIN_X, BreakroomHandler.BreakroomConfig.MIN_Y, BreakroomHandler.BreakroomConfig.MIN_Z};
    public double[] pos2 = {BreakroomHandler.BreakroomConfig.MAX_X, BreakroomHandler.BreakroomConfig.MAX_Y, BreakroomHandler.BreakroomConfig.MAX_Z};

    @Override
    public String getCommandName(){
        return "breakroom";
    }

    @Override
    public String getCommandUsage(ICommandSender sender){
        return "breakroom <pos1|pos2|current>";
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
                pos1[0] = player.posX;
                pos1[1] = player.posY;
                pos1[2] = player.posZ;
                sender.addChatMessage(new ChatComponentText("Pos1 set to " + pos1[0] + ", " + pos1[1] + ", " + pos1[2] + ", "));
                updateBreakroomConfig();
                break;

            case "pos2":
                pos2[0] = player.posX;
                pos2[1] = player.posY;
                pos2[2] = player.posZ;
                sender.addChatMessage(new ChatComponentText("Pos2 set to " + pos2[0] + ", " + pos2[1] + ", " + pos2[2] + ", "));
                updateBreakroomConfig();
                break;

            case "current":
                sender.addChatMessage(new ChatComponentText("Pos1: " + BreakroomHandler.BreakroomConfig.MIN_X + ", " + BreakroomHandler.BreakroomConfig.MIN_Y+ ", " + BreakroomHandler.BreakroomConfig.MIN_Z));
                sender.addChatMessage(new ChatComponentText("Pos2: " + BreakroomHandler.BreakroomConfig.MAX_X + ", " + BreakroomHandler.BreakroomConfig.MAX_Y+ ", " + BreakroomHandler.BreakroomConfig.MAX_Z));
                break;

            default:
                sender.addChatMessage(new ChatComponentText("Unknown Subcommmand."));
        }

    }

    private void updateBreakroomConfig() {

        if (pos1 == null || pos2 == null) return;

        BreakroomHandler.BreakroomConfig.MIN_X = Math.min(pos1[0], pos2[0]);
        BreakroomHandler.BreakroomConfig.MAX_X = Math.max(pos1[0], pos2[0]);
        BreakroomHandler.BreakroomConfig.MIN_Y = Math.min(pos1[1], pos2[1]);
        BreakroomHandler.BreakroomConfig.MAX_Y = Math.max(pos1[1], pos2[1]);
        BreakroomHandler.BreakroomConfig.MIN_Z = Math.min(pos1[2], pos2[2]);
        BreakroomHandler.BreakroomConfig.MAX_Z = Math.max(pos1[2], pos2[2]);
    }
}
