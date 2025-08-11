package com.fletime.neofoxport.mixin;

import com.fletime.neofoxport.NeoFoxPort;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.texture.TextureManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * SplashOverlay Mixin - 在启动界面添加狐狸动画
 */
@Mixin(SplashOverlay.class)
public class SplashOverlayMixin {
    
    @Shadow @Final private MinecraftClient client;
    
    @Inject(at = @At("HEAD"), method = "init")
    private static void onInit(TextureManager textureManager, CallbackInfo ci) {
        NeoFoxPort.registerTextures(textureManager);
    }
    
    @Inject(at = @At("TAIL"), method = "render")
    private void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        NeoFoxPort.render(client, context, delta);
    }
} 