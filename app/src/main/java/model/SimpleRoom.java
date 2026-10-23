package model;

public class SimpleRoom extends Room{

    public SimpleRoom() {
        super();
    }
    public float getPrice() {
        return Room.basePrice;
    }
}