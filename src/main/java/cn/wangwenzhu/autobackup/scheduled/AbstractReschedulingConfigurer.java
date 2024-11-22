package cn.wangwenzhu.autobackup.scheduled;

import java.time.Instant;
import javax.annotation.Nonnull;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.config.Task;
import org.springframework.scheduling.config.TriggerTask;

public abstract class AbstractReschedulingConfigurer implements SchedulingConfigurer {

    private ScheduledTaskRegistrar taskRegistrar;

    private ScheduledTask scheduledTask;

    private boolean immediate;

    public AbstractReschedulingConfigurer(boolean immediate) {
        this.immediate = immediate;
    }

    public abstract Task configureTask();

    @Override
    public void configureTasks(@Nonnull ScheduledTaskRegistrar taskRegistrar) {
        this.taskRegistrar = taskRegistrar;

        Task task = configureTask();
        if (task instanceof TriggerTask target) {
            this.scheduledTask = taskRegistrar.scheduleTriggerTask(warpTask(target));
        }
    }

    public void rescheduleTask(boolean immediate) {
        if (scheduledTask != null) {
            Task task = scheduledTask.getTask();
            if (task instanceof TriggerTask target) {
                cancelTask();
                this.immediate = immediate;
                this.scheduledTask = taskRegistrar.scheduleTriggerTask(target);
            }
        }
    }

    private TriggerTask warpTask(TriggerTask task) {
        return new TriggerTask(() -> {
            if (!immediate) {
                immediate = true;
                return;
            }

            task.getRunnable().run();
        }, triggerContext -> {
            Trigger trigger = task.getTrigger();

            Instant nextedExecution = trigger.nextExecution(triggerContext);
            if (nextedExecution == null) {
                cancelTask();
            }
            return nextedExecution;
        });
    }

    private void cancelTask() {
        if (scheduledTask != null) {
            scheduledTask.cancel();
        }
    }
}
