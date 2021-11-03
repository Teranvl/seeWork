package protocol;

import dataBase.ReadDataBase;
import mainPackage.ParserRTF;

public class PressureProtocolElementalThisHalfInner extends PressureProtocol{
    //поэлементный протокол давления с 1/2

    public PressureProtocolElementalThisHalfInner(ReadDataBase readDataBase, ParserRTF parserRTF, int protocolScopeNumber) {
        super(readDataBase, parserRTF, protocolScopeNumber);
    }

    @Override
    public void calculation() {

        for (int i = 0; i < protocolPointsTable.size(); i++) { //вычисляем необходимые столбцы и добавляем в конец таблицы
            protocolPointsTable.get(i).add(calculation.calculationDmpp());
            protocolPointsTable.get(i).add(calculation.calculationDmcym(getDm(i), calculation.getDmpp()));
            protocolPointsTable.get(i).add(calculation.calculationDrcym12VP(getStandard(i), calculation.getDmcym()));
            protocolPointsTable.get(i).add(calculation.calculationDecym12VP(getStandard(i), calculation.getDmcym()));
            protocolPointsTable.get(i).add(calculation.calculationDecym(calculation.getDmcym()));
        }
    }

    @Override
    public void setFormulaAndDecryptionAndError() {

        formula = "Dе=max{[1.1•√ (Dmi^2 + Dmппi^2)/|Xдиап|] • 100%} = ";
        formulaThisValues = String.format("\n= {[1.1•√ (%1.4f^2 + %1.4f^2) / |%1.4f|] • 100%%} = %1.4f %%",
                            protocolPointsTable.get(0).get(5 + parserRTF.getCountDownMod()),
                            protocolPointsTable.get(0).get(7 + parserRTF.getCountDownMod()),
                            parserRTF.getXdiap(),
                            protocolPointsTable.get(0).get(11 + parserRTF.getCountDownMod()));

        decryptionReductionsInFormula = "где Dmппi – погрешность первичного преобразователя в i-той точке (определяется по паспортным данным y = " +
                readDataBase.getValueY().get(protocolScopeNumber) + " %), " + unitsInThisProtocol + "; " +
                "Dmi – погрешность ИВК в i-той точке, " + unitsInThisProtocol + "; " +
                "Хдиап – разница между значениями верхнего и нижнего предела измерения, " + unitsInThisProtocol + "; ";

        firstError = String.format("Приведенная погрешность (Dесум1/2ВП): %1.4f %% в диапазоне от %1.4f до %1.4f " + unitsInThisProtocol, getMaxDecym12VP(),
                parserRTF.getMinOnRange(),
                getRealValueHalfInnerLimit());

        secondError = String.format("Относительная погрешность (Drсум1/2ВП): %1.4f %% в диапазоне от %1.4f до %1.4f " + unitsInThisProtocol, getMaxDrcym12VP(),
                getRealValueHalfInnerLimit(),
                parserRTF.getMaxOnRange());

        checkingOneAndSecondErrors(getMaxDecym12VP(), getMaxDrcym12VP());
    }
}
