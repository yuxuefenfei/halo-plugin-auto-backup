package cn.wangwenzhu.autobackup;

import java.util.Objects;
import lombok.Data;

@Data
public class AutoBackupConfig {

    private String timeUnit;
    private int interval;
    private int maxBackupCount;

    public AutoBackupConfig() {
    }

    public AutoBackupConfig(String timeUnit, int interval, int maxBackupCount) {
        this.timeUnit = timeUnit;
        this.interval = interval;
        this.maxBackupCount = maxBackupCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AutoBackupConfig that)) {
            return false;
        }
        return interval == that.interval &&
            maxBackupCount == that.maxBackupCount &&
            Objects.equals(timeUnit, that.timeUnit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeUnit, interval, maxBackupCount);
    }
}
