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

public class FXstarter {
    public static void main(String[] args) {

        // force old version
        if(args.length == 1 && args[0].equals("-old"))
        {
            System.err.println("Needs to support JavaFX8!");
            PasswordStrength.main(args);
        }

        try {
            FXstarter.class.getClassLoader().loadClass("javafx.application.Application");
            AppFX.main(args);
        } catch (ClassNotFoundException e) {
            System.err.println("Needs to support JavaFX8!");
            PasswordStrength.main(args);
        }
    }
}
