package com.zia.magiccard.Util;

import java.util.List;

/**
 * Created by zia on 17-8-20.
 */

public class CollectionUtil {
    /**
     * 交换集合元素顺序
     * @param list
     * @param oldPosition
     * @param newPosition
     * @param <T>
     */
    public static <T> void swap(List<T> list, int oldPosition, int newPosition){
        if(null == list){
            throw new IllegalStateException("The list can not be empty...");
        }
        T tempElement = list.get(oldPosition);

        // 向前移动，前面的元素需要向后移动
        if(oldPosition < newPosition){
            for(int i = oldPosition; i < newPosition; i++){
                list.set(i, list.get(i + 1));
            }
            list.set(newPosition, tempElement);
        }
        // 向后移动，后面的元素需要向前移动
        if(oldPosition > newPosition){
            for(int i = oldPosition; i > newPosition; i--){
                list.set(i, list.get(i - 1));
            }
            list.set(newPosition, tempElement);
        }
    }
}
