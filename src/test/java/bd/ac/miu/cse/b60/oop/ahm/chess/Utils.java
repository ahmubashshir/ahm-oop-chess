package bd.ac.miu.cse.b60.oop.ahm.chess;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public final class Utils {

	@FunctionalInterface
	interface ReflectiveMethod<R> {
		R call(Object... t);
	}

	public static final <R> ReflectiveMethod<R> find(
	    Class<?> c,
	    String name,
	    Class<?>... param
	) {
		return (Object... t) -> {
			try {
				Method m = c.getDeclaredMethod(name, param);
				m.setAccessible(true);
				Object result;
				if (Modifier.isStatic(m.getModifiers())) {
					result = m.invoke(null, t);
				} else {
					result = m.invoke(t[0], Arrays.copyOfRange(t, 1, t.length));
				}
				@SuppressWarnings("unchecked")
				R castedResult = (R) result;
				return castedResult;
			} catch (
				    NoSuchMethodException
				    | IllegalAccessException
				    | InvocationTargetException e
				) {
				throw new RuntimeException(e);
			}
		};
	}
}
