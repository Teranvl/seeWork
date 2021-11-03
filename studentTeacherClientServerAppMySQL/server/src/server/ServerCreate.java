package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerCreate {//класс создания сервера

    private final int port = 4999; //порт сервера
    private ServerSocket serverSocket; //соединение сервера
    private Socket socket;//соединения для клиентов
    private WriteAndReaderDataBase writeAndReaderDataBase;//переменная для доступа к классу БД
    private ConnectionToDataBase connectionToDataBase; //соединение с БД
    private InputConnect input;//входящее соединение
    private OutputConnect output;//исходящее соединение


    public void start(){ //запуск сервера
        connectionToDataBase = new ConnectionToDataBase();//создаем новое подключние к БД
        writeAndReaderDataBase = new WriteAndReaderDataBase(connectionToDataBase.getStatement());//создаем экземпляр класса чтения и записи БД
        createServerSocket(); //создаем серверное соединение

        while (true) {
            try {
                System.out.println("Ожидание подключения клиента...");
                socket = serverSocket.accept();//ждем клинета
                System.out.println("Клиент подключен!");

                input = new InputConnect(socket, writeAndReaderDataBase, this);//создаем новое входящее соединение
                Thread inputThread = new Thread(input); //создаем новый поток для входящего соединения
                inputThread.start();//запускаем поток

                output = new OutputConnect(socket);//создаем новое исходящее соединение
                Thread outputThread = new Thread(output); //создаем новый поток для исходящего соединения
                outputThread.start();//запускаем поток

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private void createServerSocket(){ //метод создания серверного соединения
        if(serverSocket == null){ //если серверное соединение не создано
            try {
                serverSocket = new ServerSocket(port);//создаем
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public InputConnect getInput() {
        return input;
    }

    public OutputConnect getOutput() {
        return output;
    }
}
