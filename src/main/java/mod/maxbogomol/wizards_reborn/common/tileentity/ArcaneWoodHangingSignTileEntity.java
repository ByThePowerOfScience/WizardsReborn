package mod.maxbogomol.wizards_reborn.common.tileentity;

import mod.maxbogomol.wizards_reborn.WizardsReborn;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ArcaneWoodHangingSignTileEntity extends HangingSignBlockEntity {
    public ArcaneWoodHangingSignTileEntity(BlockPos pPos, BlockState pState) {
        super(pPos, pState);
    }

    @Override
    public BlockEntityType<?> getType() {
        return WizardsReborn.ARCANE_WOOD_HANGING_SIGN_TILE_ENTITY.get();
    }
}
