/**
 * Handles accessing, saving, updating, and presentation of player settings.
 *
 * This includes the /settings command, a settings menu, persistence, etc.
 * Clients using the settings API should only concern themselves with {@link rip.orbit.mars.setting.event.SettingUpdateEvent},
 * {@link rip.orbit.mars.setting.SettingHandler#getSetting(java.util.UUID, Setting)} and
 * {@link rip.orbit.mars.setting.SettingHandler#updateSetting(org.bukkit.entity.Player, Setting, boolean)},
 */
package rip.orbit.mars.setting;