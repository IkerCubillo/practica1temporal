package ud.prog3.pr01;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.logging.*;
import java.util.regex.Pattern;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;


/** Clase para crear instancias como listas de reproducci贸n,
 * que permite almacenar listas de ficheros con posici贸n de 铆ndice
 * (al estilo de un array / arraylist)
 * con marcas de error en los ficheros y con m茅todos para cambiar la posici贸n
 * de los elementos en la lista, borrar elementos y a帽adir nuevos.
 */
public class ListaDeReproduccion implements ListModel<String> {
	public ArrayList<File> ficherosLista;     // ficheros de la lista de reproducci贸n
	public int ficheroEnCurso = -1;           // Fichero seleccionado (-1 si no hay ninguno seleccionado)
	private static Logger logger = Logger.getLogger( ListaDeReproduccion.class.getName() );

	private static final boolean ANYADIR_A_FIC_LOG = false; // poner true para no sobreescribir
	static {
		try {
		logger.addHandler( new FileHandler(
				ListaDeReproduccion.class.getName()+".log.xml", ANYADIR_A_FIC_LOG ));
		} catch (SecurityException | IOException e) {
			logger.log( Level.SEVERE, "Error en creaci髇 fichero log" );
		}
	}
	
	/** Constructor de lista de reproducci贸n, crea una lista vac铆a
	 */
	public ListaDeReproduccion() {
		ficherosLista = new ArrayList<>();
	}
	
	/** Devuelve uno de los ficheros de la lista
	 * @param posi	Posici贸n del fichero en la lista (de 0 a size()-1)
	 * @return	Devuelve el fichero en esa posici贸n
	 * @throws IndexOutOfBoundsException	Si el 铆ndice no es v谩lido
	 */
	public File getFic( int posi ) throws IndexOutOfBoundsException {
		return ficherosLista.get( posi );
	}	
	

	/**
	 * intercambia las dos posiciones (no hace nada si
		cualquiera de las posiciones es err髇ea).
	 * @param posi1 Posicion del primer fichero a intercambiar
	 * @param posi2 Posicion del segundo fichero a intercambiar
	 */
	public void intercambia(int posi1, int posi2) {
		try {
			Collections.swap(ficherosLista, posi1, posi2);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 *  Devuelve el n鷐ero de elementos de la lista
	 * @return
	 */
	public int size() {
		return ficherosLista.size();
	}
	
	/**
	 * A馻de un fichero al final de la lista
	 * @param f Fichero a a馻dir
	 */
	public void add(File f) {
		
		ficherosLista.add(f);
		avisarAnyadido(ficherosLista.indexOf(f));
	}
	
	/**
	 * Elimina un fichero de la lista, dada su posici髇
	 * @param posi Posicion del fichero a eliminar
	 */
	public void removeFic(int posi) {
		ficherosLista.remove(posi);
	}
	
	/**
	 *  Borra la lista
	 */
	public void clear() {
		ficherosLista.clear();
	}

	/** A帽ade a la lista de reproducci贸n todos los ficheros que haya en la 
	 * carpeta indicada, que cumplan el filtro indicado.
	 * Si hay cualquier error, la lista de reproducci贸n queda solo con los ficheros
	 * que hayan podido ser cargados de forma correcta.
	 * @param carpetaFicheros	Path de la carpeta donde buscar los ficheros
	 * @param filtroFicheros	Filtro del formato que tienen que tener los nombres de
	 * 							los ficheros para ser cargados.
	 * 							String con cualquier letra o d铆gito. Si tiene un asterisco
	 * 							hace referencia a cualquier conjunto de letras o d铆gitos.
	 * 							Por ejemplo p*.* hace referencia a cualquier fichero de nombre
	 * 							que empiece por p y tenga cualquier extensi贸n.
	 * @return	N煤mero de ficheros que han sido a帽adidos a la lista
	 */
	public int add(String carpetaFicheros, String filtroFicheros) {
		logger.log( Level.INFO, "A馻diendo ficheros con filtro " + filtroFicheros );
		// TODO: Codificar este m茅todo de acuerdo a la pr谩ctica (pasos 3 y sucesivos)
		filtroFicheros = filtroFicheros.replaceAll( "\\.", "\\\\." );  // Pone el s铆mbolo de la expresi贸n regular \. donde figure un .
		filtroFicheros = filtroFicheros.replaceAll( "\\*", "\\.*" );  // Pone el s铆mbolo de la expresi贸n regular .* donde figure un *
		logger.log( Level.INFO, "A馻diendo ficheros con filtro " + filtroFicheros );
		
		/**
		 * Cr閐itos a Iker Garc韆 que me ha facilitado esta parte
		 * Que no consegu韆 desarrollar completamente y me ha explicado
		 * que 閘 ha utilizado un pattern
		 */
		Pattern p = Pattern.compile(filtroFicheros);
		
		int countAdd = 0; //N鷐ero de ficheros a馻didos a la carpeta
		
		File fInic = new File(carpetaFicheros);
		if (fInic.isDirectory()) {
			for( File f : fInic.listFiles() ) {
				logger.log( Level.FINE, "Procesando fichero " + f.getName() );
				try {
					// TODO: Comprobar que f.getName() cumple el patr髇 y a馻dirlo a la lista
					if(p.matcher(f.getName()).matches()) {
						add(f);
						countAdd++;
					}
				} catch (Exception e) {
					logger.log( Level.SEVERE, "Excepci髇 " + e );
					// TODO: handle exception
				}
				
			}
		}
		
		return countAdd;
//		return 0;
	}
	
	
	//
	// M茅todos de selecci贸n
	//
	
	public boolean irA(int pos){
		if (pos >= ficherosLista.size()) {
			return false;  // Devuelve error
		}
		ficheroEnCurso = pos; 
		return true;
	}
	
	/** Seleciona el primer fichero de la lista de reproducci贸n
	 * @return	true si la selecci贸n es correcta, false si hay error y no se puede seleccionar
	 */
	public boolean irAPrimero() {
		ficheroEnCurso = 0;  // Inicia
		if (ficheroEnCurso>=ficherosLista.size()) {
			ficheroEnCurso = -1;  // Si no se encuentra, no hay selecci贸n
			return false;  // Y devuelve error
		}
		return true;
	}
	
	/** Seleciona el 煤ltimo fichero de la lista de reproducci贸n
	 * @return	true si la selecci贸n es correcta, false si hay error y no se puede seleccionar
	 */
	public boolean irAUltimo() {
		ficheroEnCurso = ficherosLista.size()-1;  // Inicia al final
		if (ficheroEnCurso==-1) {  // Si no se encuentra, no hay selecci贸n
			return false;  // Y devuelve error
		}
		return true;
	}

	/** Seleciona el anterior fichero de la lista de reproducci贸n
	 * @return	true si la selecci贸n es correcta, false si hay error y no se puede seleccionar
	 */
	public boolean irAAnterior() {
		if (ficheroEnCurso>=0) ficheroEnCurso--;
		if (ficheroEnCurso==-1) {  // Si no se encuentra, no hay selecci贸n
			return false;  // Y devuelve error
		}
		return true;
	}

	/** Seleciona el siguiente fichero de la lista de reproducci贸n
	 * @return	true si la selecci贸n es correcta, false si hay error y no se puede seleccionar
	 */
	public boolean irASiguiente() {
		ficheroEnCurso++;
		if (ficheroEnCurso>=ficherosLista.size()) {
			ficheroEnCurso = ficheroEnCurso -1;  // Si no se encuentra, no hay selecci贸n
			return false;  // Y devuelve error
		}
		return true;
	}
	
	/** Selecciona un fichero aleatorio de la lista de reproducci髇.
	* @return true si la selecci髇 es correcta, false si hay error y no se puede seleccionar
	*/
	public boolean irARandom() {
		Random rand = new Random();
		if (ficherosLista.size() <= 1) {
			return false;
		}
		ficheroEnCurso = rand.nextInt(0, ficherosLista.size());
		System.out.println(ficheroEnCurso);
		return true;
	}

	/** Devuelve el fichero seleccionado de la lista
	 * @return	Posici贸n del fichero seleccionado en la lista de reproducci贸n (0 a n-1), -1 si no lo hay
	 */
	public int getFicSeleccionado() {
		return ficheroEnCurso;
	}

	//
	// M茅todos de DefaultListModel
	//
	
	@Override
	public int getSize() {
		return ficherosLista.size();
	}

	@Override
	public String getElementAt(int index) {
		return ficherosLista.get(index).getName();
	}

		// Escuchadores de datos de la lista
		ArrayList<ListDataListener> misEscuchadores = new ArrayList<>();
	@Override
	public void addListDataListener(ListDataListener l) {
		misEscuchadores.add( l );
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		misEscuchadores.remove( l );
	}
	
	// Llamar a este m茅todo cuando se a帽ada un elemento a la lista
	// (Utilizado para avisar a los escuchadores de cambio de datos de la lista)
	private void avisarAnyadido( int posi ) {
		for (ListDataListener ldl : misEscuchadores) {
			ldl.intervalAdded( new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, posi, posi ));
		}
	}
}
