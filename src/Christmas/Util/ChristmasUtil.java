package Christmas.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import Christmas.Christmas;

public class ChristmasUtil {

	public ArrayList<String> player = new ArrayList<String>();

	public Christmas main;
	public int datesched;

	public ChristmasUtil(Christmas main) {
		this.main = main;
	}

	public final String date = (new SimpleDateFormat("dd.MM.yyyy").format(new Date()));

	public ArrayList<String> getOpener(String name) {
		name = player.get(0);
		return player;
	}

	public String replaceColorCodes(String message) {
		return message.replaceAll("(?i)&([a-n0-9])", "�$1");
	}

	public void removeOpener(ArrayList<String> arrayList, String name) {
		arrayList.remove(name);
	}

	public void sendOpenedDoor(Player player) {
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DAY_OF_MONTH);
		String nowday = String.valueOf(day);
		String message = main.getConfig().getString("Messages.OpenedDoor");
		Bukkit.broadcastMessage(main.prefix + replaceColorCodes(message).replace("%player%", player.getName()).replace("%day%", nowday));
	}

	public void startScheduler(final Christmas main) {
		datesched = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {
			public void run() {
				main.ccl.load();
				Configuration cc = main.ccl.getConfig();
				int x = cc.getInt("ChristmasSign.X");
				int y = cc.getInt("ChristmasSign.Y");
				int z = cc.getInt("ChristmasSign.Z");
				String world = cc.getString("ChristmasSign.World");
				World w = main.getServer().getWorld(world);
				Block sb = w.getBlockAt(x, y, z);
				Sign s = (Sign) sb.getState();
				Calendar cal = Calendar.getInstance();
				Date currentDate = cal.getTime();
				String signLine = s.getLine(1).replace("�b", "");
				String cdate = new SimpleDateFormat("dd.MM.yyyy").format(currentDate);
				if (!(cdate.equalsIgnoreCase(signLine))) {
					s.setLine(1, ChatColor.AQUA + cdate);
					s.update();
					main.getLogger().log(Level.INFO, "Sign has been updated!");
				} else {
					main.getLogger().log(Level.INFO, "No need to update the sign.");
				}
			}
		}, 200L, 1800 * 20L);
	}

	public void stopScheduler(Christmas main) {
		main.getServer().getScheduler().cancelTask(datesched);
	}
}
