package model;

public class BusinessClassRoom extends Room {

    public BusinessClassRoom() {
        super();
    }

    public float getPrice() {
        return Room.basePrice * 1.5f;
    }
}