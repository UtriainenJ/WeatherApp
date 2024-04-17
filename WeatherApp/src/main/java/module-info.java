module fi.tuni.progthree.weatherapp {
    requires javafx.controls;
    exports fi.tuni.prog3.weatherapp;
    requires com.google.gson;
    requires okhttp3;
	opens fi.tuni.prog3.weatherapp to com.google.gson;
}
