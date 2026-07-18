package com.xyrth.shrunken.command;

import com.xyrth.shrunken.event.BreakroomHandler;
import com.xyrth.shrunken.util.BreakroomConfig;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;

import java.util.Arrays;

public class CommandBreakroom extends CommandBase {

    public double[] pos1 = {BreakroomConfig.minX, BreakroomConfig.minY, BreakroomConfig.minZ};
    public double[] pos2 = {BreakroomConfig.maxX, BreakroomConfig.maxY, BreakroomConfig.maxZ};

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
                pos1[0] = MathHelper.floor_double(player.posX);
                pos1[1] = MathHelper.floor_double(player.posY);
                pos1[2] = MathHelper.floor_double(player.posZ);
                sender.addChatMessage(new ChatComponentText("Pos1 set to " + pos1[0] + ", " + pos1[1] + ", " + pos1[2] + ", "));
                updateBreakroomConfig();
                break;

            case "pos2":
                pos2[0] = MathHelper.floor_double(player.posX);
                pos2[1] = MathHelper.floor_double(player.posY);
                pos2[2] = MathHelper.floor_double(player.posZ);
                sender.addChatMessage(new ChatComponentText("Pos2 set to " + pos2[0] + ", " + pos2[1] + ", " + pos2[2] + ", "));
                updateBreakroomConfig();
                break;

            case "current":
                sender.addChatMessage(new ChatComponentText("Pos1: " + BreakroomConfig.minX + ", " + BreakroomConfig.minY + ", " + BreakroomConfig.minZ));
                sender.addChatMessage(new ChatComponentText("Pos2: " + BreakroomConfig.maxX + ", " + BreakroomConfig.maxY + ", " + BreakroomConfig.maxZ));
                break;

            default:
                sender.addChatMessage(new ChatComponentText("Unknown Subcommmand."));
        }

    }

    private void updateBreakroomConfig() {

        if (pos1 == null || pos2 == null) return;

        BreakroomConfig.minX = (int) Math.min(pos1[0], pos2[0]);
        BreakroomConfig.maxX = (int) Math.max(pos1[0], pos2[0]);
        BreakroomConfig.minY = (int) Math.min(pos1[1], pos2[1]);
        BreakroomConfig.maxY = (int) Math.max(pos1[1], pos2[1]);
        BreakroomConfig.minZ = (int) Math.min(pos1[2], pos2[2]);
        BreakroomConfig.maxZ = (int) Math.max(pos1[2], pos2[2]);
        BreakroomConfig.save();
    }
}
