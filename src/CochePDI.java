import java.util.Random;

public class CochePDI extends Vehiculo implements Runnable{

    public CochePDI(String nombre, String matricula, Parking parking, String tipo) {
        super(nombre, matricula, parking, tipo);
    }

    public double getTemporizador(){
        return super.temporizador.tiempoTotal();
    }

    @Override
    public void run() {
        super.getParking().entrarVehiculoPDI(this);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (super.getSinPlaza()) {
            System.out.println(super.getNombre() + " a aparcar al Mercadona");
        } else {
            super.temporizador.empezar();
            try {
                Thread.sleep(super.getRand().nextInt(20000) + 2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            super.getParking().salirVehiculoPDI(this);
            super.temporizador.finalizar();
        }
    }
}
