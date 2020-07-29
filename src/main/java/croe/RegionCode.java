package croe;

import entity.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 爬取中国全国省市县乡镇地区代码
 */
public class RegionCode {
    //睡眠时间，防止对方服务器崩溃
    private final int T = 3 * 1000;
    //超时时间
    private final int T1 = 10 * 1000;
    //出错休息时间
    private final int T2 = 1 * 1000;


    public List<RegionTable> preservation() throws IOException, InterruptedException {
        String listurl = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2019/index.html";
        List<Province> province = getProvince(listurl);
        List<RegionTable> nations = new ArrayList<>();
        RegionTable nation;
        for (int i = 0; i < province.size(); i++) {
            nation = new RegionTable();
            Province province1 = province.get(i);
            nation.setCode(province1.getCode());
            nation.setName(province1.getName());
            nation.setType("1");
            nation.setPcode("0");
            nations.add(nation);
            List<City> cities = province1.getCities();
            if (cities != null) {
                for (City city : cities) {
                    nation = new RegionTable();
                    nation.setCode(city.getCode());
                    nation.setName(city.getName());
                    nation.setType("2");
                    nation.setPcode(province1.getCode());
                    nations.add(nation);
                    List<County> counties = city.getCounties();
                    if (counties != null) {
                        for (County county : counties) {
                            nation = new RegionTable();
                            nation.setCode(county.getCode());
                            nation.setName(county.getName());
                            nation.setType("3");
                            nation.setPcode(city.getCode());
                            nations.add(nation);
                            List<Country> countries = county.getCountries();
                            if (countries != null) {
                                for (Country country : countries) {
                                    nation = new RegionTable();
                                    nation.setCode(country.getCode());
                                    nation.setName(country.getName());
                                    nation.setType("4");
                                    nation.setPcode(county.getCode());
                                    nations.add(nation);
                                    List<Town> towns = country.getTowns();
                                    if (towns != null) {
                                        for (Town town : towns) {
                                            nation = new RegionTable();
                                            nation.setCode(town.getCode());
                                            nation.setName(town.getName());
                                            nation.setClassification(town.getClassification());
                                            nation.setType("5");
                                            nation.setPcode(country.getCode());
                                            nations.add(nation);
                                        }
                                    }

                                }
                            }
                        }
                    }

                }
            }
        }
        return nations;
    }

    private List<Province> getProvince(final String listurl) throws InterruptedException, IOException {
        Document doc = null;
        try {
            Thread.sleep(T);
            doc = Jsoup.parse(new URL(listurl).openStream(), "GBK", listurl);
//            doc = Jsoup.connect(listurl).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36").timeout(T1).get();
        } catch (IOException | InterruptedException e) {
            System.out.println("重新载入");
            Thread.sleep(T2);
            //调用重新载入方法
            doc = heavyLoadIn(listurl);
        }
        Elements province = doc.select(".provincetr");//省级
        //查询市
        List<Province> provinces = new ArrayList<>();
        Province province1;
        for (int i = 0; i < province.size(); i++) {
            Elements a = province.get(i).getElementsByTag("a");
            for (int i1 = 0; i1 < a.size(); i1++) {
                province1 = new Province();
                //得到所有a标签
                Element element = a.get(i1);
                //a标签获取code
                String href = element.attr("href");
                //调用转换方法
                href = getUTF8BytesFromGBKString(href);
                String code = href.substring(0, href.lastIndexOf('.'));
                province1.setCode(code);
                //a标签获取name
                String name = element.text();
                //调用转换方法
                name = getUTF8BytesFromGBKString(name);
                province1.setName(name);
                System.out.println(element.text());
                //a标签获取地址获取市
                List<City> cities = indexCity(element.absUrl("href"));
                province1.setCities(cities);
                provinces.add(province1);
            }
        }
        return provinces;
    }

    /**
     * 查询市
     *
     * @param listurl
     * @return
     */
    private List<City> indexCity(String listurl) throws InterruptedException, IOException {
        Document doc = null;
        try {
            Thread.sleep(T);
            doc = Jsoup.parse(new URL(listurl).openStream(), "GBK", listurl);
//            doc = Jsoup.connect(listurl).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36").timeout(T1).get();
        } catch (IOException | InterruptedException e) {
            System.err.println("重新载入");
            Thread.sleep(T2);
            //调用重新载入方法
            doc = heavyLoadIn(listurl);
        }
        Elements city = doc.select(".citytr");//市辖区
        //查询县
        List<City> cities = new ArrayList<>();
        City city1;
        for (int i = 0; i < city.size(); i++) {
            Elements a = city.get(i).getElementsByTag("a");
            for (int i1 = 0; i1 < a.size(); i1++) {
                city1 = new City();
                //得到所有a标签
                Element element = a.get(i1);
                //第一个标签为code第二个为名称
                //a标签获取code
                String code = element.text();
                //调用转换方法
                code = getUTF8BytesFromGBKString(code);
                city1.setCode(code);
                //a标签获取name
                String name = a.get(++i1).text();
                //调用转换方法
                name = getUTF8BytesFromGBKString(name);
                city1.setName(name);
                System.out.println(name);
                //a标签获取地址获取县
                List<County> counties = indexCounty(element.absUrl("href"));
                city1.setCounties(counties);
                cities.add(city1);
            }
        }
        return cities;
    }

    /**
     * 获取县
     *
     * @param listurl
     * @return
     */
    private List<County> indexCounty(String listurl) throws InterruptedException, IOException {
        Document doc = null;
        try {
            Thread.sleep(T);
            doc = Jsoup.parse(new URL(listurl).openStream(), "GBK", listurl);
//            doc = Jsoup.connect(listurl).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36").timeout(T1).get();
        } catch (IOException | InterruptedException e) {
            System.err.println("重新载入");
            Thread.sleep(T2);
            //调用重新载入方法
            doc = heavyLoadIn(listurl);
        }
        Elements county = doc.select(".countytr");//县
        //查询镇
        List<County> counties = new ArrayList<>();
        County county1;
        for (int i = 0; i < county.size(); i++) {
            Elements a = county.get(i).getElementsByTag("a");
            for (int i1 = 0; i1 < a.size(); i1++) {
                county1 = new County();
                //得到所有a标签
                Element element = a.get(i1);
                //第一个标签为code第二个为名称
                //a标签获取code
                String code = element.text();
                //调用转换方法
                code = getUTF8BytesFromGBKString(code);
                county1.setCode(code);
                //a标签获取name
                String name = a.get(++i1).text();
                //调用转换方法
                name = getUTF8BytesFromGBKString(name);
                county1.setName(name);
                System.out.println(name);
                //a标签获取地址获取镇
                List<Country> countries = indexCountry(element.absUrl("href"));
                county1.setCountries(countries);
                counties.add(county1);
            }
        }
        return counties;
    }

    /**
     * 获取镇
     *
     * @param listurl 获取镇url
     * @return 镇集合
     */
    private List<Country> indexCountry(String listurl) throws InterruptedException, IOException {
        Document doc = null;
        try {
            Thread.sleep(T);
            doc = Jsoup.parse(new URL(listurl).openStream(), "GBK", listurl);
//            doc = Jsoup.connect(listurl).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36").timeout(T1).get();
        } catch (IOException | InterruptedException e) {
            System.err.println("重新载入");
            Thread.sleep(T2);
            //调用重新载入方法
            doc = heavyLoadIn(listurl);
        }
        Elements country = null;//街道
        try {
            country = doc.select(".towntr");
        } catch (Exception e) {
            //调用重新载入方法
            doc = heavyLoadIn(listurl);
            country = doc.select(".towntr");
        }
        //查询村
        List<Country> countries = new ArrayList<>();
        Country country1;
        for (int i = 0; i < country.size(); i++) {
            Elements a = country.get(i).getElementsByTag("a");
            for (int i1 = 0; i1 < a.size(); i1++) {
                country1 = new Country();
                //得到所有a标签
                Element element = a.get(i1);
                //第一个标签为code第二个为名称
                //a标签获取code
                String code = element.text();
                //调用转换方法
                code = getUTF8BytesFromGBKString(code);
                country1.setCode(code);
                //a标签获取name
                Element elementName = a.get(++i1);
                String name = elementName.text();
                //调用转换方法
                name = getUTF8BytesFromGBKString(name);
                country1.setName(name);
                System.out.println(name);
                //a标签获取地址获取村
                List<Town> towns = indexTown(element.absUrl("href"));
                country1.setTowns(towns);
                countries.add(country1);
            }
        }
        return countries;
    }

    /**
     * 获取村级
     *
     * @param listurl 查询村地址
     * @return 村集合
     */
    private List<Town> indexTown(String listurl) throws InterruptedException, IOException {
        Document doc = null;
        try {
            Thread.sleep(T);
            doc = Jsoup.parse(new URL(listurl).openStream(), "GBK", listurl);
//            doc = Jsoup.connect(listurl).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36").timeout(T1).get();
        } catch (IOException | InterruptedException e) {
            //如果报错休息一段时间继续
            System.err.println("重新载入");
            Thread.sleep(T2);
            //调用重新载入方法
            doc = heavyLoadIn(listurl);
        }
        Elements town;
        try {
            town = doc.select(".villagetr");//居委会
        } catch (Exception e) {
            //调用重新载入方法
            doc = heavyLoadIn(listurl);
            town = doc.select(".villagetr");//居委会
        }
        //查询村
        List<Town> towns = new ArrayList<>();
        Town town1;
        for (int i = 0; i < town.size(); i++) {
            town1 = new Town();
            Elements td = town.get(i).getElementsByTag("td");
            String code = td.get(0).text();
            code = getUTF8BytesFromGBKString(code);
            town1.setCode(code);
            String classification = td.get(1).text();
            classification = getUTF8BytesFromGBKString(classification);
            town1.setClassification(classification);
            String name = td.get(2).text();
            name = getUTF8BytesFromGBKString(name);
            town1.setName(name);
            System.out.println(name);
            towns.add(town1);
        }
        return towns;
    }

    private Document heavyLoadIn(String listurl) {
        try {
            return Jsoup.parse(new URL(listurl).openStream(), "GBK", listurl);
        } catch (IOException e) {
            heavyLoadIn(listurl);
        }
        return null;
    }


    //有损转换
    public String getUTF8BytesFromGBKString(String gbkStr) {
        int n = gbkStr.length();
        byte[] utfBytes = new byte[3 * n];
        int k = 0;
        for (int i = 0; i < n; i++) {
            int m = gbkStr.charAt(i);
            if (m < 128 && m >= 0) {
                utfBytes[k++] = (byte) m;
                continue;
            }
            utfBytes[k++] = (byte) (0xe0 | (m >> 12));
            utfBytes[k++] = (byte) (0x80 | ((m >> 6) & 0x3f));
            utfBytes[k++] = (byte) (0x80 | (m & 0x3f));
        }
        if (k < utfBytes.length) {
            byte[] tmp = new byte[k];
            System.arraycopy(utfBytes, 0, tmp, 0, k);
            utfBytes = tmp;


        }
        String str = null;
        try {
            str = new String(utfBytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }
}