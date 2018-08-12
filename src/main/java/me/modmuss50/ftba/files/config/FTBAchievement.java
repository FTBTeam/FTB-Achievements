package me.modmuss50.ftba.files.config;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWrittenBook;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StringUtils;

import java.util.List;

/**
 * Created by Mark on 04/02/2017.
 */
public class FTBAchievement {

	public String name;

	public String description;

	public List<ItemStack> rewards;

	public ItemStack iconStack;

	public ItemStack bookStack;

	public String chatMessage;

	public boolean fireOnce = true;

	public boolean giveToAll = false;

	public String dependsOn;

	public String getTitle() {
		if (isValidBook(bookStack)) {
			NBTTagCompound nbttagcompound = bookStack.getTagCompound();
			String s = nbttagcompound.getString("title");

			if (!StringUtils.isNullOrEmpty(s)) {
				return s;
			}
		}
		return name;
	}

	private static boolean isValidBook(ItemStack stack) {
		if (stack == null || stack.getItem() != Items.WRITTEN_BOOK) {
			return false;
		}
		if (!stack.hasTagCompound()) {
			return false;
		}
		return ItemWrittenBook.validBookTagContents(stack.getTagCompound());
	}

	private NBTTagList getBookPages(ItemStack stack) {
		if (isValidBook(stack)) {
			NBTTagCompound nbttagcompound = stack.getTagCompound();
			return nbttagcompound.getTagList("pages", 8);
		}
		return null;
	}

	public String getPageData(int page) {
		NBTTagList bookPages = getBookPages(bookStack);
		return bookPages != null && page >= 0 && page < bookPages.tagCount() ? bookPages.getStringTagAt(page) : "";
	}
}
