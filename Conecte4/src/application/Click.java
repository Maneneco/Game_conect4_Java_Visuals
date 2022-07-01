package application;

public class Click {	//classe que simula os cliques no ecrã.
	private double x;	//guarda as posiçoes x e y
	private double y;
	private boolean empty;	//se estiver vazio, é inválido
	
	public Click() {
		empty = true;	//o clique inicia vazio.
	}

	//Setter
	public void set(double x, double y) {	//o set age como clique
		this.x = x;
		this.y = y;
		empty = false;
	}
	//Getter posição x
	public double getX() {
		return x;
	}
	//Getter posição y
	public double getY() {
		return y;
	}
	//Retornar click vazio
	public boolean isEmpty() {
		return empty;
	}

	//Fazer o reset do click, quando true está vazio
	public void reset() {	
		empty = true;
	}
}
