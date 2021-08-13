package com.clickadv.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.IBlockReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ActiveRenderInfo.class)
public abstract class ActiveRenderInfoMixin
{
    @Shadow
    protected abstract void setRotation(final float p_216776_1_, final float p_216776_2_);

    @Shadow
    public abstract float getXRot();

    @Shadow
    public abstract float getYRot();

    @Shadow
    protected abstract void move(final double p_216782_1_, final double p_216782_3_, final double p_216782_5_);

    @Shadow
    private float yRot;

    @Shadow
    private float xRot;

    @Redirect(method = "setup", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isSleeping()Z"))
    public boolean test(final LivingEntity entity)
    {
        return false;
    }

    @Inject(method = "setup", at = @At("RETURN"))
    public void onTick(
      final IBlockReader p_216772_1_,
      final Entity entity,
      final boolean p_216772_3_,
      final boolean p_216772_4_,
      final float p_216772_5_,
      final CallbackInfo ci)
    {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.isSleeping())
        {
            setRotation(yRot, (Minecraft.getInstance().player.getSleepTimer() / 20f));
            move((Minecraft.getInstance().player.getSleepTimer() / 20D),
              (Minecraft.getInstance().player.getSleepTimer() / 20D),
              (Minecraft.getInstance().player.getSleepTimer() / 20D));
        }
    }
}
