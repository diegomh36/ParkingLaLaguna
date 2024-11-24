import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class MaquinaPago implements Runnable {
    String nombre;
    Parking parking;

    ReentrantLock lock = new ReentrantLock();

    public MaquinaPago(String nombre, Parking parking) {
        this.nombre = nombre;
        this.parking = parking;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public void run() {
        while (true) {
            try {
                parking.pagar(this);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
