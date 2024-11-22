package cn.wangwenzhu.autobackup;

import java.util.Objects;
import lombok.Data;

@Data
public class AutoBackupConfig {

    private String timeUnit;
    private int interval;

    public AutoBackupConfig() {
    }

    public AutoBackupConfig(String timeUnit, int interval) {
        this.timeUnit = timeUnit;
        this.interval = interval;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AutoBackupConfig that = (AutoBackupConfig) o;
        return interval == that.interval && Objects.equals(timeUnit, that.timeUnit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeUnit, interval);
    }
}
