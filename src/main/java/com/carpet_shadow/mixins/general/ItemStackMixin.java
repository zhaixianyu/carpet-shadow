package com.carpet_shadow.mixins.general;

import com.carpet_shadow.interfaces.ShadowItem;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.carpet_shadow.CarpetShadow.BEGIN;


@Mixin(ItemStack.class)
public class ItemStackMixin implements ShadowItem {

    @Override
    public boolean carpet_shadow$isItShadowItem() {
        var shadowId = this.carpet_shadow$getShadowId();
        return shadowId != null && !shadowId.isEmpty() && shadowId.matches("\\S+?");
    }

    @Override
    public String carpet_shadow$getShadowId() {
//        if (!carpet_shadow$containsShadowComponent()) return null;
//        var component = ((ItemStack)(Object)this).getComponents().get(ShadowNBTData.SHADOW);
//        return component != null ? component.shadowId() : null;

        var component = ((ItemStack) (Object) this).get(DataComponentTypes.LORE);
        if (component == null || component.lines().isEmpty()) {
            return null;
        }
        AtomicReference<String> string = new AtomicReference<>();
        component.lines().stream()
                .filter(str -> str.getString().contains(BEGIN))
                .findFirst().ifPresent(
                        str -> string.set(str.getString()));
        String string1 = string.get();
        if (string1 == null) {
            return null;
        }
        String replace = string1.replace(BEGIN, "");

        return replace;
    }

    @Override
    public Text carpet_shadow$getText(String id) {
        MutableText text = Text.literal(BEGIN);
        MutableText sub = Text.literal(id);
        sub.formatted(Formatting.GOLD, Formatting.BOLD);
        text.append(sub);
        text.formatted(Formatting.DARK_PURPLE, Formatting.ITALIC);
        return text;
    }

    @Override
    public void carpet_shadow$setShadowId(String id) {
//        var components = ((TooltipStack)(Object)this).carpet_shadow$getComponentMapImpl();
//        components.set(ShadowNBTData.SHADOW, new ShadowComponent(id));
//        ((ItemStack)(Object)this).applyComponentsFrom(components);
//        ((ItemStack)(Object)this).set(ShadowNBTData.SHADOW, new ShadowComponent(id));
        LoreComponent loreComponent = ((ItemStack) (Object) this).get(DataComponentTypes.LORE);
        if (loreComponent != null) {
            ((ItemStack) (Object) this).set(DataComponentTypes.LORE, loreComponent.with(carpet_shadow$getText(id)));
        }
    }

    @Override
    public void carpet_shadow$removeShadow() {
//        ((ItemStack)(Object)this).remove(ShadowNBTData.SHADOW);
        String id = carpet_shadow$getShadowId();
        if (id != null) {
            ItemStack stack = (ItemStack) (Object) this;
            LoreComponent loreComponent = stack.get(DataComponentTypes.LORE);
            List<Text> lines = loreComponent.lines();
            if (lines.isEmpty()) return;
            List<Text> list = lines.stream().filter(text -> !text.getString().contains(BEGIN)).toList();
//            lines.removeIf(text -> {
//                String literalString = text.getString();
//                if (literalString == null) return false;
//                else return literalString.contains(BEGIN);
//            });
            stack.set(DataComponentTypes.LORE, new LoreComponent(list));
        }
//        ((TooltipStack)(Object)this).carpet_shadow$getComponentMapImpl().remove(ShadowNBTData.SHADOW);
    }
}
