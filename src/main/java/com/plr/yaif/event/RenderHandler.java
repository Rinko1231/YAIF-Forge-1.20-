package com.plr.yaif.event;

import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderBlockScreenEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.minecraft.world.effect.MobEffects.FIRE_RESISTANCE;
import static net.minecraft.world.item.Items.MILK_BUCKET;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class RenderHandler {
    @SubscribeEvent
    public static void onRenderFire(RenderBlockScreenEffectEvent event) {
        if (event.getOverlayType() != RenderBlockScreenEffectEvent.OverlayType.FIRE) return;

        Player player = event.getPlayer();
        if (Minecraft.getInstance().options.hideGui || player.isCreative()) {
            event.setCanceled(true);
            return;
        }

        //优先处理手持牛奶闪烁
        if (player.isHolding(MILK_BUCKET) && player.getEffect(FIRE_RESISTANCE) != null) {
            long t = player.level().getGameTime();

            if ((t % 20) < 10) {
                event.setCanceled(true);
            }
            //player.displayClientMessage(Component.literal("milk"), true);
            return;
        }

        if (player.hasEffect(FIRE_RESISTANCE)) {
            MobEffectInstance effect = player.getEffect(FIRE_RESISTANCE);
            if (effect == null) return;

            int duration = effect.getDuration();

            if (duration <= 200) {
                if ((duration % 20) < 10) {
                    event.setCanceled(true);
                }
                return;
            }

            event.setCanceled(true);
        }

    }
}
