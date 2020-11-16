# Springboot+Semantic UI开发个人博客（抄自https://www.bilibili.com/video/BV1PE411f7gS/?p=8）

## 1、需求与功能

### 1.1、用户故事

用户故事模板：

角色、功能、商业价值

作为一个(某个角色)使用者，我可以做(某个功能)事情，如此可以有(某个商业价值)的好处

个人博客系统的用户故事：

角色： 普通访客、管理员（我）

- 访客，可以分页查看所有的博客
- 访客，可以快速查博客数最多的6个分类
- 访客，可以查看所有的分类
- 访客，可以查看某个分类下的博客列表
- 访客，可以快速查看标记博客最多的10个标签
- 访客，可以查看所有的标签
- 访客，可以查看某个标签下的博客列表
- 访客，可以根据年度时间线查看博客列表
- 访客，可以快速查看最新的推荐博客
- 访客，可以用关键字全局搜索博客
- 访客，可以查看单个博客内容
- 访客，可以对博客内容进行评论
- 访客，可以赞赏博客内容
- 访客，可以微信扫描阅读博客内容
- 访客，可以在首页扫描公众号二维码关注我
- 我，可以用户名和密码登录后台管理
- 我，可以管理博客
  - 我，可以发布新博客
  - 我，可以对博客进行分类
  - 我，可以对博客打标签
  - 我，可以修改博客
  - 我，可以删除博客
  - 我，可以根据标题，分类，标签查询博客
- 我，可以管理博客分类
  - 我，可以新增一个分类
  - 我，可以修改一个分类
  - 我，可以删除一个分类
  - 我，可以根据分类名称查询分类
- 我，可以管理标签
  - 我，可以新增一个标签

  - 我，可以修改一个标签

  - 我，可以删除一个标签

  - 我，可以根据名称查询标签

### 1.2、功能规划

![image-20201019100918121](https://github.com/Ximpermanence/PersonBlog/blob/main/READMEimage/image-20201019100918121.png)

## 2、页面设计与开发

### 2.1、设计

前端展示：首页、页面详情、分类、标签、归档、关于我

后台管理：模板页

### 2.2、页面开发

```html
  <!-- semantic UI -->
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css">
    <script src="https://cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.js"></script>
```

semantic UI框架

https://semantic-ui.com/elements/input.html

semantic UI中的icon大多可以于此找到

https://fontawesome.com/?from=io%2Ficons

图片 https://picsum.photos/images

背景图片 https://www.toptal.com/designers/subtlepatterns/

### 2.3、插件集成

![image-20201022164126750](https://github.com/Ximpermanence/PersonBlog/blob/main/READMEimage/image-20201022164126750.png)

markdown编辑器：https://pandao.github.io/editor.md

内容排版： https://github.com/sofish/typo.css

动画 animate.css: https://daneden.github.io/animate.css/

代码高亮：https://github.com/PrismJS/prism

目录生成：https://tscanlin.github.io/tocbot

二维码生成：https://davidshimjs.github.io/qrcodejs

平滑滚动： https://github.com/flesler/jquery.scrollTo

滚动侦测：http://imakewebthings.com/waypoints/

## 3、框架搭建

### 3.1、构建与配置

1、引入SpringBoot模块：

- web
- Thymeleaf
- JPA
- MySQL
- Aspects
- DevTools

### 3.2、异常处理

1、定义错误页面

- 404
- 500
- error

2、全局处理异常

统一处理异常：

```
@ControllerAdvice
public class ControllerHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //从request获取访问的url
    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(HttpServletRequest request, Exception e) {
        logger.error("Request URL:{},Exception:{}",request.getRequestURL(),e);
        ModelAndView mv = new ModelAndView();
        mv.addObject("url",request.getRequestURL());
        mv.addObject("exception",e);
        mv.setViewName("error/error");
        return mv;
    }
}
```

错误页面异常信息显示处理

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8">
    <title>错误</title>
</head>
<body>
错误
    <div>
<!--        &lt;!&#45;&#45;注释左边部分，&#45;&#45;&gt注释右边部分remove移除当前元素的标签，此处移除div-->
        <div th:utext="'&lt;!--'" th:remove="tag"></div>
        <div th:utext="'Failed Request URL :' + ${url}" th:remove="tag"></div>
        <div th:utext="'Exception message :'" + ${exception.message} th:remove="tag"></div>
        <ul th:remove="tag">
            <li th:each="st : ${exception.stackTrace}" th:remove="tag"><span th:utext="${st}" th:remove="tag"></span></li>
        </ul>
        <div th:utext="'--&gt;'" th:remove="tag"></div>
    </div>
</body>
</html>
```



3、资源找不到异常

```java
//拿到springboot找不到的这个状态，然后就会跳转到404这个页面
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

### 3.3、日志处理

1、记录日志记录

- 请求url
- 访问者ip
- 调用方法classMethod
- 参数args
- 返回的内容

2、记录日志类

```java
package com.ch.blog;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @description:
 * @author: chenhao
 * @create:2020/10/26 9:38
 **/
@Aspect
@Component
public class LogAspect {


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Controller层切点
     */
    //web下面所有的类（*）里的任何参数的所有的方法（*(..)）
    @Pointcut("execution(* com.ch.blog.web.*.*(..))")
    public void controllerAspect() {
    }

    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request =attributes.getRequest();
        String url=request.getRequestURL().toString();
        String ip = request.getRemoteAddr();
        String classMethod = joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        RequestLog requestLog = new RequestLog(url,ip,classMethod,args);
        logger.info("Request: {}",requestLog);
    }

    @After("controllerAspect()")
    public void doAfter(){
//        logger.info("之后");
    }

    @AfterReturning(value = "controllerAspect()",returning = "result")
    public void doAfterRuning(Object result){
        logger.info("Result:{}" + result);
    }
    /**
     * 修改请求参数
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("controllerAspect()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("进入到AOP了");
        //获取用户的缓存数据
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String url = request.getRequestURL().toString();
        String ip = request.getRemoteAddr();

        // 获取方法名
       String methodName = joinPoint.getSignature().getName();

        // 反射获取目标类
        java.lang.Class<?> targetClass = joinPoint.getTarget().getClass();

        // 拿到方法对应的参数类型
        java.lang.Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();

        // 根据类、方法、参数类型（重载）获取到方法的具体信息
        Method objMethod = targetClass.getMethod(methodName, parameterTypes);

        logger.info("是{}类的{}方法，方法的具体信息是{}，参数类型是{}",targetClass,methodName,objMethod,parameterTypes);
        System.out.println("是"+targetClass+"类的"+methodName+"方法，方法的具体信息是"+objMethod+"，参数类型是"+parameterTypes);
        Object[] obj = joinPoint.getArgs();
        //通过反射实例化参数对象
        //获取字节码
        try {
            Class<?> aClass = obj[0].getClass();
        } catch (Exception e) {
            //这里有可能出现无法找到setUserModelCache的情况 所以捕获异常直接继续
            //e.printStackTrace();
        }
        System.out.println("=====修改参数==========");
        return joinPoint.proceed(obj);
    }

    /**
     * 将参数封装到一个内部类
     */
    public class RequestLog{
        private String url;
        private String ip;
        private String ClassMethod;
        private Object[] args;

        public RequestLog(String url, String ip, String classMethod, Object[] args) {
            this.url = url;
            this.ip = ip;
            ClassMethod = classMethod;
            this.args = args;
        }

        @Override
        public String toString() {
            return "{" +
                    "url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", ClassMethod='" + ClassMethod + '\'' +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }
    }
}

```

### 3.4、页面处理

1、静态页面导入project



2、thymeleaf布局

- 定义fragment
- 使用fragment布局



3、错误页面美化



## 4、设计与规范

### 4.1、实体设计

实体类：

- 博客Blog
- 博客分类Type
- 博客标签Tag
- 博客评论Comment
- 用户User

![image-20201026161237418](https://github.com/Ximpermanence/PersonBlog/blob/main/READMEimage/image-20201026161237418.png)

![image-20201026161411716](https://github.com/Ximpermanence/PersonBlog/blob/main/READMEimage/image-20201026161411716.png)

![image-20201026161645532](https://github.com/Ximpermanence/PersonBlog/blob/main/READMEimage/image-20201026161645532.png)

![image-20201026161900605](https://github.com/Ximpermanence/PersonBlog/blob/main/READMEimage/image-20201026161900605.png)

![image-20201026161911352](https://github.com/Ximpermanence/PersonBlog/blob/main/READMEimage/image-20201026161911352.png)

![image-20201026161917916](https://github.com/Ximpermanence/PersonBlog/blob/main/READMEimage/image-20201026161917916.png)



![image-20201026161938751](https://github.com/Ximpermanence/PersonBlog/blob/main/READMEimage/image-20201026161938751.png)![image-20201026161942470](https://github.com/Ximpermanence/PersonBlog/blob/main/READMEimage/image-20201026161942470.png)



### 4.2应用分层

![image-20201026162026082](https://github.com/Ximpermanence/PersonBlog/blob/main/READMEimage/image-20201026162026082.png)

### 4.3命名约定

Service/DAO层命名约定:

- 获取单个对象的方法用get做前缀。
- 获取多个对象的方法用list做前缀。
- 获取统计值的方法用count做前缀。
- 插入的方法用save(推荐)或insert做前缀。
- 删除的方法用remove(推荐)或delete做前缀。
- 修改的方法用update做前缀。

## 5、后台管理

### 5.1、登录

1、构建登陆页面和后台管理登录页面

2、UserService和UserResponsitory

3、LoginController实现登录

4、MD5加密

5、登录拦截器

### 5.2分类管理

1、分类管理页面

2、分类列表分页

``````json
//分页拿到的数据样式
{
  "content": [
    {
      "id": 123,
      "title": "blog122",
      " content": "this is blog content"
    },
    {
      "id": 122,
      "title": "blog121",
      "content ": "this is blog content"
    },
    {
      "id": 121,
      "title": "blog120",
      "content": "this is blog content"
    },
    {
      "id": 120,
      "title": "blog119",
      "content": "this is blog content"
    },
    {
      "id": 119,
      "title": "blog118",
      " content": "this is blog content"
    },
    {
      "id": 118,
      "title": "blog117",
      "content": "this is blog content"
    },
    {
      "id": 111,
      "title": "blog110",
      "content": "this is blog content"
    },
    {
      "id": 110,
      "title": "blog109",
      "content": "this is blog content"
    },
    {
      "id": 109,
      "title": "blog108",
      "content": "this is blog content"
    }
  ],
  "last": false,
  "totalPages": 9,  //总页数
  "totalElements": 123,  //总条数
  "size": 15, 		//每页条数
  "number": 0,		//第几页
  "first": true,
  "sort": [
    {
      "direction": "DESC",
      "property": "id",
      "ignoreCase": false,
      "nullHandling": "NATIVE",
      "ascending": false
    }
  ],
  "numberOfElements": 15
}

``````



3、分类新增、修改、删除

### 5.3标签管理

同分类

### 5.4博客管理

1、博客分页查询

2、博客新增

3、博客修改

4、博客删除



## 6、前端展示

### 6.1首页展示

1、博客列表

2、top分类

3、top标签

4、最新博客推荐

5、博客详情



1、Markdown 转换HTML

- commonmark-java https://github.com/atlassian/commonmark-java

- 依赖：

![image-20201102101546474](https://github.com/Ximpermanence/PersonBlog/blob/main/READMEimage/image-20201102101546474.png)

2、

- 评论信息提交与回复
- 评论信息列表展示功能
- 管理员回复评论功能

### 6.2分类页

### 6.3标签页

### 6.4归档页

### 6.5关于我页





## TODO

博客发布上传图片不显示问题





## 可添加完善的

搜索通过下拉框选择时，样式改变

![image-20201030175353607](https://github.com/Ximpermanence/PersonBlog/blob/main/READMEimage/image-20201030175353607.png)



可以在该位置添加一个评论控制功能

![image-20201103151326317](https://github.com/Ximpermanence/PersonBlog/blob/main/READMEimage/image-20201103151326317.png)





