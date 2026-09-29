package com.fletime.neofoxport.mixin;

import com.fletime.neofoxport.NeoFoxPort;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
	@Inject(
		method = "<init>",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/gui/screens/LoadingOverlay;registerTextures(Lnet/minecraft/client/renderer/texture/TextureManager;)V",
			shift = At.Shift.AFTER
		)
	)
	private void neofoxport$registerTextures(CallbackInfo ci) {
		NeoFoxPort.registerTextures(Minecraft.getInstance().getTextureManager());
	}
}
