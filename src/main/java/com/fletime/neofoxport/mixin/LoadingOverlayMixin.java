package com.fletime.neofoxport.mixin;

import com.fletime.neofoxport.NeoFoxPort;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.LoadingOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LoadingOverlay.class)
public class LoadingOverlayMixin {
	@Shadow
	private boolean fadeIn;

	@Inject(method = "extractRenderState", at = @At("TAIL"))
	private void neofoxport$render(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		if (!fadeIn) {
			NeoFoxPort.render(graphics);
		}
	}
}
