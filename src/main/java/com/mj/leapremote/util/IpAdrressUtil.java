package com.mj.leapremote.util;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Subdivision;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

public class IpAdrressUtil {
    /**
     * 获取Ip地址
     * @param request
     * @return
     */
    public static String getIpAdrress(HttpServletRequest request) {
        String Xip = request.getHeader("X-Real-IP");
        String XFor = request.getHeader("X-Forwarded-For");
        if(StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = XFor.indexOf(",");
            if(index != -1){
                return XFor.substring(0,index);
            }else{
                return XFor;
            }
        }
        XFor = Xip;
        if(StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)){
            return XFor;
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getRemoteAddr();
        }
        return XFor;
    }

    public static String getProvince(String ip) throws IOException, GeoIp2Exception {
        // 创建 GeoLite2 数据库
        File database = new File("GeoLite2-City.mmdb");
        // 读取数据库内容
        DatabaseReader reader = new DatabaseReader.Builder(database).build();
        InetAddress ipAddress = InetAddress.getByName(ip);
        // 获取查询结果
        CityResponse response = reader.city(ipAddress);
        //获取Province
        Subdivision subdivision = response.getMostSpecificSubdivision();
//        System.out.println(subdivision.getName()); // 'Guangxi Zhuangzu Zizhiqu'
//        System.out.println(subdivision.getIsoCode()); // '45'
//        System.out.println(subdivision.getNames().get("zh-CN")); // '广西壮族自治区'
        return subdivision.getNames().get("zh-CN");
    }

    public static String getCountry(String ip) throws IOException, GeoIp2Exception {
        // 创建 GeoLite2 数据库
        File database = new File("GeoLite2-City.mmdb");
        // 读取数据库内容
        DatabaseReader reader = new DatabaseReader.Builder(database).build();
        InetAddress ipAddress = InetAddress.getByName(ip);
        // 获取查询结果
        CityResponse response = reader.city(ipAddress);
        //获取country
        Country country = response.getCountry();
//        System.out.println(country.getIsoCode()); // 'CN'
//        System.out.println(country.getName()); // 'China'
//        System.out.println(country.getNames().get("zh-CN")); // '中国'
        return country.getNames().get("zh-CN");
    }

    public static String getCity(String ip) throws IOException, GeoIp2Exception {
        // 创建 GeoLite2 数据库
        File database = new File("GeoLite2-City.mmdb");//这里是下载的GeoLite2 City库的路径
        // 读取数据库内容
        DatabaseReader reader = new DatabaseReader.Builder(database).build();
        InetAddress ipAddress = InetAddress.getByName(ip);
        // 获取查询结果
        CityResponse response = reader.city(ipAddress);
        //获取City
        City city = response.getCity();
//        System.out.println(city.getName()); // 'Nanning'
//        Postal postal = response.getPostal();
//        System.out.println(postal.getCode()); // 'null'
//        System.out.println(city.getNames().get("zh-CN")); // '南宁'
//        Location location = response.getLocation();
//        System.out.println(location.getLatitude()); // 22.8167
//        System.out.println(location.getLongitude()); // 108.3167
        return city.getNames().get("zh-CN");
    }
}
