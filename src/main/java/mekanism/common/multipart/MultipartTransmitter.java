package mekanism.common.multipart;

import java.util.Collection;

import mcmultipart.multipart.IMultipartContainer;
import mekanism.api.Coord4D;
import mekanism.api.transmitters.DynamicNetwork;
import mekanism.api.transmitters.IGridTransmitter;
import mekanism.api.transmitters.TransmissionType;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.transmitters.Transmitter;
import mekanism.common.util.MekanismUtils;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class MultipartTransmitter<A, N extends DynamicNetwork<A,N>> extends Transmitter<A, N>
{
	public PartTransmitter<A, N> containingPart;

	public MultipartTransmitter(PartTransmitter<A, N> multiPart)
	{
		setPart(multiPart);
	}

	@Override
	public int getCapacity()
	{
		return getPart().getCapacity();
	}

	@Override
	public World world()
	{
		return getPart().getWorld();
	}

	@Override
	public Coord4D coord()
	{
		return new Coord4D(getPart().getPos(), getPart().getWorld());
	}

	@Override
	public Coord4D getAdjacentConnectableTransmitterCoord(EnumFacing side)
	{
		Coord4D sideCoord = coord().offset(side);

		TileEntity potentialTransmitterTile = sideCoord.getTileEntity(world());

		if(!containingPart.canConnectMutual(side))
		{
			return null;
		}

		if(MekanismUtils.hasCapability(potentialTransmitterTile, Capabilities.GRID_TRANSMITTER_CAPABILITY, side.getOpposite()))
		{
			IGridTransmitter transmitter = MekanismUtils.getCapability(potentialTransmitterTile, Capabilities.GRID_TRANSMITTER_CAPABILITY, side.getOpposite());

			if(TransmissionType.checkTransmissionType(transmitter, getTransmissionType()))
			{
				return sideCoord;
			}
		}
		
		return null;
	}

	@Override
	public A getAcceptor(EnumFacing side)
	{
		return getPart().getCachedAcceptor(side);
	}

	@Override
	public boolean isValid()
	{
		IMultipartContainer cont = getPart().getContainer();
		
		if(cont == null)
		{
			return false;
		}

		if(cont instanceof TileEntity && ((TileEntity)cont).isInvalid())
		{
			return false;
		}

		return !getPart().unloaded && coord().exists(world());
	}

	@Override
	public N createEmptyNetwork()
	{
		return getPart().createNewNetwork();
	}

	@Override
	public N getExternalNetwork(Coord4D from)
	{
		TileEntity tile = from.getTileEntity(world());
		
		if(MekanismUtils.hasCapability(tile, Capabilities.GRID_TRANSMITTER_CAPABILITY, null))
		{
			IGridTransmitter transmitter = MekanismUtils.getCapability(tile, Capabilities.GRID_TRANSMITTER_CAPABILITY, null);
			
			if(TransmissionType.checkTransmissionType(transmitter, getTransmissionType()));
			{
				return ((IGridTransmitter<A, N>)transmitter).getTransmitterNetwork();
			}
		}
		
		return null;
	}

	@Override
	public void takeShare()
	{
		containingPart.takeShare();
	}

    @Override
    public void updateShare()
    {
        containingPart.updateShare();
    }

	@Override
	public Object getBuffer()
	{
		return getPart().getBuffer();
	}

	@Override
	public N mergeNetworks(Collection<N> toMerge)
	{
		return getPart().createNetworkByMerging(toMerge);
	}

	@Override
	public TransmissionType getTransmissionType()
	{
		return getPart().getTransmissionType();
	}

	public PartTransmitter<A, N> getPart()
	{
		return containingPart;
	}

	public void setPart(PartTransmitter<A, N> containingPart)
	{
		this.containingPart = containingPart;
	}
}
