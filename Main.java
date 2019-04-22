package pricewatcher.base;

import java.awt.*;

import java.awt.event.ActionEvent;
import java.io.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * A dialog for tracking the price of an item.
 *
 * @author Javier Soon and Matthew Iglesias
 * @ID 80436654 and 80591632
 */
@SuppressWarnings("serial")
public class Main extends JFrame {

	/** Default dimension of the dialog. */
	private final static Dimension DEFAULT_SIZE = new Dimension(400, 300);

	/** Special panel to display the watched item. */
	private ItemView itemView;
//	private Toolbar toolBar;
	/** Message bar to display various messages. */
	private JLabel msgBar = new JLabel(" ");
	private Item item;
	private Item item2;

	private JList<Item> itemList;
	
	
	/**
	 * Create a new dialog.
	 * 
	 */
	public Main() {
		this(DEFAULT_SIZE);
	}

	/**
	 * Create a new dialog of the given screen dimension.
	 * 
	 */
	public Main(Dimension dim) {
		super("Price Watcher");
		setSize(dim);

		item = new Item();
		item.setName("Marvel's Spider-Man - PlayStation 4");
		item.setUrl(
				"https://www.amazon.com/Marvels-Spider-Man-PlayStation-4/dp/B01GW8YDLK/ref=sr_1_1?ie=UTF8&qid=1549050988&sr=8-1&keywords=spiderman%2Bgame&th=1");
		item.setInitialPrice((PriceFinder.priceFinder1()));

		item2 = new Item();
		item2.setName("Alphonse Figure");
		item2.setUrl(
				"https://otakumode.com/shop/596ecc1c47bf74901e1ac932/Nendoroid-Fullmetal-Alchemist-Alphonse-Elric");
		item2.setInitialPrice(PriceFinder.priceFinder3());

		// create the model and adds elements
		DefaultListModel<Item> listModel = new DefaultListModel<>();
		listModel.addElement(item);
		listModel.addElement(item2);

		// create the list
		itemList = new JList<>(listModel);

		configureUI();
		JToolBarUI();
		
		

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);

	}

	/**
	 * Callback to be invoked when the refresh button is clicked. Find the current
	 * price of the watched item and display it along with a percentage price
	 * change.
	 */
	private void refreshButtonClicked(ActionEvent event) {

		item.setCurrentPrice(PriceFinder.priceFinder2());
		itemView.repaint();
	}

	/**
	 * Callback to be invoked when the view-page icon is clicked. Launch a (default)
	 * web browser by supplying the URL of the item.
	 */
	private void viewPageClicked() {

		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
			String url = itemView.item.getUrl();
			try {
				Desktop.getDesktop().browse(java.net.URI.create(url));
			} catch (NullPointerException e) {
				System.out.println("Url does not exist.");

			} catch (UnsupportedOperationException ee) {
				// TODO Auto-generated catch block
				System.out.println("Current platform is not supported.");

			} catch (SecurityException eee) {
				System.out.println("Do not have security clearance.");

			} catch (IOException eeee) {
				System.out.println("Could not launch");
			}
		}
	}

	/** Configure UI. */
	private void configureUI() {
		setLayout(new BorderLayout());		
		
		JPanel control = makeControlPanel();
		JPanel toolbar = JToolBarUI();
		control.setBorder(BorderFactory.createEmptyBorder(10, 16, 0, 16));
		add(control, BorderLayout.NORTH);
		JPanel board = new JPanel();
		board.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 16, 0, 16),
				BorderFactory.createLineBorder(Color.GRAY)));
		board.setLayout(new GridLayout(1, 1));
//		itemView = new ItemView(item);
//
//		itemView.setClickListener(this::viewPageClicked);
//		board.add(itemView);
		add(new JScrollPane(itemList));
		board.add(itemList);
		itemList.setCellRenderer(new ItemRenderer());
		
		getContentPane().add(toolbar, BorderLayout.NORTH);
		
		add(board, BorderLayout.CENTER);
		msgBar.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 0));
		add(msgBar, BorderLayout.SOUTH);
	}
	
	
	ImageIcon openIcon = new ImageIcon(Main.class.getResource("/image/blue question.png"));
	ImageIcon saveIcon = new ImageIcon(Main.class.getResource("/image/ebay.png"));
	ImageIcon newIcon = new ImageIcon(Main.class.getResource("/image/blue settings.png"));
	
	Action openAction = new AbstractAction("About", openIcon) {
		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(null, "PriceWatcher, version 13.1");
			System.out.println("Opening About");
		}
	};
	Action saveAction = new AbstractAction("Save", saveIcon) {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Save File");
		}
	};
	Action newAction = new AbstractAction("New", newIcon) {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("New File");
		}
	};
	
	private JPanel JToolBarUI() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JMenuItem openMenuItem = new JMenuItem(openAction);
		JMenuItem saveMenuItem = new JMenuItem(saveAction);
		JMenuItem newMenuItem = new JMenuItem(newAction);

		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		fileMenu.add(openMenuItem);
		fileMenu.add(saveMenuItem);
		fileMenu.add(newMenuItem);
		menuBar.add(fileMenu);

		JToolBar toolBar = new JToolBar();
		panel.add(toolBar);
		
		toolBar.add(Box.createHorizontalGlue());
		toolBar.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
		toolBar.add(newAction);
		toolBar.add(openAction);
		toolBar.add(saveAction);
		
		return panel;
	}

	/** Create a control panel consisting of a refresh button. */
	private JPanel makeControlPanel() {		
		
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		JButton refreshButton = new JButton("Refresh Check prices");
		refreshButton.setFocusPainted(false);
		refreshButton.addActionListener(this::refreshButtonClicked);
		panel.add(refreshButton);
		return panel;
	}

	public static void main(String[] args) throws IOException {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Main();
			}
		});
	}
}
