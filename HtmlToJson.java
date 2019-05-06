package com.louislivi.htmltojson;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * html转换为小程序json
 *
 * @author : louislivi
 * @date : 2019-04-29 13:47
 */
public class HtmlToJson {

    /**
     * html转换为小程序json
     *
     * @return
     */
    public static String parse(String html) {
        List<Map<String, Object>> nodes = new ArrayList<>();
        html = HtmlToJson.htmlParse(html);
        Document document = Jsoup.parse(html);
        Elements elements = document.getElementsByTag("body").get(0).children();
        return JSONObject.toJSONString(html2json(nodes, elements));
    }

    /**
     * html转json
     *
     * @param nodes
     * @param elements
     * @return
     */
    private static List<Map<String, Object>> html2json(List<Map<String, Object>> nodes, Elements elements) {
        for (Element element : elements) {
            String nodeName = HtmlToJson.replaceNodeName(element.nodeName());
            if (!HtmlToJson.nodeFilter(nodeName)) {
                continue;
            }
            Map<String, Object> map = new HashMap<>(4);
            map.put("name", nodeName);
            Attributes attributes = element.attributes();
            Map<String, String> attrs = new HashMap<>(attributes.size());
            for (Attribute attribute : attributes) {
                if (HtmlToJson.attrFilter(attribute.getKey())) {
                    attrs.put(attribute.getKey(), attribute.getValue());
                }
            }
            if ("img".equals(nodeName)) {
                attrs.merge("style", "max-width: 100%;vertical-align:top;", (a, b) -> a + b);
            }
            if (!attrs.isEmpty()) {
                map.put("attrs", attrs);
            }
            List<Map<String, Object>> childrens = new ArrayList<>();
            if (!"".equals(element.ownText())) {
                Map<String, Object> children = new HashMap<>(4);
                children.put("type", "text");
                children.put("text", "".equals(element.ownText()) ? " " : element.ownText().replaceAll("&m#", "&#"));
                childrens.add(0, children);
            }
            HtmlToJson.html2json(childrens, element.children());
            if (!childrens.isEmpty()) {
                map.put("children", childrens);
            }
            nodes.add(map);
        }
        return nodes;
    }

    /**
     * 替换节点名称
     *
     * @param nodeName
     * @return
     */
    private static String replaceNodeName(String nodeName) {
        return nodeName.replaceAll("section", "div");
    }

    /**
     * 节点过滤
     *
     * @param nodeName
     * @return
     */
    private static Boolean nodeFilter(String nodeName) {
        String pattern = "p|a|abbr|b|blockquote|br|code|col|colgroup|dd|del|div|dl|dt|em|fieldset|h1|h2|h3|h4|h5|h6|hr|i|img|ins|label|legend|li|olp|q|span|strong|sub|sup|table|tbody|td|tfoot|th|thead|tr|ul";
        return Pattern.matches(pattern, nodeName);
    }

    /**
     * 属性过滤
     *
     * @param attrName
     * @return
     */
    private static Boolean attrFilter(String attrName) {
        String pattern = "span|width|alt|src|height|start|type|colspan|rowspan|style|class";
        return Pattern.matches(pattern, attrName);
    }

    /**
     * 处理html实体
     *
     * @param html
     * @return
     */
    private static String htmlParse(String html) {
        return html.replaceAll("&nbsp", "&m#160")
                .replaceAll("&lt", "&m#60")
                .replaceAll("&gt", "&m#62")
                .replaceAll("&amp", "&m#38")
                .replaceAll("&quot", "&m#34")
                .replaceAll("&apos", "&m#39")
                .replaceAll("&cent", "&m#162")
                .replaceAll("&pound", "&m#163")
                .replaceAll("&yen", "&m#165")
                .replaceAll("&euro", "&m#8364")
                .replaceAll("&sect", "&m#167")
                .replaceAll("&copy", "&m#169")
                .replaceAll("&reg", "&m#174")
                .replaceAll("&trade", "&m#8482")
                .replaceAll("&times", "&m#215")
                .replaceAll("&divide", "&m#247");
    }
}
