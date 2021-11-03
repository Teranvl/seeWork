package mainPackage;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ParserRTF {

    private FileInputStream fileInputStream;
    private BufferedReader bufferedReader;

    private String tempLine; //временная переменная для хранения прочитанных строк
    private String secondaryConverter = "";
    private String createProtocolUserName = "";
    private String thePathToTheFile = "";

    private int QuantityControlPoints; // количество контрольных точек
    private int countChannels = 0; //счетчик количества каналов
    private int countDownMod = 0; // обратный ход : 1 - есть,  0 - нет
    private int z = 1; //???????????
    private int temp = 0;
    private int countColumns = 0; // количество спарсеных столбцов

    private double minOnRange; // минимальное значение диапазона
    private double maxOnRange; // максимальное заначение диапазона

    private boolean columnCountComplete = false; // идникация завершения подсчета количества столбцов в исходной таблице
    private boolean directStrokeInTable = false; // наличие столбца прямого хода в таблице
    private boolean reverseMoveInTable = false; // наличие столбца обратного хода в таблице
    private boolean deInTable = false; // наличие стлбца De в таблице

    private ArrayList<String> descriptionChannel; //описание каналов
    private ArrayList<String> channelInfo;  // информация о каналах
    private ArrayList<String> protocolInformation; // содержится начальная информация протокола, дава, диапазон, наименование эталона и т.д.
    private ArrayList<String> maximumErrorAllOverTheRange; // погрешность (максимальная) на всем диапазоне
    private ArrayList<String> reducedError; // приведенная погрешность
    private ArrayList<String> massThisSecondaryConverter; // массив, в который заносится информация о вторичных преобразователях из протокола, если преобразователь в протоколе не один
    private ArrayList<ArrayList<Double>> valuesTable; // хранение значений из таблиц
    private ArrayList<String> identifiersListFromRTF; // идентификаторы (названния параметров) из протокола рекордера

    public ParserRTF(String thePathToTheFile){
        this.thePathToTheFile = thePathToTheFile;

        protocolInformation = new ArrayList<>();
        massThisSecondaryConverter = new ArrayList<>();
        descriptionChannel = new ArrayList<>();
        channelInfo = new ArrayList<>();
        valuesTable = new ArrayList<>();
        maximumErrorAllOverTheRange = new ArrayList<>();
        reducedError = new ArrayList<>();
        identifiersListFromRTF = new ArrayList<>();

        valuesTable.add(new ArrayList<>());//добавление первой строки
    }

    public void parsing() {
        try {
            fileInputStream = new FileInputStream(thePathToTheFile);
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream, "windows-1251"));

            while((tempLine = bufferedReader.readLine()) != null){
                parseInfo();
                parseQuantityControlPoint();
                parseModules();
                parseDescriptionChannel();
                skipTextInRTF();
                parseValuesTable();
            }

            valuesTable.remove(valuesTable.size()-1);

            for(int i =0; i < valuesTable.size(); i++){
                if( (i + 1) % QuantityControlPoints == 0){
                    countChannels++;
                }
            }

            minOnRange = Math.min(valuesTable.get(0).get(1), valuesTable.get(getQuantityControlPoints() - 1).get(1));
            maxOnRange = Math.max(valuesTable.get(0).get(1), valuesTable.get(getQuantityControlPoints() - 1).get(1));

            //добавление в список идентификаторов из считанного RTF протокола
            for(int i = 0; i < countChannels; i++){
                identifiersListFromRTF.add(channelInfo.get(i).split(",")[0]);
            }


            //printAllInformationFromParsingProtocol();
           // System.out.println(reverseMoveInTable +  " " + directStrokeInTable  + " " + deInTable );



        } catch (IOException e) {
            System.out.println("Ошибка открытия файла");
        }


    }

    private void parseInfo(){

        if(tempLine.contains("Испытание провел(а)")){
            createProtocolUserName = tempLine.split("Испытание провел\\(а\\) ")[1].split("_____________\\\\par")[0];
        }

        if(tempLine.contains("проверки измерительных каналов модуля MC-")){
            secondaryConverter = tempLine.split("проверки измерительных каналов модуля ")[1];
            secondaryConverter = secondaryConverter.split(".\\\\")[0];
        }

        if ((tempLine.contains("Дата")) ||
            (tempLine.contains("Диапазон проверки:")) ||
            (tempLine.contains("Количество циклов:")) ||
            (tempLine.contains("Количество порций:")) ||
            (tempLine.contains("Размер порции:")) ||
            (tempLine.contains("Обратный ход:")) ||
            (tempLine.contains("Наименование эталона:")) ||
            (tempLine.contains("Температура окружающей среды:"))) {

            if (!tempLine.contains("}{\\footer")) {
            tempLine = tempLine.split("\\\\")[0]; //поиск в строке символа \ и разделение по этому символу
            protocolInformation.add(tempLine);
            }
        }

    }

    private void parseQuantityControlPoint() throws IOException { //метод подсчета количество точек измерений

        if (tempLine.contains("Список контрольных точек")) {
            while (true) {
                tempLine = bufferedReader.readLine();

                if (tempLine.contains("Точка №\\cell }{")) {
                    tempLine = bufferedReader.readLine();

                    while (!tempLine.contains("Значение\\cell }{\\qc")) {
                        tempLine = bufferedReader.readLine();
                        QuantityControlPoints++;
                    }
                }
                if (tempLine.contains("Модули") || tempLine.contains("Каналы")) {
                    break;
                }
            }
        }
    }

    private void parseModules() throws IOException {
        String tempModules = "";

        if (tempLine.contains("Модули:\\par\\pard{\\")) {

            while (!tempLine.contains("Каналы:\\par\\pard{")) {

                if (tempLine.contains("MC")) {
                    tempModules = tempLine.split("\\\\")[0];
                    tempLine = bufferedReader.readLine();
                    tempModules += " № " + tempLine.split("\\\\")[0];
                    massThisSecondaryConverter.add(tempModules);
                }

                tempLine = bufferedReader.readLine();
            }
        }
    }

    private void parseDescriptionChannel() throws IOException { //описание и названия каналов

        if (tempLine.contains("Каналы")) {

            while (!tempLine.contains("Сводная таблица.")) {

                if (tempLine.contains(Integer.toString(z) + "\\cell }{")) {

                    channelInfo.add(bufferedReader.readLine().split("\\\\cell")[0]);
                    tempLine = bufferedReader.readLine();

                    if (!tempLine.equals("\\cell }{")) {
                        descriptionChannel.add(tempLine.split("\\\\")[0]);
                    }
                    z++;
                }
                tempLine = bufferedReader.readLine();
            }
        }
    }

    private void skipTextInRTF() throws IOException { //пропуск лишнего текста
        if (tempLine.contains("Сводная таблица.")) {
            while (!tempLine.contains("Канал")) {
                tempLine = bufferedReader.readLine();
            }
        }
    }

    private void parseValuesTable() throws IOException { //заполнение таблицы значений

        String textInColumns  = "";
        if(tempLine.contains("№\\cell }{") && !columnCountComplete) { //подсччет количества столбцов: необходимо подсчитать только один раз поэтому есть индикатор

            while (!tempLine.contains("\\cell }\\row}{\\trowd\\")) {
                textInColumns = textInColumns + tempLine;
                countColumns++;
                tempLine = bufferedReader.readLine();
            }

            textInColumns = textInColumns + tempLine;
            columnCountComplete = true;
            countColumns++;// последний столбец входит в contains поэтому досчитываем его тут
           // System.out.println("Считанное количество стобцов: "  + countColumns);

            if(textInColumns.contains("Прямой ход")) {
                directStrokeInTable = true;
            }

            if(textInColumns.contains("Обратный ход")) {
                reverseMoveInTable = true;
            }

            if(textInColumns.contains("De")) {
                deInTable = true;
            }
        }

        int coefficientCountDownMove  = countColumns;

        if(tempLine.equals("1\\cell }{\\qr")){

            if(protocolInformation.get(5).contains("есть")) {
                //coefficientCountDownMove += 1;
                countDownMod = 1;
            }

            while(!(tempLine.contains("\\cell }\\row}\\par\\pard"))){


                valuesTable.get(temp).add(Double.parseDouble(tempLine.split("\\\\")[0]));
                tempLine = bufferedReader.readLine();

                if(tempLine.contains("Погрешность (максимальная) на всем диапазоне:")){
                    valuesTable.add(new ArrayList<>());
                    temp++;
                    break;

                }

                if(valuesTable.get(temp).size() >= coefficientCountDownMove){ // 6 - без обратного хода, 7 - c обратным ходом
                    valuesTable.add(new ArrayList<>());
                    temp++;
                }

            }
            maximumErrorAllOverTheRange.add(tempLine.split("\\\\")[0]);
            reducedError.add(bufferedReader.readLine().split("\\\\")[0]);
        }

    }

    private void printAllInformationFromParsingProtocol(){

        for(String s : protocolInformation){
            System.out.println(s);
        }

        System.out.println("------------------------------");

        System.out.println("Считанное количество контрольных точек = " + QuantityControlPoints);

        System.out.println("------------------------------");
        System.out.println("Считанные модули:");

        for(int i = 0; i < massThisSecondaryConverter.size(); i++){
            System.out.println(massThisSecondaryConverter.get(i));
        }

        System.out.println("------------------------------");

        for(String s : channelInfo){
            System.out.println(s);
        }

        System.out.println("------------------------------");

        for(String s : descriptionChannel){
            System.out.println(s);
        }

        System.out.println("------------------------------");

        for(int i =0; i < valuesTable.size(); i++){
            System.out.println(valuesTable.get(i));

            if( (i + 1) % QuantityControlPoints == 0){
               // countChannels++;
                System.out.println("-----------//-------------------");
            }
        }

        System.out.println("------------------------------");

        for(String s : maximumErrorAllOverTheRange){
            System.out.println(s);
        }

        System.out.println("------------------------------");

        for(String s : reducedError) {
            System.out.println(s);
        }

        System.out.println("------------------------------");
        System.out.println("Считанное количество каналов в файле: "  + countChannels);
        System.out.println("------------------------------");

        System.out.println("Минимальное и максимальное значения в протоколе : min = " + minOnRange + "; max = "  + maxOnRange);


    }

    public int getCountChannels() {
        return countChannels;
    }

    public ArrayList<String> getDescriptionChannel() {
        return descriptionChannel;
    }

    public ArrayList<ArrayList<Double>> getValuesTable() {
        return valuesTable;
    }

    public ArrayList<String> getProtocolInformation() {
        return protocolInformation;
    }

    public ArrayList<String> getChannelInfo() {
        return channelInfo;
    }

    public int getQuantityControlPoints() {
        return QuantityControlPoints;
    }

    public int getCountDownMod() {
        return countDownMod;
    }

    public String getSecondaryConverter() {
        return secondaryConverter;
    }

    public double getMinOnRange() {
        return minOnRange;
    }

    public double getMaxOnRange() {
        return maxOnRange;
    }

    public boolean getDirectStrokeInTable() {
        return directStrokeInTable;
    }

    public boolean getReverseMoveInTable() {
        return reverseMoveInTable;
    }

    public boolean getDeInTable() {
        return deInTable;
    }

    public ArrayList<String> getMassThisSecondaryConverter() {
        return massThisSecondaryConverter;
    }

    public String getCreateProtocolUserName() {
        return createProtocolUserName;
    }

    public double getXdiap(){ //используется только для печати формулы в протоколе
        return Math.abs(getMaxOnRange() - getMinOnRange());
    }

    public ArrayList<String> getIdentifiersListFromRTF() {
        return identifiersListFromRTF;
    }
}
