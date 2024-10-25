package mod.maxbogomol.wizards_reborn.registry.common;

import mod.maxbogomol.wizards_reborn.WizardsReborn;
import mod.maxbogomol.wizards_reborn.api.arcaneenchantment.ArcaneEnchantment;
import mod.maxbogomol.wizards_reborn.api.arcaneenchantment.ArcaneEnchantmentHandler;
import mod.maxbogomol.wizards_reborn.common.arcaneenchantment.*;

public class WizardsRebornArcaneEnchantments {
    public static ArcaneEnchantment WISSEN_MENDING = new WissenMendingArcaneEnchantment(WizardsReborn.MOD_ID+":wissen_mending", 3);
    public static ArcaneEnchantment LIFE_MENDING = new LifeMendingArcaneEnchantment(WizardsReborn.MOD_ID+":life_mending", 3);
    public static ArcaneEnchantment MAGIC_BLADE = new MagicBladeArcaneEnchantment(WizardsReborn.MOD_ID+":magic_blade", 5);
    public static ArcaneEnchantment THROW = new ThrowArcaneEnchantment(WizardsReborn.MOD_ID+":throw", 1);
    public static ArcaneEnchantment LIFE_ROOTS = new LifeRootsArcaneEnchantment(WizardsReborn.MOD_ID+":life_roots", 2);
    public static ArcaneEnchantment WISSEN_CHARGE = new WissenChargeArcaneEnchantment(WizardsReborn.MOD_ID+":wissen_charge", 2);
    public static ArcaneEnchantment EAGLE_SHOT = new EagleShotArcaneEnchantment(WizardsReborn.MOD_ID+":eagle_shot", 4);
    public static ArcaneEnchantment SPLIT = new SplitArcaneEnchantment(WizardsReborn.MOD_ID+":split", 4);
    public static ArcaneEnchantment SONAR = new SonarArcaneEnchantment(WizardsReborn.MOD_ID+":sonar", 3);

    public static void register() {
        ArcaneEnchantmentHandler.register(WISSEN_MENDING);
        ArcaneEnchantmentHandler.register(LIFE_MENDING);
        ArcaneEnchantmentHandler.register(MAGIC_BLADE);
        ArcaneEnchantmentHandler.register(THROW);
        ArcaneEnchantmentHandler.register(LIFE_ROOTS);
        ArcaneEnchantmentHandler.register(WISSEN_CHARGE);
        ArcaneEnchantmentHandler.register(EAGLE_SHOT);
        ArcaneEnchantmentHandler.register(SPLIT);
        ArcaneEnchantmentHandler.register(SONAR);
    }
}
