package rip.orbit.mars.kittype.command;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import rip.orbit.mars.Mars;
import rip.orbit.mars.kittype.KitType;
import cc.fyre.proton.command.Command;
import net.minecraft.util.com.google.common.reflect.TypeToken;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;

public class KitTypeImportExportCommands {

	@Command(names = "kittype export", permission = "op", async = true)
	public static void executeExport(CommandSender sender) {
		String json = Mars.getGson().toJson(KitType.getAllTypes());

		try {
			Files.write(
					json,
					new File(Mars.getInstance().getDataFolder(), "kitTypes.json"),
					Charsets.UTF_8
			);

			sender.sendMessage(ChatColor.GREEN + "Exported.");
		} catch (IOException e) {
			e.printStackTrace();
			sender.sendMessage(ChatColor.RED + "Failed to export.");
		}
	}

	@Command(names = "kittype import", permission = "op", async = true)
	public static void executeImport(CommandSender sender) {
		File file = new File(Mars.getInstance().getDataFolder(), "kitTypes.json");

		if (file.exists()) {
			try (Reader schematicsFileReader = Files.newReader(file, Charsets.UTF_8)) {
				Type schematicListType = new TypeToken<List<KitType>>() {}.getType();
				List<KitType> kitTypes = Mars.getGson().fromJson(schematicsFileReader, schematicListType);

				for (KitType kitType : kitTypes) {
					KitType.getAllTypes().removeIf(otherKitType -> otherKitType.getId().equals(kitType.getId()));
					KitType.getAllTypes().add(kitType);
					kitType.saveAsync();
				}
			} catch (IOException e) {
				e.printStackTrace();
				sender.sendMessage(ChatColor.RED + "Failed to import.");
			}
		}

		sender.sendMessage(ChatColor.GREEN + "Imported.");
	}

}
