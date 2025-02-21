package mod.maxbogomol.wizards_reborn.common.spell.fog;

import mod.maxbogomol.fluffy_fur.client.particle.ParticleBuilder;
import mod.maxbogomol.fluffy_fur.client.particle.data.ColorParticleData;
import mod.maxbogomol.fluffy_fur.client.particle.data.GenericParticleData;
import mod.maxbogomol.fluffy_fur.client.particle.data.LightParticleData;
import mod.maxbogomol.fluffy_fur.client.particle.data.SpinParticleData;
import mod.maxbogomol.fluffy_fur.registry.client.FluffyFurParticles;
import mod.maxbogomol.fluffy_fur.registry.client.FluffyFurRenderTypes;
import mod.maxbogomol.wizards_reborn.api.crystal.CrystalUtil;
import mod.maxbogomol.wizards_reborn.api.spell.Spell;
import mod.maxbogomol.wizards_reborn.api.spell.SpellContext;
import mod.maxbogomol.wizards_reborn.common.entity.SpellEntity;
import mod.maxbogomol.wizards_reborn.registry.common.WizardsRebornCrystals;
import mod.maxbogomol.wizards_reborn.registry.common.entity.WizardsRebornEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FogSpell extends Spell {

    public FogSpell(String id, int points) {
        super(id, points);
    }

    @Override
    public boolean canUseSpell(Level level, SpellContext spellContext) {
        return false;
    }

    @Override
    public boolean useSpellOn(Level level, SpellContext spellContext) {
        if (!level.isClientSide()) {
            Vec3 pos = spellContext.getBlockPos().getCenter();
            SpellEntity entity = new SpellEntity(WizardsRebornEntities.SPELL.get(), level);
            entity.setup(pos.x(), pos.y() + 0.5f, pos.z(), spellContext.getEntity(), this.getId(), spellContext.getStats()).setSpellContext(spellContext);
            level.addFreshEntity(entity);
            spellContext.setCooldown(this);
            spellContext.removeWissen(this);
            spellContext.awardStat(this);
            spellContext.spellSound(this);
            return true;
        }
        return false;
    }

    @Override
    public void entityTick(SpellEntity entity) {
        fog(entity);
        if (!entity.level().isClientSide()) {
            if (entity.tickCount > getLifeTime(entity)) {
                entity.remove();
            }
        } else {
            fogEffect(entity);
        }
    }

    public int getLifeTime(SpellEntity entity) {
        return 200;
    }

    public int getHeight(SpellEntity entity) {
        return 2;
    }

    public int getSize(SpellEntity entity) {
        return 1;
    }

    public int getAdditionalSize(SpellEntity entity) {
        return 1;
    }

    public boolean isCircle(SpellEntity entity) {
        return true;
    }

    public void fog(SpellEntity entity) {

    }

    public void fogEffect(SpellEntity entity) {
        Color color = getColor();
        float alpha = 1;
        int lifeTime = getLifeTime(entity);

        if (entity.tickCount < 20) {
            alpha = (entity.tickCount) / 20f;
        }
        if (entity.tickCount > lifeTime - 20) {
            alpha = ((lifeTime - entity.tickCount) / 20f);
        }
        if (alpha > 1f) alpha = 1f;
        if (alpha < 0f) alpha = 0f;

        int focusLevel = CrystalUtil.getStatLevel(entity.getStats(), WizardsRebornCrystals.FOCUS);
        int size = getSize(entity) + (getSize(entity) * focusLevel);
        List<BlockPos> blocks = getBlocks(entity.level(), entity.getOnPos(), (int) (size * alpha), 4, isCircle(entity));

        for (BlockPos pos : blocks) {
            if (random.nextFloat() < 0.2f) {
                ParticleBuilder.create(FluffyFurParticles.SMOKE)
                        .setColorData(ColorParticleData.create(color).build())
                        .setTransparencyData(GenericParticleData.create(0.45f, 0).build())
                        .setScaleData(GenericParticleData.create(0.5f).build())
                        .setSpinData(SpinParticleData.create().randomSpin(0.1f).build())
                        .setLifetime(20)
                        .randomVelocity(0.007f)
                        .flatRandomOffset(0.5f, 0, 0.5f)
                        .spawn(entity.level(), pos.getX() + 0.5f, pos.getY() + 0.1f, pos.getZ() + 0.5f);
            }
            if (random.nextFloat() < 0.4f) {
                ParticleBuilder.create(FluffyFurParticles.SMOKE)
                        .setRenderType(FluffyFurRenderTypes.TRANSLUCENT_PARTICLE)
                        .setColorData(ColorParticleData.create(color).build())
                        .setTransparencyData(GenericParticleData.create(0.35f, 0).build())
                        .setScaleData(GenericParticleData.create(0.5f).build())
                        .setLightData(LightParticleData.DEFAULT)
                        .setSpinData(SpinParticleData.create().randomSpin(0.1f).build())
                        .setLifetime(20)
                        .randomVelocity(0.007f)
                        .flatRandomOffset(0.5f, 0, 0.5f)
                        .spawn(entity.level(), pos.getX() + 0.5f, pos.getY() + 0.1f, pos.getZ() + 0.5f);
            }
        }
    }

    public List<BlockPos> getBlocks(Level level, BlockPos startPos, int size, int height, boolean circle) {
        List<BlockPos> list = new ArrayList<>();

        for (int x = -size; x <= size; x++) {
            for (int z = -size; z <= size; z++) {
                for (int y = height; y > -height; y--) {
                    if (circle) {
                        float dst = (float) Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2));
                        if (dst > size) break;
                    }

                    BlockPos blockPos = new BlockPos(startPos.getX() + x, startPos.getY() + y, startPos.getZ() + z);
                    BlockState blockState = level.getBlockState(blockPos);
                    if (!blockState.isAir()) {
                        list.add(blockPos.above());
                        break;
                    }
                }
            }
        }

        return list;
    }

    public List<Entity> getEntities(Level level, List<BlockPos> blockList) {
        List<Entity> list = new ArrayList<>();

        for (BlockPos pos : blockList) {
            List<Entity> entities = level.getEntitiesOfClass(Entity.class, new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 0.5f, pos.getZ() + 1));
            for (Entity entity : entities) {
                if (!list.contains(entity)) {
                    list.add(entity);
                }
            }
        }

        return list;
    }
}
