import java.util.Random;
import java.util.concurrent.Semaphore;

public class CocheElectrico extends Vehiculo {

     Random aleatorioElectrico = new Random();
     int bateria = aleatorioElectrico.nextInt(100);
     boolean enchufado = false;

    public CocheElectrico(String nombre, String matricula, Parking parking, String tipo) {
        super(nombre, matricula, parking, tipo);
    }

    public double getTemporizador(){
        return super.temporizador.tiempoTotal();
    }

    public int getBateria() {
        return bateria;
    }

    public boolean isEnchufado() {
        return enchufado;
    }

    public void setEnchufado(boolean enchufado) {
        this.enchufado = enchufado;
    }

    @Override
    public void run() {
        super.getParking().entrarVehiculoElectrico(this);
        if(super.getSinPlaza()){
            System.out.println(super.getNombre() + " a enchufarlo a casa");
        } else {
            super.temporizador.empezar();
            try {
                Thread.sleep(aleatorioElectrico.nextInt(20000) + 2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            super.getParking().salirVehiculoElectrico(this);
            super.temporizador.finalizar();
        }
    }
}
