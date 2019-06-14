package reversi;

import java.util.ArrayList;

/**
 * 
 * Essa classe define o tipo Reversi.
 * E responsabilidade dela montar o tabuleiro e calcular jogadas possiveis
 * X- Dark- Player
 * O- Light- Maquina 
 */

public class Reversi {
    
	//Cria o tabuleiro 8x8
    private int rows = 8;
    private int cols = 8;
    private int userCont = 0;
    private int computerCont = 0;
    private int numberOfMoves =0;
    
    public Cell gameCells[][];
   
    /**
     * 
     * Este e um metodo contrutor, ele constroi o tabuleiro
     * Encontra o meio do tabuleiro (mid).
     * Fixa as 4 pecas iniciais (X e O)
     * Atribui . para as casas vazias
     * 
     */
    public Reversi(){
        int mid = rows / 2; //acha o meio do tabuleiro row = linha, row = coluna
        gameCells = new Cell[8][8];
        
        for(int i = 0; i < rows; ++i) {
            gameCells[i] = new Cell[8];
        }
        
        for(int i = 0; i < rows; ++i)
        {
            for(int j = 0; j < cols; ++j)
            {   
                gameCells[i][j] = new Cell();
                char c = (char) (97 + j);
                
                if((i == mid-1) && (j == mid-1)){
                	
                    gameCells[i][j].setPosition(c, 'X', i+1);
                    
                }else if((i == mid-1) && (j == mid)){
                	
                    gameCells[i][j].setPosition(c, 'O', i+1);
                    
                }else if((i == mid) && (j == mid-1)) {
                	
                    gameCells[i][j].setPosition(c, 'O', i+1);
                    
                }else if((i == mid) && (j == mid)){
                    
                	gameCells[i][j].setPosition(c, 'X', i+1);
                
                }else{
                    gameCells[i][j].setPosition(c, '.', i+1);                    
                }
            }	
        }  
    }
    
    /**
     * 
     * @param obje
     * Esse metodo e uma sobrecarga do metodo contrutor.
     * Ele recebe um objeto do tipo Reversi e seta uma posicao no tabuleiro
     * 
     */
    public Reversi(Reversi obje)
    {
        int y;
        char c, x;
        gameCells = new Cell[8][8];
        
        for(int i = 0; i < rows; ++i) {
            gameCells[i] = new Cell[8];
        }
        
        for(int i = 0; i < rows; ++i)
        {
            for(int j = 0; j < cols; ++j)
            {   
                gameCells[i][j] = new Cell();
                
                c = obje.gameCells[i][j].getCor();
                y = obje.gameCells[i][j].getCorY();
                x = obje.gameCells[i][j].getCorX();
                gameCells[i][j].setPosition(x, c, y);
            }
        }
    }
    
    /**
     * 
     * @param arr
     * Esse metodo encontra movimentos legais dentro do jogo
     * 
     */
    public void findLegalMove(ArrayList <Integer> arr)
    {
        int change = 0;
        this.numberOfMoves=0;

        for (int i = 0; i < rows; i++) 
        {
            for (int j = 0; j < cols; j++) 
            {
                if(gameCells[i][j].getCor() == '.')
                {
                    move(i,j,change,'X','O',numberOfMoves);
                    if(numberOfMoves != 0)
                    {
                        arr.add(i);
                        arr.add(j);
                    }    
                } 
            }
        }
    }
    
    /**
     * 
     * @return
     * Funcao de jogo para o computador
     * Conta quantos movimentos possiveis existem e quantas pecas o computador consumira.
     * No nivel dificil o computador consome o maior numero possivel de pecas na ultima posicao possivel,
     * obrigando o jogador a prever cada uma das possiveis jogadas ate a ultima.
     * 
     * No nivel medio o computador consome o maior numero possivel de pecas na primeira posicao possivel,
     * torna-se mais facil prever o comportamento.
     * 
     * No nivel facil o computador sempre ataca o menor numero de pecas possivel
     */
    public int play(int nivel) //--> play function for computer
    {  
    	int change = 0, max = 0, mX = 0, mY = 0;
    	int flag=0;
    	this.numberOfMoves = 0;
    	
    	//Percorre a matriz tabuleiro
    	for (int i = 0; i < rows; ++i){
    		for (int j = 0; j < cols; ++j) {

    			if(gameCells[i][j].getCor() == '.') // acha o primeiro lugar com . (primeira casa vazia)
    			{

    				move(i,j,change,'O','X',numberOfMoves); // passa essa posicao pra move()
    				
    				//nivel dificil -> computador come mais pecas possivel, na ultima posicao possivel
    				if(nivel == 3)
    				{
    					if(max <= numberOfMoves){ 
    						max = numberOfMoves;
    						mX = i; mY = j;
    					}
    				
    				//nivel medio -> computador come mais pecas possivel, na primeira posicao possivel
    				}else if(nivel == 2) 
    				{
    					if(max < numberOfMoves){ 
    						max = numberOfMoves; //acha onde max eh maior
    						mX = i; mY = j;
    					}
    				
    				//nivel facil -> computador ataca somente onde come tres pecas ou menos
    				}else if(nivel == 1)
    				{
    					if(max < numberOfMoves && max<=3){ 
    						max = numberOfMoves; //acha onde max eh maior
    						mX = i; mY = j;
    					}
    				}
    			}
    		}
    	}

    	computerCont = max;

    	if (computerCont == 0) //computador nao tem nenhum movimento possivel
    	{
    		//Computador passa a vez
    		computerCont = -1;
    		flag = -1;
    	}
    	if(computerCont != 0) //computador tem algum movimento possivel
    	{
    		change = 1;
    		move(mX,mY,change,'O','X',numberOfMoves); //Passa para mov a posicao onde max e maior, ou seja, onde o computador consome mais pecas
    	}
    	return flag;
    }
    
    /**
     * 
     * @param Coordenadas onde o usuario pretente atacar
     * @return
     * Funcao de jogo para o usuario
     * Conta quantos movimentos possiveis existem, confere se o movimento e legal
     * Executa o movimento se for legal
     * 
     */
    public int play(int xCor,int yCor)
    {  
        int status;
        int flag = 0;
        int change = 0, max = 0; 
        this.numberOfMoves=0;

        for (int i = 0; i < rows; ++i)
        {
            for (int j = 0; j < cols; ++j)
            {
                if(gameCells[i][j].getCor() == '.')
                {
                    move(i,j,change,'X','O',numberOfMoves);
                    if(max < numberOfMoves) {
                        max = numberOfMoves;
                    }
                }
            }
        }
        userCont = max;
        
        
        if(userCont == 0) //--> usuario nao se move se nao houver movimentos
        { 
            userCont = -1;
            flag = -1;
        }
        
        if(userCont != 0) //Usuario pode mover
        {	
            change = 1;
            if(!(gameCells[xCor][yCor].getCor() == '.')) {  
            	flag = 1; //--> posicao esta ocupada, movimento ilegal
            }
            
            status = move(xCor,yCor,change,'X','O',numberOfMoves);
            
            if(status == -1) {
            	flag = 1; // --> movimento ilegal
            }
        }
        return flag;
    }
    
    /**
     * 
     * Esse metodo controla o fim do jogo.
     * Ele pode atribuir vitoria, derrota e controla a quantidade de movimentos legais 
     * 
     */
    public int endOfGame() 
    {
        int[] arr = new int[3];
        int flag = -1;
        int cross, circular, point ;
        controlElements(arr);
        cross = arr[0];
        circular = arr[1];
        point = arr[2];
        
        if((userCont == -1 && computerCont == -1) || point == 0)
        {
            if(userCont == -1 && computerCont == -1) //Movimento ilegal
            	flag = 0;
            if(circular > cross)
            	flag = 1;
            else if(cross > circular)
            	flag = 2;
            else if(cross == 0)
            	flag = 3;
            else if(circular == 0)
            	flag = 4;
            else // sem pontuacao
            	flag = 5;
        }
        return flag;
    }
    
    /**
     * 
     * @param arr
     * Esse metodo cria um array com a quantidade de elementos.
     * Conta os X, O e '.'
     * 
     */
    public void controlElements(int arr[] )
    {
        int cross = 0, point = 0, circular = 0;

        for (int i = 0; i < rows; ++i)
        {
            for (int j = 0; j < cols; ++j)
            {
                if(gameCells[i][j].getCor() == 'X') {
                    cross++;
                }else if (gameCells[i][j].getCor() == 'O') {
                    circular++;
                }else if(gameCells[i][j].getCor() == '.') {
                    point++;
                }
            }
        } 
        arr[0] = cross; arr[1] = circular; arr[2] = point;
    }
    
    /**
     * 
     * Esse metodo limpa o tabuleiro
     * Encontra o meio do tabuleiro (mid).
     * Fixa as 4 pecas iniciais (X e O)
     * Atribui . para as casas vazias
     * 
     */
    public void reset()
    {
        int mid = rows / 2;
        for(int i = 0; i < rows; ++i)
        {
            for(int j = 0; j < cols; ++j)
            {   
                char c = (char) (97 + j);
                if((i == mid-1) && (j == mid-1))
                {
                    gameCells[i][j].setPosition(c, 'X', i+1);
                }
                else if((i == mid-1) && (j == mid))
                {
                    gameCells[i][j].setPosition(c, 'O', i+1);
                }
                else if((i == mid) && (j == mid-1))
                {
                    gameCells[i][j].setPosition(c, 'O', i+1);
                }
                else if((i == mid) && (j == mid))
                {
                    gameCells[i][j].setPosition(c, 'X', i+1);
                }
                else		
                {
                    gameCells[i][j].setPosition(c, '.', i+1);                    
                }
                
            }
        }
    }
    
    public void findLegalMove2(ArrayList <Integer> arr)
    {
        int change = 0;
        this.numberOfMoves=0;

        for (int i = 0; i < rows; i++) 
        {
            for (int j = 0; j < cols; j++) 
            {
                if(gameCells[i][j].getCor() == '.')
                {
                    move(i,j,change,'O','X',numberOfMoves);
                    if(numberOfMoves != 0)
                    {
                        arr.add(i);
                        arr.add(j);
                    }    
                } 
            }
        }
    }
    
    public int play2(int xCor,int yCor)
    {  
        int status;
        int flag = 0;
        int change = 0, max = 0; 
        this.numberOfMoves=0;

        for (int i = 0; i < rows; ++i)
        {
            for (int j = 0; j < cols; ++j)
            {
                if(gameCells[i][j].getCor() == '.')
                {	
                    move(i,j,change,'O','X',numberOfMoves);
                    if(max < numberOfMoves) {
                        max = numberOfMoves;
                    }
                }
            }
        }
        userCont = max;
         
         
        if(userCont == 0) //--> usuario nao se move se nao houver movimentos
        { 
            userCont = -1;
            flag = -1;
        }
        
        if(userCont != 0) //Usuario pode mover
        {	
            change = 1;
            if(!(gameCells[xCor][yCor].getCor() == '.')) {  
            	flag = 1; //--> posicao esta ocupada, movimento ilegal
            }
            
            status = move(xCor,yCor,change,'O','X',numberOfMoves);
            
            if(status == -1) {
            	flag = 1; // --> movimento ilegal
            }
        }
        return flag;
    }
    
    
    /**
     * 
     * @param xCor- coordanada i- linhas
     * @param yCor- coordenada j - colunas
     * @param change quando igual a 0 nao muda as cores, so conta numberOfMoves. Quando igual a 1 troca as cores e conta
     * @param char1- peca conquistadora (X para jogador, O para maquina)
     * @param char2- cor a ser conquistada (X para maquina, O para jogador)
     * @param numberOfMoves
     * @return
     * Controla a movimentacao das pecas no jogo para usuario e maquina.
     * Esse metodo e responsavel por rodar as pecas automaticamente quando um jogador atacar alguma posicao.
     * Quando esse metodo e chamado, recebe duas posicoes
     * 
     */
    int move(int xCor, int yCor,int change,char char1,char char2,int numberOfMoves) //
    {
    	int cont; //controlador para casas verticais e horizontais
    	int st2=0, st=0; //diferencial de casas-> 1 quando peca diferente, 2 quando peca igual, -1 quando casa vazia, 0 quando nenhuma casa selecionada
    	int status = -1;
    	int diagX,diagY; //Controlador para diagonais
    	int temp;
    	char str;
    	int nroCasa,y,x;//Controladores de posicao

    	x = xCor; y = yCor;
    	this.numberOfMoves = 0;
    	
    	//Entra no if quando existe casa para baixo e quando a peca logo a baixo for igual a char2
    	if((x+1 < rows) && ( gameCells[x+1][y].getCor() == char2)) //--> baixo
    	{	
    		//tentativa1
    		cont = x;
    		while((cont < rows) && (st2 != -1) && (st != 2))
    		{
    			cont++;
    			if(cont < rows){
    				if(gameCells[cont][y].getCor() == char2) { //peca atual e peca colocada tem cores diferentes
    					st = 1;
    				}else if(gameCells[cont][y].getCor() == char1) { //peca atual e peca colocada tem cores iguais
    					st = 2;
    				}else { // casa vazia
    					st2 = -1;
    				}
    			}
    		}
    		
    		if (st == 2)
    		{
    			temp = cont - x - 1;
    			this.numberOfMoves += temp;
    		}
    		
    		if(st == 2 && change == 1)
    		{
    			for (int i = x; i < cont; ++i)
    			{
    				str = gameCells[i][y].getCorX(); //Coordenada alfabetica da casa em X-> de A a H
    				nroCasa = gameCells[i][y].getCorY(); //Coordenada numerica da casa em Y-> 1 a 8
    				gameCells[i][y].setPosition(str,char1,nroCasa); //Passo coordX, peca a ser trocada e coordY
    			}
    			status = 0;
    		}
    		st=0;st2=0;
    	}
    	
    	//Entra no if quando existe casa para cima e quando a peca logo a cima for igual a char2
    	if((x-1 >= 0) && (gameCells[x-1][y].getCor() == char2)) //--> pra cima
    	{
    		//tentativa2
    		cont = x;
    		while((cont >= 0) && (st2 != -1) && (st != 2))
    		{
    			cont--;
    			if(cont >= 0){
    				if(gameCells[cont][y].getCor() == char2)
    					st = 1;
    				else if(gameCells[cont][y].getCor() == char1)
    					st = 2;
    				else 
    					st2 = -1;
    			}			
    		}
    		if (st == 2)
    		{
    			temp = x - cont - 1;
    			this.numberOfMoves += temp;             
    		}	
    		if(st == 2 && change == 1)
    		{
    			for (int i = cont; i <= x; ++i)
    			{
    				str = gameCells[i][y].getCorX();
    				nroCasa = gameCells[i][y].getCorY();
    				gameCells[i][y].setPosition(str,char1,nroCasa);
    			}
    			status = 0;
    		}		
    		st=0;st2=0;
    	}
    	
    	//Entra no if quando existe casa para direita e quando a peca logo a direita for igual a char2
    	if((y+1 < cols) && (gameCells[x][y+1].getCor() == char2)) //--> direita
    	{
    		//tentativa3
    		cont = y;
    		while((cont < cols) && (st2 != -1) && (st != 2))
    		{
    			cont++;
    			if(cont < cols){
    				if(gameCells[x][cont].getCor() == char2)
    					st = 1;
    				else if(gameCells[x][cont].getCor() == char1)
    					st = 2;
    				else 
    					st2 = -1;	
    			}	
    		}	
    		if (st == 2)
    		{
    			temp = cont - y - 1;
    			this.numberOfMoves += temp;             
    		}	
    		if(st == 2 && change == 1)
    		{
    			for (int i = y; i < cont; ++i)
    			{
    				str = gameCells[x][i].getCorX();
    				nroCasa = gameCells[x][i].getCorY();
    				gameCells[x][i].setPosition(str,char1,nroCasa);
    			}
    			status = 0;
    		}
    		st=0;st2=0;
    	}
    	
    	//Entra no if quando existe casa para esquerda e quando a peca logo a esquerda for igual a char2
    	if((y-1 >= 0) && (gameCells[x][y-1].getCor() == char2)) //--> esquerda
    	{
    		//tentativa4
    		cont = y;
    		while((cont >= 0) && (st2 != -1) && (st != 2))
    		{
    			cont--;
    			if(cont >= 0){
    				if(gameCells[x][cont].getCor() == char2)
    					st = 1;
    				else if(gameCells[x][cont].getCor() == char1)
    					st = 2;
    				else 
    					st2 = -1;	
    			}		
    		}	
    		if (st == 2)
    		{
    			temp = y - cont - 1;
    			this.numberOfMoves += temp;             
    		}	
    		if(st == 2 && change == 1)
    		{
    			for (int i = cont; i <= y; ++i)
    			{
    				str = gameCells[x][i].getCorX();
    				nroCasa = gameCells[x][i].getCorY();
    				gameCells[x][i].setPosition(str,char1,nroCasa);
    			}
    			status = 0;
    		}
    		st=0;st2=0;	
    	}
    	
    	//Entra no if quando existe casa na diagonal superior direita e quando a peca na diagonal superior direita for igual a char2
    	if((x-1 >= 0) && (y+1 < cols) && (gameCells[x-1][y+1].getCor() == char2)) //--> direita para cima (diagonal)
    	{
    		//tentativa5
    		diagY = y;
    		diagX = x;
    		while((diagX >= 0) && (diagY < cols) && (st2 != -1) && (st != 2))
    		{
    			diagX--;
    			diagY++;
    			if((diagX >= 0) && (diagY < cols)){
    				if(gameCells[diagX][diagY].getCor() == char2)
    					st = 1;
    				else if(gameCells[diagX][diagY].getCor() == char1)
    					st = 2;
    				else 
    					st2 = -1;
    			}			
    		}	
    		if (st == 2)
    		{
    			temp = x - diagX - 1;
    			this.numberOfMoves += temp;             
    		}	
    		if(st == 2 && change == 1)
    		{
    			while((diagX <= x) && (y < diagY))
    			{
    				diagX++;
    				diagY--;

    				if((diagX <= x) && (y <= diagY)){
    					str = gameCells[diagX][diagY].getCorX();
    					nroCasa = gameCells[diagX][diagY].getCorY();
    					gameCells[diagX][diagY].setPosition(str,char1,nroCasa);
    				}
    			}
    			status = 0;
    		}
    		st=0;st2=0;		
    	}
    	
    	//Entra no if quando existe casa na diagonal superior esquerda e a peca da diagonal superior esquerda for igual a char2
    	if((x-1 >= 0) && (y-1 >= 0) && (gameCells[x-1][y-1].getCor() == char2)) //--> esquerda para cima
    	{
    		//tentativa6
    		diagY = y;
    		diagX = x;
    		while((diagX >= 0) && (diagY >= 0) && (st2 != -1) && (st != 2))
    		{
    			diagX--;
    			diagY--;
    			if((diagX >= 0) && (diagY >= 0)){
    				if(gameCells[diagX][diagY].getCor() == char2)
    					st = 1;
    				else if(gameCells[diagX][diagY].getCor() == char1)
    					st = 2;
    				else 
    					st2 = -1;	
    			}		
    		}	
    		if (st == 2)
    		{
    			temp = x - diagX - 1;
    			this.numberOfMoves += temp;             
    		}	
    		if(st == 2 && change == 1)
    		{

    			while((diagX <= x) && (diagY <= y))
    			{
    				diagX++;
    				diagY++;
    				if((diagX <= x) && (diagY <= y)){
    					str = gameCells[diagX][diagY].getCorX();
    					nroCasa = gameCells[diagX][diagY].getCorY();
    					gameCells[diagX][diagY].setPosition(str,char1,nroCasa);
    				}
    			}
    			status = 0;
    		}
    		st=0;st2=0;	
    	}
    	
    	//Entra no if quando existe casa da diagonal inferior direita e a peca da diagonal inferior direita for igual a char2
    	if((x+1 < rows) && (y+1 < cols) && (gameCells[x+1][y+1].getCor() == char2)) //--> direita para baixo
    	{
    		//tentativa7
    		diagY = y;
    		diagX = x;
    		while((diagX < rows) && (diagY < cols) && (st2 != -1) && (st != 2))
    		{
    			diagX++;
    			diagY++;
    			if((diagX < rows) && (diagY < cols)){
    				if(gameCells[diagX][diagY].getCor() == char2)
    					st = 1;
    				else if(gameCells[diagX][diagY].getCor() == char1)
    					st = 2;
    				else 
    					st2 = -1;		
    			}	
    		}	
    		if (st == 2)
    		{
    			temp = diagX - x - 1;
    			this.numberOfMoves += temp;             
    		}	
    		if(st == 2 && change == 1)
    		{
    			while((diagX >= x) && (diagY >= y))
    			{
    				diagX--;
    				diagY--;

    				if((diagX >= x) && (diagY >= y)){
    					str = gameCells[diagX][diagY].getCorX();
    					nroCasa = gameCells[diagX][diagY].getCorY();
    					gameCells[diagX][diagY].setPosition(str,char1,nroCasa);
    				}
    			}
    			status = 0;
    		}
    		st=0;st2=0;
    	}
    	
    	//Entra no if quando existe casa da diagonal inferior esquerda e a peca da diagonal inferior esquerda for igual a char2
    	if((x+1 < rows) && (y-1 >= 0) && (gameCells[x+1][y-1].getCor() == char2)) //--> esquerda para baixo
    	{
    		//tentativa8
    		diagY = y;
    		diagX = x;
    		while((diagX < rows) && (diagY >= 0) && (st2 != -1) && (st != 2))
    		{
    			diagX++;
    			diagY--;
    			if((diagX < rows) && (diagY >= 0)){
    				if(gameCells[diagX][diagY].getCor() == char2)
    					st = 1;
    				else if(gameCells[diagX][diagY].getCor() == char1)
    					st = 2;
    				else 
    					st2 = -1;	
    			}		
    		}	
    		if (st == 2)
    		{
    			temp = diagX - x - 1;
    			this.numberOfMoves += temp;             
    		}	
    		if(st == 2 && change == 1)
    		{
    			while((diagX >= x) && (diagY <= y))
    			{
    				diagX--;
    				diagY++;

    				if((diagX >= x) && (diagY <= y)){
    					str = gameCells[diagX][diagY].getCorX();
    					nroCasa = gameCells[diagX][diagY].getCorY();
    					gameCells[diagX][diagY].setPosition(str,char1,nroCasa);
    				}
    			}
    			status = 0;
    		}
    		st=0;st2=0;		
    	}
    	if(status == 0)
    		return 0;
    	else
    		return -1;			
    }
}
