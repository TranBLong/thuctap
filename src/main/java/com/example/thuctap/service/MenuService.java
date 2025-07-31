package com.example.thuctap.service;

import com.example.thuctap.models.Menu;
import java.util.List;

public interface MenuService {
    List<Menu> getMenusByCustomerId(int customerId);
    List<Menu> getAllMenus();
    Menu getMenuById(Integer id);
    Menu createMenu(Menu menu);
    Menu updateMenu(Integer id, Menu menu);
    void deleteMenu(Integer id);
}
