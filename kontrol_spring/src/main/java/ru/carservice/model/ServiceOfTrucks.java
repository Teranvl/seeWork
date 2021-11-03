package ru.carservice.model;

public abstract class ServiceOfTrucks extends CarService { //сервис грузовых авто

    private int maximumWheelsDiameter = 13; //максимальный диаметр колес
    private boolean possibilityOfRepairAtPlaceOfBreakdown = false; //возможность ремонта на месте поломки
    private boolean possibilityToCallTug = false; //возможность вызова буксира


    public int getMaximumWheelsDiameter() {
        return maximumWheelsDiameter;
    }

    public void setMaximumWheelsDiameter(int maximumWheelsDiameter) throws ServiceException {

        if(maximumWheelsDiameter > 40){//проверка правильности ввода значения, если значение не верно, выбрасываем исключение
            throw new ServiceException("Диаметр колес грузовых авто не может быть более 40");
        }else{
            this.maximumWheelsDiameter = maximumWheelsDiameter;
        }

    }

    public boolean isPossibilityOfRepairAtPlaceOfBreakdown() {
        return possibilityOfRepairAtPlaceOfBreakdown;
    }

    public void setPossibilityOfRepairAtPlaceOfBreakdown(boolean possibilityOfRepairAtPlaceOfBreakdown) {
        this.possibilityOfRepairAtPlaceOfBreakdown = possibilityOfRepairAtPlaceOfBreakdown;
    }

    public boolean isPossibilityToCallTug() {
        return possibilityToCallTug;
    }

    public void setPossibilityToCallTug(boolean possibilityToCallTug) {
        this.possibilityToCallTug = possibilityToCallTug;
    }
}
