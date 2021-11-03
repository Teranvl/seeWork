package chocolate.parser;

import java.io.*;
import java.util.ArrayList;

public class ReaderDataFile { //класс для чтения файла и заполнения коллеции объектов

    private String path = "src\\resources\\originalData.txt"; //путь к начальному файлу
    private BufferedReader bufferedReader; //для чтения файла
    private FileInputStream fileInputStream;//для чтения файла
    private InputStreamReader inputStreamReader;//для чтения файла
    private ArrayList<Chocolate> chocolateList; //коллекция с шоколадом


    public ReaderDataFile() {

        try {
            //Создаем поток для чтения файла:
            fileInputStream = new FileInputStream(path);
            inputStreamReader = new InputStreamReader(fileInputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
        } catch (IOException e) {
            System.out.println("Ошибка открытия файла");;
        }

    }


    public ArrayList<Chocolate> addToList(ArrayList<Chocolate> chocolateList) { //добавляем шоколад в лист

        try {
            String tempExportCountry;//временная переменная для хранения страны
            while ((tempExportCountry = bufferedReader.readLine()) != null) {//если следующая строка не пустая, присваиваем
                Chocolate chocolate = new Chocolate();//создаем новый шоколад
                //заполняем поля шоколада:
                chocolate.setExportCountry(tempExportCountry);
                chocolate.setExportCompany(bufferedReader.readLine());
                chocolate.setBrandChocolate(bufferedReader.readLine());
                chocolate.setExpertiseIndex(Integer.parseInt(bufferedReader.readLine()));
                chocolate.setExpertiseYear(Integer.parseInt(bufferedReader.readLine()));
                chocolate.setContentCacaoInPercent(Integer.parseInt(bufferedReader.readLine().split("%")[0]));
                chocolate.setProduceCountry(bufferedReader.readLine());
                chocolate.setExpertRating(Float.parseFloat(bufferedReader.readLine()));

                chocolateList.add(chocolate);//добавляем шоколад в коллекцию
            }
        } catch (NullPointerException | IOException e) {
            System.out.println("Ошибка добавления в коллекцию");
        }

        return chocolateList;

    }

}

