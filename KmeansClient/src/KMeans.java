import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


/**
 * La classe definisce un'applet visualizzata con appletviewer. L'applet e' suddivisa in due 
 * sezioni organizzate in tab.
 * La prima identificata dal tab DATABASE con relativa icona, si occupa di effettuare richieste al server
 * per l'esecuzione di un'attivita' di scoperta dei cluster sulla base di dati, eseguendo 
 * l'algoritmo kmeans.
 * In questo caso l'applet rimarra' in attesa dei risultati da parte del server, mostrando (se 
 * tutto procede correttamente) il risultato dell'algoritmo kmeans, altrimenti
 * verra' mostrato un messaggio nel caso di errore.
 * La corretta esecuzione dell'algoritmo in questo caso produrra' inoltre su server un file
 * il cui nome corrispondera' a quello indicato nell'apposita casella di testo.
 * La seconda sezione e' identificata dal tab FILE si occupa di effettuare richieste al 
 * server per effettuare una lettura da file contenente attivita' precendenti di scoperta dei
 * cluster sul database. Il nome del file segue la stessa logica precendente e nel caso in cui tale 
 * file esiste verranno mostrati a video rispettivamente, i risultati dell'algoritmo kmeans.
 * In entrambe le tipologie di richieste nel momento in cui esse vengono effettuate e, nel caso in cui 
 * tutto proceda correttamente, verra' mostrata una finestra di dialogo che comunichera' le avvenute operazioni.
 * 
 * @author Veronico, Mazzone, Sgaramella
 * 
 */
@SuppressWarnings("serial")
public class KMeans extends JApplet {

	/**
	 * Stream di output che permette al client (l'applet) di inviare le richieste al server
	 */
	private ObjectOutputStream out;
	
	/**
	 * Stream di input che permette al client (l'applet) di ricevere i risultati da parte del server 
	 */
	private ObjectInputStream in;
	
	/**
	 * Stringa che indica l'IP del server (in questo caso locale)
	 */
	private static final String SERVER = "127.0.0.1";
	
	/**
	 * Intero che indica la porta di connessione (in questo caso di default)
	 */
	private static final int PORT = 8080;
	
	/**
	 * Variabile di tipo InetAddress (inizializzata a null) che indichera' l'indirizzo effettivo di connessione
	 */
	private static InetAddress addr = null;
	
	/**
	 * Variabile di tipo Socket importata dalla libreria java.net che permettera' di inizializzare la connessione client-server
	 */
	private static Socket socket;
	
	/**
	 * Variabile di tipo TabbedPane che permette di istanziare le componenti grafiche dell'applet
	 */
	private TabbedPane tab;

	/**
	 * Metodo che inizializza la componente grafica
	 */
	public void init() {
		tab = new TabbedPane();
		getContentPane().setLayout(new GridLayout(1,1));
		getContentPane().add(tab);	
	}

	/**
	 * Classe che estende JPanel e definisce la struttura di TabbedPane
	 */
	class TabbedPane extends JPanel {

		/**
		 * Tab per l'utilizzo delle funzionalit&agrave su database
		 */
		private JPanelCluster panelDB; 
		
		/**
		 * Tab per l'utilizzo delle funzionalit&agrave su file
		 */
		private JPanelCluster panelFile;

		/**
		 * Costruttore di classe che inizializza i membri panelDB e panelFile e li aggiunge ad un oggetto istanza della classe TabbedPane
		 */
		@SuppressWarnings("deprecation")
		TabbedPane() {
			super(new GridLayout(1,1));
			JTabbedPane jp = new JTabbedPane();
			panelDB = new JPanelCluster("Scopri cluster", new DBActionListener());			
			panelFile = new JPanelCluster("Carica dati", new FileActionListener());
			ImageIcon iconDB = new ImageIcon("../iconDB.jpg");
			ImageIcon iconFile = new ImageIcon("../iconFile.png");
			jp.addTab("Database", iconDB, panelDB, "Database");
			jp.addTab("File", iconFile, panelFile, "File");
			this.add(jp);			
			panelFile.kText.hide();
		    panelFile.tableText.hide();
			panelFile.kLabel.hide();
			panelFile.tableLabel.hide();
			panelDB.fileLabel.setText("Salva in");
			panelFile.fileLabel.setText("Carica da");
			jp.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		}

		/**
		 * Metodo che acquisisce il nome del file da caricare e visualizza il contenuto del file
		 * @throws SocketException
		 * @throws IOException
		 * @throws ClassNotFoundException
		 */
		private void learningFromFileAction() throws SocketException, IOException, ClassNotFoundException {
			try {
				addr = InetAddress.getByName(SERVER);
				System.out.println("Connessione con "+addr+" effettuata");
				socket = new Socket(addr, PORT);
				out = new ObjectOutputStream(socket.getOutputStream());
				in = new ObjectInputStream(socket.getInputStream());			
			}catch(UnknownHostException ex) {
				System.err.println("Errore nella connessione con il server");
			} catch (IOException e) {
				System.err.println("Errore instanziazione degli stream");
			}
			out.writeInt(1);
			String fileName = "";
			fileName = panelFile.fileText.getText();
			if(fileName.equals("")) {
				JOptionPane.showMessageDialog(this, "Nome file di salvataggio non inserito", "Attenzione", JOptionPane.ERROR_MESSAGE);
				return;
			}
			out.writeObject(fileName);
			String OKmsg = in.readObject().toString();
			if(OKmsg.equals("OK")) {
				JOptionPane.showMessageDialog(this, "File caricato con successo", "Avviso", JOptionPane.INFORMATION_MESSAGE);
			}
			else
			{
				JOptionPane.showMessageDialog(this, "Il File non è stato trovato", "Attenzione", JOptionPane.ERROR_MESSAGE);
				socket.close();
				return;
			}
			String StringCluster = in.readObject().toString();
			panelFile.fileText.setText("");
			panelFile.clusterOutput.setText(StringCluster);
			socket.close();
		}


		/**
		 * Metodo che acquisisce il nome della tabella, il numero dei cluster e il nome del file in cui salvare la clusterizzazione e visualizza i risultati
		 * @throws SocketException
		 * @throws IOException
		 * @throws ClassNotFoundException
		 */
		private void learningFromDBAction() throws SocketException, IOException, ClassNotFoundException {
			try {
				addr = InetAddress.getByName(SERVER);
				System.out.println("Connessione con "+addr+" effettuata" );
				socket = new Socket(addr, PORT);
				out = new ObjectOutputStream(socket.getOutputStream());
				in = new ObjectInputStream(socket.getInputStream());			
			}catch(UnknownHostException ex) {
				System.err.println("Errore nella connessione con il server");
				return;
			} catch (IOException e) {
				System.err.println("Errore instanziazione degli stream");
				return;
			}
			out.writeInt(2);
			int k = 0;	
			try{
				k = new Integer(panelDB.kText.getText()).intValue();
			}
			catch(NumberFormatException e){
				JOptionPane.showMessageDialog(this, "Inserire un valore numerico valido", "Attenzione", JOptionPane.ERROR_MESSAGE);
				return;
			}
			String tableName = "";
			String result = "";
			String fileName = "";
			tableName = panelDB.tableText.getText();
			if(tableName.equals("")) {
				JOptionPane.showMessageDialog(this, "Nome tabella non inserito", "Attenzione", JOptionPane.ERROR_MESSAGE);
				return;
			}
			fileName = panelDB.fileText.getText();
			if(fileName.equals("")) {
				JOptionPane.showMessageDialog(this, "Nome file di salvataggio non inserito", "Attenzione", JOptionPane.ERROR_MESSAGE);
				return;
			}
			out.writeObject(k);
			out.writeObject(tableName);
			out.writeObject(fileName);
			String msg = in.readObject().toString();
			if(!msg.equals("OK"))
			{
				JOptionPane.showMessageDialog(this, "Nome tabella errato", "Attenzione", JOptionPane.ERROR_MESSAGE);
				return;
			}
			String NOmsg = in.readObject().toString();
			if(NOmsg.equals("NO"))
			{
				JOptionPane.showMessageDialog(this, "Il numero di cluster inserito non è valido", "Attenzione", JOptionPane.ERROR_MESSAGE);
				return;
			}
			result = in.readObject().toString();
			panelDB.clusterOutput.setText(result);
			panelDB.fileText.setText("");
			panelDB.kText.setText("");
			panelDB.tableText.setText("");
			String OKmsg = in.readObject().toString();
			if(OKmsg.equals("OK")) {
				JOptionPane.showMessageDialog(this, "Clusterizzazione e salvataggio terminati con successo", "Avviso", JOptionPane.INFORMATION_MESSAGE);
			}
			socket.close();
		}


		/**
		 * Classe che estende JPanel e definisce la struttura grafica del tab
		 */
		class JPanelCluster extends JPanel {

			private JTextField tableText = new JTextField(20);
			private JTextField kText = new JTextField(10);
			private JTextField fileText = new JTextField(20);
			private JTextArea clusterOutput = new JTextArea();
			private JButton executeButton;

			private JPanel upPanel;
			private JPanel centralPanel;
			private JPanel downPanel;

			private JLabel tableLabel;
			private JLabel kLabel;
			private JLabel fileLabel;
			private JLabel OutputLabel;

			/**
			 * Costruttore di classe che inizializza tutte le componenti grafico del pannello e aggiunge l'ascoltatore al bottone.
			 * @param buttonName Nome bottone.
			 * @param a Identificatore del Listener.
			 */
			JPanelCluster(String buttonName, java.awt.event.ActionListener a) {
				setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
				this.upPanel = new JPanel(new FlowLayout());
				this.centralPanel = new JPanel(new BorderLayout(1,1));
				this.downPanel = new JPanel(new FlowLayout());
				this.kLabel = new JLabel("N. cluster");
				this.tableLabel = new JLabel("Nome tabella");
				this.fileLabel = new JLabel();
				this.OutputLabel = new JLabel("Risultato");

				upPanel.add(tableLabel);
				upPanel.add(tableText);
				upPanel.add(kLabel);
				upPanel.add(kText);
				upPanel.add(fileLabel);
				upPanel.add(fileText);
				add(upPanel);

				clusterOutput.setEditable(false);
				JScrollPane scrollingArea = new JScrollPane(clusterOutput);
				centralPanel.add(OutputLabel, BorderLayout.NORTH);
				centralPanel.add(scrollingArea, BorderLayout.CENTER);
				add(centralPanel);
				this.executeButton = new JButton(buttonName);
				this.executeButton.addActionListener(a);
				downPanel.add(executeButton);
				add(downPanel);
			}


		}

		/**
		 * Classe che determina il comportamente del bottone al suo click
		 */
		class DBActionListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					learningFromDBAction();
				} catch (SocketException e1) {
					System.err.println("Errore SocketException");
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					System.err.println("Errore ClassNotFoundException");
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}

		}

		/**
		 * Classe che determina il comportamente del bottone al suo click
		 */
		class FileActionListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					learningFromFileAction();
				} catch (SocketException e1) {
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					System.err.println("Errore ClassNotFoundException");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}

	}

}
