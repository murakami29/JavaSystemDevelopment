package com.example.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.User;
import com.example.demo.form.AdminProfileForm;
import com.example.demo.service.PermissionService;
import com.example.demo.service.RoleService;
import com.example.demo.service.StoreService;
import com.example.demo.service.UserService;

@Controller
public class AdminProfileController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private StoreService storeService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @GetMapping("/admin/profile/details")
    public String getAdminProfile(Model model) {
        // ログイン中のユーザーのEmailを取得
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // emailがusernameとなっている場合
        
        // Emailでユーザーを検索
        Optional<User> optionalAdminProfile = userService.findByEmail(email);

        // OptionalからUserを取得
        if (optionalAdminProfile.isPresent()) {
            User adminProfile = optionalAdminProfile.get();
            // ビューにデータを渡す
            model.addAttribute("adminProfile", adminProfile);
        } else {
            // ユーザーが見つからない場合の処理（エラーメッセージを表示するなど）
            model.addAttribute("errorMessage", "管理者情報が見つかりませんでした");
        }

        return "admin-profile-details";
    }
    
    @GetMapping("/admin/profile/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Optional<User> userOptional = userService.findById(id);

        if (userOptional.isEmpty()) {
            model.addAttribute("errorMessage", "ユーザーが見つかりませんでした");
            return "error-page";
        }

        User user = userOptional.get();
        model.addAttribute("adminProfile", user); // Userオブジェクトをそのまま渡す

        // 店舗、役職、権限のリストをModelに追加
        model.addAttribute("stores", storeService.findAll());
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("permissions", permissionService.findAll());

        return "admin-profile-edit";
    }

//    @GetMapping("/admin/profile/edit/{id}")
//    public String showEditForm(@PathVariable("id") Long id, Model model) {
//    	Logger logger = LoggerFactory.getLogger(AdminProfileController.class);
//    	Optional<User> userOptional = userService.findById(id);
//
//        // ユーザーが存在しない場合のエラーハンドリング
//        if (userOptional.isEmpty()) {
//            // エラーメッセージをモデルに追加してエラーページにリダイレクト
//            model.addAttribute("errorMessage", "ユーザーが見つかりませんでした");
//            return "error-page"; // ここでエラーページを返す
//        }
//
//        User user = userOptional.get();
//        
//        // ログにユーザーデータを出力して確認
//        logger.info("編集画面のために取得したユーザー情報: " + user.toString());
//
//        // フォームに既存データをセット
//        AdminProfileForm form = new AdminProfileForm();
//        form.setId(user.getId());
//        form.setStoreId(user.getStore().getId());
//        form.setRoleId(user.getRole().getId());
//        form.setPermissionId(user.getPermission().getId());
//        form.setFirstName(user.getFirstName());
//        form.setLastName(user.getLastName());
//        form.setEmail(user.getEmail());
//        form.setPhone(user.getPhone());
//
//        // 店舗、役職、権限のリストをModelに追加
//        model.addAttribute("adminProfileForm", form);
//        model.addAttribute("stores", storeService.findAll()); // 店舗リスト
//        model.addAttribute("roles", roleService.findAll()); // 役職リスト
//        model.addAttribute("permissions", permissionService.findAll()); // 権限リスト
//
//        return "admin-profile-edit"; // 正常時の編集画面のHTMLを返す
//    }
    
    // 管理者プロフィール編集内容を保存
    @PostMapping("/admin/profile/edit")
    public String editAdminProfile(@ModelAttribute("adminProfileForm") AdminProfileForm form, Model model) {
        Optional<User> userOptional = userService.findById(form.getId());
        
        if (userOptional.isEmpty()) {
            // ユーザーが見つからない場合のエラーハンドリング
            model.addAttribute("errorMessage", "ユーザーが見つかりませんでした");
            return "admin-profile-edit";
        }

        User user = userOptional.get();

        // フォームのデータを使ってユーザー情報を更新
        user.setStore(storeService.findById(form.getStoreId()).orElseThrow(() -> new IllegalArgumentException("無効な店舗ID")));
        user.setRole(roleService.findById(form.getRoleId()).orElseThrow(() -> new IllegalArgumentException("無効な役職ID")));
        user.setPermission(permissionService.findById(form.getPermissionId()).orElseThrow(() -> new IllegalArgumentException("無効な権限ID")));
        user.setFirstName(form.getFirstName());
        user.setLastName(form.getLastName());
        user.setEmail(form.getEmail());
        user.setPhone(form.getPhone());

        userService.save(user); // ユーザー情報を保存

        // 編集完了ページへリダイレクト
        return "redirect:/admin/profile/complete";
    }
    
    @GetMapping("/admin/profile/complete")
    public String showCompletePage() {
        return "admin-profile-complete"; // 完了ページのテンプレート名
    }

}

