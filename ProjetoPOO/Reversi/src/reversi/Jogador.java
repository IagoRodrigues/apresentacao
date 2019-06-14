package reversi;

public class Jogador implements Comparable<Jogador> {
	private String nick;
	private int pontos;
	
	public Jogador(String nick, int pontos) {
		super();
		this.nick = nick;
		this.pontos = pontos;
	}
	
	public String getNick() {
		return nick;
	}
	
	public void setNick(String nick) {
		this.nick = nick;
	}
	
	public int getPontos() {
		return pontos;
	}
	
	public void setPontos(int pontos) {
		this.pontos = pontos;
	}

	@Override
	public String toString() {
		return nick + ";" + pontos + "\n";
	}

	@Override
	public int compareTo(Jogador o) {
		int retorno = 0;
		
		if(o != null){			
			retorno = - Integer.compare(this.pontos, o.getPontos());
		}
		
		return retorno;
	}
}