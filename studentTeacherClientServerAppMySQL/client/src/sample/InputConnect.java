package sample;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InputConnect implements Runnable{ //входящее соединения, реализуем интерфейс для создания потока

    private Socket socket;
    private ObjectInputStream objectInputStream;//входной поток
    private Object inputObject;//объект, получаемый от сервера
    private String answerFromServer;//сообщение от сервера
    private Map<String, List<Integer>> mapAnswerFromServer; //стуктура даных принятая от сервера
    private boolean answerIsReady = false;//готовность к чтению ответа от сервера

    public InputConnect(Socket socket) {//конструктор
        this.socket = socket;
    }

    @Override
    public void run() { //запуск потока
        System.out.println("Поток приема запущен");
        input();
    }

    private void input(){

        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());//получаем входящий поток
            while (true){
                inputObject = objectInputStream.readObject(); //ждем ответ от сервера
                System.out.println("Ответ от сервера принят");

                if(inputObject.getClass().equals(String.class)){ //если ответ от сервера является строкой
                    System.out.println("Ответ от сервера отпределен как строка");
                    answerFromServer = (String) inputObject; //кладем ответ в переменную
                    answerIsReady = true;//выставляем флаг готовности
                }

                if(inputObject.getClass().equals(HashMap.class)){ //если ответ от сервера является структорой HashMap
                    System.out.println("Ответ от сервера отпределен как структура Map");
                    mapAnswerFromServer = (HashMap) inputObject; //записывам ответ в переменную
                    answerIsReady = true;//выставляем флаг готовности
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void disableAnswerToReady(){ //метод для снятия флага готовности
        answerIsReady = false;
    }

    public void clearAnswer(){//метод для очистки ответа от сервера
        answerFromServer = "";
        mapAnswerFromServer = null;
    }


    public String getAnswerFromServer() {
        return answerFromServer;
    }

    public boolean isAnswerIsReady() {
        return answerIsReady;
    }

    public Map<String, List<Integer>> getMapAnswerFromServer() {
        return mapAnswerFromServer;
    }
}
