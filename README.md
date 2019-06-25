# HTML转JSON

#### 介绍
在小程序中使用富文本以前是采用直接使用富文本但是存在UI兼容问题以及效率低下，目前采用官方推荐的html json格式进行富文本渲染提高渲染速度并且兼容性更好。

![小程序rich-text JAVA如何接入](https://iocaffcdn.phphub.org/uploads/images/201906/25/32040/iwcgqhPcoQ.png!large)
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

