import controller.HotelController;
import model.Hotel;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Hotel hotel = new Hotel(6, "Hotel Žalgiris");
            HotelController controller = new HotelController(hotel);
            controller.show();
        });
    }
}