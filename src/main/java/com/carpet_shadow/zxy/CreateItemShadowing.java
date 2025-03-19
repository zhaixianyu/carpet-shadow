package com.carpet_shadow.zxy;

import carpet.utils.CommandHelper;
import com.carpet_shadow.CarpetShadow;
import com.carpet_shadow.CarpetShadowSettings;
import com.carpet_shadow.Globals;
import com.carpet_shadow.interfaces.ShadowItem;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;

public class CreateItemShadowing {
    public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher){
        dispatcher.register(CommandManager.literal("craftItemShadowing")
                .requires(source -> CommandHelper.canUseCommand(source, CarpetShadowSettings.shadowItemMode != CarpetShadowSettings.Mode.UNLINK))
                .executes(CreateItemShadowing::itemShadowing));
    }
    //制作物品分身
    private static int itemShadowing(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if(player == null) return -1;
        // 获取主副手上的物品
        ItemStack main = player.getMainHandStack();
        ItemStack off = player.getOffHandStack();
        if (!main.isEmpty() && off.isEmpty()) {
            // 制作物品分身
            player.setStackInHand(Hand.OFF_HAND, main);
            String shadow_id = ((ShadowItem) (Object) main).carpet_shadow$getShadowId();
            if (shadow_id == null)
                shadow_id = CarpetShadow.shadow_id_generator.nextString();
            Globals.getByIdOrAdd(shadow_id,main);
            return 1;
        }
        return -1;
    }
}
