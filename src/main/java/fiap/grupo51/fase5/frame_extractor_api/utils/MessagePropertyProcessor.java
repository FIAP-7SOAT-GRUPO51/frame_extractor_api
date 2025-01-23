package fiap.grupo51.fase5.frame_extractor_api.utils;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ResourceBundle;

@Component
public class MessagePropertyProcessor {

    @Value("${spring.messages.basename:messages}")
    private String baseName;

    private ResourceBundle resourceBundle;

    @PostConstruct
    public void init() {
        resourceBundle = ResourceBundle.getBundle(baseName);
    }

    public void process(Object bean) throws IllegalAccessException {
        Class<?> clazz = bean.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(MessageProperty.class)) {
                MessageProperty annotation = field.getAnnotation(MessageProperty.class);
                String key = annotation.value();
                String value = resourceBundle.getString(key);
                field.setAccessible(true);
                field.set(bean, value);
            }
        }
    }
}