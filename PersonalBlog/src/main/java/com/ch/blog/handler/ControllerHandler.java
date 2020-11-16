package com.ch.blog.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @description:
 * @author: chenhao
 * @create:2020/10/23 16:26
 **/
//controlleradvice拦截所有带有@controller的··控制器
@ControllerAdvice
public class ControllerHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //从request获取访问的url
    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(HttpServletRequest request, Exception e) throws Exception {
        logger.error("Request URL:{},Exception:{}",request.getRequestURL(),e);

        //如果存在状态码，把异常抛出，让springboot去处理，否则就用自定义的error
        if(AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class)!=null){
            throw e;
        }
        ModelAndView mv = new ModelAndView();
        mv.addObject("url",request.getRequestURL());
        mv.addObject("exception",e);
        mv.setViewName("error/error");
        return mv;
    }
}
