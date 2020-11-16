package com.ch.blog.util;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TableBlock;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.AttributeProvider;
import org.commonmark.renderer.html.AttributeProviderContext;
import org.commonmark.renderer.html.AttributeProviderFactory;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.*;

/**
 * @description:
 * @author: chenhao
 * @create:2020/11/2 10:35
 **/
public class MarkdownUtil {

    /**
     * markdown格式转为HTML格式
     *
     * @param markdown
     * @return
     */
    public static String markdownToHtml(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse("");
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    /**
     * 增加扩展【标题描点，表格生成】
     * markdown格式转为HTML格式
     *
     * @param markdown
     * @return
     */
    public static String markdownToHtmlExtensions(String markdown) {
        //h标题生成id
        Set<Extension> headingAnchorExtensions = Collections.singleton(HeadingAnchorExtension.create());
        //转换table的HTML
        List<Extension> tableExtension = Arrays.asList(TablesExtension.create());
        Parser parser = Parser.builder()
                .extensions(tableExtension)
                .build();
        Node document = parser.parse(markdown);
        //HTML转换器
        HtmlRenderer renderer = HtmlRenderer.builder()
                .extensions(headingAnchorExtensions)
                .extensions(tableExtension)
                .attributeProviderFactory(new AttributeProviderFactory() {
                    @Override
                    public AttributeProvider create(AttributeProviderContext attributeProviderContext) {
                        return new CustomAttributeProvider();
                    }
                }).build();
        return renderer.render(document);
    }

    /**
     * 处理标签的属性
     */
    static class CustomAttributeProvider implements AttributeProvider {

        @Override
        public void setAttributes(Node node, String tagName, Map<String, String> attributes) {

            //改变a标签的target属性为_blank;
            if (node instanceof Link) {
                attributes.put("target", "_blank");
            }
            if(node instanceof TableBlock){
                attributes.put("class","ui cell table");
            }
        }
    }

    public static void main(String[] args){
        String table="## 6、前端展示\n" +
                "\n" +
                "### 6.1首页展示\n" +
                "\n" +
                "1、博客列表\n" +
                "\n" +
                "2、top分类\n"+"\"\n| a    | b    | c    |\\n\" +\n" +
                "                \"| ---- | ---- | ---- |\\n\" +\n" +
                "                \"| 1    | 2    | 3    |\\n\" +\n" +
                "                \"| 2    | 4    | 6    |\\n\" +\n" +
                "                \"| 3    | 5    | 7    |\"";
        String a="www.baidu.com";
        System.out.println(markdownToHtmlExtensions(a
        ));
    }

}
