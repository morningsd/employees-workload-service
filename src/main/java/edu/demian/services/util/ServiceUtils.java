package edu.demian.services.util;

import edu.demian.exceptions.ResourceHasNoSuchPropertyException;
import java.lang.reflect.Field;
import java.util.Map;
import org.springframework.util.ReflectionUtils;

public class ServiceUtils {

  public static <T> T applyPatches(T entityToPatch, Map<String, Object> partialUpdates, Class<?> clazz) {
    partialUpdates.remove("id");
    partialUpdates.forEach(
        (k, v) -> {
          Field field = ReflectionUtils.findField(clazz, k);
          if (field == null) {
            throw new ResourceHasNoSuchPropertyException("Department has no field: " + k);
          }
          field.setAccessible(true);
          ReflectionUtils.setField(field, entityToPatch, v);
        });
    return entityToPatch;
  }

}
