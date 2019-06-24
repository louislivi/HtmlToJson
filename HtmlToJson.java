package com.louislivi.htmltojson;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

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
        List<Node> nodeList = document.getElementsByTag("body").get(0).childNodes();
        return JSONObject.toJSONString(html2json(nodes, nodeList));
    }

    /**
     * html转json
     *
     * @param nodes
     * @param nodeList
     * @return
     */
    private static List<Map<String, Object>> html2json(List<Map<String, Object>> nodes, List<Node> nodeList) {
        for (Node node : nodeList) {
            String nodeName = HtmlToJson.replaceNodeName(node.nodeName());
            if (!HtmlToJson.nodeFilter(nodeName)) {
                continue;
            }
            Map<String, Object> map = new HashMap<>(4);

            //标签名
            if ("#text".equals(nodeName)) {
                //标签文本
                map.put("type", "text");
                System.out.println(node.toString());
                map.put("text", "".equals(node.toString()) ? " " : node.toString().replace("(|M-Y|)", "&#"));
            } else {
                map.put("name", nodeName);
            }

            //处理标签属性
            Attributes attributes = node.attributes();
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
            HtmlToJson.html2json(childrens, node.childNodes());
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
        return nodeName.replace("section", "div");
    }

    /**
     * 节点过滤
     *
     * @param nodeName
     * @return
     */
    private static Boolean nodeFilter(String nodeName) {
        String pattern = "p|a|abbr|b|blockquote|br|code|col|colgroup|dd|del|div|dl|dt|em|fieldset|h1|h2|h3|h4|h5|h6|hr|i|img|ins|label|legend|li|olp|q|span|strong|sub|sup|table|tbody|td|tfoot|th|thead|tr|ul|#text";
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
        return html.replace("&nbsp", "(|M-Y|)160")
                .replace("&lt", "(|M-Y|)60")
                .replace("&gt", "(|M-Y|)62")
                .replace("&amp", "(|M-Y|)38")
                .replace("&quot", "(|M-Y|)34")
                .replace("&apos", "(|M-Y|)39")
                .replace("&cent", "(|M-Y|)162")
                .replace("&pound", "(|M-Y|)163")
                .replace("&yen", "(|M-Y|)165")
                .replace("&euro", "(|M-Y|)8364")
                .replace("&sect", "(|M-Y|)167")
                .replace("&copy", "(|M-Y|)169")
                .replace("&reg", "(|M-Y|)174")
                .replace("&trade", "(|M-Y|)8482")
                .replace("&times", "(|M-Y|)215")
                .replace("&divide", "(|M-Y|)247");
    }
}
