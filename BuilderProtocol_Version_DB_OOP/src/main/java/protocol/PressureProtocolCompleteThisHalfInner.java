package protocol;

import dataBase.ReadDataBase;
import mainPackage.ParserRTF;

public class PressureProtocolCompleteThisHalfInner extends PressureProtocol{
    //комплектный протокол давления с 1/2

    public PressureProtocolCompleteThisHalfInner(ReadDataBase readDataBase, ParserRTF parserRTF, int protocolScopeNumber) {
        super(readDataBase, parserRTF, protocolScopeNumber);
    }

    @Override
    public void calculation() {

        for (int i = 0; i < protocolPointsTable.size(); i++) { //вычисляем необходимые столбцы и добавляем в конец таблицы
            protocolPointsTable.get(i).add(calculation.calculationDrcym12VPCompleteMethod(getStandard(i), getDm(i)));
            protocolPointsTable.get(i).add(calculation.calculationDecym12VPCompleteMethod(getStandard(i), getDm(i)));
            protocolPointsTable.get(i).add(calculation.calculationDecymCompleteMethod(getDm(i)));
        }
    }

    @Override
    public void setFormulaAndDecryptionAndError() {

        formula = "Dе = max{[|Dmi| / |Хдиап|] • 100%} = ";
        formulaThisValues = String.format("\n= {[|%1.4f| / |%1.4f|] • 100%%} = %1.4f %%",
                            protocolPointsTable.get(0).get(5 + parserRTF.getCountDownMod()),
                            parserRTF.getXdiap(),
                            protocolPointsTable.get(0).get(9 + parserRTF.getCountDownMod()));

        decryptionReductionsInFormula = "где Dmi – погрешность ИВК в i-той точке, " + unitsInThisProtocol + "; " +
                "Хдиап – разница между значениями верхнего и нижнего предела измерения, " + unitsInThisProtocol + "; ";

        firstError = String.format("Приведенная погрешность (Dесум1/2ВП): %1.4f %% в диапазоне от %1.4f до %1.4f " + unitsInThisProtocol, getMaxDecym12VPCompleteMethod(),
                parserRTF.getMinOnRange(),
                getRealValueHalfInnerLimit());

        secondError = String.format("Относительная погрешность (Drсум1/2ВП): %1.4f %% в диапазоне от %1.4f до %1.4f " + unitsInThisProtocol, getMaxDrcym12VPCompleteMethod(),
                getRealValueHalfInnerLimit(),
                parserRTF.getMaxOnRange());

        checkingOneAndSecondErrors(getMaxDecym12VPCompleteMethod(), getMaxDrcym12VPCompleteMethod());
    }
}
