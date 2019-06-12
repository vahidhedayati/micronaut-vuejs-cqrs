package hotel.write.event.client.eventStore;


public class Data implements AppEventData {

    String value;
    String metadata;

    public Data(String value,String metaData) {
        this.value = value;
        this.metadata = metaData;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getMetadata() {
        return this.metadata;
    }
}