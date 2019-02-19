package com.example.siy.myapplication.support;

import java.util.Collection;
import java.util.Map;

/**
 * 验证类： 1.String , 2.集合, 3.数组.
 *
 * @author Royal
 * @Email Royal.k.peng@gmail.com
 */
public class EmptyUtil {

    /**
     * 检查string是否为空
     *
     * @param str the verify String.
     * @return {@code true} is empty,false is not.
     */
    public static boolean isStringEmpty(CharSequence str) {
        return null == str || str.length() < 1;
    }

    /**
     * 检查string是否不为空
     *
     * @param str the verify String.
     * @return {@code true} is not empty,false is empty.
     */
    public static boolean isStringNotEmpty(String str) {
        return !isStringEmpty(str);
    }

    public static <T,R> boolean isMapEmpty(Map<T, R> map) {
        return null == map || map.isEmpty();
    }

    public static <T,R> boolean isMapNotEmpty(Map<T, R> map) {
        return !isMapEmpty(map);
    }

    /**
     * 检测集合类是否为空，list,map,set,
     *
     * @param collection will veifyCollections.
     * @return {@code true} this collection is empty,false is not;
     */
    public static <T> boolean isCollectionEmpty(Collection<T> collection) {
        return null == collection || collection.isEmpty();
    }

    public static <T> boolean isCollectionEmpty(T[] collection){
        return null == collection || collection.length == 0;
    }

    public static boolean isCollectionEmpty(int[] collection){
        return null == collection || collection.length == 0;
    }

    /**
     * 检测集合类是否不为空，list,map,set,
     *
     * @param collection
     * @return
     */
    public static <T> boolean isCollectionNotEmpty(Collection<T> collection) {
        return !isCollectionEmpty(collection);
    }

    public static boolean isObjectNull(Object data){
        return data == null;
    }




}
