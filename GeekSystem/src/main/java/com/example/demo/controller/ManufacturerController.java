package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Manufacturer;
import com.example.demo.form.ManufacturerCreateForm;
import com.example.demo.form.ManufacturerEditForm;
import com.example.demo.service.AdminService;
import com.example.demo.service.ManufacturerService;
import com.example.demo.service.PermissionService;
import com.example.demo.service.RoleService;
import com.example.demo.service.StoreService;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;

@Controller
public class ManufacturerController {
	@Autowired
    private UserService userService;
    
    @Autowired
    private StoreService storeService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;
    
    @Autowired
    private AdminService adminService;
    
//    @Autowired
//    private ManufacturerService manufacturerService;
    
    @Autowired
    private PasswordEncoder passwordEncoder; // PasswordEncoderを注入
    
    private final ManufacturerService manufacturerService;

    @Autowired
    public ManufacturerController(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }
    
    @GetMapping("/manufacturer/list")
    public String showManufacturerList(Model model) {
    	
        List<Manufacturer> manufacturerList = manufacturerService.findAll();

        if (manufacturerList.isEmpty()) {
            System.out.println("User not found");
            return handleError("メーカ情報が見つかりませんでした", model);
        }
        
        model.addAttribute("manufacturerList", manufacturerList);
        return "manufacturer/manufacturer-list"; 
    }
    
    // エラーハンドリングのメソッド
    private String handleError(String errorMessage, Model model) {
        model.addAttribute("errorMessage", errorMessage);
        return "error-page"; // エラーページに遷移
    }
    
    @GetMapping("/manufacturer/details/{id}")
    public String showManufacturerDetails(@PathVariable("id") Long id, Model model) {
    	Optional<Manufacturer> manufacturerOptional = manufacturerService.findById(id);

        if (manufacturerOptional.isEmpty()) {
            return handleError("メーカ情報が見つかりませんでした", model);
        }
        
        Manufacturer manufacturer = manufacturerOptional.get();
        model.addAttribute("manufacturer", manufacturer);
        
        return "manufacturer/manufacturer-details"; // テンプレートファイルへのパス
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/manufacturer/edit/{id}")
    public String showManufacturerEditForm(@PathVariable("id") Long id, Model model) {
        Optional<Manufacturer> manufacturerOptional = manufacturerService.findById(id);

        if (manufacturerOptional.isEmpty()) {
            model.addAttribute("errorMessage", "メーカが見つかりませんでした");
            return "error-page";
        }

        Manufacturer manufacturer = manufacturerOptional.get();
        
        // ManufacturerEditForm を作成し、データを設定する
        ManufacturerEditForm manufacturerEditForm = new ManufacturerEditForm();
        manufacturerEditForm.setId(manufacturer.getId());
        manufacturerEditForm.setName(manufacturer.getName());
        
        model.addAttribute("manufacturerEditForm", manufacturerEditForm); // ここでフォームを追加
        
        System.out.println("Manufacturer ID: " + manufacturerEditForm.getId());
        System.out.println("Manufacturer Email: " + manufacturerEditForm.getName());
        
        return "manufacturer/manufacturer-edit";
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/manufacturer/edit/{id}")
    public String updateAdmin(@PathVariable("id") Long id, @ModelAttribute("manufacturerEditForm") ManufacturerEditForm form, Model model) {
        Optional<Manufacturer> manufacturerOptional = manufacturerService.findById(id);
        
        if (manufacturerOptional.isEmpty()) {
            // メーカが見つからない場合のエラーハンドリング
            model.addAttribute("errorMessage", "メーカ情報が見つかりませんでした");
            return "manufacturer/manufacturer-edit";
        }

        Manufacturer manufacturer = manufacturerOptional.get();

        // フォームのデータを使ってユーザー情報を更新
        manufacturer.setId(form.getId());
        manufacturer.setName(form.getName());

        manufacturerService.save(manufacturer); // メーカ情報を保存

        // 編集完了ページへリダイレクト
        return "redirect:/manufacturer/edit/complete/{id}";
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/manufacturer/edit/complete/{id}")
    public String editComplete(Model model, @PathVariable("id") Long id) {
        model.addAttribute("manufacturerId", id);
        return "manufacturer/manufacturer-edit-complete";
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/manufacturer/create")
    public String showCreateForm(Model model) {
        model.addAttribute("manufacturerCreateForm", new ManufacturerCreateForm());
        
        return "manufacturer/manufacturer-create";
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/manufacturer/create")
    public String createManufacturer(@Valid @ModelAttribute ManufacturerCreateForm manufacturerCreateForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "manufacturer/manufacturer-create";
        }

        // Manufacturer エンティティの作成
        Manufacturer newManufacturer = new Manufacturer();
        newManufacturer.setName(manufacturerCreateForm.getName());
        
        // 新しいメーカ情報を保存
        manufacturerService.save(newManufacturer);

        return "redirect:/manufacturer/create/complete"; // 完了画面にリダイレクト
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/manufacturer/create/complete")
    public String showManufacturerCreateComplete() {
        return "manufacturer/manufacturer-create-complete";  // 対応するテンプレート名を返す
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/manufacturer/delete/{id}")
    public String deleteManufacturer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
        	manufacturerService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "メーカ情報が削除されました");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "削除に失敗しました");
            return "redirect:/manufacturer/list";
        }
        
        return "manufacturer/manufacturer-delete-complete";
    }
}
