package com.mj.leapremote.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @Author lixiang
 * @CreateTime 2019-07-15
 **/
public class StringTools {
    /**
     * 把首字符转成大写的
     * @param word
     * @return
     */
    public static String first2BigLetter(String word){
        return word.substring(0,1).toUpperCase().concat(word.substring(1));
    }

    /**
     * 把首字符转成小写的
     * @param word
     * @return
     */
    public static String first2LowLetter(String word){
        return word.substring(0,1).toLowerCase().concat(word.substring(1));
    }

    /**
     * 去除String里面所有的空格
     * @param oldStr
     * @return
     */
    public static String removeAllSpace(String oldStr){
        return oldStr.replaceAll("\\s*","");
    }

    /**
     * 从字符串中返回指定两个指定标记之间的子字符串（不包含开头和结尾）
     * @param originalStr
     * @param from
     * @param to
     * @return
     */
    public static String[] getSubstringBetween(String originalStr , String from , String to){
        List<String> list = new ArrayList<>();
        String tempStr = originalStr.substring(originalStr.indexOf(from));
        while (true){
            if(!tempStr.contains(from) || !tempStr.contains(to)){
                break;
            }
            tempStr = tempStr.substring(tempStr.indexOf(from)+from.length());
            list.add(tempStr.substring(0,tempStr.indexOf(to)));
            tempStr = tempStr.substring(tempStr.indexOf(to)+to.length());

        }

        return list.toArray(new String[0]);
    }

    /**
     * 返回从第 num 个 from 开始标记之后到 to 标记的字符串
     * @param originalStr
     * @param num
     * @param from
     * @param to
     * @return
     */
    public static String getBetweenAndNum(String originalStr ,Integer num,String from , String to){
        String result = null;
        String tempStr = originalStr.substring(originalStr.indexOf(from));
        for (int i = 0; i < num; i++) {

            if( !tempStr.contains(to)){
                break;
            }
            tempStr = tempStr.substring(tempStr.indexOf(from)+from.length());
            result = tempStr.substring(0,tempStr.indexOf(to));
            tempStr = tempStr.substring(tempStr.indexOf(to)+to.length());

        }

        return result;
    }



    /**
     * 从字符串中返回指定第一次出现开头和最后一次出现结尾之间的子字符串（不包含开头和结尾）
     * @param originalStr
     * @param from
     * @param to
     * @return
     */
    public static String getSubstringBetweenFL(String originalStr , String from , String to){
        return originalStr.substring(originalStr.indexOf(from)+from.length(),originalStr.lastIndexOf(to));
    }
    /**
     * 从字符串中返回指定第一次出现开头和第一次出现结尾之间的子字符串（包含开头和结尾）
     * @param originalStr
     * @param from
     * @param to
     * @return
     */
    public static String[] getSubstringBetweenC(String originalStr , String from , String to){
        List<String> list = new ArrayList<>();
        String tempStr = originalStr.substring(originalStr.indexOf(from));
        while (true){
            if(!tempStr.contains(from) || !tempStr.contains(to)){
                break;
            }
            tempStr = tempStr.substring(tempStr.indexOf(from));
            list.add(tempStr.substring(tempStr.indexOf(from),tempStr.indexOf(to)+to.length()));
            tempStr = tempStr.substring(tempStr.indexOf(to)+to.length());

        }
        return list.toArray(new String[0]);
    }

    /**
     * 从字符串中返回指定第一次出现开头和最后一次出现结尾之间的子字符串（包含开头和结尾）
     * @param originalStr
     * @param from
     * @param to
     * @return
     */
    public static String getSubstringBetweenCFL(String originalStr , String from , String to){
        return originalStr.substring(originalStr.indexOf(from),originalStr.lastIndexOf(to)+1);
    }

    /**
     * 向一个String 在 指定字符串 addFlag第一次出现位置之后添加 addStr
     * @param originalStr
     * @param addFlag
     * @param addStr
     * @return
     */
    public static String insertIntoStringAF(String originalStr,String addFlag,String addStr){
        int index = originalStr.indexOf(addFlag)+addFlag.length();
        String first  = originalStr.substring(0,index);
        String last = originalStr.substring(index);
        return first+addStr+last;
    }

    /**
     * 向一个String 在 指定字符串 addFlag最后一次出现位置之前添加 addStr
     * @param originalStr
     * @param addFlag
     * @param addStr
     * @return
     */
    public static String insertIntoStringBL(String originalStr,String addFlag,String addStr){
        int index = originalStr.lastIndexOf(addFlag);
        String first  = originalStr.substring(0,index);
        String last = originalStr.substring(index);
        return first+addStr+last;
    }


    /**
     * 下划线转成驼蜂命名
     * @param underScope
     * @return
     */
    public static String underScope2Camel(String underScope){

        String[] ss = underScope.split("_");
        StringBuilder sb = new StringBuilder(ss[0]);
        for (int i = 0; i < ss.length; i++) {
            if (i==0) {
                continue;
            }
            sb.append(first2BigLetter(ss[i]));
        }
        return sb.toString();
    }

    public static String camel2UnderScope(String camel){
        //思路是把大写的替换成 _加小写的
        char[] chars = camel.toCharArray();
        StringBuilder sb =new StringBuilder();

        int i = 0;
        for (char aChar : chars) {

            if(Character.isUpperCase(aChar)){
                if(i!=0){
                    sb.append("_");
                }
                sb.append(Character.toLowerCase(aChar));
            }else {
                sb.append(aChar);
            }
            i++;
        }
        return sb.toString();
    }

    public static boolean isBlank(String s){
        return s==null||s.length()<=0;
    }

    public static boolean isNotBlank(String s){
        return !isBlank(s);
    }

    /**
     * 获取指定长度的简要信息
     * @param s 原来的内容
     * @param num 简要信息的长度
     * @return
     */
    public static String getBrief(String s,Integer num){

        if(isBlank(s) || s.length() < num ){
            return s;
        }

        return s.substring(0,num);
    }

    /**
     *
     * @param value
     * @return
     */
    public static String strToAscii(String value)
    {
        StringBuffer sbu = new StringBuffer();
        char[] chars = value.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : chars) {
            stringBuilder.append("\\u"+Integer.toHexString(c));
        }
        return stringBuilder.toString();

    }

    public static boolean areNotBlank(String... values) {
        boolean result = true;
        if (values == null || values.length == 0) {
            result = false;
        } else {
            for (String value : values) {
                result &= !isBlank(value);
            }
        }
        return result;
    }

    /**
     * 按字典升序的比较器
     */
    public static Comparator<String> StringComparator = (o1, o2) -> {
        char[] chars1=o1.toCharArray();
        char[] chars2=o2.toCharArray();
        int i=0;
        while(i<chars1.length && i<chars2.length){
            if(chars1[i]>chars2[i]){
                return 1;
            }else if(chars1[i]<chars2[i]){
                return -1;
            }else{
                i++;
            }
        }
        if(i==chars1.length){  //o1到头
            return -1;
        }
        if(i== chars2.length){ //o2到头
            return 1;
        }
        return 0;
    };
    public static void main(String[] args) {

        String ss = getBetweenAndNum("abcdefaecdea",1,"a","c");
        System.out.println(ss);
    }
}