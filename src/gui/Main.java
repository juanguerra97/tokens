package gui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	
	public static Connection conexion = null;

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Parent root = FXMLLoader.load(getClass().getResource("/gui/GUI.fxml"));
		Scene scn = new Scene(root,750,450);
		primaryStage.setScene(scn);
		
		primaryStage.setTitle("Contador de tokens");
		primaryStage.setMinWidth(750);
		primaryStage.setMinHeight(450);
		primaryStage.setOnCloseRequest(we -> desconectarDB());
		primaryStage.show();
		
	}

	public static void main(String[] args) {
		try {
			conectarDB("jdbc:sqlite:tokens.db");// conexion a base de datos por defecto
		} catch (SQLException e) {
			e.printStackTrace();
		}
		launch(args);
	}
	
	public static void conectarDB(String url) throws SQLException {
		assert url != null;
		conexion = DriverManager.getConnection(url);
		try(Statement st = conexion.createStatement()){
			st.executeUpdate("CREATE TABLE IF NOT EXISTS tokens("
					+ "token VARCHAR(100) NOT NULL PRIMARY KEY COLLATE NOCASE"
					+ "); ");
		}catch(SQLException ex) {
			throw new SQLException("NO se puede crear la base de datos", ex);
		}
	}
	
	public static void desconectarDB() {
		if(conexion != null) {
			try {
				conexion.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
