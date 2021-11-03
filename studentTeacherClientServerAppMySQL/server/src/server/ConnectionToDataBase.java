package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionToDataBase { //класс подключение к БД

    private String url = "jdbc:mysql://localhost:3306/university?serverTimezone=Europe/Moscow"; //адрес БД
    private String userName = "root"; //логин к БД
    private String password = "1234";//пароль к БД
    private Connection connection;
    private Statement statement;

    public ConnectionToDataBase() {

        try {
            connection = DriverManager.getConnection(url, userName, password); //создаем новое соединение
            statement = connection.createStatement(); //создаем обект, для выполнния запросов sql к БД
            System.out.println("БД успешно подключена");

        } catch (SQLException throwables) {
            System.out.println("Ошибка подключения к БД");
            throwables.printStackTrace();
        }
    }

    public Statement getStatement(){
        return statement;
    }

}
