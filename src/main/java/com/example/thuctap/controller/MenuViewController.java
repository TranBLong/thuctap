package com.example.thuctap.controller;

import com.example.thuctap.models.Food;
import com.example.thuctap.models.Menu;
import com.example.thuctap.service.FoodService;
import com.example.thuctap.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.thuctap.models.Food;
import com.example.thuctap.models.Menu;
import com.example.thuctap.repository.FoodRepository;
import com.example.thuctap.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.List;

@Controller
@RequestMapping("/menu")
public class MenuViewController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private FoodService foodService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private FoodRepository foodRepository;

    @GetMapping
    public String viewMenus(Model model) {
        List<Menu> menus = menuService.getAllMenus();
        model.addAttribute("menus", menus);
        return "menu/menu";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("menu", new Menu());
        model.addAttribute("foods", foodService.getAllFoods()); // để chọn món ăn thêm vào menu
        return "menu/create";
    }

    @PostMapping("/create")
    public String createMenu(@ModelAttribute Menu menu, @RequestParam(value = "foodIds", required = false) List<Integer> foodIds) {
        if (foodIds != null) {
            List<Food> selectedFoods = foodService.getFoodsByIds(foodIds);
            menu.setFoods(selectedFoods);
        }
        menuService.createMenu(menu);
        return "redirect:/menu";
    }

    @GetMapping("/delete/{id}")
    public String deleteMenu(@PathVariable Integer id) {
        menuService.deleteMenu(id);
        return "redirect:/menu";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        Optional<Menu> optionalMenu = menuRepository.findById(id);
        if (optionalMenu.isPresent()) {
            model.addAttribute("menu", optionalMenu.get());
            model.addAttribute("foods", foodRepository.findAll());
            return "menu/edit"; // trả về templates/menu/edit.html
        } else {
            return "redirect:/menu";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateMenu(@PathVariable("id") Integer id,
                             @ModelAttribute Menu menu,
                             @RequestParam(required = false) List<Integer> foodIds) {
        Optional<Menu> existingMenuOpt = menuRepository.findById(id);
        if (existingMenuOpt.isPresent()) {
            Menu existingMenu = existingMenuOpt.get();
            existingMenu.setName(menu.getName());
            existingMenu.setDescription(menu.getDescription());

            if (foodIds != null) {
                List<Food> selectedFoods = foodRepository.findAllById(foodIds);
                existingMenu.setFoods(selectedFoods);
            } else {
                existingMenu.setFoods(new ArrayList<>());
            }

            menuRepository.save(existingMenu);
        }

        return "redirect:/menu";
    }
    // (Tuỳ chọn) Thêm phần edit sau nếu bạn muốn chỉnh sửa menu
}
