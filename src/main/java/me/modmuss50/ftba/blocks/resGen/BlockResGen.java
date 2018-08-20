package me.modmuss50.ftba.blocks.resGen;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import me.modmuss50.ftba.FTBAchievements;
import me.modmuss50.ftba.modCompat.top.ITopInfo;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import prospector.shootingstar.ShootingStar;
import prospector.shootingstar.model.ModelCompound;
import reborncore.api.tile.IMachineGuiHandler;
import reborncore.common.blocks.BlockMachineBase;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Mark on 18/06/2017.
 */
public class BlockResGen extends BlockMachineBase implements ITopInfo {

	public BlockResGen() {
		setUnlocalizedName("resgen");
		setCreativeTab(FTBAchievements.Tab.FTBA_TAB);
		ShootingStar.registerModel(new ModelCompound("ftbachievements", this, ""));
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileResGen();
	}

	@Override
	public IMachineGuiHandler getGui() {
		return null;
	}

	@Override
	public void addProbeInfo(ProbeMode mode,
	                         IProbeInfo probeInfo,
	                         EntityPlayer player,
	                         World world,
	                         IBlockState blockState,
	                         IProbeHitData data,
	                         @Nullable
		                         TileEntity tileEntity) {
		if (tileEntity != null && tileEntity instanceof TileResGen) {
			TileResGen te = (TileResGen) tileEntity;
			if (te.currentResource != null) {
				probeInfo.item(te.currentResource.stack.copy());
			}

			if (te.currentResource.processTime > 20) {
				probeInfo.progress((int)te.progress, te.currentResource.processTime, probeInfo.defaultProgressStyle()
					.suffix(" Progress"));
			}
			if (te.speedModifier != 0) {
				probeInfo.text("Speed " + te.speedModifier * 100 + "%");
			}
			if (te.powerModifier != 0) {
				probeInfo.text("Energy " + te.powerModifier * 100 + "%");
			}
			if (te.luckModifier != 0) {
				probeInfo.text("Luck +" + te.luckModifier + "%");
			}

		}
	}

	@Deprecated
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		NonNullList<ItemStack> items = NonNullList.create();
		items.add(new ItemStack(this));
		return items;
	}

}
