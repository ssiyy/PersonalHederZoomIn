package com.example.siy.myapplication.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Siy on 2018/12/20.
 *
 * @author Siy
 */
public class RandomUtils {


    /**
     * 自定义方法获取不重复随机数
     *
     * @param sz 随机数的区间，如果是10，那么就是[0,10)
     * @param num 随机数的个数
     * @return 随机组
     */
    public static int[] randomNums(int sz, int num) {
        Random rd = new Random();
        //随机数数组
        int[] rds = new int[sz];
        //序号
        int n = 0;
        //存放有序数字集合
        List<Integer> lst = new ArrayList<>();
        //获取随机数数组, 里面有重复数字
        while (n < rds.length) {
            lst.add(n);
            rds[n++] = (int) (rd.nextFloat() * sz);
        }
        //把随机数和有序集合进行匹对, 把随机数在集合出现的数字从集合中移除掉.
        for (int rd1 : rds) {
            for (int j = 0; j < lst.size(); j++) {
                if (rd1 == lst.get(j)) {
                    lst.remove(j);
                    break;
                }
            }
        }
        //把数组中重复的第二个数字用集合的第一个数字替换掉, 并移除掉数组的第一个数字
        for (int i = 0; i < rds.length; i++) {
            for (int j = 0; j < rds.length; j++) {
                if (i != j && rds[i] == rds[j]) {
                    rds[j] = lst.get(0);
                    lst.remove(0);
                    break;
                }
            }
        }
        //得到的  rds  数组就是不重复的随机数组
        return Arrays.copyOfRange(rds, 0, num);
    }
}
