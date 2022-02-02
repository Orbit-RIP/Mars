/**
 * Handles accessing, saving, updating, and presentation of player settings.
 *
 * This includes the /settings commands, a settings menu, persistence, etc.
 * Clients using the settings API should only concern themselves with {@link rip.bridge.practice.setting.event.SettingUpdateEvent},
 * {@link rip.bridge.practice.setting.SettingHandler#getSetting(org.bukkit.entity.Player, rip.bridge.practice.setting.Setting)} (java.util.UUID, Setting)} and
 * {@link rip.bridge.practice.setting.SettingHandler#updateSetting(org.bukkit.entity.Player, Setting, boolean)},
 */
package rip.bridge.practice.setting;