package server;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteAndReaderDataBase { //класс для работы с БД

    private Statement statement;

    public WriteAndReaderDataBase(Statement statement){
        this.statement = statement;
    }

    public boolean authorisationTeacher(String login, String password){//авторизация преподавателя
        try {
            String query = "SELECT * FROM teacher_accounts WHERE login_value = '" + login + "' AND password_value = '" + password + "'"; //формируем запрос
            ResultSet resultSet = statement.executeQuery(query); //отправляем запрос и получаем ответ

            if(resultSet.next()){//если есть совпадение в базе данных
                return true;
            }else {
                return false;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public boolean authorisationStudent(String login, String password) { //авторизация студента
        try {
            String query = "SELECT * FROM student_accounts WHERE login_value = '" + login + "' AND password_value = '" + password + "'";//формируем запрос
            ResultSet resultSet = statement.executeQuery(query);//отправляем запрос и получаем ответ

            if(resultSet.next()){//если есть совпадение в базе данных
                return true;
            }else {
                return false;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public boolean createSubjectInDataBase(String subjectName, int countControlPoints){ //создание БД дисциплины

        StringBuilder strForQuery = new StringBuilder(); //строка является частью запроса -> динамическое создание количества контрольных точек по предмету

        //в цикле ниже происходит формирование части запроса, для создания контрольных точек в БД
        for(int i = 1; i <= countControlPoints; i++){
            String tmp = "point_number_" + i + " INT NOT NULL DEFAULT 0";

            if( i != countControlPoints ){
                tmp += ",";
            }
            strForQuery.append(tmp);
        }

        try {
            statement.execute("CREATE TABLE " + subjectName +
                                  " (count_students INT NOT NULL AUTO_INCREMENT," +
                                  "PRIMARY KEY (count_students)," +
                                  "student_id INT NOT NULL REFERENCES students (student_id)," +
                                   strForQuery + ")"); //форрмируем полность запрос и отправлям

            System.out.println("Предмет " + subjectName + " добавлен в БД");
            return true;

        } catch (SQLException throwables) {
            System.out.println("Ошибка создания таблицы предмета " + subjectName + " в БД");
            throwables.printStackTrace();
            return false;
        }
    }

    public boolean addStudentToSubject(String firstName, String lastName, String subjectName){ //добавляем студента в БД предмета

        try {
            String query = "SELECT * FROM students WHERE first_name = '" + firstName + "' AND last_name = '" + lastName + "'";//формируем запрос
            ResultSet resultSetStudents = statement.executeQuery(query); //отправляем запрос и получаем ответ

            if(!resultSetStudents.next()){//если студента в БД нет, то добавляем
                addStudentInStudentsTable(firstName, lastName);
            }

            resultSetStudents = statement.executeQuery(query);//отправляем запрос и получаем ответ

            if(resultSetStudents.next()){ //если студент найден в БД

                int tmpStudentId = resultSetStudents.getInt("student_id");
                String tmpFirstName = resultSetStudents.getString("first_name");
                String tmpLastName = resultSetStudents.getString("last_name");

                ResultSet resultSetSubject = statement.executeQuery("SELECT * FROM " + subjectName + " WHERE student_id = " + tmpStudentId);//ищем студента в БД дисциплины

                if(!resultSetSubject.next()){//если студент не добавлен в БД дисциплины
                    statement.execute("INSERT INTO " + subjectName +
                            " (student_id)" +
                            " VALUES (" + tmpStudentId + ")");

                    System.out.println("студент = " + tmpFirstName + " " + tmpLastName + " добавлен к дисциплине " + subjectName);
                    return true;
                }else{//если уже есть в БД дисциплине, то не добавляем
                    System.out.println("ОШИБКА ДОБАВЛЕНИЯ СТУДЕНТА К ПРЕДМЕТУ. Студент " + tmpFirstName + " " + tmpLastName + " уже есть в БД дисциплины " + subjectName);
                    return false;
                }
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return false;

    }

    private void addStudentInStudentsTable(String firstName, String lastName) { //добавляем студента в базу данных студентов
        try {
            statement.execute("INSERT INTO students (first_name, last_name) VALUES ('" + firstName + "', '" + lastName + "')");//отправляем запрос
            System.out.println("Студент " + firstName + " " + lastName + " успешно добавлен в БД студентов");
        } catch (SQLException throwables) {
            System.out.println("Ошибка добавления студента в БД студентов");
            throwables.printStackTrace();
        }

    }

    public Map<String, List<Integer>> getStudentsControlPoints(String subjectName){//получить список студентов и их контрольных точек
        try {
            Map<String, List<Integer>> studentsThisControlPoints = new HashMap<>();//структура для хранения данных студентов и их контрольных точек

            ResultSet resultSetSubject = statement.executeQuery("SELECT * FROM " + subjectName + " inner join students on " + subjectName + ".student_id = students.student_id");//запрос для получения контрольных точек и имен студентов
            ResultSetMetaData resultSetMetaData = resultSetSubject.getMetaData();//для получения значения количества столбцов
            int countColumn = resultSetMetaData.getColumnCount(); //получаем количество столбцов

            while(resultSetSubject.next()){

                String tmpStudentName = resultSetSubject.getString("first_name") + "_" + resultSetSubject.getString("last_name") + "_" + resultSetSubject.getInt("student_id"); //преобразуем имя в одну строку
                List<Integer> tmpStudentPoints = new ArrayList<>(); //создаем массив для контрольных точек

                for(int i = 1; i <= countColumn - 5; i++){//цикл по контрольным точкам. -5 -> т.к. в выборке 5 лишних столбцов
                    tmpStudentPoints.add(resultSetSubject.getInt("point_number_" + i));//добавляем в массив контрольные точки
                }

                studentsThisControlPoints.put(tmpStudentName, tmpStudentPoints);//добаляем студента в структуру
            }

            return studentsThisControlPoints;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;///////////
        }



    }

    public boolean setStudentsPointsTable(Map<String, List<Integer>> studentsControlPoints, String subjectName){//установка контрольных точек для студнетов

        for(Map.Entry<String, List<Integer>> studentControlPoints : studentsControlPoints.entrySet()){ //цикл по котрольным точкам

            int tmpStudentId = Integer.parseInt(studentControlPoints.getKey().split("_")[2]);
            String partQuery = "";

            //в цикле ниже происходим формирование части запроса для установки контрольных точек
            for(int i = 0; i < studentControlPoints.getValue().size(); i++){
                partQuery += "point_number_" + (i + 1) + " = " + studentControlPoints.getValue().get(i) + ", ";

                if(i == studentControlPoints.getValue().size() - 1){
                    partQuery += "point_number_" + (i + 1) + " = " + studentControlPoints.getValue().get(i);
                }

            }

            try {
                String fullQuery = "UPDATE " + subjectName + " SET " + partQuery + " WHERE student_id = " + tmpStudentId; //формируем польностью запрос
                statement.execute(fullQuery);//отправлям запрос

            } catch (SQLException throwables) {
                throwables.printStackTrace();
                return false;
            }

        }

        return true;

    }



}
