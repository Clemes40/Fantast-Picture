package application;

import entite.Person;
import entite.Photo;
import entite.Note;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import service.Data;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class VueController implements Initializable {

    @FXML
    private ListView<String> listVille;
    @FXML
    private ImageView imageView;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private TextField champLogin;
    @FXML
    private TextField champPassword;
    @FXML
    private TextField champNote;
  
    private Person currentUser;  
    private Data data;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.data = new Data();
        ObservableList<String> cities = FXCollections.observableArrayList(data.getCityNames());
        listVille.setItems(cities);
        
        imageView.setImage(null);
        listVille.setMouseTransparent(true); 
        listVille.setFocusTraversable(false);

        listVille.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateImageView(newValue);
            }
        });
    }

    private void updateImageView(String cityName) {
        String imageUrl = data.getUrlByCityName(cityName);
        if (imageUrl != null) {
            Image image = new Image(getClass().getResourceAsStream(imageUrl));
            imageView.setImage(image);
        }
        
        if (currentUser != null) {
            Note note = data.getNoteForUserAndPhoto(currentUser.getName(), cityName);
            if (note != null) {
                champNote.setText(String.valueOf(note.getNote()));
            } else {
                champNote.clear();
            }
        }
    }
    
    @FXML
    private void handleLoginAction(ActionEvent event) {
        try {
            String login = champLogin.getText();
            String password = champPassword.getText();
            Person user = data.authenticateUser(login, password);

            currentUser = user;
            loginMessageLabel.setText("Connexion réussie !");
            
            listVille.setMouseTransparent(false); 
            listVille.setFocusTraversable(true);
        } catch (Exception e) {
            loginMessageLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void handleNoteAction(ActionEvent event) {
        if (currentUser != null && listVille.getSelectionModel().getSelectedItem() != null) {
            String photoName = listVille.getSelectionModel().getSelectedItem();
            int noteValue;
            try {
                noteValue = Integer.parseInt(champNote.getText());
                Photo selectedPhoto = new Photo(photoName, "");
                Note note = new Note(noteValue, selectedPhoto, currentUser);
                data.saveNote(note);
                loginMessageLabel.setText("Note enregistrée !");
            } catch (NumberFormatException e) {
                loginMessageLabel.setText("Veuillez entrer une note valide.");
            } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } else {
            loginMessageLabel.setText("Veuillez sélectionner une photo et vous connecter.");
        }
    }
}
