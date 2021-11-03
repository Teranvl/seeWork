package sample;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;


public class OutputConnect implements Runnable{ //класс исходящего соединения, реализуем интерфейс для создания потока

    private Socket socket;
    private ObjectOutputStream objectOutputStream; //выходной поток
    private boolean requestIsReady = false; //готовность запроса к серверу
    private Object request; //сам запрос от сервера

    public OutputConnect(Socket socket) {//конструтор
        this.socket = socket;
    }

    @Override
    public void run() { //запуск потока
        System.out.println("Поток отправки запущен");
        output();
    }

    private void output(){

        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream()); //получаем исходящий поток

            while(true) {

                if(requestIsReady){ //если есть разрещение на отправку
                    requestIsReady = false; //снимает разрешние
                    objectOutputStream.writeObject(request); //записываем запрос для отправки
                    objectOutputStream.flush(); //отправляем
                }else{
                    Thread.sleep(100); //приостанавливаем поток на время
                }
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


    }

    public void sendRequestToServer(Object request){
        this.request = request;
        requestIsReady = true;
    }

}
