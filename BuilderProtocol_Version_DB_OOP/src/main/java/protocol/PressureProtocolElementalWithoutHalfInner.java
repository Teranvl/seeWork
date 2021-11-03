package protocol;

import dataBase.ReadDataBase;
import mainPackage.ParserRTF;

public class PressureProtocolElementalWithoutHalfInner extends PressureProtocol{
    //поэлементный протокол давления без 1/2

    public PressureProtocolElementalWithoutHalfInner(ReadDataBase readDataBase, ParserRTF parserRTF, int protocolScopeNumber) {
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
    public void setFormulaAndDecryptionAndError() {

        formula = "Dесум=max{[1.1•√ (Dmi^2 + Dmппi^2)/|Хдиап|] • 100%} = ";
        formulaThisValues = String.format("\n= {[1.1•√ (%1.4f^2 + %1.4f^2) / |%1.4f|] • 100%%} = %1.4f %%",
                            protocolPointsTable.get(0).get(5 + parserRTF.getCountDownMod()),
                            protocolPointsTable.get(0).get(7 + parserRTF.getCountDownMod()),
                            parserRTF.getXdiap(),
                            protocolPointsTable.get(0).get(9 + parserRTF.getCountDownMod()));

        decryptionReductionsInFormula = "где Dmппi – погрешность первичного преобразователя в i-той точке (определяется по паспортным данным y = " +
                readDataBase.getValueY().get(protocolScopeNumber) + " %), " + unitsInThisProtocol + "; " +
                "Dmi – погрешность ИВК в i-той точке, " + unitsInThisProtocol + "; " +
                "Хдиап – разница между значениями верхнего и нижнего предела измерения, " + unitsInThisProtocol + "; ";


        firstError = String.format("Погрешность на всем диапазоне (Dmсум): %1.4f " + unitsInThisProtocol + ";", getMaxDmcym());
        secondError = String.format("Приведенная погрешность (Dесум): %1.4f %%", getMaxDecym());

        checkingOneError(getMaxDecym());
    }
}
