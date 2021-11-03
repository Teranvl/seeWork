package protocol;

public enum TemperatureSensors {
    //датчики температуры

    P109     ("П-109"),
    TC1388   ("ТС-1388"),
    P77      ("П-77"),
    CHPT     ("ЧЭПТ-30"),
    TCP0313  ("ТСП 0313-00"),
    TCP0131  ("ТСП 0131-00"),
    TC1288   ("ТС-1288"),
    TCPY0104 ("ТСПУ-0104"),
    TXA      ("ТХА"),
    TXK      ("ТХК"),
    TR10C    ("TR 10-C"),
    P148     ("П-148");

    private String sensor;

    TemperatureSensors(String sensor){
        this.sensor = sensor;
    }

    public String getStringSensor(){
        return sensor;
    }

}
