package mod.maxbogomol.wizards_reborn.common.item;

import mod.maxbogomol.fluffy_fur.client.particle.ParticleBuilder;
import mod.maxbogomol.fluffy_fur.client.particle.data.ColorParticleData;
import mod.maxbogomol.fluffy_fur.client.particle.data.GenericParticleData;
import mod.maxbogomol.fluffy_fur.client.particle.data.SpinParticleData;
import mod.maxbogomol.fluffy_fur.common.easing.Easing;
import mod.maxbogomol.fluffy_fur.common.item.IParticleItem;
import mod.maxbogomol.fluffy_fur.registry.client.FluffyFurParticles;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

import java.util.Random;

public class ArcaciteItem extends Item implements IParticleItem {

    private static final Random random = new Random();

    public ArcaciteItem(Properties properties) {
        super(properties);
    }

    @Override
    public void addParticles(Level level, ItemEntity entity) {
        if (random.nextFloat() < 0.05) {
            ParticleBuilder.create(FluffyFurParticles.SPARKLE)
                    .setColorData(ColorParticleData.create(0.694f, 0.275f, 0.310f).build())
                    .setTransparencyData(GenericParticleData.create(0.4f, 0.5f, 0).setEasing(Easing.QUINTIC_IN_OUT).build())
                    .setScaleData(GenericParticleData.create(0.05f, 0.1f, 0).setEasing(Easing.QUINTIC_IN_OUT).build())
                    .setSpinData(SpinParticleData.create().randomOffset().randomSpin(0.5f).build())
                    .setLifetime(30)
                    .randomVelocity(0.01f)
                    .randomOffset(0.125f)
                    .spawn(level, entity.getX(), entity.getY() + 0.25F, entity.getZ());
        }
    }
}
