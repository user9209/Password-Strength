/*
    Copyright (C) 2017, 2018  Georg Schmidt <gs-develop@gs-sys.de>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package de.gs_sys.gui.jfx;

import de.gs_sys.lib.crypto.passwords.PasswordStrength;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
;
import java.net.URL;

import java.util.ResourceBundle;


public class ControllerMain implements Initializable {


    private final String versionString = "V 0.2 fx";

    private static ControllerMain instance;

    @FXML
    private TextField password_field;

    @FXML
    private Button b_version;

    @FXML
    private Label passwordQuality;

    @FXML
    private Button button_check;

    @FXML
    void onButtonVersion() {
        password_field.setText("Copyright (c) 2018 Georg Schmidt");
    }

    @FXML
    private void onCheck() {

        setQuality(password_field.getText());
    }

    private void setQuality(String password) {
        passwordQuality.setText("Corresponds to " + PasswordStrength.complexity(password).getBit() + " bit.");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;

        // disables most console output
        PasswordStrength.setCli(false);

        b_version.setText(versionString);

        password_field.textProperty().addListener((obs, oldValue, newValue) -> onCheck());

        // Default in button
        Platform.runLater(() -> button_check.requestFocus());
    }

    /**
     * Registers the hotkeys
     */
    public static void loadHotkeys(Stage stage)
    {
        stage.getScene().setOnKeyReleased(keyEvent1 -> {
            switch (keyEvent1.getCode())
            {
                case ESCAPE:
                    System.exit(0);
                    break;
                case ENTER:
                    instance.onCheck();
                    break;
                default:
            }
        });
    }
}
