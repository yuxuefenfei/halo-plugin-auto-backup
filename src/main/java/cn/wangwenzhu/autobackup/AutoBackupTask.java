package cn.wangwenzhu.autobackup;

import cn.wangwenzhu.autobackup.scheduled.AbstractReschedulingConfigurer;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.scheduling.config.Task;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;
import run.halo.app.extension.ExtensionClient;
import run.halo.app.extension.Metadata;
import run.halo.app.migration.Backup;
import run.halo.app.plugin.SettingFetcher;

@Log4j2
@Component
public class AutoBackupTask extends AbstractReschedulingConfigurer {

    private final static Map<String, ChronoUnit> timeUnitMapping = Map.of(
        "HOUR", ChronoUnit.HOURS,
        "DAY", ChronoUnit.DAYS
    );

    private final ExtensionClient client;

    private final SettingFetcher settingFetcher;

    public AutoBackupTask(ExtensionClient client, SettingFetcher settingFetcher) {
        super(true);
        this.client = client;
        this.settingFetcher = settingFetcher;
    }

    private void autoBackup() {
        log.info("Auto backup task started");

        String seq = Long.toHexString(System.currentTimeMillis() / 1000);

        var metadata = new Metadata();
        metadata.setName("backup-" + seq);
        var backup = new Backup();
        backup.setMetadata(metadata);
        backup.getSpec().setFormat("zip");

        Date expiresAt = DateUtils.addDays(new Date(), 7);
        backup.getSpec().setExpiresAt(expiresAt.toInstant());

        client.create(backup);

        log.info("Auto backup task finished");
    }

    @Override
    public Task configureTask() {
        return new TriggerTask(this::autoBackup, triggerContext -> {

            Optional<AutoBackupConfig> config =
                settingFetcher.fetch("base", AutoBackupConfig.class);

            if (config.isPresent()) {
                String timeUnit = config.get().getTimeUnit();
                int interval = config.get().getInterval();

                PeriodicTrigger trigger = new PeriodicTrigger(
                    Duration.of(interval, timeUnitMapping.get(timeUnit))
                );
                return trigger.nextExecution(triggerContext);
            }
            return null;
        });
    }
}
