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

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * Creates JavaFX sage from a fxml file
 */
public class StageCreator {

    /**
     * Opens a .fxml file and returns a stage using this file and the given title
     * @param title Title of the window
     * @param file  .fxml file with the window configuration
     * @return stage out of the fxml file
     */
    @SuppressWarnings("ConstantConditions")
    public static Stage getStageFromFXML(String title, String file)
    {
        if(file == null || file.isEmpty())
            return null;

        // create new stage
        Stage newStage = new Stage(StageStyle.DECORATED); // normal window

        // set title if exist
        if(title != null && !title.isEmpty())
            newStage.setTitle(title);

        try {
            // create new scene
            BorderPane root = FXMLLoader.load(StageCreator.class.getClassLoader().getResource(file));
            Scene scene = new Scene(root);

            /* final stage setup */
            newStage.setScene(scene);
            newStage.show();
            return newStage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
