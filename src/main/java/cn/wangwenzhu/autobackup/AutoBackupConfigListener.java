package cn.wangwenzhu.autobackup;

import lombok.extern.java.Log;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import run.halo.app.infra.utils.JsonUtils;
import run.halo.app.plugin.PluginConfigUpdatedEvent;

@Log
@Component
public class AutoBackupConfigListener {

    private final AutoBackupTask autoBackupTask;

    public AutoBackupConfigListener(AutoBackupTask autoBackupTask) {
        this.autoBackupTask = autoBackupTask;
    }


    @EventListener
    public void onConfigUpdated(PluginConfigUpdatedEvent event) {

        AutoBackupConfig newConfig = JsonUtils.jsonToObject(
            event.getNewConfig().get("base").toString(), AutoBackupConfig.class);

        AutoBackupConfig oldConfig = JsonUtils.jsonToObject(
            event.getOldConfig().get("base").toString(), AutoBackupConfig.class);

        if (!newConfig.equals(oldConfig)) {
            log.info("AutoBackup plugin config updated");
            autoBackupTask.rescheduleTask(false);
        }
    }
}
