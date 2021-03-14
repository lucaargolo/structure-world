package io.github.lucaargolo.structureworld.mixin;

import io.github.lucaargolo.structureworld.ModServer;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.gen.GeneratorOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.Properties;

@Mixin(value = GeneratorOptions.class, priority = -693)
public abstract class GeneratorOptionsMixin {

    @Inject(at = @At("HEAD"), method = "fromProperties", cancellable = true)
    private static void fromProperties(DynamicRegistryManager dynamicRegistryManager, Properties properties, CallbackInfoReturnable<GeneratorOptions> info) {
        ModServer.fromPropertiesHook(dynamicRegistryManager, properties, info);
    }

    @SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "unchecked"})
    @Redirect(at = @At(value = "INVOKE", target = "Ljava/util/Optional;orElse(Ljava/lang/Object;)Ljava/lang/Object;"), method = "fromProperties")
    private static<T> Object onDefaultLevelType(Optional<T> optional, T other) {
        if(other.equals("default") && ModServer.OVERRIDED_LEVEL_TYPE != null) {
            return optional.orElse((T) ModServer.OVERRIDED_LEVEL_TYPE);
        }
        return optional.orElse(other);
    }

}
