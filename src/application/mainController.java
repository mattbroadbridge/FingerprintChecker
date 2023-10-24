package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

public class mainController {

	@FXML
	private TextField firstFileField;

	@FXML
	private TextField secondFileField;

	@FXML
	private TextArea firstFileArea;

	@FXML
	private TextArea secondFileArea;

	@FXML
	private ChoiceBox<String> firstFileChoice;

	@FXML
	private ChoiceBox<String> secondFileChoice;

	@FXML
	private Text fpCheck;

	private static final String INITIALCHOICE = "SHA-1";

	ObservableList<String> choices = FXCollections.observableArrayList("SHA-1", "SHA-256");

	File file1 = null;

	File file2 = null;

	String file1Key = null;

	String file2Key = null;

	FileChooser fc = new FileChooser();




	public void firstFileButtonPress(){							//false = 2, true = 1

		fileBrowser(true);

	}

	public void secondFileButtonPress(){

		fileBrowser(false);

	}

	private void fileBrowser(boolean whichFile){

		if(whichFile){
			file1 = fc.showOpenDialog(null);
			firstFileField.setText(file1.getPath());
			firstFileChoice.setItems(choices);
			firstFileChoice.setValue(INITIALCHOICE);
		}else{
			file2 = fc.showOpenDialog(null);
			secondFileField.setText(file2.getPath());
			secondFileChoice.setItems(choices);
			secondFileChoice.setValue(INITIALCHOICE);
		}

	}

	public void fpFileOne() throws NoSuchAlgorithmException, IOException{
		String whichKey = firstFileChoice.getSelectionModel().getSelectedItem();
		generateFingerprint(true, whichKey);
	}

	public void fpFileTwo() throws NoSuchAlgorithmException, IOException{
		String whichKey = secondFileChoice.getSelectionModel().getSelectedItem();
		generateFingerprint(false, whichKey);
	}

	public void generateFingerprint(boolean whichFile, String whichKey) throws NoSuchAlgorithmException, IOException{

		MessageDigest digest = MessageDigest.getInstance(whichKey);

		InputStream fis = null;

		if (whichFile){
			fis = new FileInputStream(file1);
		}else{
			fis = new FileInputStream(file2);
		}

		int n = 0;
		byte[] buffer = new byte[8192];
		while(n != -1){
			n = fis.read(buffer);
			if(n>0){
				digest.update(buffer, 0, n);
			}

		}

		String fingerPrint = new HexBinaryAdapter().marshal(digest.digest());

		if (whichFile){
			firstFileArea.setText(fingerPrint);
			file1Key = fingerPrint;
		}else{
			secondFileArea.setText(fingerPrint);
			file2Key = fingerPrint;
		}

		fis.close();
	}


	public void comparePrints(){


		String checkString = null;


		if (file1Key.equals(file2Key)){
			checkString = ("The fingerprints are identical");
		}else{
			checkString = ("The fingerprints are not identical");
		}


		fpCheck.setText(checkString);


	}

	public void compareText(){

		String checkString = null;

		if (firstFileArea.getText().equals(secondFileArea.getText())){
			checkString = ("The fingerprints are identical");
		}else{
			checkString = ("The fingerprints are not identical");
		}

		fpCheck.setText(checkString);

	}





}
