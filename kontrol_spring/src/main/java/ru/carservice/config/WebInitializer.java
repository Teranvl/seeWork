package ru.carservice.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
//для реализации перенаправления запросов пользователя в dispatcher servlet реализуем абстрактный класс


    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {SpringConfig.class};//подставляем конфигаруционный файл
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"};//означает, что любой http запрос пользователя перенаправляем в dispatcher servlet
    }

}
