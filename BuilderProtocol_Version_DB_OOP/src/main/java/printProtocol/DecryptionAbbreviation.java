package printProtocol;

public enum DecryptionAbbreviation {

    Number("№","Номер"),
    Standard("Эталон \n","Эталон"),
    Measured("Измерено \n","Измерено"),
    S("S \n", "S – оценка систематической составляющей погрешности"),
    A("A \n", "A – оценка случайной составляющей погрешности"),
    H("H \n", "H - оценка вариации"),
    Dm("Dm \n", "Dm – оценка погрешности (максимум)"),
    Dr("Dr \n", "Dr – относительная погрешность"),
    Dmpp("Dmпп \n", "Dmпп – абсолютная погрешность первичного преобразователя"),
    Dmcym("Dmсум \n", "Dmсум – оценка погрешности измерительного канала (с учетом погрешности первичного преобразователя)"),
    Dmxc("Dmxc \n", "Dmхс – оценка погрешности канала измерения холодного спая"),
    Drcym12VP("Drсум \n1/2ВП \n", "Drcyм 1/2ВП - относительная погрешность измерительного канала от 1/2ВП"),
    Decym12VP("Dесум \n1/2ВП \n", "Dесум 1/2ВП - приведенная погрешность измерительного канала от 1/2ВП"),
    Drcym("Drcyм \n", "Drcyм – относительная погрешность измерительного канала (с учетом погрешности первичного преобразователя)"),
    Decym("Deсум \n", "Deсум - приведенная погрешность измерительного канала");

    private String decryption;
    private String textInTableColumn;


    DecryptionAbbreviation(String textInTableColumn, String decryption){
        this.decryption = decryption;
        this.textInTableColumn = textInTableColumn;
    }

    public String getDecryption(){
        return decryption;
    }

    public String getTextInTableColumn() {
        return textInTableColumn;
    }


}
