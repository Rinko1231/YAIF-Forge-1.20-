package com.plr.yaif.event;

import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderBlockScreenEffectEvent;

import static net.minecraft.world.effect.MobEffects.FIRE_RESISTANCE;
import static net.minecraft.world.item.Items.MILK_BUCKET;

@EventBusSubscriber(value = Dist.CLIENT)
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
