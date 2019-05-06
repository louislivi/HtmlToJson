# HTML转JSON

#### 介绍
html转微信小程序rich-text组件nodes

#### 所需依赖

- jsoup
- fastjson

#### 使用说明

``` java
System.out.println(HtmlToJson.parse("<html><body><h1>我的第一个标题</h1><p>我的第一个段落。</p></body></html>"));
//打印 [{"name":"h1","children":[{"type":"text","text":"我的第一个标题"}]},{"name":"p","children":[{"type":"text","text":"我的第一个段落。"}]}]
```

#### 详细介绍

> https://developers.weixin.qq.com/miniprogram/dev/component/rich-text.html

