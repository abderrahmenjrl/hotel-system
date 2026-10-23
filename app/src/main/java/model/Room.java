package model;
import java.util.ArrayList;
import java.util.List;

public abstract class Room {
    protected static float basePrice = 20;
    protected static int roomCount = 1;
    private int id;
    private Guest currentGuest;
    private List<Guest> guestHistory;
    private float profit;
    
    Room(){
        this.id = roomCount;
        this.currentGuest = null;
        this.guestHistory = new ArrayList<>();
        this.profit = 0;
        Room.roomCount++;
    }

    abstract public float getPrice();
    
    public boolean isAvailable(){
        return currentGuest == null;
    }
    public void checkIn(Guest guest){
        currentGuest = guest;
        guestHistory.add(guest);
        profit += getPrice();
    }
    public void checkOut(){
        currentGuest = null;
    }
    public int getOccupancyCount(){
        return guestHistory.size();
    }
    public int getId(){
        return id;
    }
    public float getProfit(){
        return profit;
    }
    public Guest getCurrentGuest() {
        return currentGuest;
    }

    public List<Guest> getGuestHistory() {
        return new ArrayList<>(guestHistory);
    }
}