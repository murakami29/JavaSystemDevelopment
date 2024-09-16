package com.example.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.Store;
import com.example.demo.entity.User;
import com.example.demo.form.StoreEditForm;
import com.example.demo.service.StoreService;
//import com.example.demo.service.PermissionService;
//import com.example.demo.service.RoleService;
//import com.example.demo.service.StoreService;
import com.example.demo.service.UserService;

@Controller
public class StoreManagementController {
	
    @Autowired
    private UserService userService;
    
    @Autowired
    private StoreService storeService;

//    @Autowired
//    private RoleService roleService;
//
//    @Autowired
//    private PermissionService permissionService;
    
    @GetMapping("/store/details")
    public String getStoreDetails(Model model, Authentication authentication) {
        String email = authentication.getName();
        
        Optional<User> optionalUser = userService.findByEmail(email);

        if (optionalUser.isEmpty()) {
            System.out.println("User not found");
            return handleError("ユーザー情報が見つかりませんでした", model);
        }
        
        User user = optionalUser.get();
        Store store = user.getStore();
        if (store == null) {
            System.out.println("Store not found for user");
            return handleError("ユーザーに関連付けられた店舗情報が見つかりませんでした", model);
        }

        model.addAttribute("store", store);
        System.out.println("Store details fetched successfully");
        
        return "store-management/store-details"; // テンプレートファイルへのパス
    }
    // エラーハンドリングのメソッド
    private String handleError(String errorMessage, Model model) {
        model.addAttribute("errorMessage", errorMessage);
        return "error-page"; // エラーページに遷移
    }
    
    @GetMapping("/store/edit/{id}")
    public String showStoreEditForm(@PathVariable("id") Long id, Model model) {
        Optional<Store> storeOptional = storeService.findById(id);

        if (storeOptional.isEmpty()) {
            model.addAttribute("errorMessage", "店舗が見つかりませんでした");
            return "error-page";
        }

        Store store = storeOptional.get();

        // StoreEditForm を作成し、データを設定する
        StoreEditForm storeEditForm = new StoreEditForm();
        storeEditForm.setStoreId(store.getId());
        storeEditForm.setName(store.getName());
        storeEditForm.setAddress(store.getAddress());
        
        model.addAttribute("storeEditForm", storeEditForm); // ここでフォームを追加

        return "store-management/store-edit";
    }
    
    // 店舗管理編集内容を保存
    @PostMapping("/store/edit")
    public String updateStore(@ModelAttribute("storeEditForm") StoreEditForm form, Model model) {
        Optional<Store> storeOptional = storeService.findById(form.getStoreId());
        
        if (storeOptional.isEmpty()) {
            // 店舗情報が見つからない場合のエラーハンドリング
            model.addAttribute("errorMessage", "店舗が見つかりませんでした");
            return "store-management/store-edit";
        }

        Store store = storeOptional.get();

        // フォームから取得したデータで店舗を更新

//        store.setId(form.getStoreId());
        store.setName(form.getName());
        store.setAddress(form.getAddress());

        storeService.save(store); // 店舗情報を保存

        // 編集完了ページへリダイレクト
        return "redirect:/store/edit/complete";
    }
    
    @GetMapping("/store/edit/complete")
    public String showCompletePage() {
        return "store-management/store-edit-complete"; // 完了ページのテンプレート名
    }
}

