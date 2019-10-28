package icesi.vip.alien.alien;

import java.io.IOException;

import icesi.vip.alien.networks.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class MenuController {

	@FXML
    private TextField one;

    @FXML
    private Label distanceText;

    @FXML
    private TextField two;

    @FXML
    private ScrollPane floydScrollPane;

    @FXML
    private Tab shortPathTab;
    
    private AdjListGraph<Integer> graph;
    
    @FXML
    private void initialize() throws IOException {
    	
    }
    
    @FXML
    void dijkstraButton(ActionEvent event) {
    	int a = Integer.parseInt(one.getText());
    	int b = Integer.parseInt(two.getText());
    	
    	graph.dijkstra(graph.searchVertex(a));
    	distanceText.setText(""+graph.searchVertex(b).getD());
    	
    }

    @FXML
    void floydButton(ActionEvent event) {
    	GridPane gridPaneOne = new GridPane();
    	gridPaneOne.setPadding(new Insets(20));
    	floydScrollPane.setContent(gridPaneOne);
    	
    	double[][] mat = graph.floydwarshall();
	    	for (int i = 0; i < mat.length; i++) {
				for (int j = 0; j < mat[i].length; j++) {
					double cur = mat[i][j];
					
					if(cur==Integer.MAX_VALUE) {
						cur = -1;
						Button  button = new Button(""+cur);
						button.setTextFill(Color.BLUE);
						button.setPadding(new Insets(20));
						gridPaneOne.add(button, j, i);
					}else {
						Button  button = new Button(""+cur);
						button.setPadding(new Insets(20));
						gridPaneOne.add(button, j, i);
					}
				}
			}
    }
    
    @FXML
    void graph1(ActionEvent event) throws IOException {
    	 graph = new AdjListGraph<>("src/main/java/icesi/vip/alien/networks/test.cases/case1.txt", true);
    }

    @FXML
    void graph2(ActionEvent event) throws IOException {
    	 graph = new AdjListGraph<>("src/main/java/icesi/vip/alien/networks/test.cases/case2.txt", true);
    }

    @FXML
    void graph3(ActionEvent event) throws IOException {
    	 graph = new AdjListGraph<>("src/main/java/icesi/vip/alien/networks/test.cases/case3.txt", true);
    }

}
