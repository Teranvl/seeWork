package dataBase;

import mainPackage.Main;
import mainPackage.ParserRTF;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import protocol.TemperatureSensors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReadDataBase {

    private XSSFSheet stendList;
    private ParserRTF parserRTF;
    private Main main;

    //в массивах ниже храняться значения только для одного обрабатываемого протокола Recorder
    private List<String> identifiersFromDataBase; //идентификаторы из базы данных
    private List<String> measuringInstrument; //средство измерений
    private List<String> stendNumber; // номер установки(5 строка первой таблицы)
    private List<String> nameMeasuringChannel; //наименование измерительных каналов
    private List<String> typeFirstConverter; // тип первичного преобразователя
    private List<String> typeSecondaryConverter; // тип вторичного преобразователя
    private List<String> workStandard; //рабочий эталон
    private List<String> methodOfRationing; // способ нормирования МХ
    private List<Double> minOfRange; // значение минимального диапазона
    private List<Double> maxOfRange; // значение максимального диапазона
    private List<String> units; //единицы измерения
    private List<String> error; // допустимая погрешность
    private List<String> errorHalfInnerLimit; // допустимая погрешность с 1/2
    private List<Double> errorValue; // значение допустимой погрешности
    private List<Double> errorHalfInnerLimitValue; // значение допустимой погрешности с 1/2
    private List<Boolean> HalfInnerLimit; // наличие 1/2 диапазона
    private List<String> protocolNumber; // номер протокола
    private List<Double> valueY; //значение Y
    private List<Integer> numbersProtocolInRecorderProtocolMass; //массив с номерами протоколов в протоколе рекордера
    private int numberProtocolInRecorderProtocol;//номер протокола в протоколе рекордера, по порядку

    public ReadDataBase(XSSFSheet stendList, ParserRTF parserRTF, Main main){
        this.stendList = stendList;
        this.parserRTF = parserRTF;
        this.main = main;
        initialization();
    }

    private void initialization(){
        identifiersFromDataBase = new ArrayList<>();
        measuringInstrument = new ArrayList<>();
        stendNumber= new ArrayList<>();
        nameMeasuringChannel = new ArrayList<>();
        typeFirstConverter = new ArrayList<>();
        typeSecondaryConverter = new ArrayList<>();
        workStandard = new ArrayList<>();
        methodOfRationing = new ArrayList<>();
        minOfRange= new ArrayList<>();
        maxOfRange= new ArrayList<>();
        units = new ArrayList<>();
        error = new ArrayList<>();
        errorHalfInnerLimit = new ArrayList<>();
        errorValue = new ArrayList<>();
        errorHalfInnerLimitValue = new ArrayList<>();
        HalfInnerLimit = new ArrayList<>();
        protocolNumber = new ArrayList<>();
        valueY = new ArrayList<>();
        numbersProtocolInRecorderProtocolMass = new ArrayList<>();
    }


    private void addToLists(XSSFRow row){

        row.getCell(17).setCellType(CellType.STRING); //приводим ячейки с номером установке к строке
        row.getCell(14).setCellType(CellType.STRING); //приводим ячейки с номером протокола к строке

        //добавляем инфу в листы
        identifiersFromDataBase.add(row.getCell(0).getStringCellValue());
        nameMeasuringChannel.add(row.getCell(1).getStringCellValue());

        if(row.getCell(2).getCellType().equals(CellType.NUMERIC)){
            minOfRange.add(row.getCell(2).getNumericCellValue());
        }else{
            System.out.println("Ошибка при чтении базы данных (некорректное заполнение строк) в параметре: " + row.getCell(0) + " в столбце 2, значение : " + row.getCell(2));
        }

        if(row.getCell(3).getCellType().equals(CellType.NUMERIC)){
            maxOfRange.add(row.getCell(3).getNumericCellValue());
        }else{
            System.out.println("Ошибка при чтении базы данных (некорректное заполнение строк) в параметре: " + row.getCell(0) + " в столбце 3, значение : " + row.getCell(3));
        }

        units.add(row.getCell(4).getStringCellValue());
        typeFirstConverter.add(row.getCell(5).getStringCellValue());
        methodOfRationing.add(row.getCell(6).getStringCellValue());
        error.add(row.getCell(7).getStringCellValue());

        if(row.getCell(8).getCellType().equals(CellType.NUMERIC)){
            errorValue.add(row.getCell(8).getNumericCellValue());
        }else{
            System.out.println("Ошибка при чтении базы данных (некорректное заполнение строк) в параметре: " + row.getCell(0) + " в столбце 8, значение : " + row.getCell(8));
        }


        errorHalfInnerLimit.add(row.getCell(9).getStringCellValue());

        if(row.getCell(10).getCellType().equals(CellType.NUMERIC)){
            errorHalfInnerLimitValue.add(row.getCell(10).getNumericCellValue());
        }else{
            if(row.getCell(10).getStringCellValue().equals("no")){
                errorHalfInnerLimitValue.add(-1.0);//если а базе данных нет значения столбца, записываем -1.0
            }else{
                System.out.println("Ошибка при чтении базы данных (некорректное заполнение строк) в параметре: " + row.getCell(0) + " в столбце 10, значение :" + row.getCell(10));
            }
        }


        workStandard.add(row.getCell(11).getStringCellValue());
        typeSecondaryConverter.add(row.getCell(12).getStringCellValue());

        if(row.getCell(13).getStringCellValue().equals("yes")){
            HalfInnerLimit.add(true);
        }else{
            HalfInnerLimit.add(false);
        }


        protocolNumber.add(row.getCell(14).getStringCellValue());


        if(row.getCell(15).getCellType().equals(CellType.NUMERIC)){
            valueY.add(row.getCell(15).getNumericCellValue());
        }else{
            if(row.getCell(15).getStringCellValue().equals("no")){
                valueY.add(-10.0);//если а базе данных нет значения столбца, записываем -10.0
            }else{
                System.out.println("Ошибка при чтении базы данных (некорректное заполнение строк) в параметре: " + row.getCell(0) + " в столбце 15, значение :" + row.getCell(15));
            }
        }

        measuringInstrument.add(row.getCell(16).getStringCellValue());
        stendNumber.add(row.getCell(17).getStringCellValue());

        numbersProtocolInRecorderProtocolMass.add(numberProtocolInRecorderProtocol);

    }


    public void read(ArrayList<String> identifiers, String thePathToTheFile){ //на входе лист с идентификаторами

        boolean isMic140ProtocolXA = false;
        boolean isMic140ProtocolXK = false;
        boolean identifierIsFound;
        Iterator<Row> rowIterator;
        numberProtocolInRecorderProtocol = 0;


        if(thePathToTheFile.contains("_ХК.rtf")){
            isMic140ProtocolXK = true;
        }
        if(thePathToTheFile.contains("_ХА.rtf")){
            isMic140ProtocolXA = true;
        }

        for(String identifier : identifiers){ //проходим по всем идентификаторам

            rowIterator = stendList.rowIterator(); //обновляем итератор
            identifierIsFound = false; //флаг успешного поиска в БД

            while(rowIterator.hasNext()){ //итератор по строкам БД

                XSSFRow row = (XSSFRow) rowIterator.next();

                if(isMic140ProtocolXA || isMic140ProtocolXK){ //если протокол ШКТИ, то надо проверять еще тип термопары

                    if(isMic140ProtocolXA){
                        if(row.getCell(0).getStringCellValue().equals(identifier) &&
                           row.getCell(5).getStringCellValue().equals(TemperatureSensors.TXA.getStringSensor()) &&
                           row.getCell(3).getNumericCellValue() == parserRTF.getMaxOnRange() &&
                           row.getCell(2).getNumericCellValue() == parserRTF.getMinOnRange()){

                            identifierIsFound = true;
                            addToLists(row);
                            break; //если совпадение, прерываем цикл
                        }
                    }

                    if(isMic140ProtocolXK){
                        if(row.getCell(0).getStringCellValue().equals(identifier) &&
                           row.getCell(5).getStringCellValue().equals(TemperatureSensors.TXK.getStringSensor()) &&
                           row.getCell(3).getNumericCellValue() == parserRTF.getMaxOnRange() &&
                           row.getCell(2).getNumericCellValue() == parserRTF.getMinOnRange()){

                           // System.out.println("Протокол _ 3: " + row.getCell(0).getStringCellValue() + " " + row.getCell(5).getStringCellValue());
                            identifierIsFound = true;
                            addToLists(row);
                            break; //если совпадение, прерываем цикл
                        }
                    }

                }else{ //если протокол не ШКТИ
                    if(row.getCell(0).getStringCellValue().equals(identifier)){
                       // System.out.println("Протокол _ 4 " +  row.getCell(0).getStringCellValue());
                        identifierIsFound = true;
                        addToLists(row);
                        break; //если совпадение, прерываем цикл

                    }
                }


            }
            if(!identifierIsFound){
                System.out.println("Внимание! " + identifier + " не найден в базе данных!");
            }
            numberProtocolInRecorderProtocol++;

        }

        if(identifiersFromDataBase.size() == 0){
            System.out.println("Ни один протокол не был сформирован!");
            main.setBadProtocol(true);
        }

      // printLists();
    }




    private void printLists(){

        for(int i = 0; i < identifiersFromDataBase.size(); i++){

            System.out.println(identifiersFromDataBase.get(i));
            System.out.println(measuringInstrument.get(i));
            System.out.println(stendNumber.get(i));
            System.out.println(nameMeasuringChannel.get(i));
            System.out.println(typeFirstConverter.get(i));
            System.out.println(typeSecondaryConverter.get(i));
            System.out.println(workStandard.get(i));
            System.out.println(methodOfRationing.get(i));
            System.out.println(minOfRange.get(i));
            System.out.println(maxOfRange.get(i));
            System.out.println(units.get(i));
            System.out.println(error.get(i));
            System.out.println(errorHalfInnerLimit.get(i));
            System.out.println(errorValue.get(i));
            System.out.println(errorHalfInnerLimitValue.get(i));
            System.out.println(HalfInnerLimit.get(i));
            System.out.println(protocolNumber.get(i));
            System.out.println(valueY.get(i));
            System.out.println("----------");

        }



    }

    public List<String> getIdentifiersFromDataBase() {
        return identifiersFromDataBase;
    }

    public List<String> getMeasuringInstrument() {
        return measuringInstrument;
    }

    public List<String> getStendNumber() {
        return stendNumber;
    }

    public List<String> getNameMeasuringChannel() {
        return nameMeasuringChannel;
    }

    public List<String> getTypeFirstConverter() {
        return typeFirstConverter;
    }

    public List<String> getTypeSecondaryConverter() {
        return typeSecondaryConverter;
    }

    public List<String> getWorkStandard() {
        return workStandard;
    }

    public List<String> getMethodOfRationing() {
        return methodOfRationing;
    }

    public List<Double> getMinOfRange() {
        return minOfRange;
    }

    public List<Double> getMaxOfRange() {
        return maxOfRange;
    }

    public List<String> getUnits() {
        return units;
    }

    public List<String> getError() {
        return error;
    }

    public List<String> getErrorHalfInnerLimit() {
        return errorHalfInnerLimit;
    }

    public List<Double> getErrorValue() {
        return errorValue;
    }

    public List<Double> getErrorHalfInnerLimitValue() {
        return errorHalfInnerLimitValue;
    }

    public List<Boolean> getHalfInnerLimit() {
        return HalfInnerLimit;
    }

    public List<String> getProtocolNumber() {
        return protocolNumber;
    }

    public List<Double> getValueY() {
        return valueY;
    }

    public List<Integer> getNumbersProtocolInRecorderProtocolMass() {
        return numbersProtocolInRecorderProtocolMass;
    }
}
