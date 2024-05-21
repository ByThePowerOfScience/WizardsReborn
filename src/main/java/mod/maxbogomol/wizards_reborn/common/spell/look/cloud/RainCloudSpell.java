package mod.maxbogomol.wizards_reborn.common.spell.look.cloud;

import mod.maxbogomol.wizards_reborn.WizardsReborn;
import mod.maxbogomol.wizards_reborn.api.crystal.CrystalUtils;
import mod.maxbogomol.wizards_reborn.common.crystalritual.ArtificialFertilityCrystalRitual;
import mod.maxbogomol.wizards_reborn.common.entity.SpellProjectileEntity;
import mod.maxbogomol.wizards_reborn.common.item.equipment.arcane.ArcaneArmorItem;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.awt.*;

public class RainCloudSpell extends CloudSpell {
    public RainCloudSpell(String id, int points) {
        super(id, points);
        addCrystalType(WizardsReborn.WATER_CRYSTAL_TYPE);
    }

    @Override
    public Color getColor() {
        return WizardsReborn.waterSpellColor;
    }

    @Override
    public void rain(SpellProjectileEntity entity, Player player) {
        float size = getCloudSize(entity);

        int focusLevel = CrystalUtils.getStatLevel(entity.getStats(), WizardsReborn.FOCUS_CRYSTAL_STAT);
        float magicModifier = ArcaneArmorItem.getPlayerMagicModifier(entity.getSender());
        float chance = (0.1f + ((focusLevel + magicModifier) * 0.025f));

        if (random.nextFloat() < chance) {
            float x = (float) (entity.getX() + ((random.nextFloat() - 0.5F) * 2 * size));
            float z = (float) (entity.getZ() + ((random.nextFloat() - 0.5F) * 2 * size));
            HitResult hit = getHitPos(entity.level(), new Vec3(x, entity.getY(), z), new Vec3(x, entity.getY() - 30, z));

            BlockPos blockPos = new BlockPos(Mth.floor(hit.getPosHit().x()), Mth.floor(hit.getPosHit().y()), Mth.floor(hit.getPosHit().z()));
            ArtificialFertilityCrystalRitual.growCrop(entity.level(), blockPos);
            ArtificialFertilityCrystalRitual.growCrop(entity.level(), blockPos.below());
        }
    }
}
