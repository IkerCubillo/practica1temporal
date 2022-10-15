package ud.prog03.pr01;


import static org.junit.Assert.*;

import java.io.File;
import java.util.Arrays;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ud.prog3.pr01.ListaDeReproduccion;

public class ListaDeReproduccionTest {
	private ListaDeReproduccion lr1;
	private ListaDeReproduccion lr2;
	private ListaDeReproduccion lr3;
	
	private final File FIC_TEST1 = new File( "test/res/No del grupo.mp4" );
	private final File FIC_TEST2 = new File( "test/res/[Official Video] Daft Punk - Pentatonix.mp4" );
	private final File FIC_TEST3 = new File( "test/res/[Official Video] I Need Your Love - Pentatonix (Calvin Harris feat. Ellie Goulding Cover).mp4" );
	private final File FIC_TEST4 = new File( "test/res/Fichero erroneo Pentatonix.mp4" );
	private final File FIC_TEST5 = new File( "test/res/Fichero Pentatonix no video.txt" );
	
	@Before
	public void setUp() throws Exception {
	lr1 = new ListaDeReproduccion();
	lr2 = new ListaDeReproduccion();
	lr2.add( FIC_TEST1 );
	
	lr3 = new ListaDeReproduccion();
	lr3.add( FIC_TEST1 );
	lr3.add( FIC_TEST2 );
	lr3.add( FIC_TEST3 );
	lr3.add( FIC_TEST4 );
	lr3.add( FIC_TEST5 );
	}
	
	@After
	 public void tearDown() {
	lr2.clear();
	}

	// Chequeo de error por getFic(índice) por encima de final
	@Test(expected = IndexOutOfBoundsException.class)
	public void testGet_Exc1() {
	lr1.getFic(0); // Debe dar error porque aún no existe la posición 0
	}
	// Chequeo de error por get(índice) por debajo de 0
	@Test(expected = IndexOutOfBoundsException.class)
	public void testGet_Exc2() {
	lr2.getFic(-1); // Debe dar error porque aún no existe la posición -1
	}
	// Chequeo de funcionamiento correcto de get(índice)
	@Test public void testGet() {
	assertEquals( FIC_TEST1, lr2.getFic(0) ); // El único dato es el fic-test1
	}
	
	// Chequeo de intercambio de elementos
	@Test
	public void intercambia() {
		File fich1 = lr3.getFic(1); //guardamos el fichero 1
		File fich4 = lr3.getFic(4); //guardamos el fichero 3
		lr3.intercambia( 1, 4 );  // intercambiamos los elementos 1 y 3
		assertEquals( fich1, lr3.getFic(4) );  // Comprobamos que los elementos cambiados son los seleccionados
		assertEquals( fich4, lr3.getFic(1) );
		lr3.intercambia( 1, 8 );  // Comprobamos el no error al fallar
		assertEquals( fich4, lr3.getFic(1) );  // comprobamos no haber cambiado nada en el anterior
	}

	// Chequeo de añadido y borrado de elementos
	@Test
	public void addremove() { 
		// no tiene sentido ahcer dos donde añades y eliminas, ya que son independientes 
		//los procesos, lo más efectivo es hacerlos ambos a la vez
		
		lr3.add( null );	//añadimos null al final
		assertEquals( lr3.getFic(5), null ); //  comprovamos si es null
		lr3.removeFic(5); //eliminamos el elemento 5 (6)
		assertEquals( lr3.size(), 5 ); //comprobamos su actual tamaño
	}
	
	// add de una carpeta
	@Test public void addCarpeta() {
		String carpetaTest = "test/res/";
		String filtroTest = "*Pentatonix*.mp4";
		ListaDeReproduccion lr = new ListaDeReproduccion();
		lr.add( carpetaTest, filtroTest );
//		fail( "Método sin acabar" );
		
		// Comrpobamos si hay 3 elementos
		assertEquals( lr.size(), 3 );
		// Comrppbamos si los elementos son los que realmente cumplen
		File[] fics = new File[] { FIC_TEST2, FIC_TEST3, FIC_TEST4 };
		assertTrue( Arrays.asList(fics).contains( lr.getFic(0) ) );
		assertTrue( Arrays.asList(fics).contains( lr.getFic(1) ) );
		assertTrue( Arrays.asList(fics).contains( lr.getFic(2) ) );
		lr.clear();
		// Comprobar que la lista se finaliza bien
		assertEquals( 0, lr.size() );
		}
	
	@Test
	public void remove() {
		lr3.removeFic(4);
		assertEquals( lr3.size(), 4 );
	}
	
//	Chequeo random
	@Test
	public void numeroRandom() {
		assertEquals(lr3.ficheroEnCurso, -1);
		assertTrue(lr3.irARandom());
		for(int i = 0; i < 10; i++) {
			lr3.irARandom();
			assertTrue(lr3.ficheroEnCurso >= 0 && lr3.ficheroEnCurso <= lr3.ficherosLista.size()-1);
		}
	}
	
	// Chequeo de tamaño
	@Test
	public void size() {
		assertEquals( lr1.size(), 0 );
		assertEquals( lr2.size(), 1 );
		assertEquals( lr3.size(), 5 );
	}
}
