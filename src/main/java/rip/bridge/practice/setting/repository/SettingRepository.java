package rip.bridge.practice.setting.repository;

import rip.bridge.practice.setting.Setting;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public interface SettingRepository {

    Map<Setting, Boolean> loadSettings(UUID playerUuid) throws IOException;
    void saveSettings(UUID playerUuid, Map<Setting, Boolean> settings) throws IOException;

}