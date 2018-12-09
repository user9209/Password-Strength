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

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class AppFX extends Application {

    public static void main(String[] args) {
        launch(args);   
    }

    @Override
    public void start(Stage stage) throws Exception {
        // ignores the given stage !

        /* create and setup main stage */
        Stage mainStage = StageCreator.getStageFromFXML("Password Gen","gui.fxml");

        /* Load hotkeys */
        ControllerMain.loadHotkeys(mainStage);

        // Close App on X (Needs to be AFTER addStage() to overwrite default close)
        mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });

        // No resize
        mainStage.setResizable(false);
    }
}