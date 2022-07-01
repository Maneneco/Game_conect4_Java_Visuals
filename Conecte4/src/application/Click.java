package application;

public class Click {	//classe que simula os cliques no ecrã.
	private double x;	//guarda as posiçoes x e y
	private double y;
	private boolean empty;	//se estiver vazio, é inválido
	
	public Click() {
		empty = true;	//o clique inicia vazio.
	}

	public void set(double x, double y) {	//o set age como clique
		this.x = x;
		this.y = y;
		empty = false;
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public boolean isEmpty() {
		return empty;
	}

	public void reset() {	//o reset invalida o clique.
		empty = true;
	}
}
