package reversi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * 
 * Essa classe ira lidar com pontuacao.
 * Cabe a ela criar o arquivo com as pontucoes, ler esse arquivo para reclassificacao, apos isso,
 * sobreescrever o arquivo corretamente
 * 
 * */
public class Pontuacao {
	private Set<Jogador> dados = new TreeSet<>();
	private static Pontuacao instancia;
	
	/**
	 * 
	 * Adiciona um jogador com sua pontuacao no set
	 * @param jogador
	 * @return 
	 * @throws IOException
	 * 
	 */
	public void setPontuacao(Jogador jogador) throws IOException{
		adicionar(jogador);
		escrever();
		carregar();
		escreverFinal();
	}
	
	/**
	 * 
	 * @throws IOException
	 * Singletron da classe pontuacao
	 */
	private Pontuacao() throws IOException{
		escrever();
		carregar();
		escreverFinal();
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 * Get instance de Pontuacao
	 */
	public static Pontuacao getInstance() throws IOException {
		if(instancia == null) {
			instancia = new Pontuacao();
		}
		return instancia;
	}
	
	/**
	 * 
	 * @throws IOException
	 * Sobreescreve o arquivo com os jogadores em ordem decrescente de pontuacao
	 */
	private void escrever() throws IOException {
		Iterator<Jogador> iterator;
		iterator = dados.iterator();
		FileWriter fileWriter = null;
		PrintWriter printWriter = null;
		
		String caminho = new File("../reversi/pontuacao.csv").getCanonicalPath();
		
		fileWriter = new FileWriter(caminho, true);
		printWriter = new PrintWriter(fileWriter);
			
		while (iterator.hasNext()){
			printWriter.printf(iterator.next().toString());
		}
		
		printWriter.close();
		fileWriter.close();
	}

	/**
	 * 
	 * Adiciona um jogador com sua pontuacao no Set para que sua pontuacao apareca no arquivo
	 * @param jogador
	 * 
	 */
	private void adicionar(Jogador jogador) {
		if(jogador != null) {
			dados.add(jogador);
		}		
	}

	/**
	 * 
	 * Esse metodo carrega o arquivo criado para dentro do programa.
	 * Esse procedimento e importante para, quando um jogador superar a pontuacao de outro
	 * seja possivel reorganizar as pontuacoes dentro do arquivo
	 * @throws IOException 
	 * 
	 * */
	public void carregar() throws IOException {
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		String linha;
		
		String caminho = new File("../reversi/pontuacao.csv").getCanonicalPath();
		inputStream = new FileInputStream(caminho);

		inputStreamReader = new InputStreamReader(inputStream);
		bufferedReader = new BufferedReader(inputStreamReader);

		while((linha = bufferedReader.readLine()) != null){
			String[] attributes = linha.split(";");
			Jogador jogador = criarDados(attributes);

			dados.add(jogador);
		}

		bufferedReader.close();		
	}
	
	/**
	 * 
	 * Esse metodo cria um novo jogador a partir dos atributos recebidos do metodo carregar;
	 * @param attributes
	 * @return Jogador(pontos, nick);
	 * 
	 */
	private Jogador criarDados(String[] attributes) {
		String nick = attributes[0];
		int pontos = Integer.parseInt(attributes[1]);
		
		return new Jogador(nick, pontos);
	}
	
	/**
	 * 
	 * Esse metodo escreve as pontuacoes no arquivo pontuacao.
	 * As pontuacoes inseridas no arquivo estarao na ordem correta.
	 * @throws IOException
	 * 
	 */
	private void escreverFinal() throws IOException {
		Iterator<Jogador> iterator;
		iterator = dados.iterator();
		FileWriter fileWriter = null;
		PrintWriter printWriter = null;
		
		String caminho = new File("../reversi/pontuacao.csv").getCanonicalPath();
		
		fileWriter = new FileWriter(caminho, false);
		printWriter = new PrintWriter(fileWriter);
			
		while (iterator.hasNext()){
			printWriter.printf(iterator.next().toString());
		}
		
		printWriter.close();
		fileWriter.close();
	}
	
	/**
	 * 
	 * @param pos
	 * @return
	 * @throws IOException
	 * 
	 * Seleciona uma posicao no set e traz o jogador correspondente
	 * 
	 */
	public String getPontuacao(int pos) throws IOException{
		carregar();
		return dados.toArray()[pos].toString();
	}
	
	/**
	 * Obtem o tamanho do set dados
	 * @return
	 */
	public int getDadosSize() {
		return dados.size();
	}
}
