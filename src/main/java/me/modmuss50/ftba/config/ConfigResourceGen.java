package me.modmuss50.ftba.config;

import com.google.gson.Gson;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by modmuss50 on 22/06/2017.
 */
public class ConfigResourceGen {
	public static List<DuplicationResource> duplicationResources = new ArrayList<>();

	public static File configFile;

	private static Gson gson = new Gson();

	public static void save() throws IOException {
		if (duplicationResources.isEmpty()) {
			return;
		}
		NBTTagCompound tagCompound = new NBTTagCompound();
		tagCompound.setInteger("size", duplicationResources.size());
		for (int i = 0; i < duplicationResources.size(); i++) {
			tagCompound.setTag(i + "", toNBT(duplicationResources.get(i)));
		}
		CompressedStreamTools.write(tagCompound, configFile);
	}

	static NBTTagCompound toNBT(DuplicationResource resource) {
		NBTTagCompound tagCompound = new NBTTagCompound();

		NBTTagCompound stackTag = new NBTTagCompound();
		resource.stack.writeToNBT(stackTag);
		tagCompound.setTag("stack", stackTag);

		tagCompound.setInteger("powerUsage", resource.powerUsage);
		tagCompound.setInteger("processTime", resource.processTime);
		return tagCompound;
	}

	static DuplicationResource fromNBT(NBTTagCompound compound) {
		DuplicationResource duplicationResource = new DuplicationResource(new ItemStack(compound.getCompoundTag("stack")), compound.getInteger("powerUsage"), compound.getInteger("processTime"));
		return duplicationResource;
	}

	public static void load() throws IOException {
		if (!configFile.exists()) {
			return;
		}
		duplicationResources.clear();
		NBTTagCompound compound = CompressedStreamTools.read(configFile);

		for (int i = 0; i < compound.getInteger("size"); i++) {
			NBTTagCompound data = compound.getCompoundTag(i + "");
			DuplicationResource resource = fromNBT(data);
			duplicationResources.add(resource);
		}

	}

}
