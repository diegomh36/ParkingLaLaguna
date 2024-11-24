import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Random;

public class Vehiculo implements Runnable {
    private String nombre;
    private String matricula;
    private String tipo;
    private Parking parking;
    private Boolean sinPlaza = false;
    private Random rand = new Random();
    Timer temporizador = new Timer();


    //region Getter, Setter y Constructor
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getTemporizador() {
        return temporizador.tiempoTotal();
    }

    public void setTemporizador(Timer temporizador) {
        this.temporizador = temporizador;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Parking getParking() {
        return parking;
    }

    public void setParking(Parking parking) {
        this.parking = parking;
    }

    public Boolean getSinPlaza() {
        return sinPlaza;
    }

    public void setSinPlaza(Boolean sinPlaza) {
        this.sinPlaza = sinPlaza;
    }

    public Random getRand() {
        return rand;
    }

    public void setRand(Random rand) {
        this.rand = rand;
    }

    public Vehiculo(String nombre, String matricula, Parking parking, String tipo) {
        this.nombre = nombre;
        this.matricula = matricula;
        this.parking = parking;
        this.tipo = tipo;
    }

    //endregion

    @Override
    public void run() {
        parking.entrarVehiculo(this);
        if (sinPlaza) {
            System.out.println(nombre + " a aparcar al Mercadona");
        } else {
            temporizador.empezar();

            try {
                Thread.sleep(rand.nextInt(20000) + 2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            parking.salirVehiculo(this);
            temporizador.finalizar();
        }
    }
}
