package protocol;

import dataBase.ReadDataBase;
import mainPackage.ParserRTF;

public class TemperatureProtocol_TCPY0104 extends ThermoResistanceProtocol {

    public TemperatureProtocol_TCPY0104(ReadDataBase readDataBase, ParserRTF parserRTF, int protocolScopeNumber) {
        super(readDataBase, parserRTF, protocolScopeNumber);
    }

    @Override
    public void calculation() {
        for (int i = 0; i < protocolPointsTable.size(); i++) { //вычисляем необходимые столбцы и добавляем в конец таблицы

            protocolPointsTable.get(i).add(calculation.calculationDmpp());
            protocolPointsTable.get(i).add(calculation.calculationDmcym(getDm(i), calculation.getDmpp()));
            protocolPointsTable.get(i).add(calculation.calculationDecym(calculation.getDmcym()));

        }
    }

    @Override
    public void setDecryptionDmppi() {
        decryptionDmppi = "где Dmппi – погрешность первичного преобразователя в i-той точке (определяется по паспортным данным y = " +
                          readDataBase.getValueY().get(protocolScopeNumber) + " %), ";
    }
}
