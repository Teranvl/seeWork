package ru.carservice.dao;

import org.springframework.stereotype.Component;
import ru.carservice.model.CarService;

import java.util.ArrayList;
import java.util.List;

@Component//команда спрингу, чтобы он создал бин этого класса
public class ServiceDao { //класс, который занимаесть работой с базой данных
                            //в данном случая базой данных является arraylist

    private static int SERVICE_ID;//ид сервиса
    private List<CarService> services; //список сервисов

    public ServiceDao() { //конструктор
        services = new ArrayList<>(); //
    }

    public List getServices() { //получить список сервисов
        return services;
    }

    public void addToServicesList(CarService service){ //добавить в список сервисов
        service.setId(++SERVICE_ID); //присваиваем ид сервису
        services.add(service);//добавляем
    }

    public CarService getService(int id) {//получить сервис по ид
        for(CarService service : services){//ищем в цикле
            if(service.getId() == id){ //когда находим
                return service;//возвращаем сервис
            }
        }

        return null;
    }
}
