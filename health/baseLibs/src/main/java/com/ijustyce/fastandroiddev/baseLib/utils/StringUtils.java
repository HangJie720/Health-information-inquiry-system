package com.ijustyce.fastandroiddev.baseLib.utils;

/**
 * Created by yc on 15-12-24.   string 工具类
 */
public class StringUtils {

    public static boolean isEmpty(String text){

        return text== null || text.replaceAll(" ", "").length() == 0;
    }

    public static int getInt(String value){

        return getInt(value, 0);
    }

    public static int getInt(String value, int defaultValue){

        if (!RegularUtils.isInt(value)) return defaultValue;
        try {
            return Integer.parseInt(value);
        }catch (Exception e){
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static double getDouble(String value){

        return getDouble(value, 0.0);
    }

    public static double getDouble(String value, double defaultValue){

        if (!RegularUtils.isNumber(value)) return defaultValue;
        try {
            return Double.parseDouble(value);
        }catch (Exception e){
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static float getFloat(String value){

        return getFloat(value, 0);
    }

    public static float getFloat(String value, float defaultValue){

        if (!RegularUtils.isNumber(value)) return defaultValue;
        try {
            return Float.parseFloat(value);
        }catch (Exception e){
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static long getLong(String value, long defaultValue){

        if (!RegularUtils.isInt(value)) return defaultValue;
        try {
            return Long.parseLong(value);
        }catch (Exception e){
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static long getLong(String value){

        return getLong(value, 0);
    }

    /**
     * 判断是否为新版本
     * @param net   网络返回的版本 比如 0.5.09
     * @param local 本地版本      比如 0.5.1
     * @return  是否为新版本
     */
    public static boolean isNewVersion(String net, String local){

        net = RegularUtils.deleteNoNumber(net);
        local = RegularUtils.deleteNoNumber(local);
        if (StringUtils.isEmpty(net) || StringUtils.isEmpty(local)) return false;
        String[] netVersion = net.split("\\.");
        String[] localVersion = local.split("\\.");

        int size = netVersion.length > localVersion.length ? localVersion.length : netVersion.length;
        for (int i =0; i < size; i++){
            double netTmp = getDouble("0." + netVersion[i]);
            double localTmp = getDouble("0." + localVersion[i]);
            if (netTmp > localTmp){
                return true;
            }if (netTmp < localTmp){
                return false;
            }
        }
        return netVersion.length > localVersion.length; //  除非长度不一且前面每位都相等，否则不可能到这步，
    }
}
