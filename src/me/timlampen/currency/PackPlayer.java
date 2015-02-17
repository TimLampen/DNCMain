package me.timlampen.currency;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

public class PackPlayer {
	public ArrayList<Integer> slots = new ArrayList<>();
	public ArrayList<ItemStack> items = new ArrayList<>();
	
	
	public ArrayList<Integer> getSlots(){
		return slots;
	}
	
	public Integer getSlot(int i){
		return slots.get(i);
	}
	
	public ArrayList<ItemStack> getItems(){
		return items;
	}
	
	public ItemStack getItem(int i){
		return items.get(i);
	}
	
	public void addItems(int slot, ItemStack is){
		slots.add(slot);
		items.add(is);
	}
	
	public void clear(){
		slots.clear();
		items.clear();
	}
}
