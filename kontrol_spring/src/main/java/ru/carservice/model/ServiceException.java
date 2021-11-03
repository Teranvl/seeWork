package ru.carservice.model;

public class ServiceException extends Exception { //класс исключения

    public ServiceException (String textInException){ //конструктор, в параметры которого записывается тест исключения
        super(textInException);
    }


}
