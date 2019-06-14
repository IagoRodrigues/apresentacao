package reversi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import reversi.Reversi;

@SuppressWarnings("serial")
public class ReversiGui extends JPanel {

	JPanel panel;
	public static JPanel boardPanel;
	static JLabel score1;
	static JLabel score2;
	static JButton novoJogo;
	static JButton volta;
	static JButton[] cell;
	static JRadioButton pvp;
	static JRadioButton pvm;
	static JRadioButton nivel1;
	static JRadioButton nivel2;
	static JRadioButton nivel3;
	static public Reversi board;
	static public ArrayList<Reversi> arrReversi = new ArrayList<Reversi>();
	static int countUndo = 0;
	public static JPanel dificuldade; 

	static public int playerScore = 2;
	static public int pcScore = 2;
	private static Reversi start;
	private static int rows = 8;
	private static int cols = 8;
	private static int dificuldadePVM;

	public static JLabel dark = new JLabel();
	public static JLabel light = new JLabel();
	public JLabel legalMove = new JLabel();
	public static Image imgDark;
	public static Image imglight;
	public static Image imgLegalMove;

	public ReversiGui() throws IOException {
		super(new BorderLayout());
		setPreferredSize(new Dimension(900, 700));
		setLocation(0, 0);

		try {
			imgDark = ImageIO.read(getClass().getResource("images/dark.png"));
		} catch (IOException ex) {
		}

		try {
			imglight = ImageIO.read(getClass().getResource("images/light.png"));
		} catch (IOException ex) {
		}
		try {
			imgLegalMove = ImageIO.read(getClass().getResource("images/legalMoveIcon.png"));
		} catch (IOException ex) {
		}

		dark.setIcon(new ImageIcon(imgDark));
		light.setIcon(new ImageIcon(imglight));
		legalMove.setIcon(new ImageIcon(imgLegalMove));

		iniciaBoard(this);
		IniciaScorePanel(this);

	}

	static class Action implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
					
			if(nivel1.isSelected()) {
				dificuldadePVM = 1;
			} else if (nivel2.isSelected()){
				dificuldadePVM = 2;
			}else if (nivel3.isSelected()) {
				dificuldadePVM = 3;
			}
			
			if (e.getSource() == novoJogo) {
				btnNovo();
			} else if (e.getSource() == volta) {
				btnVolta();
			} else {
				for (int i = 0; i < 64; i++) {
					if (e.getSource() == cell[i]) {
						int xCor, yCor;
						int ct = -100;
						int arr[] = new int[3];

						if (i == 0) {
							xCor = 0;
							yCor = 0;
						} else {
							yCor = i % 8;
							xCor = i / 8;
						}
						ct = board.play(xCor, yCor);
						if (ct == 0) {
							arrReversi.add(new Reversi(board));
							int k = 0;
							for (int row = 0; row < rows; row++) {
								for (int colum = 0; colum < cols; colum++) {
									if (board.gameCells[row][colum].getCor() == 'X') {
										cell[k].setIcon(new ImageIcon(imgDark));
										
									} else if (board.gameCells[row][colum].getCor() == 'O') {
										cell[k].setIcon(new ImageIcon(imglight));
										
									}
									k++;
								}
							}
							board.controlElements(arr);
							playerScore = arr[0];
							pcScore = arr[1];
							score1.setText("Player : " + playerScore + "  ");
							score2.setText("Computer : " + pcScore + "  ");
							
						}
						if (ct == 0 || ct == -1) {
							board.play(dificuldadePVM);
							arrReversi.add(new Reversi(board));
							ArrayList<Integer> arrList = new ArrayList<Integer>();
							int k = 0;
							for (int row = 0; row < rows; row++) {
								for (int colum = 0; colum < cols; colum++) {
									if (board.gameCells[row][colum].getCor() == 'X') {
												cell[k].setIcon(new ImageIcon(imgDark));
									} else if (board.gameCells[row][colum].getCor() == 'O') {
										cell[k].setIcon(new ImageIcon(imglight));
										
									} else if (board.gameCells[row][colum].getCor() == '.') {
										cell[k].setIcon(null);
									}
									k++;
								}
							}
							board.findLegalMove(arrList);
							for (int j = 0; j < arrList.size(); j += 2) {
											cell[arrList.get(j) * rows + arrList.get(j + 1)].setIcon(new ImageIcon(imgLegalMove));
								
							}
							board.controlElements(arr);
							playerScore = arr[0];
							pcScore = arr[1];
							score1.setText("Player : " + playerScore + "  ");
							score2.setText("Computer : " + pcScore + "  ");
							
						}
					}

				}
				finalizaJogo();
			}
		}
	}
	/**
	 * 
	 * Este metodo salva nick e pontos do usuario no arquivo para persistir os
	 * dados.
	 * 
	 */
	static void salvaPontos() {
		try {
			Pontuacao.getInstance().setPontuacao(new Jogador(JOptionPane.showInputDialog("Digite seu nick!"), playerScore));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	static void iniciaBoard(ReversiGui boardson) {

		board = new Reversi();
		start = board;
		arrReversi.add(new Reversi(board));

		// Board Panel
		boardPanel = new JPanel(new GridLayout(8, 8));
		cell = new JButton[64];
		int k = 0;
		for (int row = 0; row < rows; row++) {
			for (int colum = 0; colum < cols; colum++) {
				cell[k] = new JButton();
				cell[k].setSize(50, 50);
				cell[k].setBackground(Color.gray);
				cell[k].setBorder(BorderFactory.createLineBorder(Color.BLACK));
				if (board.gameCells[row][colum].getCor() == 'X') {
					cell[k].setIcon(new ImageIcon(imgDark));
				} else if (board.gameCells[row][colum].getCor() == 'O') {

					cell[k].setIcon(new ImageIcon(imglight));

				} else if (row == 2 && colum == 4 || row == 3 && colum == 5 || row == 4 && colum == 2
						|| row == 5 && colum == 3) {
					cell[k].setIcon(new ImageIcon(imgLegalMove));
				}
				cell[k].addActionListener(new Action());
				boardPanel.add(cell[k]);
				k++;
			}
		}
		boardson.add(boardPanel, BorderLayout.CENTER);

	}

	@SuppressWarnings("unchecked")
	static void IniciaScorePanel(ReversiGui Scoreson) throws IOException {

		JPanel scorePanel = new JPanel(new GridLayout(8, 1));

		scorePanel.setPreferredSize(new Dimension(310, 800));
		ButtonGroup grupo1 = new ButtonGroup();
		ButtonGroup grupo2 = new ButtonGroup();

		score1 = new JLabel();
		score1.setText(" Player : " + playerScore + "  ");
		score1.setFont(new Font("Serif", Font.BOLD, 22));

		score2 = new JLabel();
		score2.setText(" Computer : " + pcScore + "  ");
		score2.setFont(new Font("Serif", Font.BOLD, 22));

		novoJogo = new JButton();// Cria o Botao Novo Jogo. Para iniciar novo jogo
		novoJogo.setPreferredSize(new Dimension(80, 50)); // Define o tamaho do botao

		try // abre o try catch para buscar a imagem do botao
		{
			Image img = ImageIO.read(Scoreson.getClass().getResource("images/start.png"));// define um objeto do tipo
																							// imagem para
			// receber a imagem do botao de
			// iniciar
			novoJogo.setIcon(new ImageIcon(img));// seta o icon do botao como a imagem definido na linha anterior
		} catch (IOException ex) {
		} // lanca a exception
		novoJogo.addActionListener(new Action());// adiona o action Listener do botao

		volta = new JButton();// Cria o Botao volta. Para voltar o ultimo movimento
		volta.setPreferredSize(new Dimension(80, 50));// Define o tamaho do botão

		try // abre o try catch para buscar a imagem do botao
		{
			Image img2 = ImageIO.read(Scoreson.getClass().getResource("images/undo.png"));// define um objeto do tipo
																							// imagem para
			volta.setIcon(new ImageIcon(img2));// seta o icon do botao como a imagem definido na linha anterior
		} catch (IOException ex) {
		} // lanca a exception

		volta.addActionListener(new Action());// adiona o action Listener do botao

		pvp = new JRadioButton("Player Vs Player", false);
		pvm = new JRadioButton("Player Vs Computer", false);
		pvm.addActionListener(new ActionListener(){
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            dificuldade.setVisible(true);

	        }
	    });
		nivel1 = new JRadioButton("NIVEL FACIL");
		nivel2 = new JRadioButton("NIVEL MEDIO");
		nivel3 = new JRadioButton("NIVEL DIFICIL");

		JPanel radiosModo = new JPanel(new BorderLayout());
		dificuldade = new JPanel(new BorderLayout());
		JPanel botoes = new JPanel(new BorderLayout());
		JPanel score = new JPanel(new BorderLayout());
		JPanel PanelScore2 = new JPanel(new BorderLayout());
		JPanel ranking = new JPanel(new BorderLayout());

		radiosModo.setBorder(BorderFactory.createLineBorder(Color.black));

		ranking.setBorder(BorderFactory.createLineBorder(Color.black));

		@SuppressWarnings("rawtypes")
		DefaultListModel model = new DefaultListModel();
		
		for(int i=0; i < 4; i++) {
			model.addElement(Pontuacao.getInstance().getPontuacao(i)); //Mostra pontuacao, um item por linha
		}
		
		@SuppressWarnings("rawtypes")
		JList list = new JList(model);

		grupo2.add(nivel1);
		grupo2.add(nivel2);
		grupo2.add(nivel3);

		pvp.setSize(350, 100);
		pvm.setSize(350, 100);
		grupo1.add(pvp);
		grupo1.add(pvm);
// ---------------------------------------------------------------------
		radiosModo.add(pvp, BorderLayout.NORTH);
		radiosModo.add(pvm, BorderLayout.CENTER);
		scorePanel.add(radiosModo);
//----------------------------------------------------------------------        

		dificuldade.add(nivel1, BorderLayout.NORTH);
		dificuldade.add(nivel2, BorderLayout.CENTER);
		dificuldade.add(nivel3, BorderLayout.SOUTH);
		dificuldade.setVisible(false);
		scorePanel.add(dificuldade);
// -----------------------------------------------------------------------------
		botoes.add(novoJogo, BorderLayout.WEST);
		botoes.add(volta, BorderLayout.EAST);
		scorePanel.add(botoes);
//------------------------------------------------------------------------------

		score.add(dark, BorderLayout.WEST);
		score.add(score1, BorderLayout.CENTER);
		scorePanel.add(score);
//------------------------------------------------------------------------------

		PanelScore2.add(light, BorderLayout.WEST);
		PanelScore2.add(score2, BorderLayout.CENTER);
		scorePanel.add(PanelScore2);
//--------------------------------------------------------------------

		ranking.add(list, BorderLayout.CENTER);
		scorePanel.add(ranking);

		Scoreson.add(scorePanel, BorderLayout.EAST);

	}

	static void btnNovo() {
		board.reset();
		arrReversi.clear();
		arrReversi.add(new Reversi(start));
		int k = 0;
		for (int row = 0; row < rows; row++) {
			for (int colum = 0; colum < cols; colum++) {
				cell[k].setIcon(null);
				if (board.gameCells[row][colum].getCor() == 'X') {

					cell[k].setIcon(new ImageIcon(imgDark));

				} else if (board.gameCells[row][colum].getCor() == 'O') {

					cell[k].setIcon(new ImageIcon(imglight));

				}
				if (row == 2 && colum == 4 || row == 3 && colum == 5 || row == 4 && colum == 2
						|| row == 5 && colum == 3) {

					cell[k].setIcon(new ImageIcon(imgLegalMove));

				}
				++k;
			}
		}
		playerScore = 2;
		pcScore = 2;
		score1.setText(" Player : " + playerScore + "  ");
		score2.setText(" Computer : " + pcScore + "  ");
	}

	static void btnVolta() {

		countUndo++;
		int y;
		char c, x;
		int arr[] = new int[3];
		ArrayList<Integer> arrList = new ArrayList<Integer>();

		if (arrReversi.size() - countUndo - 1 >= 0) {
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					c = arrReversi.get(arrReversi.size() - countUndo - 1).gameCells[i][j].getCor();
					x = arrReversi.get(arrReversi.size() - countUndo - 1).gameCells[i][j].getCorX();
					y = arrReversi.get(arrReversi.size() - countUndo - 1).gameCells[i][j].getCorY();
					board.gameCells[i][j].setPosition(x, c, y);
				}
			}
			int k = 0;
			for (int row = 0; row < rows; row++) {
				for (int colum = 0; colum < cols; colum++) {
					cell[k].setIcon(null);
					if (board.gameCells[row][colum].getCor() == 'X') {
						cell[k].setIcon(new ImageIcon(imgDark));
					} else if (board.gameCells[row][colum].getCor() == 'O') {
						cell[k].setIcon(new ImageIcon(imglight));
					}
					++k;
				}
			}
			board.findLegalMove(arrList);
			for (int j = 0; j < arrList.size(); j += 2) {
				cell[arrList.get(j) * rows + arrList.get(j + 1)].setIcon(new ImageIcon(imgLegalMove));
			}
			board.controlElements(arr);
			playerScore = arr[0];
			pcScore = arr[1];
			score1.setText("Player : " + playerScore + "  ");
			score2.setText("Computer : " + pcScore + "  ");
		}

	}
	
	static void finalizaJogo() {
		int st = board.endOfGame();
		if (st == 0) {
			if (playerScore > pcScore) {
				JOptionPane.showMessageDialog(null, "No legal move!\nPlayer Win!", "Game Over",
						JOptionPane.PLAIN_MESSAGE);
				salvaPontos();
			} else {
				JOptionPane.showMessageDialog(null, "No legal move!\nComputer Win!", "Game Over",
						JOptionPane.PLAIN_MESSAGE);
				salvaPontos();
			}
		} else if (st == 1 || st == 3) {
			JOptionPane.showMessageDialog(null, "Computer Win!", "Game Over", JOptionPane.PLAIN_MESSAGE);
			salvaPontos();
		} else if (st == 2 || st == 4) {
			JOptionPane.showMessageDialog(null, "Player Win!", "Game Over", JOptionPane.PLAIN_MESSAGE);
			salvaPontos();
		} else if (st == 3) {
			JOptionPane.showMessageDialog(null, "Scoreless!", "Game Over", JOptionPane.PLAIN_MESSAGE);
			salvaPontos();
		}
	}
}