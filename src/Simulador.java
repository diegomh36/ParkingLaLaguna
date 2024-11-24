import java.util.ArrayList;
import java.util.Random;

public class Simulador {
    public static void main(String[] args) throws InterruptedException {
        Parking parking = new Parking();
        ArrayList<Vehiculo> vehiculos = new ArrayList<>();
        ArrayList<Moto> motos = new ArrayList<>();

        MaquinaPago maquinaPago = new MaquinaPago("Maquina de pago 1", parking);
        MaquinaPago maquinaPago2 = new MaquinaPago("Maquina de pago 2", parking);
        MaquinaPago maquinaPago3 = new MaquinaPago("Maquina de pago 3", parking);

        for (int i = 0; i < 30; i++) motos.add(new Moto("Moto " + i, generarMatricula(), parking, "Moto"));

        for (int i = 0; i < 100; i++) vehiculos.add(new CocheElectrico("Tesla " + i, generarMatricula(), parking, "Electrico"));
        for (int i = 0; i < 150; i++) vehiculos.add(new Vehiculo("Coche " + i, generarMatricula(), parking, "Coche"));
        for (int i = 0; i < 50; i++) vehiculos.add(new CochePDI("Coche PDI " + i, generarMatricula(), parking, "PDI"));

        ArrayList<Thread> listaHilosM = new ArrayList();
        motos.forEach(m -> listaHilosM.add(new Thread(m)));

        ArrayList<Thread> listaHilosV = new ArrayList();
        vehiculos.forEach(v -> listaHilosV.add(new Thread(v)));

        Thread hiloMaquina = new Thread(maquinaPago);
        Thread hiloMaquina2 = new Thread(maquinaPago2);
        Thread hiloMaquina3 = new Thread(maquinaPago3);
        hiloMaquina.start();
        hiloMaquina2.start();
        hiloMaquina3.start();


        listaHilosM.forEach(Thread::start);
        listaHilosV.forEach(Thread::start);

        hiloMaquina.join();
        hiloMaquina2.join();
        hiloMaquina3.join();

    }

    public static String generarMatricula() {
        Random random = new Random();

        int numeros = random.nextInt(10000);

        StringBuilder letras = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            char letra = (char) ('A' + random.nextInt(26));
            letras.append(letra);
        }

        return String.format("%04d-%s", numeros, letras);
    }
}
