package sample;

import java.io.IOException;
import java.net.Socket;

public class ConnectToServer {

    private InputConnect inputConnect;//входящее соединения
    private OutputConnect outputConnect; //исходящее соединение
    private Socket socket;

    public void startClientConnect(){ //запуск соединения клиента
        try {
            socket = new Socket("127.0.0.1", 4999);//создаем соект, для соединения с сервером, указываем порт и ip

            outputConnect = new OutputConnect(socket);//новое исходящее соединение
            Thread output = new Thread(outputConnect); //создаем новый поток с исходящим соединением
            output.start();//запускаем поток

            inputConnect = new InputConnect(socket);//новое входящее соединение
            Thread input = new Thread(inputConnect);//создаем новый поток с входящим соединением
            input.start();//запускаем поток

        } catch (IOException e) {
            System.out.println("Ошибка подключение к серверу");
            e.printStackTrace();
        }
    }

    public InputConnect getInputConnect() {
        return inputConnect;
    }

    public OutputConnect getOutputConnect() {
        return outputConnect;
    }


}
