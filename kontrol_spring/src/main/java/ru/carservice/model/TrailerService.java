package ru.carservice.model;

public class TrailerService extends ServiceOfTrucks { //сервис тягачей

    private boolean possibilityOfCheckingCouplingDevice = false; //возможность проверки сцепного устройства
    private int maximumHeight = 7; //максимальная высота тягача
    private int maximumLength = 100; //максимальная длина


    @Override
    public void ballJointRepair() {
        System.out.println(this.getServiceName() + " будет проводить операцию: " + "Ремонт шаровой опоры тягача");
    }

    @Override
    public void changeOilInEngine() {
        System.out.println(this.getServiceName() + " будет проводить операцию: " + "Замена масла в двигателе тягача");
    }

    @Override
    public void repairFuelPump() {
        System.out.println(this.getServiceName() + " будет проводить операцию: " + "Ремонт дизельного насоса тягача");
    }

    @Override
    public void openCarService() {
        System.out.println(this.getServiceName() + " - Сервис для тягачей открыт");
    }

    @Override
    public void closeCarService() {
        System.out.println(this.getServiceName() + " - Сервис для тягачей закрыт");
    }


    public boolean isPossibilityOfCheckingCouplingDevice() {
        return possibilityOfCheckingCouplingDevice;
    }

    public void setPossibilityOfCheckingCouplingDevice(boolean possibilityOfCheckingCouplingDevice) {
        this.possibilityOfCheckingCouplingDevice = possibilityOfCheckingCouplingDevice;
    }

    public int getMaximumHeight() {
        return maximumHeight;
    }

    public void setMaximumHeight(int maximumHeight) throws ServiceException {

        if(maximumHeight > 7){//проверка правильности ввода значения, если значение не верно, выбрасываем исключение
            throw new ServiceException("Максимальня высота тягача не может быть более 7");
        }else{
            this.maximumHeight = maximumHeight;
        }

    }

    public int getMaximumLength() {
        return maximumLength;
    }

    public void setMaximumLength(int maximumLength) throws ServiceException {

        if(maximumLength > 100){//проверка правильности ввода значения, если значение не верно, выбрасываем исключение
            throw new ServiceException("Максимальня длинна тягача не может быть более 100");
        }else{
            this.maximumLength = maximumLength;
        }

    }
}
