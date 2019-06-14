package reversi;

/**
 * 
 * Essa classe define o tipo celula
 *
 */

public class Cell{
	
    private int corY; //Coordenada numerica da casa em Y-> 1 a 8
    private char corX; //Coordenada alfabetica da casa em X-> de A a H
    private char cor; //Peca ocupando a casa
    
    public Cell(char x, int y, char c){
        corY = y;
        corX = x;
        cor = c;  
    }

    public Cell() {
    	
    }
    
    public char getCorX(){
        return corX; 
    }
    
    public int getCorY(){ 
        return corY; 
    }
    
    public char getCor(){
        return cor; 
    }
    
    public void setPosition(char x, char c, int y){
        corX = x;
        corY = y;
        cor = c;
    }
}