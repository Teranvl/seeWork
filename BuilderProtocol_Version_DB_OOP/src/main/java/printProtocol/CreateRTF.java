package printProtocol;

import protocol.Protocol;
import com.lowagie.text.Rectangle;
import com.lowagie.text.*;
import com.lowagie.text.rtf.RtfWriter2;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class CreateRTF {

    private Protocol protocol;
    private int protocolScopeNumber;
    private Document doc;

    public CreateRTF(Protocol protocol){
        this.protocol = protocol;
        protocolScopeNumber = protocol.getProtocolScopeNumber();
    }

    private void createDocFile(){

        String fileName = protocol.getProtocolNumber() + " - " + protocol.getReadDataBase().getIdentifiersFromDataBase().get(protocolScopeNumber);

        doc = new Document(PageSize.A4, 25 ,25, 30, 20); // поля внутри документа// первая цифра - слево
        try {

            if(fileName.contains("\\")){//решение ошибки плохого имени файла
                fileName = fileName.replace("\\", "-");
            }

            new File(protocol.getThePathToSaveForGroupProtocols()).mkdir(); // создание католога, если он не существует. Есть еще mkdirs - создает еще и родительские катологи


            RtfWriter2.getInstance(doc, new FileOutputStream(protocol.getThePathToSaveForGroupProtocols() + "\\" + fileName + ".rtf"));
        } catch (FileNotFoundException e) {
            System.out.println("Ошибка в методе createDocFile");
        }
        doc.open();

    } // создание документа

    private void writeInFirstPathRtf() throws DocumentException {

        Chunk chunk = new Chunk(protocol.getDateAndTime() + "\n"); //Получаем значение времени
        doc.add(chunk);

        Paragraph firstParagraph = new Paragraph();

        firstParagraph.add("Протокол калибровки № " + protocol.getReadDataBase().getProtocolNumber().get(protocolScopeNumber) + "\n");
        firstParagraph.add("к сертификату о периодической калибровке № " + protocol.getCertificate());
        firstParagraph.setAlignment(Element.ALIGN_CENTER);
        firstParagraph.setAlignment(Element.ALIGN_CENTER);

        doc.add(firstParagraph);

        //Таблица информациии
        Table table = new Table(2,17); // создание таблицы строк и столбцов
        table.setWidth(100); // установка ширины всей таблицы
        table.setBorder(Rectangle.NO_BORDER); // сделать границы пунктирными

        int [] widths = new int[2]; //массив для установки ширины столбцов таблицы
        widths [0] = 40; // ширина 1 столбца
        widths [1] = 60; // ширина второго столбца

        table.setWidths(widths); // добавляем значения ширины в таблицу

        for(int i =0; i < 17; i++){ // т.к. 16 строк в верхней таблице
            table.addCell(SomethingCreate.createCellNoBorder(protocol.getProtocolInformationLinesName().get(i)), i, 0); //заполняем названиями
            table.addCell(SomethingCreate.createCellNoBorder(protocol.getProtocolInformationTable().get(i)), i, 1); // заполняем значениями

        }


        doc.add(table);

        doc.add(new Chunk("\nТаблица результатов калибровки"));

    } // заполнение первой части документа

    private void createSecondTableInRtf() throws DocumentException {

      //  int count_columns = oneChannelPoints.get(0).size(); // количество столбцов в таблице
        int count_columns = protocol.getProtocolPointsTable().get(0).size();

        Table secondTable; //вторая таблица
        int [] widths; //ширина каждого столбца - это массив

        secondTable = new Table(count_columns); // создание таблицы с указанием количества столбцов
        widths = new int [count_columns]; // массив для установки ширины каждого столбца


        for(int i = 0; i < widths.length; i++){  // установка значения ширины стобцов, для каждой ячейки

            if(i == 0) {
                widths[i] = 3; //для первой ширина меньше, т.к. там распложился только номер
            }
            if(i == 1 || i == 2) {

                widths[i] = 9; // второй столбец и т.д
            }

            if( i > 2){
                widths[i] = 8;
            }

        }

   /*     System.out.println("Информация о ширине стобцов: ");
        for(int i = 0; i < widths.length; i++)
            System.out.print(widths[i] + " | ");*/


        secondTable.setWidths(widths); // добавляем массив с шириной
        secondTable.setWidth(100); // ширина всей таблицы

        for(int i = 0; i < protocol.getNameFields().size(); i++) { // цикл для вставки названий столбцов в первую строку таблицы

            if(i == 0){//в нулевом стобце хранится номер, ему не надо дописывать единицы измерения
                secondTable.addCell(SomethingCreate.createCellForNameFields(protocol.getNameFields().get(i).getTextInTableColumn()), 0, i); //вставка названия ячеек таблицы
            }else{ //во всех остальных случаях надо дописать единицы измерения
                secondTable.addCell(SomethingCreate.createCellForNameFields(protocol.getNameFields().get(i).getTextInTableColumn() + protocol.getUnitsInColumnsNameFields().get(i)), 0, i); //вставка названия ячеек таблицы
            }
        }


        for(int i =0 ; i < protocol.getProtocolPointsTable().size() ; i++){ // i - строка, j - столбец . Заполнение таблицы значениями
            for(int j =0; j < protocol.getProtocolPointsTable().get(i).size(); j++){
                if(protocol.getProtocolPointsTable().get(i).get(j) == -123.456){ // проверяем на значение - 123.456, если находим то в этой ячейке таблицы ничего не печатаем. Только для 1/2ВП
                    secondTable.addCell(SomethingCreate.createCellForNumbersInTables(" "), i+1, j);
                }else{
                    if(j == 0){ // вставка номера
                        secondTable.addCell(SomethingCreate.createCellForNumbersInTables(String.format("%1.0f",(protocol.getProtocolPointsTable().get(i).get(j)))), i+1, j); // +1 т.к. первая строка занята названиями столбцов
                    }else{
                        if(j == 1){ //эталон
                            secondTable.addCell(SomethingCreate.createCellForValues(String.format("%1.4f",(protocol.getProtocolPointsTable().get(i).get(j)))), i+1, j);
                        }else{
                            if(protocol.getParserRTF().getCountDownMod() == 1){ //если обратный ход
                                if(j == 3 || j == 4 || j == 5){//переводим в экспоненциальный вид 3 столбца
                                    if(zeroCounter(protocol.getProtocolPointsTable().get(i).get(j))){
                                        secondTable.addCell(SomethingCreate.createCellForValues(String.format("%1.2e", protocol.getProtocolPointsTable().get(i).get(j))), i+1, j);
                                    }else{
                                        secondTable.addCell(SomethingCreate.createCellForValues(String.format("%1.4f", protocol.getProtocolPointsTable().get(i).get(j))), i+1, j);
                                    }
                                }else{
                                    secondTable.addCell(SomethingCreate.createCellForValues(String.format("%1.4f", protocol.getProtocolPointsTable().get(i).get(j))), i+1, j);
                                }
                            }else {
                                if(j == 3 || j == 4){//переводим в экспоненциальный вид 2 столбца
                                    if(zeroCounter(protocol.getProtocolPointsTable().get(i).get(j))){
                                        secondTable.addCell(SomethingCreate.createCellForValues(String.format("%1.2e", protocol.getProtocolPointsTable().get(i).get(j))), i+1, j);
                                    }else{
                                        secondTable.addCell(SomethingCreate.createCellForValues(String.format("%1.4f", protocol.getProtocolPointsTable().get(i).get(j))), i+1, j);
                                    }
                                }else{
                                    secondTable.addCell(SomethingCreate.createCellForValues(String.format("%1.4f", protocol.getProtocolPointsTable().get(i).get(j))), i+1, j);
                                }
                            }
                        }
                    }
                }
            }
        }

        doc.add(secondTable);

    }

    private void decryptionSecondTable() throws DocumentException {

        Paragraph secondParagraph = new Paragraph(); //параграф для описания сокращения столбцов таблицы
        secondParagraph.setAlignment(Element.ALIGN_JUSTIFIED);  // выравнивание текста по ширине


        secondParagraph.add(protocol.getDecryptionProtocolPointsTable()); //добавляем строку в документ

        secondParagraph.add(new Chunk("\nПриведенная погрешность измерения определяется по формуле:"));

        doc.add(secondParagraph);

    }

    private void printingFormulasAndErrors() throws DocumentException {

        Paragraph thirdParagraph = new Paragraph(); // параграф для формул
        thirdParagraph.setAlignment(Element.ALIGN_CENTER);

        thirdParagraph.add(new Chunk(protocol.getFormula()));
        thirdParagraph.add(protocol.getFormulaThisValues());
        doc.add(thirdParagraph);

        Paragraph fourthParagraph = new Paragraph(); // параграф для расшифровки обозначений в формулах
        fourthParagraph.add(new Chunk(protocol.getDecryptionReductionsInFormula()));
        fourthParagraph.setAlignment(Element.ALIGN_JUSTIFIED);


        fourthParagraph.add(new Chunk("\n\n" + protocol.getFirstError()));
        fourthParagraph.add(new Chunk("\n" + protocol.getSecondError()));


        fourthParagraph.add(new Chunk("\nКанал: " + protocol.getResultOfProtocol()).setBackground(protocol.getBackgroundColor()));

        fourthParagraph.add(new Chunk("\n\n\tКалибровщик\t\t\t\t\t\t\t\t\t\t" + protocol.getParserRTF().getCreateProtocolUserName()));
        doc.add(fourthParagraph);
        doc.close();

    }

    private boolean zeroCounter(double n) { //функция ведет подсчет нулей в числе

        n = Math.abs(n);

        int count = 0;

        if(n == 0){
            return false;
        }

        if (n > 1) {
            return false;

        } else {

            while (n < 1) {
                n *= 10;
                count++;
            }
            count -= 1;

            if(count > 2){
                return true;
            }else {
                return false;
            }

        }

    }

    public void print(){

        try {

            createDocFile();
            writeInFirstPathRtf();
            createSecondTableInRtf();
            decryptionSecondTable();
            printingFormulasAndErrors();

        } catch (DocumentException e) {
            System.out.println("!Ошибка в методе печати!");
        }


    }

}

