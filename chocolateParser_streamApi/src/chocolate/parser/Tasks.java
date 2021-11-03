package chocolate.parser;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class Tasks {//класс с заданиями

    private Path path;//переменная пути к файлу
    private BufferedWriter writer;//с помощью этого объекта будем записываеть в файл


    public void printChocolateThisHighExpertRatingThisHighAndLowCacao(ArrayList<Chocolate> chocolateList){
        //Вывести на консоль информацию о лучшей марке шоколада с самым высоким и самым низким содержанием какао

        List<Chocolate> chocolatesThisHighRating = chocolateList //коллекция шоколада с высоким рейтингом
                .stream().filter(a -> a.getExpertRating() == chocolateList
                .stream().max((chocolate1, chocolate2) -> Float.compare(chocolate1.getExpertRating(), chocolate2.getExpertRating()))
                .get().getExpertRating()).collect(toList());//добавляем в лист список элементов с максимальным рейтингом

        List<Chocolate> result = new ArrayList<>();

        result.addAll(chocolatesThisHighRating
                .stream().filter(a -> a.getContentCacaoInPercent() == chocolatesThisHighRating
                .stream().max((chocolate1, chocolate2) -> Float.compare(chocolate1.getContentCacaoInPercent(), chocolate2.getContentCacaoInPercent()))
                .get().getContentCacaoInPercent()).collect(toList()));//тут фильтруем по максимальному количеству какао и выводим


        result.addAll(chocolatesThisHighRating
                .stream().filter(a -> a.getContentCacaoInPercent() == chocolatesThisHighRating
                .stream().min((chocolate1, chocolate2) -> Float.compare(chocolate1.getContentCacaoInPercent(), chocolate2.getContentCacaoInPercent()))
                .get().getContentCacaoInPercent()).collect(toList()));//тут фильтруем по минимальному количеству какао и выводим


        System.out.println("Результат решения задания №1, Вывести на консоль информацию о лучшей марке шоколада с " +
                           "самым высоким и самым низким содержанием какао: название, содержание какао, страна-экспортер " +
                           "какао-бобов, страна выпуска:");

        for(Chocolate chocolate : result){
            System.out.println("Название шоколада: " + chocolate.getBrandChocolate() +
                               "; Содержание какао: " + chocolate.getContentCacaoInPercent() +
                               "; Страна-экспортер какао-бобов: " + chocolate.getExportCountry() +
                               "; Страна выпуска: " + chocolate.getProduceCountry() );
        }

    }

    public void outputInFileInformationAboutCountAndListBrandChocolateThisExpertRatingAbove3(ArrayList<Chocolate> chocolateList, int contentCacaoInPercent, int expertiseYear){
    //В текстовый файл вывести количество и список марок шоколада с заданными пользователем содержанием какао и годом, которые получили оценку не ниже 3.

        List<Chocolate> chocolatesForOutput = chocolateList
                .stream().filter(chocolate -> chocolate.getExpertRating() >= 3.0) //получаем весь шоколад, с оценкой больше 3
                .filter(chocolate -> chocolate.getContentCacaoInPercent() == contentCacaoInPercent && chocolate.getExpertiseYear() == expertiseYear) //фильтруем по году и по процентам какао
                .collect(toList());//результат преобразуем в тип лист

        path = Paths.get("src\\resources\\newData.txt"); //путь к файлу

        try {
            writer = Files.newBufferedWriter(path, //объект для записи фалйа
                                          StandardOpenOption.CREATE, //если файла нет -> создаем
                                          StandardOpenOption.TRUNCATE_EXISTING); //если файла есть -> перезаписываем

            writer.write("Количество шоколада в файле = " + chocolatesForOutput.size() +  "; \n");
            writer.write("----------------------------\n");
            chocolatesForOutput.forEach(chocolate -> { //проходимся по коллекции и пишем в файл нужную информацию
                try {
                    writer.write("Марка = "+ chocolate.getBrandChocolate() +
                                     "; Содержание какао = " + chocolate.getContentCacaoInPercent() +
                                     "; Год экспертизы = " + chocolate.getExpertiseYear() +
                                     "; Экспертная оценка = " + chocolate.getExpertRating() + "\n");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            writer.close();//закрываем поток для записи

        } catch (IOException e) {
            e.printStackTrace();
        }

    }




    public void outputInFileListThisProduceCountrySortByExpertRating(ArrayList<Chocolate> chocolateList){
    //В текстовый файл вывести список стран-производителей, отсортированных по возрастанию средней экспертной оценки выпускаемого
    // шоколада и для каждой страны определить лучшую страну экспортёра какао-бобов.


        path = Paths.get("src\\resources\\newData1.txt");

        try {
            writer = Files.newBufferedWriter(path,
                                          StandardOpenOption.CREATE,
                                          StandardOpenOption.TRUNCATE_EXISTING);


            Map<String, DoubleSummaryStatistics> produceCountryRating = chocolateList
                    .stream().collect(Collectors.groupingBy(chocolate -> chocolate.getProduceCountry(),//осуществляем группировку по стране производителей
                                      Collectors.summarizingDouble(chocolate -> chocolate.getExpertRating())));//суммируем экспертную оценку

            List<Map.Entry<String, DoubleSummaryStatistics>> sortedProduceCountryByAverageExpertRating = produceCountryRating.entrySet()
                    .stream().sorted(Comparator.comparingDouble(country -> country.getValue().getAverage())).collect(toList());//для каждой страны производителя считаем среднюю оценку и сортируем по ее значению

            writer.write("Список стран производителей, отсортированных по возрастанию средней экспертной оценки выпускаемого шоколада:" + " \n");
            writer.write("------------------------" + " \n");

            for(Map.Entry<String, DoubleSummaryStatistics> item : sortedProduceCountryByAverageExpertRating){ //цикл по реузльтатам для вывода
                writer.write(item.getKey() + " - " + item.getValue().getAverage() + "; \n");
            }

            writer.write("------------------------" + " \n");
            writer.write("Для каждой страны определить лучшую страну экспортёра какао-бобов:" + " \n");
            writer.write("------------------------" + " \n");

            chocolateList.stream().collect(Collectors.groupingBy(Chocolate::getProduceCountry)).entrySet().forEach(action -> {  //осуществляем группировку по стране производителей,
                // после чего создаем цикл по полученному списку
                try {
                    writer.write("Для страны производителя: " + action.getKey() + ", лучшей страной экспортёром какао-бобов является: " +
                                                                    action.getValue().stream()
                                                                    .collect(Collectors.groupingBy(Chocolate::getExportCountry,//группировка по стране-экспортеру
                                                                             Collectors.counting())).entrySet().stream()//считаем количетсов раз, сколько встретилась страна экспортер
                                                                    .max(Comparator.comparing((x -> x.getValue().floatValue()))).get().getKey() + "; \n");//сравниваем все страны и берем значение максимальное
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });

            writer.close();//закрываем поток записи

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
