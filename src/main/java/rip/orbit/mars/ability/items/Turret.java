package rip.orbit.mars.ability.items;

/**
 * @author LBuddyBoy (lbuddyboy.me)
 * 02/07/2021 / 1:39 AM
 * HCTeams / rip.orbit.mars.ability.items
 */
public class Turret  {

//	public Cooldowns cd = new Cooldowns();
//
//	public static List<Gun> turrets = new ArrayList<>();
//
//	@Override
//	public Cooldowns cooldown() {
//		return cd;
//	}
//
//	@Override
//	public String name() {
//		return "turret";
//	}
//
//	@Override
//	public String displayName() {
//		return CC.translate("&6&lTurret");
//	}
//
//	@Override
//	public int data() {
//		return 0;
//	}
//
//	@Override
//	public Material mat() {
//		return Material.DISPENSER;
//	}
//
//	@Override
//	public boolean glow() {
//		return true;
//	}
//
//	@Override
//	public List<String> lore() {
//		return CC.translate(
//				Arrays.asList(
//						" ",
//						"&7Place this to start a 10 second period that anyone",
//						"&7not in your faction will be targetted by the turret",
//						"&7and shot.",
//						" ",
//						"&c&lNOTE&7: This turret only shoots people not on your team and from 8 blocks away.",
//						" "
//				)
//		);
//	}
//
//	@Override
//	public List<String> foundInfo() {
//		return CC.translate(Arrays.asList(
//				"Ability Packages",
//				"Partner Crates"
//		));
//	}
//
//	@EventHandler(priority = EventPriority.LOWEST)
//	public void onPlace(BlockPlaceEvent event) {
//		if (event.isCancelled())
//			return;
//		if (isSimilar(event.getItemInHand())) {
//			if (!canUse(event.getPlayer())) {
//				event.setCancelled(true);
//				return;
//			}
//			addCooldown(event.getPlayer(), 60);
//			Location loc = event.getBlock().getLocation();
//			BukkitTask bukkitTask = new BukkitRunnable() {
//				@Override
//				public void run() {
//					for (Player near : event.getBlock().getWorld().getEntitiesByClass(Player.class)) {
//						if (near.getLocation().distance(loc) <= 8) {
//							Team team = HCF.getInstance().getTeamHandler().getTeam(event.getPlayer().getUniqueId());
//							if (team != null) {
//								if (team.getOnlineMembers().contains(near))
//									continue;
//							}
//							if (near.getUniqueId() == event.getPlayer().getUniqueId())
//								continue;
//							Location location = event.getBlock().getLocation().add(0.5, 0, 0.5);
//
//							Zombie z = event.getBlock().getWorld().spawn(location, Zombie.class);
//							z.getEquipment().setItemInHand(null);
//							z.getEquipment().setHelmet(null);
//							z.getEquipment().setChestplate(null);
//							z.getEquipment().setLeggings(null);
//							z.getEquipment().setBoots(null);
//							z.setTarget(near);
//
//							z.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 255, 255));
//							new BukkitRunnable() {
//								@Override
//								public void run() {
//									Arrow snowball = z.launchProjectile(Arrow.class);
//									snowball.setMetadata("turret", new FixedMetadataValue(HCF.getInstance(), true));
//									snowball.setCritical(true);
//									z.remove();
//								}
//							}.runTaskLater(HCF.getInstance(), 15);
//
//						}
//					}
//				}
//			}.runTaskTimer(HCF.getInstance(), 20, 20);
//			Gun gun = new Gun(event.getPlayer().getUniqueId(), 0, loc, bukkitTask);
//
//			turrets.add(gun);
//			new BukkitRunnable() {
//				@Override
//				public void run() {
//					gun.getTask().cancel();
//					gun.getLocation().getBlock().setType(Material.AIR);
//					turrets.remove(gun);
//				}
//			}.runTaskLater(HCF.getInstance(), 20 * 10);
//		}
//	}
//
//	@EventHandler
//	public void onHit(BlockBreakEvent event) {
//		for (Gun gun : turrets) {
//			if (gun.getLocation().distance(event.getBlock().getLocation()) < 1) {
//				event.setCancelled(true);
//				event.getPlayer().sendMessage(CC.translate("&cYou cannot break turrets."));
//			}
//		}
//	}
//
//	@EventHandler
//	public void onHit(PlayerInteractEvent event) {
//		if (event.getClickedBlock() == null)
//			return;
//		if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
//			for (Gun gun : turrets) {
//				if (gun.getLocation().distance(event.getClickedBlock().getLocation()) < 1) {
//					gun.setHits(gun.getHits() + 1);
//					if (gun.getHits() == 10) {
//						gun.getLocation().getBlock().setType(Material.AIR);
//						gun.getTask().cancel();
//						turrets.remove(gun);
//					}
//				}
//			}
//		}
//	}
//
//	@EventHandler
//	public void onLeave(PlayerQuitEvent event) {
//		for (Gun turret : turrets) {
//			if (turret.getOwner() == event.getPlayer().getUniqueId()) {
//				turret.getTask().cancel();
//				turret.getLocation().getBlock().setType(Material.AIR);
//				turrets.remove(turret);
//			}
//		}
//	}

}
