package chapter3;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class Controller extends JFrame implements KeyListener, ConstantValues,
		GameEventListener, ActionListener, MouseListener {

	/**
	 * 
	 */

	public static int alienCount = 0;
	public static int bossHealth = 25;
	protected static int bossCount = 0;
	
	private boolean ignoreBossCollision = false;
	public static boolean ignoreCollision = false;
	private boolean bossFight = false;

	
	private static final long serialVersionUID = 1L;
	private byte keys = 0x0;
	private Timer timer;
	private long timeStamp = System.currentTimeMillis();
	private Wall walls;
	private SpaceShip ship;
	@SuppressWarnings("unused")
	private MarchingAlien alien;

	private int rank = 5, file = 14;

	private enum GameState {
		Running, Paused, Stopped, Somesuch
	};

	private static GameState gameState = GameState.Stopped;
	private Canvas canvas;

	private ArrayList<Entity> entities = new ArrayList<>();

	private ArrayList<Entity> removeList = new ArrayList<>();
	private ArrayList<MarchingAlien> army = new ArrayList<>();
	private Graphics2D gc;
	private Rectangle somesuchButton = new Rectangle(100,100,20,20);

	public Controller() {

		super("Space Invaders");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		add(canvas);

		pack();
		canvas.createBufferStrategy(2);
		addKeyListener(this);
		setVisible(true);

		JMenuBar bar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		JMenuItem pause = new JMenuItem("Pause");
		JMenuItem about = new JMenuItem("About");
		JMenuItem exit = new JMenuItem("Exit");
		JMenuItem reStart = new JMenuItem("Restart");
		JMenuItem controls = new JMenuItem("Controls");

		fileMenu.add(controls);
		fileMenu.add(pause);
		fileMenu.add(about);
		fileMenu.add(exit);
		fileMenu.add(exit);

		exit.addActionListener(this);
		about.addActionListener(this);
		controls.addActionListener(this);
		pause.addActionListener(this);

		bar.add(fileMenu);

		setJMenuBar(bar);
		canvas.addMouseListener(this);
		
	}

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		
		super.paint(g);
	}
	
	public void actionPerformed(ActionEvent e) {
		String buttonString = e.getActionCommand();
		String message = "Message here";
		String title = "some title";

		if (buttonString.equals("Exit")) {
			if (JOptionPane.showConfirmDialog(this, "Are You Sure?",
					"Exit Game", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
				this.dispose();
				System.exit(0);
			}
		
		} else if (buttonString.equals("About")) {

			JOptionPane.showMessageDialog(this, "This is a game developed \n "
					+ "for my COMP 240 class \n "
					+ "kill all of the aliens to win \n"
					+ "if an alien hits your ship you lose", "About Game",
					JOptionPane.INFORMATION_MESSAGE);
		} else if (buttonString.equals("Controls")) {
			JOptionPane.showMessageDialog(this,
					"Press left arrow or A to move left \n"
							+ "Press right arrow or D to move right \n"
							+ "Press Space Bar to fire \n"
							+ "Press ESC to exit the game", "Controls Menu",
					JOptionPane.INFORMATION_MESSAGE);
		} else if (buttonString.equals("Pause")) {
			timer.stop();
			if (JOptionPane.showConfirmDialog(this, "Game Paused",
					"Pause Menu", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
				timer.start();
				timeStamp = System.currentTimeMillis();
			}

		}
	}		// TODO Auto-generated method stub


	public void addAliens(int rows, int cols) {

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				Rectangle bounds = getBounds();
				int alienWidth = SCREEN_WIDTH / 20;
				int alienHeight = SCREEN_HEIGHT / 20;

				MarchingAlien a = new MarchingAlien(this, new Dimension(
						alienWidth, alienHeight));
				a.setPosition(j * bounds.width / 20, i * bounds.height / 20);
				a.setVelocity(0.15f, 0);
				army.add(a);
				a.activate();
				alienCount++;
			}

		}
		entities.addAll(army);
	}

	public void addBoss() {
		
		bossHealth = 25;
		int alienWidth = SCREEN_WIDTH / 7;
		int alienHeight = SCREEN_HEIGHT / 7;

		BossAlien a = new BossAlien(this,
				new Dimension(alienWidth, alienHeight));
		a.setPosition(0, 0);
		a.setVelocity(0.5f, 0);
		a.activate();

		entities.add(a);

	}

	public void addEntity(Entity entity) {
		entities.add(entity);
		entity.setVelocity(0, 0);
	}

	public void start() {
		timer = new Timer(1000 / FPS, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				switch (gameState) {
				case Running:
					render();
					break;
				case Stopped:
					startScreen();
					break;
				case Paused:
					timer.stop();
					break;
				case Somesuch:
					//Do something
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					break;
				}
			}

		});
		timer.start();
		timeStamp = System.currentTimeMillis();

	}

	public void endScreenLose() {
		BufferStrategy strategy = canvas.getBufferStrategy();
		timer.stop();
		Graphics2D gc = (Graphics2D) strategy.getDrawGraphics();
		gc.setColor(Color.BLACK);
		gc.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

		gameState = GameState.Stopped;
		displayMessage("Drats! The invaders took over the earth!", gc,		// TODO Auto-generated method stub

				Color.RED, 0);
		displayMessage("Press enter to play again", gc,
				Color.RED, 15);
		gc.dispose();
		strategy.show();
		requestFocus();
	}

	private void endScreenWin() {
		BufferStrategy strategy = canvas.getBufferStrategy();
		timer.stop();
		Graphics2D gc = (Graphics2D) strategy.getDrawGraphics();
		gc.setColor(Color.BLACK);
		gc.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

		SoundFX.WIN.play();

		gameState = GameState.Stopped;
		displayMessage("Congrats! You beat the alien threat", gc, Color.BLUE, 0);
		displayMessage("The Earth will live another day!", gc, Color.BLUE, 15);
		displayMessage("Press enter to play again", gc, Color.BLUE, 30);
		gc.dispose();
		strategy.show();
		requestFocus();
	}

	public void startScreen() {

		BufferStrategy strategy = canvas.getBufferStrategy();

		Graphics2D gc = (Graphics2D) strategy.getDrawGraphics();
		gc.setColor(Color.BLACK);
		gc.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		requestFocus();
		gc.setColor(Color.red);
		gc.drawRect(somesuchButton.x, somesuchButton.y, somesuchButton.width, somesuchButton.height);
		displayMessage(
				"Press enter to start the game. Press esc to exit. Press P to pause.",
				gc, Color.WHITE, 0);
		gc.dispose();
		strategy.show();

	}

	private void render() {
		ignoreCollision = false;
		BufferStrategy strategy = canvas.getBufferStrategy();

		Graphics2D gc = (Graphics2D) strategy.getDrawGraphics();
		long elapsedTime = System.currentTimeMillis() - timeStamp;
		gc.setColor(Color.BLACK);
		gc.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.processKeys(keys);
			e.move(elapsedTime);
			for (int j = i + 1; j < entities.size(); j++) {
				Entity a = entities.get(j);
				if (e.inCollision(a)) {
					a.inCollision(e);
				}

			}
			e.draw(gc);
		}

		gc.dispose();
		entities.removeAll(removeList);
		removeList.clear();
		strategy.show();
		timeStamp = System.currentTimeMillis();

	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
		case KeyEvent.VK_W:
			keys |= UP_KEY;
			break;
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_A:
			keys |= LEFT_KEY;
			break;
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_S:
			keys |= DOWN_KEY;
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_D:
			keys |= RIGHT_KEY;
			break;
		case KeyEvent.VK_SPACE:
			keys |= SPACE_KEY;
			break;
		case KeyEvent.VK_ESCAPE:
			askExit();
			break;
		case KeyEvent.VK_ENTER:

			if (gameState == GameState.Running)
				return;

			SoundFX.STARTUP.play();

			if (gameState == GameState.Stopped) {
				gameState = GameState.Running;
				ship = new SpaceShip(this, new Dimension(SCREEN_WIDTH / 20,
						SCREEN_HEIGHT / 20));
				ship.setPosition(SCREEN_WIDTH / 2
						- (int) ship.getDimension().width / 2, SCREEN_HEIGHT
						- (int) ship.getDimension().height - 50);
				resetScreen();

				addEntity(ship);
				addAliens(rank, file);
				walls = new Wall(0, 0, new Dimension(SCREEN_WIDTH,
						SCREEN_HEIGHT), this);
				addEntity(walls);
				bossFight = false;
				
				timer.start();
				timeStamp = System.currentTimeMillis();
			}

			break;
		case KeyEvent.VK_P:
			if (gameState == GameState.Running)
				gameState = GameState.Paused;
			else
				gameState = GameState.Running;
			timer.start();
			timeStamp = System.currentTimeMillis();

		}
	}

	private void resetScreen() {
		entities.clear();
		army.clear();
		removeList.clear();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_SPACE:
			keys &= ~SPACE_KEY;
			break;
		case KeyEvent.VK_UP:
		case KeyEvent.VK_W:
			keys &= ~UP_KEY;
			break;
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_A:
			keys &= ~LEFT_KEY;
			break;
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_S:
			keys &= ~DOWN_KEY;
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_D:
			keys &= ~RIGHT_KEY;
			break;
		case KeyEvent.VK_ENTER:
			break;
		case KeyEvent.VK_ESCAPE:
			break;
		}

	}

	public void rePositionAliens() {
		Rectangle bounds = getBounds();
		int width = bounds.width;
		int height = bounds.height;
		Iterator<MarchingAlien> iterator = army.iterator();
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 15; j++) {
				MarchingAlien a = iterator.next();
				a.setPosition(j * width / 20, i * height / 20);
			}

		}
	}

	private void askExit() {
		timer.stop();
		if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(this,
				"Do you want to exit?", "Exit", JOptionPane.OK_CANCEL_OPTION)) {
			System.exit(0);
		}

		timer.start();
		timeStamp = System.currentTimeMillis();

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEndOfLife(Entity e) {
		// remove the missile
		removeList.add(e);
		if (e instanceof SpaceShip) {
			endScreenLose();
		}
		if (e instanceof MarchingAlien) {
			SoundFX.COLLIDE.play();
		}
		if (alienCount == 0 && !bossFight) {
			bossFight = true;
			addBoss();
			bossCount++;


		}
		if(e instanceof BossAlien){
			SoundFX.COLLIDE.play();
			if(bossHealth == 0)
				
				endScreenWin();
		}
	}

	@Override
	public void onFire(Entity e) {
		Missile missile = new Missile(this, new Dimension(SCREEN_WIDTH / 40,
				SCREEN_HEIGHT / 30));
		addEntity(missile);
		SoundFX.SHOOT.play();
		missile.setPosition(((ship.getRx() + (ship.dimension.width / 2) - 10)),
				ship.getRy() - missile.dimension.height); // test missile
															// position
	}

	@Override
	public void requestLogic(Entity e) {
		if (e instanceof MarchingAlien) {
			if (ignoreCollision)
				return;
			ignoreCollision = true;

			for (MarchingAlien a : army) {
				a.setPosition(a.getRx(), a.getRy() + 20);
				a.setVelocity(-a.getVx() * 1.07f, 0);
				if (a.getRy() == SCREEN_HEIGHT - a.getDimension().height) {
					endScreenLose();
				}

			}
		}
		if (e instanceof BossAlien) {
			e.setPosition(e.getRx(), e.getRy() + e.getDimension().height);
			e.setVelocity(-e.getVx(), 0);
			if(e.getRy() == SCREEN_HEIGHT - e.dimension.height)
				endScreenLose();
		}
	}

	@SuppressWarnings("unused")
	private static void displayMessage(String message, Graphics2D gc,
			Color color, int voffSet) {
		Color oldColor = gc.getColor();
		Font oldFont = gc.getFont();
		Font font = new Font("Courier New", Font.PLAIN, 16);
		gc.setFont(font);
		gc.setColor(color);
		int stringWidth = gc.getFontMetrics().stringWidth(message);
		gc.drawString(message, (SCREEN_WIDTH - stringWidth) / 2,
				(SCREEN_HEIGHT / 2) + voffSet);
		gc.setColor(oldColor);
		gc.setFont(oldFont);

	}

	public static void main(String[] args) {
		Controller gui = new Controller();
		gui.start();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (somesuchButton.contains(new Point(e.getX(), e.getY())))
			System.out.println("Button clicked!");
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}