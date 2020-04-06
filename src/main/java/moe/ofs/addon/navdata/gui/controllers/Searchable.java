package moe.ofs.addon.navdata.gui.controllers;

import javafx.scene.control.ListView;
import moe.ofs.backend.services.CrudService;

public interface Searchable {
    default <T> void search(ListView<T> listView, String newValue, CrudService<T> service) {
        if(!newValue.equals("")) {
            listView.getItems().clear();
            service.findAll().stream()
                    .filter(navaid -> navaid.toString().toLowerCase()
                            .contains(newValue.toLowerCase()))
                    .forEach(listView.getItems()::add);
        } else {
            service.findAll().forEach(listView.getItems()::add);
        }
    }
}
