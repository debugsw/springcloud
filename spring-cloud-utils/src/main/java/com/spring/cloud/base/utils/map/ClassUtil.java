package com.spring.cloud.base.utils.map;

import com.spring.cloud.base.utils.*;
import com.spring.cloud.base.utils.base.ReflectUtil;
import com.spring.cloud.base.utils.exception.IORuntimeException;
import com.spring.cloud.base.utils.exception.UtilException;
import com.spring.cloud.base.utils.interf.Filter;
import com.spring.cloud.base.utils.str.StrUtil;
import com.spring.cloud.base.utils.utils.ArrayUtil;
import com.spring.cloud.base.utils.utils.CharsetUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.time.temporal.TemporalAccessor;
import java.util.*;

/**
 * @Author: ls
 * @Description: 类工具类
 * @Date: 2023/4/13 16:11
 */
public class ClassUtil {

	/**
	 * {@code null}安全的获取对象类型
	 *
	 * @param <T> 对象类型
	 * @param obj 对象，如果为{@code null} 返回{@code null}
	 * @return 对象类型，提供对象如果为{@code null} 返回{@code null}
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getClass(T obj) {
		return ((null == obj) ? null : (Class<T>) obj.getClass());
	}

	/**
	 * 获取类名
	 *
	 * @param obj      获取类名对象
	 * @param isSimple 是否简单类名，如果为true，返回不带包名的类名
	 * @return 类名
	 */
	public static String getClassName(Object obj, boolean isSimple) {
		if (null == obj) {
			return null;
		}
		final Class<?> clazz = obj.getClass();
		return getClassName(clazz, isSimple);
	}

	/**
	 * 获取类名
	 *
	 * @param clazz    类
	 * @param isSimple 是否简单类名，如果为true，返回不带包名的类名
	 * @return 类名
	 */
	public static String getClassName(Class<?> clazz, boolean isSimple) {
		if (null == clazz) {
			return null;
		}
		return isSimple ? clazz.getSimpleName() : clazz.getName();
	}

	/**
	 * 获得对象数组的类数组
	 *
	 * @param objects 对象数组，如果数组中存在{@code null}元素，则此元素被认为是Object类型
	 * @return 类数组
	 */
	public static Class<?>[] getClasses(Object... objects) {
		Class<?>[] classes = new Class<?>[objects.length];
		Object obj;
		for (int i = 0; i < objects.length; i++) {
			obj = objects[i];
			if (obj instanceof NullWrapperBean) {
				classes[i] = ((NullWrapperBean<?>) obj).getWrappedClass();
			} else if (null == obj) {
				classes[i] = Object.class;
			} else {
				classes[i] = obj.getClass();
			}
		}
		return classes;
	}

	/**
	 * 指定类是否与给定的类名相同
	 *
	 * @param clazz      类
	 * @param className  类名，可以全类名（包含包名），也可以简单类名（不包含包名）
	 * @param ignoreCase 是否忽略大小写
	 * @return 指定类是否与给定的类名相同
	 */
	public static boolean equals(Class<?> clazz, String className, boolean ignoreCase) {
		if (null == clazz || StrUtil.isBlank(className)) {
			return false;
		}
		if (ignoreCase) {
			return className.equalsIgnoreCase(clazz.getName()) || className.equalsIgnoreCase(clazz.getSimpleName());
		} else {
			return className.equals(clazz.getName()) || className.equals(clazz.getSimpleName());
		}
	}

	/**
	 * 扫描指定包路径下所有指定类或接口的子类或实现类
	 *
	 * @param packageName 包路径
	 * @param superClass  父类或接口
	 * @return 类集合
	 */
	public static Set<Class<?>> scanPackageBySuper(String packageName, final Class<?> superClass) {
		return ClassScanner.scanPackageBySuper(packageName, superClass);
	}

	/**
	 * 扫描该包路径下所有class文件
	 *
	 * @return 类集合
	 */
	public static Set<Class<?>> scanPackage() {
		return ClassScanner.scanPackage();
	}

	/**
	 * 扫描该包路径下所有class文件
	 *
	 * @param packageName 包路径 com | com. | com.abs | com.abs.
	 * @return 类集合
	 */
	public static Set<Class<?>> scanPackage(String packageName) {
		return ClassScanner.scanPackage(packageName);
	}

	/**
	 * 扫描包路径下满足class过滤器条件的所有class文件
	 *
	 * @param packageName 包路径 com | com. | com.abs | com.abs.
	 * @param classFilter class过滤器，过滤掉不需要的class
	 * @return 类集合
	 */
	public static Set<Class<?>> scanPackage(String packageName, Filter<Class<?>> classFilter) {
		return ClassScanner.scanPackage(packageName, classFilter);
	}

	/**
	 * 获得指定类中的Public方法名<br>
	 * 去重重载的方法
	 *
	 * @param clazz 类
	 * @return 方法名Set
	 */
	public static Set<String> getPublicMethodNames(Class<?> clazz) {
		return ReflectUtil.getPublicMethodNames(clazz);
	}

	/**
	 * 获得本类及其父类所有Public方法
	 *
	 * @param clazz 查找方法的类
	 * @return 过滤后的方法列表
	 */
	public static Method[] getPublicMethods(Class<?> clazz) {
		return ReflectUtil.getPublicMethods(clazz);
	}

	/**
	 * 获得指定类过滤后的Public方法列表
	 *
	 * @param clazz          查找方法的类
	 * @param excludeMethods 不包括的方法
	 * @return 过滤后的方法列表
	 */
	public static List<Method> getPublicMethods(Class<?> clazz, Method... excludeMethods) {
		return ReflectUtil.getPublicMethods(clazz, excludeMethods);
	}

	/**
	 * 获得指定类过滤后的Public方法列表
	 *
	 * @param clazz              查找方法的类
	 * @param excludeMethodNames 不包括的方法名列表
	 * @return 过滤后的方法列表
	 */
	public static List<Method> getPublicMethods(Class<?> clazz, String... excludeMethodNames) {
		return ReflectUtil.getPublicMethods(clazz, excludeMethodNames);
	}

	/**
	 * 查找指定Public方法 如果找不到对应的方法或方法不为public的则返回{@code null}
	 *
	 * @param clazz      类
	 * @param methodName 方法名
	 * @param paramTypes 参数类型
	 * @return 方法
	 * @throws SecurityException 无权访问抛出异常
	 */
	public static Method getPublicMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) throws SecurityException {
		return ReflectUtil.getPublicMethod(clazz, methodName, paramTypes);
	}

	/**
	 * 获得指定类中的Public方法名<br>
	 * 去重重载的方法
	 *
	 * @param clazz 类
	 * @return 方法名Set
	 */
	public static Set<String> getDeclaredMethodNames(Class<?> clazz) {
		return ReflectUtil.getMethodNames(clazz);
	}

	/**
	 * 获得声明的所有方法，包括本类及其父类和接口的所有方法和Object类的方法
	 *
	 * @param clazz 类
	 * @return 方法数组
	 */
	public static Method[] getDeclaredMethods(Class<?> clazz) {
		return ReflectUtil.getMethods(clazz);
	}

	/**
	 * 查找指定对象中的所有方法（包括非public方法），也包括父对象和Object类的方法
	 *
	 * @param obj        被查找的对象
	 * @param methodName 方法名
	 * @param args       参数
	 * @return 方法
	 * @throws SecurityException 无访问权限抛出异常
	 */
	public static Method getDeclaredMethodOfObj(Object obj, String methodName, Object... args) throws SecurityException {
		return getDeclaredMethod(obj.getClass(), methodName, getClasses(args));
	}

	/**
	 * 查找指定类中的所有方法（包括非public方法），也包括父类和Object类的方法 找不到方法会返回
	 *
	 * @param clazz          被查找的类
	 * @param methodName     方法名
	 * @param parameterTypes 参数类型
	 * @return 方法
	 * @throws SecurityException 无访问权限抛出异常
	 */
	public static Method getDeclaredMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws SecurityException {
		return ReflectUtil.getMethod(clazz, methodName, parameterTypes);
	}

	/**
	 * 查找指定类中的所有字段（包括非public字段）， 字段不存在则返回
	 *
	 * @param clazz     被查找字段的类
	 * @param fieldName 字段名
	 * @return 字段
	 * @throws SecurityException 安全异常
	 */
	public static Field getDeclaredField(Class<?> clazz, String fieldName) throws SecurityException {
		if (null == clazz || StrUtil.isBlank(fieldName)) {
			return null;
		}
		try {
			return clazz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {

		}
		return null;
	}

	/**
	 * 查找指定类中的所有字段（包括非public字段)
	 *
	 * @param clazz 被查找字段的类
	 * @return 字段
	 * @throws SecurityException 安全异常
	 */
	public static Field[] getDeclaredFields(Class<?> clazz) throws SecurityException {
		if (null == clazz) {
			return null;
		}
		return clazz.getDeclaredFields();
	}


	/**
	 * 获得ClassPath，不解码路径中的特殊字符（例如空格和中文）
	 *
	 * @return ClassPath集合
	 */
	public static Set<String> getClassPathResources() {
		return getClassPathResources(false);
	}

	/**
	 * 获得ClassPath
	 *
	 * @param isDecode 是否解码路径中的特殊字符（例如空格和中文）
	 * @return ClassPath集合
	 */
	public static Set<String> getClassPathResources(boolean isDecode) {
		return getClassPaths(StrUtil.EMPTY, isDecode);
	}

	/**
	 * 获得ClassPath，不解码路径中的特殊字符（例如空格和中文）
	 *
	 * @param packageName 包名称
	 * @return ClassPath路径字符串集合
	 */
	public static Set<String> getClassPaths(String packageName) {
		return getClassPaths(packageName, false);
	}

	/**
	 * 获得ClassPath
	 *
	 * @param packageName 包名称
	 * @param isDecode    是否解码路径中的特殊字符（例如空格和中文）
	 * @return ClassPath路径字符串集合
	 */
	public static Set<String> getClassPaths(String packageName, boolean isDecode) {
		String packagePath = packageName.replace(StrUtil.DOT, StrUtil.SLASH);
		Enumeration<URL> resources;
		try {
			resources = getClassLoader().getResources(packagePath);
		} catch (IOException e) {
			throw new UtilException(e, "Loading classPath [{}] error!", packagePath);
		}
		final Set<String> paths = new HashSet<>();
		String path;
		while (resources.hasMoreElements()) {
			path = resources.nextElement().getPath();
			paths.add(isDecode ? URLUtil.decode(path, CharsetUtil.systemCharsetName()) : path);
		}
		return paths;
	}

	/**
	 * 获得ClassPath，将编码后的中文路径解码为原字符<br>
	 * 这个ClassPath路径会文件路径被标准化处理
	 *
	 * @return ClassPath
	 */
	public static String getClassPath() {
		return getClassPath(false);
	}

	/**
	 * 获得ClassPath，这个ClassPath路径会文件路径被标准化处理
	 *
	 * @param isEncoded 是否编码路径中的中文
	 * @return ClassPath
	 */
	public static String getClassPath(boolean isEncoded) {
		final URL classPathURL = getClassPathURL();
		String url = isEncoded ? classPathURL.getPath() : URLUtil.getDecodedPath(classPathURL);
		return FileUtil.normalize(url);
	}

	/**
	 * 获得ClassPath URL
	 *
	 * @return ClassPath URL
	 */
	public static URL getClassPathURL() {
		return getResourceURL(StrUtil.EMPTY);
	}

	/**
	 * 获得资源的URL<br>
	 * 路径用/分隔，例如:
	 *
	 * <pre>
	 * config/a/db.config
	 * spring/xml/test.xml
	 * </pre>
	 *
	 * @param resource 资源（相对Classpath的路径）
	 * @return 资源URL
	 * @see ResourceUtil#getResource(String)
	 */
	public static URL getResourceURL(String resource) throws IORuntimeException {
		return ResourceUtil.getResource(resource);
	}

	/**
	 * 获取指定路径下的资源列表<br>
	 * 路径格式必须为目录格式,用/分隔，例如:
	 *
	 * <pre>
	 * config/a
	 * spring/xml
	 * </pre>
	 *
	 * @param resource 资源路径
	 * @return 资源列表
	 * @see ResourceUtil#getResources(String)
	 */
	public static List<URL> getResources(String resource) {
		return ResourceUtil.getResources(resource);
	}

	/**
	 * 获得资源相对路径对应的URL
	 *
	 * @param resource  资源相对路径
	 * @param baseClass 基准Class，获得的相对路径相对于此Class所在路径，如果为null则相对ClassPath
	 * @return {@link URL}
	 */
	public static URL getResourceUrl(String resource, Class<?> baseClass) {
		return ResourceUtil.getResource(resource, baseClass);
	}

	/**
	 * @return 获得Java ClassPath路径，不包括 jre
	 */
	public static String[] getJavaClassPaths() {
		return System.getProperty("java.class.path").split(System.getProperty("path.separator"));
	}

	/**
	 * 获取当前线程的{@link ClassLoader}
	 *
	 * @return 当前线程的class loader
	 * @see ClassLoaderUtil#getClassLoader()
	 */
	public static ClassLoader getContextClassLoader() {
		return ClassLoaderUtil.getContextClassLoader();
	}

	/**
	 * 获取{@link ClassLoader}
	 *
	 * @return 类加载器
	 */
	public static ClassLoader getClassLoader() {
		return ClassLoaderUtil.getClassLoader();
	}

	/**
	 * 比较判断types1和types2两组类，如果types1中所有的类都与types2对应位置的类相同，或者是其父类或接口，则返回{@code true}
	 *
	 * @param types1 类组1
	 * @param types2 类组2
	 * @return 是否相同、父类或接口
	 */
	public static boolean isAllAssignableFrom(Class<?>[] types1, Class<?>[] types2) {
		if (ArrayUtil.isEmpty(types1) && ArrayUtil.isEmpty(types2)) {
			return true;
		}
		if (null == types1 || null == types2) {
			// 任何一个为null不相等（之前已判断两个都为null的情况）
			return false;
		}
		if (types1.length != types2.length) {
			return false;
		}

		Class<?> type1;
		Class<?> type2;
		for (int i = 0; i < types1.length; i++) {
			type1 = types1[i];
			type2 = types2[i];
			if (isBasicType(type1) && isBasicType(type2)) {
				// 原始类型和包装类型存在不一致情况
				if (BasicType.unWrap(type1) != BasicType.unWrap(type2)) {
					return false;
				}
			} else if (false == type1.isAssignableFrom(type2)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 加载类
	 *
	 * @param <T>           对象类型
	 * @param className     类名
	 * @param isInitialized 是否初始化
	 * @return 类
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> loadClass(String className, boolean isInitialized) {
		return (Class<T>) ClassLoaderUtil.loadClass(className, isInitialized);
	}

	/**
	 * 加载类并初始化
	 *
	 * @param <T>       对象类型
	 * @param className 类名
	 * @return 类
	 */
	public static <T> Class<T> loadClass(String className) {
		return loadClass(className, true);
	}

	/**
	 * 执行方法
	 * @param <T>                     对象类型
	 * @param classNameWithMethodName 类名和方法名表达式，类名与方法名用连接
	 * @param args                    参数，必须严格对应指定方法的参数类型和数量
	 * @return 返回结果
	 */
	public static <T> T invoke(String classNameWithMethodName, Object[] args) {
		return invoke(classNameWithMethodName, false, args);
	}

	/**
	 * 执行方法
	 * @param <T>                     对象类型
	 * @param classNameWithMethodName 类名和方法名表达式
	 * @param isSingleton             是否为单例对象，如果此参数为false，每次执行方法时创建一个新对象
	 * @param args                    参数，必须严格对应指定方法的参数类型和数量
	 * @return 返回结果
	 */
	public static <T> T invoke(String classNameWithMethodName, boolean isSingleton, Object... args) {
		if (StrUtil.isBlank(classNameWithMethodName)) {
			throw new UtilException("Blank classNameDotMethodName!");
		}

		int splitIndex = classNameWithMethodName.lastIndexOf('#');
		if (splitIndex <= 0) {
			splitIndex = classNameWithMethodName.lastIndexOf('.');
		}
		if (splitIndex <= 0) {
			throw new UtilException("Invalid classNameWithMethodName [{}]!", classNameWithMethodName);
		}

		final String className = classNameWithMethodName.substring(0, splitIndex);
		final String methodName = classNameWithMethodName.substring(splitIndex + 1);

		return invoke(className, methodName, isSingleton, args);
	}

	/**
	 * 执行方法
	 *
	 * @param <T>        对象类型
	 * @param className  类名，完整类路径
	 * @param methodName 方法名
	 * @param args       参数，必须严格对应指定方法的参数类型和数量
	 * @return 返回结果
	 */
	public static <T> T invoke(String className, String methodName, Object[] args) {
		return invoke(className, methodName, false, args);
	}

	/**
	 * 执行方法
	 *
	 * @param <T>         对象类型
	 * @param className   类名，完整类路径
	 * @param methodName  方法名
	 * @param isSingleton 是否为单例对象，如果此参数为false，每次执行方法时创建一个新对象
	 * @param args        参数，必须严格对应指定方法的参数类型和数量
	 * @return 返回结果
	 */
	public static <T> T invoke(String className, String methodName, boolean isSingleton, Object... args) {
		Class<Object> clazz = loadClass(className);
		try {
			final Method method = getDeclaredMethod(clazz, methodName, getClasses(args));
			if (null == method) {
				throw new NoSuchMethodException(StrUtil.format("No such method: [{}]", methodName));
			}
			if (isStatic(method)) {
				return ReflectUtil.invoke(null, method, args);
			} else {
				return ReflectUtil.invoke(isSingleton ? Singleton.get(clazz) : clazz.newInstance(), method, args);
			}
		} catch (Exception e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 是否为包装类型
	 *
	 * @param clazz 类
	 * @return 是否为包装类型
	 */
	public static boolean isPrimitiveWrapper(Class<?> clazz) {
		if (null == clazz) {
			return false;
		}
		return BasicType.WRAPPER_PRIMITIVE_MAP.containsKey(clazz);
	}

	/**
	 * 是否为基本类型（包括包装类和原始类）
	 *
	 * @param clazz 类
	 * @return 是否为基本类型
	 */
	public static boolean isBasicType(Class<?> clazz) {
		if (null == clazz) {
			return false;
		}
		return (clazz.isPrimitive() || isPrimitiveWrapper(clazz));
	}

	/**
	 * 是否简单值类型或简单值类型的数组
	 *
	 * @param clazz 属性类
	 * @return 是否简单值类型或简单值类型的数组
	 */
	public static boolean isSimpleTypeOrArray(Class<?> clazz) {
		if (null == clazz) {
			return false;
		}
		return isSimpleValueType(clazz) || (clazz.isArray() && isSimpleValueType(clazz.getComponentType()));
	}

	/**
	 * 是否为简单值类型
	 *
	 * @param clazz 类
	 * @return 是否为简单值类型
	 */
	public static boolean isSimpleValueType(Class<?> clazz) {
		return isBasicType(clazz) //
				|| clazz.isEnum() //
				|| CharSequence.class.isAssignableFrom(clazz) //
				|| Number.class.isAssignableFrom(clazz) //
				|| Date.class.isAssignableFrom(clazz) //
				|| clazz.equals(URI.class) //
				|| clazz.equals(URL.class) //
				|| clazz.equals(Locale.class) //
				|| clazz.equals(Class.class)//
				// jdk8 date object
				|| TemporalAccessor.class.isAssignableFrom(clazz); //
	}

	/**
	 * 检查目标类是否可以从原类转化
	 * @param targetType 目标类型
	 * @param sourceType 原类型
	 * @return 是否可转化
	 */
	public static boolean isAssignable(Class<?> targetType, Class<?> sourceType) {
		if (null == targetType || null == sourceType) {
			return false;
		}

		// 对象类型
		if (targetType.isAssignableFrom(sourceType)) {
			return true;
		}

		// 基本类型
		if (targetType.isPrimitive()) {
			// 原始类型
			Class<?> resolvedPrimitive = BasicType.WRAPPER_PRIMITIVE_MAP.get(sourceType);
			return targetType.equals(resolvedPrimitive);
		} else {
			// 包装类型
			Class<?> resolvedWrapper = BasicType.PRIMITIVE_WRAPPER_MAP.get(sourceType);
			return resolvedWrapper != null && targetType.isAssignableFrom(resolvedWrapper);
		}
	}

	/**
	 * 指定类是否为Public
	 *
	 * @param clazz 类
	 * @return 是否为public
	 */
	public static boolean isPublic(Class<?> clazz) {
		if (null == clazz) {
			throw new NullPointerException("Class to provided is null.");
		}
		return Modifier.isPublic(clazz.getModifiers());
	}

	/**
	 * 指定方法是否为Public
	 *
	 * @param method 方法
	 * @return 是否为public
	 */
	public static boolean isPublic(Method method) {
		Assert.notNull(method, "Method to provided is null.");
		return Modifier.isPublic(method.getModifiers());
	}

	/**
	 * 指定类是否为非public
	 *
	 * @param clazz 类
	 * @return 是否为非public
	 */
	public static boolean isNotPublic(Class<?> clazz) {
		return false == isPublic(clazz);
	}

	/**
	 * 指定方法是否为非public
	 *
	 * @param method 方法
	 * @return 是否为非public
	 */
	public static boolean isNotPublic(Method method) {
		return false == isPublic(method);
	}

	/**
	 * 是否为静态方法
	 *
	 * @param method 方法
	 * @return 是否为静态方法
	 */
	public static boolean isStatic(Method method) {
		Assert.notNull(method, "Method to provided is null.");
		return Modifier.isStatic(method.getModifiers());
	}

	/**
	 * 设置方法为可访问
	 *
	 * @param method 方法
	 * @return 方法
	 */
	public static Method setAccessible(Method method) {
		if (null != method && false == method.isAccessible()) {
			method.setAccessible(true);
		}
		return method;
	}

	/**
	 * 是否为抽象类
	 *
	 * @param clazz 类
	 * @return 是否为抽象类
	 */
	public static boolean isAbstract(Class<?> clazz) {
		return Modifier.isAbstract(clazz.getModifiers());
	}

	/**
	 * 是否为标准的类
	 * @param clazz 类
	 * @return 是否为标准类
	 */
	public static boolean isNormalClass(Class<?> clazz) {
		return null != clazz
				&& !clazz.isInterface()
				&& !isAbstract(clazz)
				&& !clazz.isEnum()
				&& !clazz.isArray()
				&& !clazz.isAnnotation()
				&& !clazz.isSynthetic()
				&& !clazz.isPrimitive();
	}

	/**
	 * 获得给定类的第一个泛型参数
	 *
	 * @param clazz 被检查的类，必须是已经确定泛型类型的类
	 * @return {@link Class}
	 */
	public static Class<?> getTypeArgument(Class<?> clazz) {
		return getTypeArgument(clazz, 0);
	}

	/**
	 * 获得给定类的泛型参数
	 *
	 * @param clazz 被检查的类，必须是已经确定泛型类型的类
	 * @param index 泛型类型的索引号，即第几个泛型类型
	 * @return {@link Class}
	 */
	public static Class<?> getTypeArgument(Class<?> clazz, int index) {
		final Type argumentType = TypeUtil.getTypeArgument(clazz, index);
		return TypeUtil.getClass(argumentType);
	}

	/**
	 * 获得给定类所在包的名称
	 *
	 * @param clazz 类
	 * @return 包名
	 */
	public static String getPackage(Class<?> clazz) {
		if (clazz == null) {
			return StrUtil.EMPTY;
		}
		final String className = clazz.getName();
		int packageEndIndex = className.lastIndexOf(StrUtil.DOT);
		if (packageEndIndex == -1) {
			return StrUtil.EMPTY;
		}
		return className.substring(0, packageEndIndex);
	}

	/**
	 * 获得给定类所在包的路径
	 *
	 * @param clazz 类
	 * @return 包名
	 */
	public static String getPackagePath(Class<?> clazz) {
		return getPackage(clazz).replace(StrUtil.C_DOT, StrUtil.C_SLASH);
	}

	/**
	 * 获取指定类型分的默认值
	 *
	 * @param clazz 类
	 * @return 默认值
	 */
	public static Object getDefaultValue(Class<?> clazz) {
		// 原始类型
		if (clazz.isPrimitive()) {
			return getPrimitiveDefaultValue(clazz);
		}
		return null;
	}

	/**
	 * 获取指定原始类型分的默认值
	 *
	 * @param clazz 类
	 * @return 默认值
	 */
	public static Object getPrimitiveDefaultValue(Class<?> clazz) {
		if (long.class == clazz) {
			return 0L;
		} else if (int.class == clazz) {
			return 0;
		} else if (short.class == clazz) {
			return (short) 0;
		} else if (char.class == clazz) {
			return (char) 0;
		} else if (byte.class == clazz) {
			return (byte) 0;
		} else if (double.class == clazz) {
			return 0D;
		} else if (float.class == clazz) {
			return 0f;
		} else if (boolean.class == clazz) {
			return false;
		}
		return null;
	}

	/**
	 * 获得默认值列表
	 *
	 * @param classes 值类型
	 * @return 默认值列表
	 */
	public static Object[] getDefaultValues(Class<?>... classes) {
		final Object[] values = new Object[classes.length];
		for (int i = 0; i < classes.length; i++) {
			values[i] = getDefaultValue(classes[i]);
		}
		return values;
	}

	public static boolean isJdkClass(Class<?> clazz) {
		final Package objectPackage = clazz.getPackage();
		if (null == objectPackage) {
			return false;
		}
		final String objectPackageName = objectPackage.getName();
		return objectPackageName.startsWith("java.") //
				|| objectPackageName.startsWith("javax.") //
				|| clazz.getClassLoader() == null;
	}


}
