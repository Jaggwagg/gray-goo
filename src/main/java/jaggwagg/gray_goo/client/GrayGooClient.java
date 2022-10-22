package jaggwagg.gray_goo.client;

import jaggwagg.gray_goo.screen.GrayGooScreenHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class GrayGooClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        GrayGooScreenHandlers.initClient();
    }
}
