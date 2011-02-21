package assemblers.helpers;

import java.lang.reflect.Field;
import play.Logger;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class ModelMerger {
	
	/**
	 * Copies all fields from object1 to object2 (where the fields exist on object2).
	 * 
	 * Does not copy the "id" field.
	 * 
	 * @param object1
	 * @param object2
	 * @return
	 */
	public static Object merge(Object object1, Object object2) {
		Field[] fields = object1.getClass().getDeclaredFields();
		for (Field dtoField : fields) {
			try {
				if (dtoField.get(object1) != null && !dtoField.getName().equals("id")) {
					try {
						Field objectField = object2.getClass().getDeclaredField(dtoField.getName());
						objectField.set(object2, dtoField.get(object1));
					} catch (SecurityException e) {
						Logger.error("SecurityException", e.getLocalizedMessage());
					} catch (NoSuchFieldException e) {
						Logger.debug("NoSuchFieldException for " + dtoField.getName(), e.getLocalizedMessage());
					}
				}
			} catch (IllegalArgumentException e) {
				Logger.error("IllegalArgumentException", e.getLocalizedMessage());
			} catch (IllegalAccessException e) {
				Logger.error("IllegalAccessException", e.getLocalizedMessage());
			}
		}
		return object2;
	}

}
