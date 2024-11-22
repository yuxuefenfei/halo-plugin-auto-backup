package cn.wangwenzhu.autobackup;

import lombok.Data;

@Data
public class IntervalVo {

    private String label;

    private int value;

    public IntervalVo(String label, int value) {
        this.label = label;
        this.value = value;
    }
}
