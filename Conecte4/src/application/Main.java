package application;
	
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class Main extends Application {	//classe principal gerada pelo javafx
	@Override
	public void start(Stage primaryStage) {	//inicia a aplicação
		try {
			Parent root = FXMLLoader.load(getClass().getResource("MainScene.fxml")); //recebe o root da estrutura fxml
			Scene scene = new Scene(root); //cria uma cena com esta raiz
			primaryStage.setScene(scene); //a cena inicial é a cena criada a partir desta raiz
			primaryStage.show(); //a cena é mostrada.
			
			//	Iniciar em ecrã inteiro, desativado para testes e debug.
			
			double pre = scene.getHeight();	//guarda a altura inicial do ecrã
			primaryStage.setFullScreen(true); //configura a cena para o ecrã cheio
			double factor = scene.getHeight()/pre; //guarda a razao de escala da cena atual/anterior.
			
			Scale scale = new Scale(factor,factor); //Escala que indica o novo tamanho da cena
			scale.setPivotX(0); //pivot x e y da escala a ser aplicada.
			scale.setPivotY(0);
			scene.getRoot().getTransforms().setAll(scale); //aplica a escala aos transforms da cena raiz.
			
			// 
			
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {	//ao fechar a aplicação
				
				@Override
				public void handle(WindowEvent arg0) {	// chama esta funcao
					Platform.setImplicitExit(true); //indica que a saida é implicita
					Platform.exit(); //sai da plataforma jfx
					System.exit(0); //sai da aplicacao java
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {	//funcao principal da aplicacao java
		launch(args); //lanca a aplicacao jfx
	}
}
