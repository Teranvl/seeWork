package ru.carservice.model;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public abstract class CarService implements RunningGearRepair {

    private String serviceAddress = "someAddress"; //адрес сервиса
    private String serviceName = "someName"; //название сервиса
    private String phoneNumber = "+79998887776"; //телефонный номер
    private int maxCountCarsInService = 2; //максимально-возможное количество машин в сервисе
    private int carsIntoServiceInTurn = -1; //количество машин в очереди на сервис
    private int timeCarInService = 200; //время, за которое производится ремонт одной машины в сервисе
    private BlockingQueue<Integer> queue; //очередь внутри сервиса(наполненность сервиса машинами)
    private ArrayList<String> imitationProtocol = new ArrayList<>();//используется для хранения и отображения результатов имитации работы сервиса
    private boolean serviceIsReady = false; //флаг,что работа сервиса закончена
    private int id; //ид сервиса


    public void runServiceCars() throws ServiceException { //метод запуска имитации работы сервиса

        if( maxCountCarsInService == -1 || carsIntoServiceInTurn == -1 || timeCarInService == -1 ){ //если не инициализированы поля, то запуск невозможен

            throw new ServiceException("Для правильной работы сервиса, необходимо установить значения следующих переменных: " + //выбрасываем исключение
                    "maxCountCarsInService & carsIntoServiceInTurn & timeCarInService!");

        }else {

            queue = new ArrayBlockingQueue<>(maxCountCarsInService); //создаем объект очереди, передаем в него количество транспорта, одновременно находящегося в сервисе

            Thread produce = new Thread(() -> { //создаем первый поток для имитации начала ремонта транспорта в сервисе

                try {
                    produce(); //запускаем метод
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            });

            Thread consume = new Thread(() -> { //создаем второй поток для имитации окончания ремонта транспорта в сервисе

                try {
                    consume();//запускаем метод
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            });

            produce.start();//запускаем первый поток
            consume.start();//запускаем второй поток
        }
    }


    private void produce() throws InterruptedException { //имитации начала ремонта транспорта в сервисе

        while(carsIntoServiceInTurn != 0){ //пока машины в очереди около сервиса не закончатся

            synchronized (this) { //синхронизация на текущем объекте
                if (queue.size() == maxCountCarsInService) { //если очередь полностью заполнена
                    wait(); //освобождаем intrinsic Locks
                }

                queue.put(carsIntoServiceInTurn); //ставим их в очередь //метод put при полной занятости очереди ждем, пока место в ней не освободится
                System.out.println("Transport number " + carsIntoServiceInTurn + " - stopped at the service " + serviceName + "; Places in the service = " + (maxCountCarsInService - queue.size())); //выводим информацию о заполненности сервиса
                imitationProtocol.add("Transport number " + carsIntoServiceInTurn + " - stopped at the service " + serviceName + "; Places in the service = " + (maxCountCarsInService - queue.size()));
                carsIntoServiceInTurn--; //декрементируем количество машины в очереди
            }
        }
    }

    private void consume() throws InterruptedException {//имитации окончания ремонта транспорта в сервисе

        int carNumber; // номер машины(используем только для наглядности)

        while(!(queue.size() == 0 && carsIntoServiceInTurn == 0)){ //пока очередь не закончится и машин у сервиса не останется, делаем:

            Thread.sleep(timeCarInService); //приостонавливаем поток, на время, которое необходимо для ремонта одной машины
            synchronized (this) {//синхронизация на текущем объекте
                carNumber = queue.take();//забираем из очереди значение номера готовой машины, тем самым освобождая одно место в очереди
                System.out.println("Transport number " + carNumber + " - leave the service " + serviceName + "; Places in the service = " + (maxCountCarsInService - queue.size()));//выводим информацию о заполненности
                imitationProtocol.add("Transport number " + carNumber + " - leave the service " + serviceName + "; Places in the service = " + (maxCountCarsInService - queue.size()));
                notify(); //предоставляем другому потоку intrinsic Locks
            }
        }

        imitationProtocol.add("------------all-works-complete--------------");
        serviceIsReady = true;//сервис выполнил работы
    }


    public abstract void changeOilInEngine(); //сменить масло в двигателе

    public abstract void repairFuelPump(); //ремонт топливного насоса

    public abstract void openCarService(); //открыть сервис

    public abstract void closeCarService(); //закрыть сервис

    public String getServiceAddress() {
        return serviceAddress;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getMaxCountCarsInService() {
        return maxCountCarsInService;
    }

    public void setMaxCountCarsInService(int maxCountCarsInService) {
        this.maxCountCarsInService = maxCountCarsInService;
    }

    public int getTimeCarInService() {
        return timeCarInService;
    }

    public void setTimeCarInService(int timeCarInService) {
        this.timeCarInService = timeCarInService;
    }

    public int getCarsIntoServiceInTurn() {
        return carsIntoServiceInTurn;
    }

    public void setCarsIntoServiceInTurn(int carsIntoServiceInTurn) {
        this.carsIntoServiceInTurn = carsIntoServiceInTurn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public BlockingQueue<Integer> getQueue() {
        return queue;
    }

    public void setQueue(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    public ArrayList<String> getImitationProtocol() {
        return imitationProtocol;
    }

    public void setImitationProtocol(ArrayList<String> imitationProtocol) {
        this.imitationProtocol = imitationProtocol;
    }

    public boolean isServiceIsReady() {
        return serviceIsReady;
    }

    public void setServiceIsReady(boolean serviceIsReady) {
        this.serviceIsReady = serviceIsReady;
    }
}
