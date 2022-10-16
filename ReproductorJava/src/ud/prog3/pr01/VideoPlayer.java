package ud.prog3.pr01;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.hamcrest.core.IsNull;

import java.io.File;

import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

/** Ventana principal de reproductor de vídeo
 * Utiliza la librería VLCj que debe estar instalada y configurada
 *     (http://www.capricasoftware.co.uk/projects/vlcj/index.html)
 * @author Andoni Eguíluz Morán
 * Facultad de Ingeniería - Universidad de Deusto
 */
public class VideoPlayer extends JFrame {
	private static final long serialVersionUID = 1L;
	
	// Varible de ventana principal de la clase
	private static VideoPlayer miVentana;

	// Atributo de VLCj
	private EmbeddedMediaPlayerComponent mediaPlayerComponent;
	// Atributos manipulables de swing
	private JList<String> lCanciones = null;  // Lista vertical de vídeos del player
	private JProgressBar pbVideo = null;      // Barra de progreso del vídeo en curso
	private JCheckBox cbAleatorio = null;     // Checkbox de reproducción aleatoria
	private JLabel lMensaje = null;           // Label para mensaje de reproducción
	private JFileChooser fcElegirCarpeta = null;
	// Datos asociados a la ventana
	private ListaDeReproduccion listaRepVideos;  // Modelo para la lista de vídeos

	public VideoPlayer() {
		// Creación de datos asociados a la ventana (lista de reproducción)
		listaRepVideos = new ListaDeReproduccion();
		// Creación de componentes/contenedores de swing
		lCanciones = new JList<String>( listaRepVideos );
		pbVideo = new JProgressBar( 0, 10000 );
		cbAleatorio = new JCheckBox("Rep. aleatoria");
		lMensaje = new JLabel( "" );
		JPanel pBotonera = new JPanel();
		JButton bAnyadir = new JButton( new ImageIcon( VideoPlayer.class.getResource("img/Button Add.png")) );
		bAnyadir.setRolloverIcon(new ImageIcon( VideoPlayer.class.getResource("img/Button Add" + "-RO" + ".png")));
		bAnyadir.setPressedIcon(new ImageIcon( VideoPlayer.class.getResource("img/Button Add" + "-CL" + ".png")));
		JButton bAtras = new JButton( new ImageIcon( VideoPlayer.class.getResource("img/Button Rewind.png")) );
		bAtras.setRolloverIcon(new ImageIcon( VideoPlayer.class.getResource("img/Button Rewind" + "-RO" + ".png")));
		bAtras.setPressedIcon(new ImageIcon( VideoPlayer.class.getResource("img/Button Rewind" + "-CL" + ".png")));
		JButton bPausaPlay = new JButton( new ImageIcon( VideoPlayer.class.getResource("img/Button Play Pause.png")) );
		bPausaPlay.setRolloverIcon(new ImageIcon( VideoPlayer.class.getResource("img/Button Play Pause" + "-RO" + ".png")));
		bPausaPlay.setPressedIcon(new ImageIcon( VideoPlayer.class.getResource("img/Button Play Pause" + "-CL" + ".png")));		
		JButton bAdelante = new JButton( new ImageIcon( VideoPlayer.class.getResource("img/Button Fast Forward.png")) );
		bAdelante.setRolloverIcon(new ImageIcon( VideoPlayer.class.getResource("img/Button Fast Forward" + "-RO" + ".png")));
		bAdelante.setPressedIcon(new ImageIcon( VideoPlayer.class.getResource("img/Button Fast Forward" + "-CL" + ".png")));		
		JButton bMaximizar = new JButton( new ImageIcon( VideoPlayer.class.getResource("img/Button Maximize.png")) );
		bMaximizar.setRolloverIcon(new ImageIcon( VideoPlayer.class.getResource("img/Button Maximize" + "-RO" + ".png")));
		bMaximizar.setPressedIcon(new ImageIcon( VideoPlayer.class.getResource("img/Button Maximize" + "-CL" + ".png")));		
		// Componente de VCLj
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();

		// Configuración de componentes/contenedores
		setTitle("Video Player - Deusto Ingeniería");
		setLocationRelativeTo( null );  // Centra la ventana en la pantalla
		setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		setSize( 800, 600 );
		lCanciones.setPreferredSize( new Dimension( 200,  500 ) );
		pBotonera.setLayout( new FlowLayout( FlowLayout.LEFT ));
		
		// Enlace de componentes y contenedores
		pBotonera.add( bAnyadir );
		botonConfiguracion(bAnyadir);
		pBotonera.add( bAtras );
		botonConfiguracion(bAtras);
		pBotonera.add( bPausaPlay );
		botonConfiguracion(bPausaPlay);
		pBotonera.add( bAdelante );
		botonConfiguracion(bAdelante);
		pBotonera.add( bMaximizar );
		botonConfiguracion(bMaximizar);
		pBotonera.add( cbAleatorio );
		pBotonera.add( lMensaje );
		getContentPane().add( mediaPlayerComponent, BorderLayout.CENTER );
		getContentPane().add( pBotonera, BorderLayout.NORTH );
		getContentPane().add( pbVideo, BorderLayout.SOUTH );
		getContentPane().add( new JScrollPane( lCanciones ), BorderLayout.WEST );
		
		// Escuchadores
		bAnyadir.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File fPath = new File(pedirCarpeta());
				if (fPath==null) return;
				path = fPath.getAbsolutePath();
				// TODO: pedir ficheros por ventana de entrada (JOptionPane)
				Object ficheros = JOptionPane.showInputDialog(
						null, 
						"Escriba el nombre de los ficheros",
						"Entrada",
						JOptionPane.QUESTION_MESSAGE,null,null, "*Pentatonix*.mp4");
				
				// ficheros = ...
				listaRepVideos.add( path, ficheros+"" );
				lCanciones.repaint();
			}
		});
		bAtras.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				paraVideo();
				if(cbAleatorio.isSelected()) {
					listaRepVideos.irARandom();
				}else {listaRepVideos.irAAnterior();}
				lanzaVideo();
			}
		});
		bAdelante.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				paraVideo();
				if(cbAleatorio.isSelected()) {
					listaRepVideos.irARandom();
				}else { listaRepVideos.irASiguiente();}
				lanzaVideo();
			}
		});
		bPausaPlay.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (mediaPlayerComponent.mediaPlayer().status().isPlayable()) {
					if (mediaPlayerComponent.mediaPlayer().status().isPlaying())
						mediaPlayerComponent.mediaPlayer().controls().pause();
					else
						mediaPlayerComponent.mediaPlayer().controls().play();
				} else {
					lanzaVideo();
				}
			}
		});
		bMaximizar.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (mediaPlayerComponent.mediaPlayer().fullScreen().isFullScreen())
			        mediaPlayerComponent.mediaPlayer().fullScreen().set(false);
				else
					mediaPlayerComponent.mediaPlayer().fullScreen().set(true);
			}
		});
		lCanciones.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent evt) {
		        if (evt.getClickCount() == 2) {

		            // Double-click detected
		        	paraVideo();
		            listaRepVideos.irA(lCanciones.getSelectedIndex());
		            lanzaVideo();
		        }
		    }
		});
		
		
		
		addWindowListener( new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				mediaPlayerComponent.mediaPlayer().controls().stop();
				mediaPlayerComponent.mediaPlayer().release();
			}
		});
		mediaPlayerComponent.mediaPlayer().events().addMediaPlayerEventListener( 
			new MediaPlayerEventAdapter() {
				@Override
				public void finished(MediaPlayer mediaPlayer) {
					listaRepVideos.irASiguiente();
					lanzaVideo();
				}
				@Override
				public void error(MediaPlayer mediaPlayer) {
					listaRepVideos.irASiguiente();
//					lanzaVideo();
					lCanciones.repaint();
				}
			    @Override
			    public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
					pbVideo.setValue( (int) ( 10000.0 * 
							mediaPlayerComponent.mediaPlayer().status().time() /
							mediaPlayerComponent.mediaPlayer().status().length() ) );
					pbVideo.repaint();
			    }
		});
		
		pbVideo.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mediaPlayerComponent.mediaPlayer().controls().setTime( 
					mediaPlayerComponent.mediaPlayer().status().length() * e.getX() / pbVideo.getWidth() );
			}
		});
	}

	//
	// Métodos sobre el player de vídeo
	//
	
	// Para la reproducción del vídeo en curso
	private void paraVideo() {
		if (mediaPlayerComponent.mediaPlayer()!=null)
			mediaPlayerComponent.mediaPlayer().controls().stop();
	}

	// Empieza a reproducir el vídeo en curso de la lista de reproducción
	private void lanzaVideo() {
		if (mediaPlayerComponent.mediaPlayer()!=null &&
			listaRepVideos.getFicSeleccionado()!=-1) {
			File ficVideo = listaRepVideos.getFic(listaRepVideos.getFicSeleccionado());
			mediaPlayerComponent.mediaPlayer().media().play(  
				ficVideo.getAbsolutePath() );
			lCanciones.setSelectedIndex( listaRepVideos.getFicSeleccionado() );
		} else {
			lCanciones.setSelectedIndices( new int[] {} );
		}
	}
	
	// Pide interactivamente una carpeta para coger vídeos
	// (null si no se selecciona)
	private static String pedirCarpeta() {
		// TODO: Pedir la carpeta usando JFileChooser
		JFileChooser fileChooser = new JFileChooser();
		
		fileChooser.setCurrentDirectory(new File("H:\\git\\practica1temporal\\ReproductorJava\\test"));
	    fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	    
	    int result = fileChooser.showOpenDialog(null);

	    if (result != JFileChooser.CANCEL_OPTION) {

	        File fileName = fileChooser.getSelectedFile();
	        
	        if(fileName != null) {
	        	return(fileName+"");
	        }
	    }
		return null;
	}

		private static String ficheros;
		private static String path;
	/** Ejecuta una ventana de VideoPlayer.
	 * El path de VLC debe estar en la variable de entorno "vlc".
	 * Comprobar que la versión de 32/64 bits de Java y de VLC es compatible.
	 * @param args	Un array de dos strings. El primero es el nombre (con comodines) de los ficheros,
	 * 				el segundo el path donde encontrarlos.  Si no se suministran, se piden de forma interactiva. 
	 */
	public static void main(String[] args) {
		// Para probar carga interactiva descomentar o comentar la línea siguiente:
		args = new String[] { "*Pentatonix*.mp4", "test/res/" };
		if (args.length == 0) { 
			Object fichero = JOptionPane.showInputDialog(
					null, 
					"Escriba el nombre de los ficheros",
					"Entrada",
					JOptionPane.QUESTION_MESSAGE,null,null, "*Pentatonix*.mp4");
			
			ficheros = fichero + "";
			args = new String[] {ficheros,pedirCarpeta()};
			}
		
		if (args.length < 2) {
			// No hay argumentos: selección manual
			File fPath = new File(pedirCarpeta());
			if (fPath==null) return;
				path = fPath.getAbsolutePath();
				// TODO : Petición manual de ficheros con comodines (showInputDialog)
				Object fichero = JOptionPane.showInputDialog(
						null, 
						"Escriba el nombre de los ficheros",
						"Entrada",
						JOptionPane.QUESTION_MESSAGE,null,null, "*Pentatonix*.mp4");
				
				ficheros = fichero + "";
		} else {
			ficheros = args[0];
			path = args[1];

			System.out.println(path);
		}
		
		// Inicializar VLC.
		// Probar con el buscador nativo...
		boolean found = new NativeDiscovery().discover();
    	// System.out.println( LibVlc.INSTANCE.libvlc_get_version() );  // Visualiza versión de VLC encontrada
    	// Si no se encuentra probar otras opciones:
    	if (!found) {
			// Buscar vlc como variable de entorno
			String vlcPath = System.getenv().get( "vlc" );
			if (vlcPath==null) {  // Poner VLC a mano
	        	System.setProperty("jna.library.path", "\"H:\\\\libs\\\\vlcj-4.7.1\"");
			} else {  // Poner VLC desde la variable de entornok/co/caprica/vlcj/player/component/EmbeddedMediaPlayerComponent
				System.setProperty( "jna.library.path", vlcPath );
			}
		}
    	
    	// Lanzar ventana
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				miVentana = new VideoPlayer();
				// Descomentar esta línea y poner una ruta válida para ver un vídeo de ejemplo
				miVentana.listaRepVideos.ficherosLista.add( new File("test/res/[Official Video] Daft Punk - Pentatonix.mp4") );				
				miVentana.setVisible( true );
				miVentana.listaRepVideos.add( path, ficheros );
				miVentana.listaRepVideos.irAPrimero();
				miVentana.lanzaVideo();
			}
		});
	}
	
	//	Configuraci�n boton
	public void botonConfiguracion(JButton boton) {
		boton.setRolloverEnabled(true);
		boton.setFocusPainted(false);
		boton.setOpaque(false);
		boton.setContentAreaFilled(false);
		boton.setBorderPainted(false);
//		boton.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
	}
	
}
