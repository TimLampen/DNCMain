package me.timlampen.util;


import net.minecraft.server.v1_8_R1.Explosion;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;

public class Explosions {
	public void explode(Location loc, Entity explodeAs, float level, boolean setFires, boolean terrainDamage) {
		Explosion explosion = new Explosion(((CraftWorld) loc.getWorld()).getHandle(),
				((CraftEntity) explodeAs).getHandle(), loc.getX(), loc.getY(), loc.getZ(), level, setFires, terrainDamage);
		explosion.a();
		explosion.a(true);
		loc.getWorld().playEffect(loc, Effect.SMOKE, 4);
		}
}
