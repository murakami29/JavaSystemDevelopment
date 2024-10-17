package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Permission;
import com.example.demo.entity.Role;
import com.example.demo.entity.Store;
import com.example.demo.entity.User;
import com.example.demo.form.AdminCreateForm;
import com.example.demo.form.AdminEditForm;
import com.example.demo.service.AdminService;
import com.example.demo.service.PermissionService;
import com.example.demo.service.RoleService;
import com.example.demo.service.StoreService;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;

@Controller
public class AdminManagementController {
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
    
    @Autowired
    private PasswordEncoder passwordEncoder; // PasswordEncoderを注入
    
    @GetMapping("/admin/list")
    public String showAdminList(Model model, Authentication authentication) {
        
    	authentication = SecurityContextHolder.getContext().getAuthentication();
    	System.out.println("Authorities: " + authentication.getAuthorities());

        List<User> adminList = userService.findAll();

        if (adminList.isEmpty()) {
            System.out.println("User not found");
            return handleError("ユーザー情報が見つかりませんでした", model);
        }
        

        model.addAttribute("adminList", adminList);
        return "admin-management/admin-list"; 
    }
    
    // エラーハンドリングのメソッド
    private String handleError(String errorMessage, Model model) {
        model.addAttribute("errorMessage", errorMessage);
        return "error-page"; // エラーページに遷移
    }
    
    @GetMapping("/admin/details/{id}")
    public String showAdminDetails(@PathVariable("id") Long id, Model model) {
    	Optional<User> userOptional = userService.findById(id);

        if (userOptional.isEmpty()) {
            return handleError("管理者情報が見つかりませんでした", model);
        }
        
        User admin = userOptional.get();
        model.addAttribute("admin", admin);
        
        return "admin-management/admin-details"; // テンプレートファイルへのパス
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/edit/{id}")
    public String showAdminEditForm(@PathVariable("id") Long id, Model model) {
        Optional<User> userOptional = userService.findById(id);

        if (userOptional.isEmpty()) {
            model.addAttribute("errorMessage", "管理者が見つかりませんでした");
            return "error-page";
        }

        User admin = userOptional.get();
        
        // AdminEditForm を作成し、データを設定する
        AdminEditForm adminEditForm = new AdminEditForm();
        adminEditForm.setId(admin.getId());
        adminEditForm.setFirstName(admin.getFirstName());
        adminEditForm.setLastName(admin.getLastName());
        adminEditForm.setEmail(admin.getEmail());
        adminEditForm.setPhone(admin.getPhone());
        adminEditForm.setRoleId(admin.getRole().getId());
        adminEditForm.setPermissionId(admin.getPermission().getId());
        adminEditForm.setStoreId(admin.getStore().getId());
        
        model.addAttribute("adminEditForm", adminEditForm); // ここでフォームを追加
        model.addAttribute("stores", storeService.findAll());
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("permissions", permissionService.findAll());
        
        System.out.println("Admin ID: " + adminEditForm.getId());
        System.out.println("Admin Email: " + adminEditForm.getEmail());
        
        return "admin-management/admin-edit";
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/edit/{id}")
    public String updateAdmin(@PathVariable("id") Long id, @ModelAttribute("adminEditForm") AdminEditForm form, Model model) {
        Optional<User> userOptional = userService.findById(id);
        
        if (userOptional.isEmpty()) {
            // ユーザーが見つからない場合のエラーハンドリング
            model.addAttribute("errorMessage", "ユーザーが見つかりませんでした");
            return "admin-management/admin-edit";
        }

        User admin = userOptional.get();

        // フォームのデータを使ってユーザー情報を更新
        admin.setId(form.getId());
        admin.setStore(storeService.findById(form.getStoreId()).orElseThrow(() -> new IllegalArgumentException("無効な店舗ID")));
        admin.setRole(roleService.findById(form.getRoleId()).orElseThrow(() -> new IllegalArgumentException("無効な役職ID")));
        admin.setPermission(permissionService.findById(form.getPermissionId()).orElseThrow(() -> new IllegalArgumentException("無効な権限ID")));
        admin.setFirstName(form.getFirstName());
        admin.setLastName(form.getLastName());
        admin.setEmail(form.getEmail());
        admin.setPhone(form.getPhone());

        userService.save(admin); // ユーザー情報を保存

        // 編集完了ページへリダイレクト
        return "redirect:/admin/edit/complete/{id}";
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/edit/complete/{id}")
    public String editComplete(Model model, @PathVariable("id") Long id) {
        model.addAttribute("adminId", id);
        return "admin-management/admin-edit-complete";
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/create")
    public String showCreateForm(Model model) {
        model.addAttribute("adminCreateForm", new AdminCreateForm());
        
        model.addAttribute("stores", storeService.findAll());
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("permissions", permissionService.findAll());
        
        return "admin-management/admin-create";
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/create")
    public String createAdmin(@Valid @ModelAttribute AdminCreateForm adminCreateForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "admin-management/admin-create";
        }

        // Store, Role, Permission のエンティティを取得
        Optional<Store> store = storeService.findById(adminCreateForm.getStoreId());
        Optional<Role> role = roleService.findById(adminCreateForm.getRoleId());
        Optional<Permission> permission = permissionService.findById(adminCreateForm.getPermissionId());

        // User エンティティの作成
        User newUser = new User();
        newUser.setStore(store.get());
        newUser.setRole(role.get());
        newUser.setPermission(permission.get());
        newUser.setFirstName(adminCreateForm.getFirstName());
        newUser.setLastName(adminCreateForm.getLastName());
        newUser.setEmail(adminCreateForm.getEmail());
        newUser.setPhone(adminCreateForm.getPhone());

        // パスワードをハッシュ化してからセット
        String hashedPassword = passwordEncoder.encode(adminCreateForm.getPassword());
        newUser.setPassword(hashedPassword); // 必要に応じてパスワードのハッシュ化
        
        // 新しいユーザーを保存
        userService.save(newUser);

        return "redirect:/admin/create/complete"; // 完了画面にリダイレクト
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/create/complete")
    public String showAdminCreateComplete() {
        return "admin-management/admin-create-complete";  // 対応するテンプレート名を返す
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/delete/{id}")
    public String deleteAdmin(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            adminService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "管理者が削除されました");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "削除に失敗しました");
            return "redirect:/admin/list";
        }
        
        return "admin-management/admin-delete-complete";
    }
//   
//    @GetMapping("/admin/delete/complete")
//    public String showAdminDeleteComplete() {
//        return "admin-management/admin-delete-complete";  // 対応するテンプレート名を返す
//    }

}
