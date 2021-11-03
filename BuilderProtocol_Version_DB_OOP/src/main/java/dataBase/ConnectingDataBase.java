package dataBase;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class ConnectingDataBase {
//база данных представлена таблицей в Exel

    public static final String pathToDataBase = "C:\\BuilderProtocol\\data_base\\dataBase.xlsx";
    private XSSFWorkbook book; //книга Exel
    private XSSFSheet stendList; //лист в книге Exel
    private FileInputStream fileInputStream;
    private String stendName;
    private ArrayList<XSSFSheet> sheetsInDataBase;

    public ConnectingDataBase(){

        connect();

    }

    private void connect(){

        try {
            fileInputStream = new FileInputStream(pathToDataBase);
            book = new XSSFWorkbook(fileInputStream);
            System.out.println("База данных успешно подключена!");

        } catch (FileNotFoundException e) {
            System.out.println("База данных не найдена!");
            //e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Ошибка открытия базы данных!");
            //  e.printStackTrace();
        }

    }

    public void close(){
        try {
            fileInputStream.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


    public void connectToDataBaseStend(){
        stendList = null;
        stendList = book.getSheet(stendName); //лист выбранного стенда

    }

    public boolean checkFoundStendNameInDataBase(String stendName){

        if(book.getSheet(stendName) == null){
            System.out.println("Не найдена таблица для стенда : " + stendName + " в базе данных");
            return false;
        }else{
            this.stendName = stendName;
            return true;
        }
    }

    public ArrayList<XSSFSheet> getSheetsInDataBase(){
        sheetsInDataBase = new ArrayList<>();

        for(int i = 0; i < book.getNumberOfSheets(); i++){
            sheetsInDataBase.add(book.getSheetAt(i));
        }

        return sheetsInDataBase;
    }


    public XSSFSheet getStendList(){
        return stendList;
    }

}
