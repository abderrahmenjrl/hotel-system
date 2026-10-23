package controller;

import model.Hotel;
import model.Room;
import view.HotelView;

public class HotelController implements HotelView.Actions {

    private final Hotel hotel;
    private final HotelView view;

    public HotelController(Hotel hotel) {
        this.hotel = hotel;
        this.view = new HotelView(hotel, this);
    }

    @Override
    public Room registerGuest(String name, String surname) {
        return hotel.registerGuest(name, surname);
    }

    @Override
    public boolean checkOut(int roomNumber) {
        return hotel.checkOut(roomNumber);
    }

    public void show() {
        view.setVisible(true);
    }
    @Override
    public void addRoomPairs(int numberOfPairs) {
        hotel.addRoomPairs(numberOfPairs);
    }

    @Override
    public void changeBasePrice(float newBasePrice) {
        hotel.setBasePrice(newBasePrice);
    }
}