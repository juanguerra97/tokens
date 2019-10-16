package gui;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import db.DBTokens;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.WindowEvent;
import tokens.ResultadoToken;
import tokens.TokenFound;
import tokens.Tokenizador;

public class Controller {
	
	@FXML
    private ListView<String> listTokens;

    @FXML
    private MenuItem menuItemDeseleccionar;

    @FXML
    private MenuItem menuItemEliminar;

    @FXML
    private TextField fieldNewToken;

    @FXML
    private Button btnNewToken;

    @FXML
    private TextArea areaTexto;
    
    @FXML
    private Button btnBuscarTokens;

    @FXML
    private TableView<ResultadoToken> tableResultado;

    @FXML
    private TableColumn<ResultadoToken, String> colToken;

    @FXML
    private TableColumn<ResultadoToken, Long> colCantidad;
    
    private FileChooser fChooserTexto;
    
    private DBTokens db = null;
    private ObservableList<String> tokens;
    
    private void loadTokens() {
    	tokens.setAll(db.selectAll());
    }
	
	@FXML
	private void initialize() {
		
		db = new DBTokens(Main.conexion);
		tokens = listTokens.getItems();
		
		tableResultado.setSelectionModel(null);
		colToken.setCellValueFactory(new PropertyValueFactory<ResultadoToken,String>("token"));
		colCantidad.setCellValueFactory(new PropertyValueFactory<ResultadoToken,Long>("cantidad"));
	
		fieldNewToken.textProperty().addListener((ob,viejo,nuevo)->{
			final String newText = nuevo.trim();
			btnNewToken.setDisable(newText.isEmpty() || 
					tokens.stream().anyMatch(t -> t.equalsIgnoreCase(newText)));
		});
		fieldNewToken.setOnAction(e -> {
			if(!btnNewToken.isDisable()) {
				onNuevoToken(e);
			}
		});
		
		areaTexto.textProperty().addListener((ob,viejo,nuevo)->{
			btnBuscarTokens.setDisable(nuevo.trim().isEmpty());
		});
		
		fChooserTexto = new FileChooser();
		fChooserTexto.setTitle("Abrir archivo con el texto");
		fChooserTexto.getExtensionFilters().addAll(
		         new ExtensionFilter("Archivos de texto", "*.txt")
		         /*new ExtensionFilter("All Files", "*.*")*/);
		
		loadTokens();
	}
	
	@FXML
	private void onBuscarTokens(ActionEvent event) {
		List<TokenFound> tokensEncontrados = Tokenizador.findTokens(areaTexto.getText(), tokens);
		tableResultado.setItems(FXCollections.observableList(Tokenizador.resumirTokens(tokensEncontrados)));
		tableResultado.getItems().sort((x,y)->Long.compare(y.getCantidad(),x.getCantidad()));
		tableResultado.requestFocus();
	}
	
	@FXML
	private void onNuevoToken(ActionEvent event) {
		String newToken = fieldNewToken.getText().trim();
		if(db.insert(newToken)) {
			tokens.add(newToken);
			tokens.sort(String::compareToIgnoreCase);
			fieldNewToken.clear();
			listTokens.requestFocus();
		}
	}
	
	@FXML
	private void onListCMenuShown(WindowEvent event) {
		boolean haySeleccion = listTokens.getSelectionModel().getSelectedItem() != null;
		menuItemDeseleccionar.setDisable(!haySeleccion);
		menuItemEliminar.setDisable(!haySeleccion);
	}
	
	@FXML
	private void onEliminarToken(ActionEvent event) {
		String token = listTokens.getSelectionModel().getSelectedItem();
		if(db.delete(token)) {
			tokens.remove(token);
			listTokens.getSelectionModel().clearSelection();
		}
	}
	
	@FXML
	private void onDeseleccionarToken(ActionEvent event) {
		listTokens.getSelectionModel().clearSelection();
	}
	
	@FXML
	private void onCargarArchivoTexto(ActionEvent event) {
		File archivo = fChooserTexto.showOpenDialog(listTokens.getScene().getWindow());
		if(archivo == null)
			return;
		try {
			areaTexto.clear();
			Files.readAllLines(archivo.toPath()).stream()
				.map(line->line+'\n')
				.forEach(areaTexto::appendText);
			areaTexto.requestFocus();
			areaTexto.positionCaret(areaTexto.lengthProperty().get());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
