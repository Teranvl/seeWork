package sample.structure;

public class StudentControlPoints {//класс контрольной точки студента
    //переменные экземпляра:
    private int number;
    private int value;

    public StudentControlPoints(int number, int value) {//конструктор
        this.number = number;
        this.value = value;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
