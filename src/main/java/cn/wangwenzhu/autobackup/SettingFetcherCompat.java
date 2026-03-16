package cn.wangwenzhu.autobackup;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SettingFetcherCompat {

    private final Object settingFetcherBean;
    private final Method fetchMethod;

    public SettingFetcherCompat(ApplicationContext applicationContext) {
        try {
            Class<?> settingFetcherClass = Class.forName("run.halo.app.plugin.SettingFetcher");
            this.settingFetcherBean = applicationContext.getBean(settingFetcherClass);
            this.fetchMethod = settingFetcherClass.getMethod("fetch", String.class, Class.class);
        } catch (ClassNotFoundException | NoSuchMethodException ex) {
            throw new IllegalStateException("Cannot resolve Halo SettingFetcher API", ex);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> fetch(String group, Class<T> clazz) {
        try {
            Object result = fetchMethod.invoke(settingFetcherBean, group, clazz);
            if (!(result instanceof Optional<?> optional)) {
                return Optional.empty();
            }
            return optional.map(clazz::cast);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new IllegalStateException("Failed to invoke SettingFetcher.fetch", ex);
        }
    }
}
