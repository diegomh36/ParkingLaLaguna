import java.util.concurrent.TimeUnit;

/*
 * 
 * Clase sencilla en Java que se encarga de calcular el tiempo ocurrido entre que empieza un programa y termina.
 * @author Alejandro Navarro Castillo, 2ªDAM CIFP César Manrique
 *
 */
public class Timer {
	/* El instante en el que se inicia la cuenta atrás */
	private long tiempoInicio;
	/* El instante en el que termina la cuenta atrás */
	private long tiempoFin;
	/* booleano que indica si la cuenta ha terminado o no */
	private boolean terminado;
	
	/** Crea una instancia del contador.
	*/
	public Timer() {
		terminado = false;
		empezar();
	}
	
	/** Incializa una cuenta atrás, usando el método nanoTime(), el que devuelve el tiempo actual
	 *  en nano segundos.
	*/
	public void empezar() {
		tiempoInicio = System.nanoTime();
	}
	
	/** Finaliza la cuenta del tiempo, y establece el booleano terminado a true.
	*/
	public void finalizar() {
		if (!terminado) {
			tiempoFin = System.nanoTime();
			terminado = true;
		}
	}
	
	/** Devuelve el tiempo total transcurrido entre el inicio de la cuenta y el final
	 * @return: EL número de segundos transcurridos, o -1 si no se ha terminado la cuenta atrás.
	*/
	
	public double tiempoTotal() {
		if (terminado) return TimeUnit.MILLISECONDS.convert(tiempoFin - tiempoInicio, TimeUnit.NANOSECONDS)/1000.0;
		else return -1;
	}
	
	

}
