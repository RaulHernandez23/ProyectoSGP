package controlador;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import modelo.pojo.Defecto;

import java.net.URL;
import java.util.ResourceBundle;

public class FXMLConsultarDetallesDefectosController implements Initializable {
    
    private Defecto defecto;

    private TextField cbEstadoDefecto;

    @FXML
    private TextField tfTitulo;

    @FXML
    private TextArea tbDescripcion;

    @FXML
    private TextField tfFechaInicio;

    @FXML
    private TextField tfFechaFin;

    @FXML
    private TextField tfEstudiante;

    @FXML
    private TextField tfEsfuerzo;
    @FXML
    private TextField tfEstado;
    
    /*
    public FXMLConsultarDetallesDefectosController(Defecto defecto) {
        this.defecto = defecto;
        tfTitulo.setText(defecto.getTitulo());
        cbEstadoDefecto.setText(defecto.getEstadoDefecto());
    }
    */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void inicializarVentana(Defecto defectoSeleccionado) {
        this.defecto = defectoSeleccionado;
        
    }
}
