package ru.carservice.model;

public class PassengerCarService extends CarService{ //сервис легковых авто

    private int maximumWeight = 500; //максимальный вес авто
    private int minimumYearProduction = 2000; //год выпуска не менее
    private boolean possibilityRecordByPhone = false; //возможность записи по телефону


    @Override
    public void changeOilInEngine() {
        System.out.println(this.getServiceName() + " будет проводить операцию: " + "Замена масла в двигателе легкового автомобиля");
    }

    @Override
    public void repairFuelPump() {
        System.out.println(this.getServiceName() + " будет проводить операцию: " + "Ремонт бензинового насоса легкового автомобиля");
    }

    @Override
    public void openCarService() {
        System.out.println(this.getServiceName() + " - Сервис для легковых автомобилей открыт");
    }

    @Override
    public void closeCarService() {
        System.out.println(this.getServiceName() + " - Сервис для легковых автомобилей закрыт");
    }

    @Override
    public void ballJointRepair() {
        System.out.println(this.getServiceName() + " будет проводить операцию: " + "Ремонт шаровой опоры легкового автомобиля");
    }


    public int getMaximumWeight() {
        return maximumWeight;
    }

    public void setMaximumWeight(int maximumWeight) throws ServiceException {

        if(maximumWeight < 500){//проверка правильности ввода значения, если значение не верно, выбрасываем исключение
            throw new ServiceException("Вес легкового автомобиля не можеть быть менее 500");
        }else{
            this.maximumWeight = maximumWeight;
        }

    }

    public int getMinimumYearProduction() {
        return minimumYearProduction;
    }

    public void setMinimumYearProduction(int minimumYearProduction) throws ServiceException {

        if(minimumYearProduction < 2000){//проверка правильности ввода значения, если значение не верно, выбрасываем исключение
            throw new ServiceException("Год выпуска легкового автомобиля не можеть быть ранее 2000");
        }else{
            this.minimumYearProduction = minimumYearProduction;
        }

    }

    public boolean isPossibilityRecordByPhone() {
        return possibilityRecordByPhone;
    }

    public void setPossibilityRecordByPhone(boolean possibilityRecordByPhone) {
        this.possibilityRecordByPhone = possibilityRecordByPhone;
    }
}
