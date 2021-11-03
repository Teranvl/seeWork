package chocolate.parser;

import java.util.ArrayList;

public class Main {


    public static void main(String[] args) {

        ReaderDataFile readerDataFile = new ReaderDataFile();//создаем объект класса для чтения файла
        ArrayList<Chocolate> chocolateList = readerDataFile.addToList(new ArrayList<>());//создаем основную коллекцию шоколада

        Tasks tasks = new Tasks();//объект класса с методами выполнения заданий
        tasks.printChocolateThisHighExpertRatingThisHighAndLowCacao(chocolateList);//метод первого задания
        tasks.outputInFileInformationAboutCountAndListBrandChocolateThisExpertRatingAbove3(chocolateList, 72,2009);//метод второго задания
        tasks.outputInFileListThisProduceCountrySortByExpertRating(chocolateList);//метод третьего задания

    }




}
