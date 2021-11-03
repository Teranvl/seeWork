package ru.carservice.model;


public class BusService extends ServiceOfTrucks { //автобусный сервис

    private int maximumNumberOfSeats = 1; //максимальное количество мест для сидения
    private boolean possibilityOfTestingAnEmergencyExit = false; //возможность тестирования запасного выхода
    private boolean possibilityOfTestingSeats = false; //возможность тестирования кресел
    private boolean possibilityOfRepairingTrolleybuses = false; //возможность ремонта троллейбусов




    @Override
    public void changeOilInEngine() {
        System.out.println(this.getServiceName() + " будет проводить операцию: " + "Замена масла в двигателе автобуса");
    }

    @Override
    public void repairFuelPump() {
        System.out.println(this.getServiceName() + " будет проводить операцию: " + "Ремонт дизельного насоса автобуса");
    }

    @Override
    public void openCarService() {
        System.out.println(this.getServiceName() + " - Автобусный сервис открыт");
    }

    @Override
    public void closeCarService() {
        System.out.println(this.getServiceName() + " - Автобусный сервис закрыт");
    }

    @Override
    public void ballJointRepair() {
        System.out.println(this.getServiceName() + " будет проводить операцию: " + "Ремонт шаровой опоры автобуса");
    }


    public int getMaximumNumberOfSeats() {
        return maximumNumberOfSeats;
    }

    public void setMaximumNumberOfSeats(int maximumNumberOfSeats) throws ServiceException {

        if(maximumNumberOfSeats < 1){ //проверка правильности ввода значения, если значение не верно, выбрасываем исключение
            throw new ServiceException("Количество мест для седения в автобусе не может быть менее 1");
        }else{
            this.maximumNumberOfSeats = maximumNumberOfSeats;
        }

    }

    public boolean isPossibilityOfTestingAnEmergencyExit() {
        return possibilityOfTestingAnEmergencyExit;
    }

    public void setPossibilityOfTestingAnEmergencyExit(boolean possibilityOfTestingAnEmergencyExit) {
        this.possibilityOfTestingAnEmergencyExit = possibilityOfTestingAnEmergencyExit;
    }

    public boolean isPossibilityOfTestingSeats() {
        return possibilityOfTestingSeats;
    }

    public void setPossibilityOfTestingSeats(boolean possibilityOfTestingSeats) {
        this.possibilityOfTestingSeats = possibilityOfTestingSeats;
    }

    public boolean isPossibilityOfRepairingTrolleybuses() {
        return possibilityOfRepairingTrolleybuses;
    }

    public void setPossibilityOfRepairingTrolleybuses(boolean possibilityOfRepairingTrolleybuses) {
        this.possibilityOfRepairingTrolleybuses = possibilityOfRepairingTrolleybuses;
    }
}
