package jaggwagg.gray_goo.screen;

import jaggwagg.gray_goo.GrayGoo;
import jaggwagg.gray_goo.client.gui.screen.NaniteModifierScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class GrayGooScreenHandlers {
    public static final ScreenHandlerType<NaniteModifierScreenHandler> NANITE_ASSEMBLER = Registry.register(Registry.SCREEN_HANDLER, new Identifier(GrayGoo.MOD_ID, "nanite_assembler"), new ScreenHandlerType<>(NaniteModifierScreenHandler::new));

    public static void initClient() {
        HandledScreens.register(NANITE_ASSEMBLER, NaniteModifierScreen::new);
    }
}
