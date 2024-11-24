
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Parking {
    Semaphore plazasCoches = new Semaphore(155);//+20 plazas reservadas para PDI y +5 para coches eléctricos con baja bateria.
    Semaphore plazasPDI = new Semaphore(20);
    Semaphore plazasElectrico = new Semaphore(5);
    Semaphore plazasMotos = new Semaphore(26);//26 plazas de moto
    ReentrantLock monitorEntrada = new ReentrantLock();
    ReentrantLock monitorEntradaPDI = new ReentrantLock();

    ReentrantLock monitorRegistro = new ReentrantLock();
    Condition condicionRegistro = monitorRegistro.newCondition();
    Semaphore maquinas = new Semaphore(1);

    String rutaRegistro = "src/registro.txt";
    String rutaDinero = "src/moneymoney.dat";
    Queue<Vehiculo> vehiculosPendientesPago = new LinkedList<>();
    static double dineroTotal = 0.0;

    public void entrarVehiculo(Vehiculo vehiculo) {
        monitorEntrada.lock();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(vehiculo.getNombre() + " quiere entrar al parking");
        if (vehiculo.getTipo().compareToIgnoreCase("Moto") == 0) {
            if (plazasMotos.tryAcquire()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(vehiculo.getNombre() + " entra al parking");
            } else {
                vehiculo.setSinPlaza(true);
            }
        }
        if (vehiculo.getTipo().compareToIgnoreCase("Coche") == 0) {
            if (plazasCoches.tryAcquire()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(vehiculo.getNombre() + " entra al parking");
            } else {
                vehiculo.setSinPlaza(true);
            }
        }
        monitorEntrada.unlock();
    }

    public void salirVehiculo(Vehiculo vehiculo) {
        if (vehiculo.getTipo().compareToIgnoreCase("Coche") == 0) {
            plazasCoches.release();
        }
        if (vehiculo.getTipo().compareToIgnoreCase("Moto") == 0) {
            plazasMotos.release();
        }
        System.out.println(vehiculo.getNombre() + " sale del parking.");
        vehiculosPendientesPago.add(vehiculo);
    }

    public void entrarVehiculoElectrico(CocheElectrico cocheElectrico) {
        monitorEntrada.lock();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(cocheElectrico.getNombre() + " quiere entrar al parking");

        if (cocheElectrico.getBateria() < 20 && plazasElectrico.tryAcquire()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(cocheElectrico.getNombre() + " entra al parking enchufado con " + cocheElectrico.getBateria() + "%");
            cocheElectrico.setEnchufado(true);
        } else if (cocheElectrico.getBateria() >= 20) {
            if (plazasCoches.tryAcquire()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(cocheElectrico.getNombre() + " entra al parking con " + cocheElectrico.getBateria() + "%");
            } else {
                cocheElectrico.setSinPlaza(true);
            }
        } else {
            cocheElectrico.setSinPlaza(true);
        }


        monitorEntrada.unlock();
    }

    public void salirVehiculoElectrico(CocheElectrico cocheElectrico) {
        if (cocheElectrico.isEnchufado()) {
            plazasElectrico.release();
        } else {
            plazasCoches.release();
        }
        System.out.println(cocheElectrico.getNombre() + " sale del parking.");
        vehiculosPendientesPago.add(cocheElectrico);
    }

    public void entrarVehiculoPDI(CochePDI cochePDI) {
        monitorEntradaPDI.lock();
        try {
            System.out.println(cochePDI.getNombre() + " quiere entrar al parking de PDI");

            if (plazasPDI.tryAcquire()) {
                System.out.println(cochePDI.getNombre() + " entra al parking");
            } else {
                cochePDI.setSinPlaza(true);
            }
        }finally {
            monitorEntradaPDI.unlock();
        }
    }

    public void salirVehiculoPDI(CochePDI cochePDI) {
        plazasPDI.release();
        System.out.println(cochePDI.getNombre() + " sale del parking de profesores.");
        vehiculosPendientesPago.add(cochePDI);
    }


    public void pagar(MaquinaPago maquina) throws InterruptedException, IOException {
        monitorRegistro.lock();
        try {
            Vehiculo vehiculo = vehiculosPendientesPago.poll();
            if (vehiculo != null) {
                System.out.println("El vehiculo " + vehiculo.getNombre() + " va ha proceder a pagar");

                double precio = vehiculo.getTemporizador() * 1.5;

                BufferedWriter bw = null;
                try {
                    bw = new BufferedWriter(new FileWriter(rutaRegistro, true));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                Date date = new Date();

                try {
                    bw.write("Matricula  "+vehiculo.getMatricula() + ", Tipo: " + vehiculo.getTipo() + ", Hora salida: " + dateFormat.format(date) +", Gasto: "+String.format("%.2f",precio));
                    bw.newLine();
                    bw.flush();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }finally {
                    bw.close();
                }

                maquinas.tryAcquire();
                File fichero = new File(rutaDinero);
                dineroTotal += precio;
                DataOutputStream dataOS = new
                        DataOutputStream(new FileOutputStream(fichero));

                dataOS.writeDouble(dineroTotal);

                dataOS.close();
                maquinas.release();
                System.out.println("Registro completo");
                Thread.sleep(500);
                condicionRegistro.signalAll();
            }else{
                Thread.sleep(300);
            }

        }finally {
            monitorRegistro.unlock();
        }
    }
}
