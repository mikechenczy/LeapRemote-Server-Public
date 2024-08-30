package com.mj.leapremote.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mike_Chen
 * @date 2022/5/18
 * @apiNote
 */
public class GameUtil {

    public static List<String> serializeCards(List<String> cards) {
        List<String> result = new ArrayList<>();
        for(String s : cards) {
            result.add(s.replaceAll("♠", "黑桃").replaceAll("♥", "红桃")
                    .replaceAll("♣", "梅花").replaceAll("♦", "方块"));
        }
        return result;
    }
}
