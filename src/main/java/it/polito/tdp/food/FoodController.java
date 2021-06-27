/**
 * Sample Skeleton for 'Food.fxml' Controller Class
 */

package it.polito.tdp.food;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FoodController {
	
	private Model model;
	private boolean grafoCreato = false;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtPorzioni"
    private TextField txtPorzioni; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalorie"
    private Button btnCalorie; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="boxFood"
    private ComboBox<Food> boxFood; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	if(this.txtPorzioni.getText().equals("")) {
    		this.txtResult.appendText("Selezionare il numero di porzioni\n");
    		return;
    	}
    	int nPorzioni = 0;
    	try {
    		nPorzioni = Integer.parseInt(this.txtPorzioni.getText());
    	}catch(NumberFormatException e) {
    		this.txtResult.appendText("Il numero di porzioni deve essere un intero positivo\n");
    		return;
    	}
    	
    	this.model.creaGrafo(nPorzioni);
    	this.grafoCreato = true;
    	this.boxFood.getItems().addAll(this.model.getFoods(nPorzioni));
    	this.txtResult.appendText("Grafo creato con "+this.model.getNumVertex()+" vertici e "+this.model.getNumEdges()+" archi\n");
    	
    }
    
    @FXML
    void doCalorie(ActionEvent event) {
    	txtResult.clear();
    	if(!grafoCreato) {
    		this.txtResult.appendText("Creare il grafo\n");
    		return;
    	}
    	Food f = this.boxFood.getValue();
    	if(f==null) {
    		this.txtResult.appendText("Selezionare un cibo dal menu a tendina\n");
    		return;
    	}
    	this.txtResult.appendText(this.model.doCalorie(f));
    }

    @FXML
    void doSimula(ActionEvent event) {
    	txtResult.clear();
    	if(!grafoCreato) {
    		this.txtResult.appendText("Creare il grafo\n");
    		return;
    	}
    	Food f = this.boxFood.getValue();
    	if(f==null) {
    		this.txtResult.appendText("Selezionare un cibo dal menu a tendina\n");
    		return;
    	}
    	if(this.txtK.getText().equals("")) {
    		this.txtResult.appendText("Selezionare il valore K\n");
    		return;
    	}
    	int K = 0;
    	try {
    		K = Integer.parseInt(this.txtK.getText());
    	} catch(NumberFormatException e) {
    		this.txtResult.appendText("K deve essere un intero positivo\n");
    		return;
    	}
    	this.txtResult.appendText(this.model.doSimulazione(f, K));
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtPorzioni != null : "fx:id=\"txtPorzioni\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCalorie != null : "fx:id=\"btnCalorie\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Food.fxml'.";
        assert boxFood != null : "fx:id=\"boxFood\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
