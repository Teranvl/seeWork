package sample.structure;

import java.util.List;

public class Student {//класс студент
    //переменные экземпляра:
    private String id;
    private String firstName;
    private String lastName;
    private Double totalScore;
    private List<Integer> controlPoints;

    public Student(String id, String firstName, String lastName, List<Integer> controlPoints) {//конструктор класса
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.controlPoints = controlPoints;
        setTotalScore();//при вызове конструктора идет подсчет рейтинга студнета
    }

    private void setTotalScore(){ //подсчет рейтинга студента
        double score = 0.0;
        for(int i : controlPoints){
            score += i;
        }
        totalScore = score / controlPoints.size();
    }


    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<Integer> getControlPoints() {
        return controlPoints;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setControlPoints(List<Integer> controlPoints) {
        this.controlPoints = controlPoints;
    }

    public Double getTotalScore() {
        return totalScore;
    }
}
