package com.blackbaka.sc.core.util;


import com.blackbaka.sc.core.enums.ServiceEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/***
 *通用枚举工具类
 *
 *@author Thinker Wu
 *@author Kaiyi Zhang
 *@date 2019/4/23 10:59
 */
public class EnumUtil {

    private final static Logger log = LoggerFactory.getLogger(EnumUtil.class);

    // 实现ServiceEnum接口的枚举类的value做key
    private static Map<VKey, Value> valueMap = Collections.unmodifiableMap(new HashMap<>());

    // 实现ServiceEnum接口的枚举类的label做key
    private static Map<LKey, Value> labelMap = Collections.unmodifiableMap(new HashMap<>());

    // 实现ServiceEnum接口的枚举类Class SimpleName做Key，枚举类Class做value
    private static Map<String, Class<? extends ServiceEnum<?>>> enumMap = Collections.unmodifiableMap(new HashMap<>());


    /**
     * 生成枚举操作相关使用的缓存，减少频繁反射调用enum.values()并遍历比较所带来的性能消耗
     * 新扫表到的枚举类将会加入到缓存中，原缓存中存在的枚举类依旧存在，新旧枚举类相同时旧缓存会被覆盖
     *
     * @param basePackages 扫描包
     */
    public synchronized static <T extends ServiceEnum<U>, U> void generateCache(String... basePackages) {
        if (basePackages == null) {
            return;
        }
        Set<String> packages = Arrays.stream(basePackages).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
        // Map<Class<? extends ServiceEnum<?>>.name().replace("Enum","") , Class<? extends ServiceEnum<?>> >
        Set<Class> enumClasses = new HashSet<>();

        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
        for (String p : packages) {
            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                    + ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(p)) + "/**/*Enum.class";

            try {
                Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
                for (Resource resource : resources) {
                    if (!resource.isReadable()) {
                        continue;
                    }
                    MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                    String className = metadataReader.getClassMetadata().getClassName();
                    Class<?> clazz = null;
                    try {
                        clazz = Class.forName(className);
                    } catch (Throwable e) {
//                        log.warn(e.getMessage(), e);
                    }
                    // 检查是否为实现了ServiceEnum接口的枚举类
                    if (clazz != null && isServiceEnum(clazz)) {
                        enumClasses.add(clazz);
                    }
                }
            } catch (Throwable e) {
                log.error("Enum 缓存构建失败，" + e.getMessage(), e);
            }


        }
        //构建枚举工具类的枚举类信息缓存
        EnumUtil.generateCache(enumClasses);
    }


    /**
     * 生成枚举操作相关使用的缓存，减少频繁反射调用enum.values()并遍历比较所带来的性能消耗
     * 新扫表到的枚举类将会加入到缓存中，原缓存中存在的枚举类依旧存在，新旧枚举类相同时旧缓存会被覆盖
     *
     * @param classes 枚举类集合
     */
    public synchronized static <T extends ServiceEnum<U>, U> void generateCache(Set<Class> classes) {

        if (classes == null || classes.isEmpty()) {
            return;
        }
        classes.removeIf(cl -> !isServiceEnum(cl));

        Map<VKey, Value> vMap = new HashMap<>();
        Map<LKey, Value> lMap = new HashMap<>();
        Map<String, Class<? extends ServiceEnum<?>>> eMap = new HashMap<>();
        for (Class<T> clazz : classes) {
            if (clazz == null) {
                continue;
            }
            try {
                Method values = clazz.getMethod("values");
                T[] items = (T[]) values.invoke(null, null);
                if (items == null || items.length <= 0) {
                    continue;
                }
                for (T item : items) {
                    VKey vKey = new VKey(clazz.getName(), item.getValue());
                    LKey lKey = new LKey(clazz.getName(), item.getLabel());
                    Value<T, U> value = new Value<>(item, item.getValue(), item.getLabel());
                    vMap.put(vKey, value);
                    lMap.put(lKey, value);
                }
                eMap.put(clazz.getSimpleName(), clazz);
            } catch (Exception e) {
                // do nothing
            }
        }

        vMap.putAll(valueMap);
        lMap.putAll(labelMap);
        eMap.putAll(enumMap);

        EnumUtil.valueMap = Collections.unmodifiableMap(vMap);
        EnumUtil.labelMap = Collections.unmodifiableMap(lMap);
        EnumUtil.enumMap = Collections.unmodifiableMap(eMap);

        log.info("EnumUtil 枚举缓存已重新构建");
    }


    /**
     * 根据enum class name获取enum class
     *
     * @param enumName
     * @return
     */
    public static Class<? extends ServiceEnum<?>> getEnum(String enumName) {
        return EnumUtil.enumMap.get(enumName);
    }


    /**
     * 由value获取枚举
     *
     * @param clazz 枚举类
     * @param value 编码值
     * @return 枚举实例
     */
    public static <T extends ServiceEnum<U>, U> T valueOf(Class<T> clazz, U value) {
        if (clazz == null || value == null) {
            return null;
        }

        Value<T, U> v = valueMap.get(new VKey(clazz.getName(), value));
        if (v != null) {
            return v.getServiceEnum();
        }

        try {
            Method values = clazz.getMethod("values");
            T[] items = (T[]) values.invoke(null, null);
            for (T item : items) {
                if (item.getValue().toString().equals(value.toString())) {
                    return item;
                }
            }
            return null;

        } catch (Exception ex) {
            //失败返回null即可
            return null;
        }
    }


    /**
     * 由label获取枚举
     *
     * @param clazz 枚举类
     * @param label 文本描述
     * @return 枚举对象
     */
    public static <T extends ServiceEnum<U>, U> T labelOf(Class<T> clazz, String label) {
        if (clazz == null || label == null) {
            return null;
        }

        Value<T, U> v = labelMap.get(new LKey(clazz.getName(), label));
        if (v != null) {
            return v.getServiceEnum();
        }

        try {
            Method values = clazz.getMethod("values");
            T[] items = (T[]) values.invoke(null, null);
            for (T item : items) {
                if (item.getLabel().toString().equals(label.toString())) {
                    return item;
                }
            }
            return null;

        } catch (Exception ex) {
            //失败返回null即可
            return null;
        }
    }


    /**
     * 判断是否存在编码值为value且枚举类为clazz的枚举
     *
     * @param clazz 枚举类
     * @param value 编码值
     * @return true/false
     */
    public static <T extends ServiceEnum<U>, U> boolean matchValue(Class<T> clazz, U value) {
        if (clazz == null || value == null) {
            return false;
        }

        Value<T, U> v = valueMap.get(new VKey(clazz.getName(), value));
        if (v != null && v.getServiceEnum().getValue().toString().equals(value.toString())) {
            return true;
        } else if (v == null) {
            try {
                Method values = clazz.getMethod("values");
                T[] items = (T[]) values.invoke(null, null);
                for (T item : items) {
                    if (item.getValue().toString().equals(value.toString())) {
                        return true;
                    }
                }
                return false;
            } catch (Exception ex) {
                //失败返回false即可
                return false;
            }
        } else {
            return false;
        }

    }


    /**
     * 通过label获取对应枚举的value值
     *
     * @param clazz 枚举类
     * @param label 文本描述
     * @return 枚举value || null
     */
    public static <T extends ServiceEnum<U>, U> U getValueByLabel(Class<T> clazz, String label) {
        T serviceEnum = labelOf(clazz, label);
        if (serviceEnum != null) {
            return serviceEnum.getValue();
        } else {
            return null;
        }
    }

    /**
     * 通过value获取对应枚举的label值
     *
     * @param clazz
     * @param value
     * @return
     */
    public static <T extends ServiceEnum<U>, U> String getLabelByValue(Class<T> clazz, U value) {
        T serviceEnum = valueOf(clazz, value);
        if (serviceEnum != null) {
            return serviceEnum.getLabel();
        } else {
            return "";
        }
    }

    /**
     * 是否是实现了ServiceEnum的枚举类
     *
     * @param clazz
     * @return true / false
     */
    public static boolean isServiceEnum(Class<?> clazz) {
        Class<?>[] interfaces = clazz.getInterfaces();
        if (clazz.isEnum() && interfaces != null && interfaces.length > 0) {
            for (Class cl : interfaces) {
                if (cl.equals(ServiceEnum.class)) {
                    return true;
                }
            }
        }
        return false;
    }


    private static class VKey<T extends ServiceEnum<U>, U> {

        final String className;

        final U value;

        public VKey(String className, U value) {
            this.className = className;
            this.value = value;
        }

        public String getClassName() {
            return className;
        }

        public U getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            VKey<?, ?> vKey = (VKey<?, ?>) o;
            return Objects.equals(className, vKey.className) &&
                    Objects.equals(value, vKey.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(className, value);
        }
    }

    private static class LKey {

        final String className;

        final String label;

        public LKey(String className, String label) {
            this.className = className;
            this.label = label;
        }

        public String getClassName() {
            return className;
        }

        public String getLabel() {
            return label;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LKey lKey = (LKey) o;
            return Objects.equals(className, lKey.className) &&
                    Objects.equals(label, lKey.label);
        }

        @Override
        public int hashCode() {
            return Objects.hash(className, label);
        }
    }


    private static class Value<T extends ServiceEnum<U>, U> {
        final T serviceEnum;

        final U value;

        final String label;

        public Value(T serviceEnum, U value, String label) {
            this.serviceEnum = serviceEnum;
            this.value = value;
            this.label = label;
        }

        public T getServiceEnum() {
            return serviceEnum;
        }

        public U getValue() {
            return value;
        }

        public String getLabel() {
            return label;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Value<?, ?> value1 = (Value<?, ?>) o;
            return Objects.equals(serviceEnum, value1.serviceEnum) &&
                    Objects.equals(value, value1.value) &&
                    Objects.equals(label, value1.label);
        }

        @Override
        public int hashCode() {
            return Objects.hash(serviceEnum, value, label);
        }
    }


}
