package com.example.thuctap.service;

import com.example.thuctap.models.Menu;
import com.example.thuctap.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Override
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    @Override
    public Menu getMenuById(Integer id) {
        return menuRepository.findById(id).orElse(null);
    }

    @Override
    public Menu createMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    @Override
    public Menu updateMenu(Integer id, Menu updatedMenu) {
        Optional<Menu> existing = menuRepository.findById(id);
        if (existing.isPresent()) {
            Menu menu = existing.get();
            menu.setName(updatedMenu.getName());
            menu.setDescription(updatedMenu.getDescription());
            menu.setFoods(updatedMenu.getFoods());
            return menuRepository.save(menu);
        }
        return null;
    }

    @Override
    public void deleteMenu(Integer id) {
        menuRepository.deleteById(id);
    }


}
