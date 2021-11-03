package protocol;

import dataBase.ReadDataBase;
import mainPackage.ParserRTF;

public class PressureProtocolCompleteWithoutHalfInner extends PressureProtocol{
    //комплектный протокол давления без 1/2

    public PressureProtocolCompleteWithoutHalfInner(ReadDataBase readDataBase, ParserRTF parserRTF, int protocolScopeNumber) {
        super(readDataBase, parserRTF, protocolScopeNumber);
    }

    @Override
    public void calculation() {

        for (int i = 0; i < protocolPointsTable.size(); i++) { //вычисляем необходимые столбцы и добавляем в конец таблицы
            protocolPointsTable.get(i).add(calculation.calculationDecymCompleteMethod(getDm(i)));
        }
    }

    @Override
    public void setFormulaAndDecryptionAndError() {

        formula = "Dесум = max{[|Dmi| / |Хдиап|] • 100%} = ";
        formulaThisValues = String.format("\n= {[|%1.4f| / |%1.4f|] • 100%%} = %1.4f %%",
                            protocolPointsTable.get(0).get(5 + parserRTF.getCountDownMod()),
                            parserRTF.getXdiap(),
                            protocolPointsTable.get(0).get(7 + parserRTF.getCountDownMod()));

        decryptionReductionsInFormula = "где Dmi – погрешность ИВК в i-той точке, " + unitsInThisProtocol + "; " +
                "Хдиап – разница между значениями верхнего и нижнего предела измерения, " + unitsInThisProtocol + "; ";

        firstError = String.format("Максимальная погрешность на всем диапазоне (Dm): %1.4f " + unitsInThisProtocol, getMaxDm());
        secondError = String.format("Приведенная погрешность (Dесум): %1.4f %%", getMaxDecymCompleteMethod());

        checkingOneError(getMaxDecymCompleteMethod());
    }
}
