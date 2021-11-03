package ru.carservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.carservice.dao.ServiceDao;
import ru.carservice.model.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Controller//даем понять спрингу,что это класс контроллера
@RequestMapping("/services") //все url начинаются с этого адреса
public class ServicesController {

    private ServiceDao serviceDao;//ссылка на объект, для работы с БД

    @Autowired//даем понять спрингу, чтобы он автоматически внедрил зависимость в конструктор
    public ServicesController(ServiceDao serviceDao) {//конструтор
        this.serviceDao = serviceDao;
    }

    @GetMapping() //прописываем маппинг т.е. при переходе по localhost/services будем попадать в метод showHomePage() и возвращать представление services/homePage
    public String showHomePage(Model model){//метод отображения домашней страницы сервиса
        model.addAttribute("services", serviceDao.getServices());//в модель добавляем атрибут под названием services, передаем лист сервсов для отображения на домашней странице
        return "services/homePage"; //возвращаем отображение домашней страницы
    }

    @GetMapping("/{id}")//маппинг, при переходе по localhost/services/ид_сервиса, попадаем в этот метод
    public String showServicePage(@PathVariable("id") int id, Model model){//считываем ид из ссылки

        ArrayList<String> mass = new ArrayList<>(); //создаем массив для "специальных опций"

        if(serviceDao.getService(id) instanceof PassengerCarService){ //проверяем принадлежность созданного сервиса к сервису легковых авто

            PassengerCarService passengerCarService = (PassengerCarService) serviceDao.getService(id);//получаем сервис по ид и кладем ссылку на него в переменную passengerCarService
            //добавляем поля, присущие только passengerCarService:
            mass.add("Maximum weight : " + passengerCarService.getMaximumWeight());
            mass.add("Minimum year production : " + passengerCarService.getMinimumYearProduction());
            mass.add("Possibility record by phone : " + passengerCarService.isPossibilityRecordByPhone());

        }

        if(serviceDao.getService(id) instanceof ServiceOfTrucks){//проверяем принадлежность созданного сервиса к сервису легковых авто

            ServiceOfTrucks serviceOfTrucks = (ServiceOfTrucks) serviceDao.getService(id);//получаем сервис по ид и кладем ссылку на него в переменную
            //добавляем поля, присущие только ServiceOfTrucks:
            mass.add("Maximum wheels diameter : " + serviceOfTrucks.getMaximumWheelsDiameter());
            mass.add("Possibility of repair at place of breakdown : " + serviceOfTrucks.isPossibilityOfRepairAtPlaceOfBreakdown());
            mass.add("Possibility to call tug : " + serviceOfTrucks.isPossibilityToCallTug());

            if(serviceDao.getService(id) instanceof BusService){ //проверяем принадлежность созданного сервиса к сервису автобусов

                BusService busService = (BusService) serviceDao.getService(id);//получаем сервис по ид и кладем ссылку на него в переменную
                //добавляем поля, присущие только BusService:
                mass.add("Maximum number of seats : " + busService.getMaximumNumberOfSeats());
                mass.add("Possibility of testing an emergency exit : " + busService.isPossibilityOfTestingAnEmergencyExit());
                mass.add("Possibility of testing seats : " + busService.isPossibilityOfTestingSeats());
                mass.add("Possibility of repairing trolleybuses : " + busService.isPossibilityOfRepairingTrolleybuses());

            }

            if(serviceDao.getService(id) instanceof TrailerService){ //проверяем принадлежность созданного сервиса к сервису тягочей

                TrailerService trailerService = (TrailerService) serviceDao.getService(id);//получаем сервис по ид и кладем ссылку на него в переменную
                //добавляем поля, присущие только TrailerService:
                mass.add("Maximum length : " + trailerService.getMaximumLength());
                mass.add("Maximum height : " + trailerService.getMaximumHeight());
                mass.add("Possibility of checking coupling device : " + trailerService.isPossibilityOfCheckingCouplingDevice());

            }
        }


        model.addAttribute("service", serviceDao.getService(id));//добавляем атрибут в модель
        model.addAttribute("specialOptions", mass);//добавляем атрибут в модель

        return "services/servicePage"; //возращаем страницу, с отображением информации о сервисе
    }


    @GetMapping("/createPassengerCarService") //при переходе по /createPassengerCarService
    public String showPageCreateNewPassengerCarService(Model model){

        model.addAttribute("service", new PassengerCarService()); //добавляем атрибут

        return "services/createPagePassengerCarService"; //возвращаем представление
    }

    @GetMapping("/createTrailerService")//при переходе по /createTrailerService
    public String showPageCreateNewTrailerService(Model model){

        model.addAttribute("service", new TrailerService());//добавляем атрибут

        return "services/createPageTrailerService";//возвращаем представление
    }

    @GetMapping("/createBusService")//при переходе по /createBusService
    public String showPageCreateNewService(Model model){

        model.addAttribute("service", new BusService());//добавляем атрибут

        return "services/createPageBusService";//возвращаем представление
    }


    @PostMapping("/createService") //при отправке post запроса на сервер, при создании сервера
    public String addNewServicePassengerCarService(HttpServletRequest request){ //получаем объект запроса

        CarService carService = null; //создаем переменную
        String serviceType = request.getParameter("serviceType"); //из запроса достаем информацию о типе создаваемого сервиса

        switch (serviceType) { //перебор вариантов, в зависимости от того что было в запросе, тот объект и создаем

            case ("passenger") : {
                carService = new PassengerCarService();
                break;
            }
            case ("trailer") : {
                carService = new TrailerService();
                break;
            }
            case ("bus") : {
                carService = new BusService();
                break;
            }

        }
        //инициализируем поля:
        carService.setServiceName(request.getParameter("serviceName"));
        carService.setServiceAddress(request.getParameter("serviceAddress"));
        carService.setPhoneNumber(request.getParameter("phoneNumber"));
        carService.setMaxCountCarsInService(Integer.parseInt(request.getParameter("maxCountCarsInService")));
        carService.setTimeCarInService(Integer.parseInt(request.getParameter("timeCarInService")));

        if(carService instanceof PassengerCarService){ //если сервис для легковых авто
        //инициализируем поля:
            ((PassengerCarService) carService).setPossibilityRecordByPhone(Boolean.parseBoolean(request.getParameter("possibilityRecordByPhone")));

            try {
                ((PassengerCarService) carService).setMaximumWeight(Integer.parseInt(request.getParameter("maximumWeight")));
                ((PassengerCarService) carService).setMinimumYearProduction(Integer.parseInt(request.getParameter("minimumYearProduction")));
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }

        if(carService instanceof TrailerService){//если сервис для тягочей
            //инициализируем поля:
            ((TrailerService) carService).setPossibilityOfRepairAtPlaceOfBreakdown(Boolean.parseBoolean(request.getParameter("possibilityOfRepairAtPlaceOfBreakdown")));
            ((TrailerService) carService).setPossibilityToCallTug(Boolean.parseBoolean(request.getParameter("possibilityToCallTug")));
            ((TrailerService) carService).setPossibilityOfCheckingCouplingDevice(Boolean.parseBoolean(request.getParameter("possibilityOfCheckingCouplingDevice")));

            try {
                ((TrailerService) carService).setMaximumHeight(Integer.parseInt(request.getParameter("maximumHeight")));
                ((TrailerService) carService).setMaximumLength(Integer.parseInt(request.getParameter("maximumLength")));
                ((TrailerService) carService).setMaximumWheelsDiameter(Integer.parseInt(request.getParameter("maximumWheelsDiameter")));
            }catch (ServiceException serviceException){
                serviceException.printStackTrace();
            }
        }

        if(carService instanceof BusService){//если сервис для автбусов
            //инициализируем поля:
            ((BusService) carService).setPossibilityToCallTug(Boolean.parseBoolean(request.getParameter("possibilityToCallTug")));
            ((BusService) carService).setPossibilityOfRepairAtPlaceOfBreakdown(Boolean.parseBoolean(request.getParameter("possibilityOfRepairAtPlaceOfBreakdown")));
            ((BusService) carService).setPossibilityOfTestingAnEmergencyExit(Boolean.parseBoolean(request.getParameter("possibilityOfTestingAnEmergencyExit")));
            ((BusService) carService).setPossibilityOfTestingSeats(Boolean.parseBoolean(request.getParameter("possibilityOfTestingSeats")));
            ((BusService) carService).setPossibilityOfRepairingTrolleybuses(Boolean.parseBoolean(request.getParameter("possibilityOfRepairingTrolleybuses")));

            try {
                ((BusService) carService).setMaximumWheelsDiameter(Integer.parseInt(request.getParameter("maximumWheelsDiameter")));
                ((BusService) carService).setMaximumNumberOfSeats(Integer.parseInt(request.getParameter("maximumNumberOfSeats")));
            }catch (ServiceException serviceException){
                serviceException.printStackTrace();
            }
        }

        serviceDao.addToServicesList(carService);//добавляем сервис в базу данных

        return "redirect:/services"; //перенаправляем на другой url, на домашнюю страницу
    }

    @PostMapping("/imitation") //при отправке post запроса на сервер, для имитации работы сервиса
    public String imitationServiceWork(HttpServletRequest request){ //берем запроса
        int countCars = Integer.parseInt(request.getParameter("carsIntoServiceInTurn"));//считываем количество машин
        int id = Integer.parseInt(request.getParameter("idService"));//считываем ид сервиса

        serviceDao.getService(id).setCarsIntoServiceInTurn(countCars);//устанавливаем в выбранный сервис количество машин перед сервисом

        try {
            serviceDao.getService(id).runServiceCars(); //запускаем имитацию работы сервиса
        } catch (ServiceException serviceException) {
            serviceException.printStackTrace();
        }

        while(!serviceDao.getService(id).isServiceIsReady()){//ждем пока сервис не закончит работу
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        serviceDao.getService(id).setServiceIsReady(false); //сбрасываем флаг, что сервис завершил работы



        return "redirect:/services/imitationProtocol?idService=" + id; //перенаправляем пользователя на страницу с результатом имитации

    }

    @GetMapping("/imitationProtocol") //при переходе по /imitationProtocol после имитации работы сервиса
    public String showPageImitationServiceWork(HttpServletRequest request, Model model){

        int id = Integer.parseInt(request.getParameter("idService")); //из запроса достаем ид сервиса
        model.addAttribute("imitationProtocol", serviceDao.getService(id).getImitationProtocol());//добавляем абрибут с протоколом имитации сервиса для отображения

        return "services/imitationProtocolPage";//возвращаем представление
    }




}
