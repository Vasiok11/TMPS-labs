import java.util.List;

public interface IOrder {
    String getBeverage();
    String getSize();
    List<String> getExtras();
    String describe();
}
