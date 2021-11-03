package protocol;

import calculation.Calculation;
import dataBase.ReadDataBase;
import mainPackage.ParserRTF;
import printProtocol.CreateRTF;
import printProtocol.DecryptionAbbreviation;
import java.awt.*;
import java.util.ArrayList;

public abstract class Protocol {

    private double MaxDmcym;
    private double MaxDrcym;
    private double MaxDecym;
    private double MaxDrcym12VP;
    private double MaxDecym12VP;
    private double MaxDrcym12VPCompleteMethod;
    private double MaxDecym12VPCompleteMethod;
    private double MaxDecymCompleteMethod;
    private double MaxDm;// используется при комплектном методе без 1/2

    //для сериализации:
    private double actualErrorValue; // рассчитанное значение допустимой погрешности
    private double actualErrorHalfInnerLimitValue; // рассчитанное значение допустимой погрешности с 1/2

    private String thePathToSaveForGroupProtocols; //путь к группе протоколов
    private Color backgroundColor = Color.green; //цвет годности протокола
    private String resultOfProtocol = "ГОДЕН"; // вывод о годности протокола
    private String protocolNumber; //номер протокола
    private String dateAndTime; //дата и время
    private String certificate; // номер сертификата
    protected String unitsInThisProtocol; //единицы измерения для протокола
    protected String formula; //формула в протоколе
    protected String formulaThisValues; //формула со значениями
    protected String decryptionReductionsInFormula; //расшифровка формулы
    private String decryptionProtocolPointsTable; //расшифровка столбцов в расчетной таблице
    protected String firstError; // погрешность первая
    protected String secondError;// погрешность вторая
    private ArrayList<DecryptionAbbreviation> nameFields; //массив значений, для шапки расчетной таблицы
    private ArrayList<String> protocolInformationLinesName; //названия строк таблицы с информацией
    private ArrayList<String> protocolInformationTable; //таблица информации протокола - значения строк
    private ArrayList<String> unitsInColumnsNameFields; //единицы измерения в первой строке расчетной таблицы
    protected ArrayList<ArrayList<Double>> protocolPointsTable; //рассчетная таблица
    private double realValueHalfInnerLimit; // значения 1/2 для каналов в протоколе
    protected int protocolScopeNumber;//номер текущего обрабатываемого протокола
    protected Calculation calculation;
    protected CreateRTF createRTF;

    protected ReadDataBase readDataBase;
    protected ParserRTF parserRTF;

    public Protocol(ReadDataBase readDataBase, ParserRTF parserRTF, int protocolScopeNumber){
        this.parserRTF = parserRTF;
        this.readDataBase = readDataBase;
        this.protocolScopeNumber = protocolScopeNumber;
    }

    public void initialize(){

        protocolInformationLinesName = new ArrayList<>();
        protocolInformationTable = new ArrayList<>();
        nameFields = new ArrayList<>();
        protocolPointsTable = new ArrayList<>();
        unitsInColumnsNameFields = new ArrayList<>();

        calculateValueHalfInnerLimit(); //рассчет 1\2 если надо
        setUnitsInThisProtocol(); //установка единиц измерения
        setDateAndTime(parserRTF.getProtocolInformation().get(0));//установка значения времени
        setProtocolNumber(); //установка номера протокола
        setProtocolInformationTable(); //добавление значений в информационную таблиуц протокола(которая сверху)
        setNameFields(); //добавление названия столбцов в таблицу расчета
        decryptionProtocolPointsTable = setDecryptionProtocolPointsTable(); //заполнения строки расшифровки значений в шапке таблицы расчета

        calculation = new Calculation(this);

    }

    private void setUnitsInThisProtocol(){ // установка единиц измерения для протоколов из одного файла
        try {
            unitsInThisProtocol = readDataBase.getUnits().get(0); //берем первое значение, т.к. в рамках одного протокола единицы измерения одинаковые

        }catch (IndexOutOfBoundsException indexOutOfBoundsException){
            System.out.println("ошибка в методе setUnitsInThisProtocol");
        }


    }

    private void setProtocolInformationLinesName(){
        // установка стандартных названий в информационной таблице(которая сверху)

        protocolInformationLinesName.add(" Средство измерений");
        protocolInformationLinesName.add(" Место размещения");
        protocolInformationLinesName.add("\tЦех");
        protocolInformationLinesName.add("\tСтанция");
        protocolInformationLinesName.add("\tУстановка");
        protocolInformationLinesName.add(" Наименование измерительного канала");
        protocolInformationLinesName.add(" Обозначение");
        protocolInformationLinesName.add(" Тип первичного преобразователя");
        protocolInformationLinesName.add(" Тип вторичного преобразователя");
        protocolInformationLinesName.add(" Рабочий эталон");
        protocolInformationLinesName.add(" Способ нормирования МХ");
        protocolInformationLinesName.add(" Диапазон измерения");
        protocolInformationLinesName.add(" Условия калибровки");
        protocolInformationLinesName.add("\tТемпература воздуха");
        protocolInformationLinesName.add("\tАтмосферное давление");
        protocolInformationLinesName.add("\tВлажность воздуха");
        protocolInformationLinesName.add(" Допустимая погрешность ");

    }

    private void setProtocolInformationTable(){ //заполнение верхней таблицы протокола

        setProtocolInformationLinesName();//установка названий строк для тадлицы с информацией

        protocolInformationTable.add(" " + readDataBase.getMeasuringInstrument().get(protocolScopeNumber)); //средство измерения
        protocolInformationTable.add(" "); //Место размещение - пустое поле
        protocolInformationTable.add("\t21");//цех 21
        protocolInformationTable.add("\t1");// станция 1
        protocolInformationTable.add("\t" + readDataBase.getStendNumber().get(protocolScopeNumber)); //установка
        protocolInformationTable.add(" " + readDataBase.getNameMeasuringChannel().get(protocolScopeNumber)); //Наименование измерительного канала
        protocolInformationTable.add(" " + readDataBase.getIdentifiersFromDataBase().get(protocolScopeNumber)); //Обозначение
        protocolInformationTable.add(" " + readDataBase.getTypeFirstConverter().get(protocolScopeNumber)); //Тип первичного преобразователя
        setSecondaryConverter(protocolScopeNumber); //Тип вторичного преобразователя
        protocolInformationTable.add(" " + readDataBase.getWorkStandard().get(protocolScopeNumber)); //Рабочий эталон
        protocolInformationTable.add(" " + readDataBase.getMethodOfRationing().get(protocolScopeNumber)); //Способ нормирования МХ
        setRange(protocolScopeNumber); //Диапазон измерения
        protocolInformationTable.add("");//Условия калибровки - пустое
        setTemperaturePressureHumidity(); //Температура воздуха //Атмосферное давление //Влажность воздуха
        setErrors(protocolScopeNumber); //Допустимая погрешность

    }

    private void setSecondaryConverter(int scopeNumber){//!!!!!!!!!!!!!!
        try {

            if (parserRTF.getMassThisSecondaryConverter().size() != 0) {//если обнаружена таблитца с модулями
                for (int k = 0; k < parserRTF.getMassThisSecondaryConverter().size(); k++) {
                    if (parserRTF.getChannelInfo().get(readDataBase.getNumbersProtocolInRecorderProtocolMass().get(scopeNumber)).contains(parserRTF.getMassThisSecondaryConverter().get(k).split(" № ")[1])) {
                        protocolInformationTable.add(" " + readDataBase.getTypeSecondaryConverter().get(scopeNumber) +
                                ", " + parserRTF.getMassThisSecondaryConverter().get(k) + "," +
                                parserRTF.getChannelInfo().get(readDataBase.getNumbersProtocolInRecorderProtocolMass().get(scopeNumber)).split(",")[1]);
                    }

                }
     /*           if (protocolInformationTable.size() == 0) {
                    protocolInformationTable.add(" " + readDataBase.getTypeSecondaryConverter().get(scopeNumber) + " " +
                            parserRTF.getSecondaryConverter() + "," +
                            parserRTF.getChannelInfo().get(scopeNumber).split(",")[1]);
                }*/
                //убрал, для теста. Непонятно что это тут делает

            }


            if (parserRTF.getMassThisSecondaryConverter().size() == 0) { //если в читаемом протоколе не обнаружена таблица с модулями

                if(protocolInformationTable.get(6).contains("NS9116") || protocolInformationTable.get(7).contains("NS9116")){ //если в обозначении или в типе первичного преобразователя NS9116, то вторичный преобразователь указывать не надо
                    protocolInformationTable.add(" --- ");

                }else{

                    if(parserRTF.getSecondaryConverter().isEmpty()){ //если вторичного преобразователя нет в читаемом протоколе, обычно это MIC-140
                        protocolInformationTable.add(" " + readDataBase.getTypeSecondaryConverter().get(scopeNumber) + "," +
                                parserRTF.getChannelInfo().get(readDataBase.getNumbersProtocolInRecorderProtocolMass().get(scopeNumber)).split(",")[1]);
                    }else {
                        protocolInformationTable.add(" " + readDataBase.getTypeSecondaryConverter().get(scopeNumber) + ", " +
                                parserRTF.getSecondaryConverter() + "," +
                                parserRTF.getChannelInfo().get(readDataBase.getNumbersProtocolInRecorderProtocolMass().get(scopeNumber)).split(",")[1]);
                    }

                }

            }


        }catch (IndexOutOfBoundsException exception){
            protocolInformationTable.add("Error");
            System.out.println("Ошибка добавления вторичного преобразователя");
        }



    }

    private void setRange(int i){


        if(readDataBase.getMinOfRange().get(i) < 0){
            protocolInformationTable.add(String.format(" минус %1.2f ÷ %1.2f" + " " + unitsInThisProtocol,
                    Math.abs(readDataBase.getMinOfRange().get(i)),
                    readDataBase.getMaxOfRange().get(i)));

        }else{
            protocolInformationTable.add(String.format(" %1.2f ÷ %1.2f" + " " + unitsInThisProtocol,
                    readDataBase.getMinOfRange().get(i),
                    readDataBase.getMaxOfRange().get(i)));
        }


    }

    private void setTemperaturePressureHumidity (){
        try { // проверка наличия значения температуры воздуха в исходном протоколе
            protocolInformationTable.add("\t" + parserRTF.getProtocolInformation().get(7).split(",")[0].split(": ")[1]);//Температура воздуха
        }catch (IndexOutOfBoundsException e){
            protocolInformationTable.add("\t" + "не задано");
        }

        protocolInformationTable.add("\t745 мм рт.ст.");//Атмосферное давление

        try {// проверка наличия значения влажности воздуха в исходном протоколе
            protocolInformationTable.add("\t" + parserRTF.getProtocolInformation().get(7).split(",")[1].split(": ")[1]);//Влажность воздуха
        }catch (IndexOutOfBoundsException e){
            protocolInformationTable.add("\t" + "не задано");
        }

    }

    private void setErrors(int i){

        if(readDataBase.getHalfInnerLimit().get(i)){ // для печати двух строк в первой таблице в графе погрешность

            String specificErrorVersion_2 = String.format(" ± %1.2f %% " + readDataBase.getError().get(i) +
                            " от %1.2f до %1.2f " + unitsInThisProtocol + "\n" + " ± %1.2f %% "  +
                            readDataBase.getErrorHalfInnerLimit().get(i) + " от %1.2f до %1.2f " + unitsInThisProtocol,
                    readDataBase.getErrorValue().get(i), readDataBase.getMinOfRange().get(i), realValueHalfInnerLimit, readDataBase.getErrorHalfInnerLimitValue().get(i), realValueHalfInnerLimit,
                    readDataBase.getMaxOfRange().get(i));

            protocolInformationTable.add(specificErrorVersion_2);//Допустимая погрешность
        }else{
            String error = String.format(" ± %1.2f ", readDataBase.getErrorValue().get(i));

            if(readDataBase.getError().get(i).equals("абсолютная")){ //если погрешность абсолютная, то надо вписывать единицы измеряемой величины
                protocolInformationTable.add(error + unitsInThisProtocol + " " + readDataBase.getError().get(i));//Допустимая погрешность
            }else{ //если не абсолютная, то проценты
                protocolInformationTable.add(error + "% " + readDataBase.getError().get(i));//Допустимая погрешность
            }
        }
    }

    private void setNameFields(){

        nameFields.add(DecryptionAbbreviation.Number);
        unitsInColumnsNameFields.add("");//в номере нет единиц измерения
        nameFields.add(DecryptionAbbreviation.Standard);
        unitsInColumnsNameFields.add(unitsInThisProtocol);
        nameFields.add(DecryptionAbbreviation.Measured);
        unitsInColumnsNameFields.add(unitsInThisProtocol);
        nameFields.add(DecryptionAbbreviation.S);
        unitsInColumnsNameFields.add(unitsInThisProtocol);
        nameFields.add(DecryptionAbbreviation.A);
        unitsInColumnsNameFields.add(unitsInThisProtocol);

        if(parserRTF.getCountDownMod() == 1){ // если есть обратный ход добавим еще один столбец
            nameFields.add(DecryptionAbbreviation.H);
            unitsInColumnsNameFields.add(unitsInThisProtocol);
        }

        nameFields.add(DecryptionAbbreviation.Dm);
        unitsInColumnsNameFields.add(unitsInThisProtocol);
        nameFields.add(DecryptionAbbreviation.Dr);
        unitsInColumnsNameFields.add("%");

        if(this instanceof PressureProtocol) {

            if (readDataBase.getMethodOfRationing().get(protocolScopeNumber).equals("комплектный")) { // если выбран комплектный метод

                if (readDataBase.getHalfInnerLimit().get(protocolScopeNumber)) { // если нужна 1/2ВП
                    nameFields.add(DecryptionAbbreviation.Drcym12VP);
                    unitsInColumnsNameFields.add("%");
                    nameFields.add(DecryptionAbbreviation.Decym12VP);
                    unitsInColumnsNameFields.add("%");
                }

            }

            if (readDataBase.getMethodOfRationing().get(protocolScopeNumber).equals("поэлементный")) {

                nameFields.add(DecryptionAbbreviation.Dmpp);
                unitsInColumnsNameFields.add(unitsInThisProtocol);
                nameFields.add(DecryptionAbbreviation.Dmcym);
                unitsInColumnsNameFields.add(unitsInThisProtocol);

                if (readDataBase.getHalfInnerLimit().get(protocolScopeNumber)) {// если нужна 1/2ВП
                    nameFields.add(DecryptionAbbreviation.Drcym12VP);
                    unitsInColumnsNameFields.add("%");
                    nameFields.add(DecryptionAbbreviation.Decym12VP);
                    unitsInColumnsNameFields.add("%");
                }

            }
        }

        if(this instanceof TemperatureProtocol){

            if(this instanceof TemperatureThermocoupleProtocol){
                nameFields.add(DecryptionAbbreviation.Dmpp);
                unitsInColumnsNameFields.add(unitsInThisProtocol);
                nameFields.add(DecryptionAbbreviation.Dmxc);
                unitsInColumnsNameFields.add(unitsInThisProtocol);
                nameFields.add(DecryptionAbbreviation.Dmcym);
                unitsInColumnsNameFields.add(unitsInThisProtocol);
            }else{
                nameFields.add(DecryptionAbbreviation.Dmpp);
                unitsInColumnsNameFields.add(unitsInThisProtocol);
                nameFields.add(DecryptionAbbreviation.Dmcym);
                unitsInColumnsNameFields.add(unitsInThisProtocol);
            }
        }

        if(this instanceof TemperatureProtocol_CHPT){
            nameFields.add(DecryptionAbbreviation.Drcym);
            unitsInColumnsNameFields.add("%");
        }else{
            nameFields.add(DecryptionAbbreviation.Decym);
            unitsInColumnsNameFields.add("%");
        }



    }

    private void calculateValueHalfInnerLimit(){
        //расчет 1/2

        if(readDataBase.getHalfInnerLimit().get(protocolScopeNumber)){
            double point = parserRTF.getQuantityControlPoints();
            int value; //

            if(point % 2 == 0){ //если четное количество точек
                point = point / 2;
                //System.out.println("точка " + (int)(point - 1));
                value = (int) point - 1;
            }else{ //нечетное количество точек
                point = point / 2;
                //System.out.println("!точка " + (int)(point));
                value = (int) point;
            }

            realValueHalfInnerLimit = parserRTF.getValuesTable().get(value).get(1);
        }

    }

    private String setDecryptionProtocolPointsTable(){

        StringBuilder decryption = new StringBuilder();// создание строки с расшифровкой

        for(int i = 3; i < nameFields.size(); i++){ //цикл от 4 столбца, т.к. первые три столбца это номер, стандарт, измерение
            decryption.append(nameFields.get(i).getDecryption()); //добавляем расшифровку

            if(i < nameFields.size() - 1){ //если не последний столбик
                decryption.append(", ");
            }
        }

        decryption.append("."); // в конце ставим точку

        return decryption.toString();
    }

    public void print(){
        createRTF = new CreateRTF(this);
        createRTF.print();
    }

    protected void checkingOneError(double error){ //определение годности протокола
        actualErrorValue = error;

        if(error > readDataBase.getErrorValue().get(protocolScopeNumber)){
            resultOfProtocol = "НЕ ГОДЕН";
            backgroundColor = Color.red;
        }

    }

    protected void checkingOneAndSecondErrors(double error, double secondError){ // определение годностии протокола

        actualErrorValue = error;
        actualErrorHalfInnerLimitValue = secondError;

        if(error > readDataBase.getErrorValue().get(protocolScopeNumber) || secondError > readDataBase.getErrorHalfInnerLimitValue().get(protocolScopeNumber)){
            resultOfProtocol = "НЕ ГОДЕН";
            backgroundColor = Color.red;
        }

    }



    public abstract void calculation();//расчет для протокола и добавление результатов в массив значений

    public abstract void setFormulaAndDecryptionAndError(); //строки формул, расшифровки, погрешности



    protected double getStandard(int i){
        return protocolPointsTable.get(i).get(1);
    }

    protected double getDm(int i){
        return protocolPointsTable.get(i).get(5 + parserRTF.getCountDownMod()); //5 - без обратного хода, 6 с обратным ходом
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public void setProtocolNumber() {
        this.protocolNumber = readDataBase.getProtocolNumber().get(protocolScopeNumber);
    }

    public ArrayList<ArrayList<Double>> getProtocolPointsTable() {
        return protocolPointsTable;
    }

    public ReadDataBase getReadDataBase() {
        return readDataBase;
    }

    public double getRealValueHalfInnerLimit() {
        return realValueHalfInnerLimit;
    }

    public double getMaxDmcym() {
        return MaxDmcym;
    }

    public double getMaxDecym() {
        return MaxDecym;
    }

    public double getMaxDrcym12VP() {
        return MaxDrcym12VP;
    }

    public double getMaxDecym12VP() {
        return MaxDecym12VP;
    }

    public double getMaxDrcym12VPCompleteMethod() {
        return MaxDrcym12VPCompleteMethod;
    }

    public double getMaxDecym12VPCompleteMethod() {
        return MaxDecym12VPCompleteMethod;
    }

    public double getMaxDecymCompleteMethod() {
        return MaxDecymCompleteMethod;
    }

    public double getMaxDm() {
        return MaxDm;
    }

    public void setMaxDmcym(double maxDmcym) {
        MaxDmcym = maxDmcym;
    }

    public void setMaxDecym(double maxDecym) {
        MaxDecym = maxDecym;
    }

    public void setMaxDrcym12VP(double maxDrcym12VP) {
        MaxDrcym12VP = maxDrcym12VP;
    }

    public void setMaxDecym12VP(double maxDecym12VP) {
        MaxDecym12VP = maxDecym12VP;
    }

    public void setMaxDrcym12VPCompleteMethod(double maxDrcym12VPCompleteMethod) {
        MaxDrcym12VPCompleteMethod = maxDrcym12VPCompleteMethod;
    }

    public void setMaxDecym12VPCompleteMethod(double maxDecym12VPCompleteMethod) {
        MaxDecym12VPCompleteMethod = maxDecym12VPCompleteMethod;
    }

    public void setMaxDecymCompleteMethod(double maxDecymCompleteMethod) {
        MaxDecymCompleteMethod = maxDecymCompleteMethod;
    }

    public void setMaxDm(double maxDm) {
        MaxDm = maxDm;
    }

    public int getProtocolScopeNumber() {
        return protocolScopeNumber;
    }

    public ParserRTF getParserRTF() {
        return parserRTF;
    }

    public double getMaxDrcym() {
        return MaxDrcym;
    }

    public void setMaxDrcym(double maxDrcym) {
        MaxDrcym = maxDrcym;
    }

    public void setThePathToSaveForGroupProtocols(String thePathToSaveForGroupProtocols) {
        this.thePathToSaveForGroupProtocols = thePathToSaveForGroupProtocols;
    }

    public String getThePathToSaveForGroupProtocols() {
        return thePathToSaveForGroupProtocols;
    }

    public String getCertificate() {
        return certificate;
    }

    public ArrayList<String> getProtocolInformationLinesName() {
        return protocolInformationLinesName;
    }

    public ArrayList<String> getProtocolInformationTable() {
        return protocolInformationTable;
    }

    public ArrayList<DecryptionAbbreviation> getNameFields() {
        return nameFields;
    }

    public String getDecryptionProtocolPointsTable() {
        return decryptionProtocolPointsTable;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public String getResultOfProtocol() {
        return resultOfProtocol;
    }

    public String getProtocolNumber() {
        return protocolNumber;
    }

    public String getFormula() {
        return formula;
    }

    public String getFormulaThisValues() {
        return formulaThisValues;
    }

    public String getDecryptionReductionsInFormula() {
        return decryptionReductionsInFormula;
    }

    public String getFirstError() {
        return firstError;
    }

    public String getSecondError() {
        return secondError;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public ArrayList<String> getUnitsInColumnsNameFields() {
        return unitsInColumnsNameFields;
    }

    public double getActualErrorValue() {
        return actualErrorValue;
    }

    public double getActualErrorHalfInnerLimitValue() {
        return actualErrorHalfInnerLimitValue;
    }

}
