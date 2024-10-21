package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.entity.LargeCategory;
import com.example.demo.entity.MiddleCategory;
import com.example.demo.entity.OrderHistory;
import com.example.demo.entity.Product;
import com.example.demo.entity.SmallCategory;
import com.example.demo.entity.User;
import com.example.demo.service.AdminService;
import com.example.demo.service.CategoryService;
import com.example.demo.service.OrderHistoryService;
import com.example.demo.service.PermissionService;
import com.example.demo.service.ProductService;
import com.example.demo.service.RoleService;
import com.example.demo.service.StoreService;
import com.example.demo.service.UserService;

@Controller
public class CategoryController {
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
    private ProductService productService;
    
    @Autowired
    private OrderHistoryService orderHistoryService;
    @Autowired
    private PasswordEncoder passwordEncoder; // PasswordEncoderを注入
    
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    
    @GetMapping("/category/list/large")
    public String showLargeCategoryList(Model model) {
    	
    	List<LargeCategory> largeCategories = categoryService.findAllLargeCategories();

        if (largeCategories.isEmpty()) {
            System.out.println("Large categories not found");
            return handleError("大カテゴリ情報が見つかりませんでした", model);
        }
        
        model.addAttribute("largeCategories", largeCategories);
        return "category/category-large-list"; 
    }
    
    // エラーハンドリングのメソッド
    private String handleError(String errorMessage, Model model) {
        model.addAttribute("errorMessage", errorMessage);
        return "error-page"; // エラーページに遷移
    }
    
    @GetMapping("/category/list/large/{largeId}/middle")
    public String showMiddleCategoryList(@PathVariable("largeId") Long largeId, Model model) {
    	
	    // 大カテゴリ情報の取得
        Optional<LargeCategory> largeCategory = categoryService.findLargeCategoryById(largeId);

        if (largeCategory.isPresent()) {
            model.addAttribute("largeCategory", largeCategory.get());
        } else {
            return handleError("大カテゴリ情報が見つかりませんでした", model);
        }
        
        // 中カテゴリ情報の取得
    	List<MiddleCategory> middleCategories = categoryService.findMiddleCategoriesByLargeCategoryId(largeId);
 
        if (middleCategories.isEmpty()) {
            return handleError("中カテゴリ情報が見つかりませんでした", model);
        }

        model.addAttribute("middleCategories", middleCategories);
        model.addAttribute("largeId", largeId);
        
        return "category/category-middle-list";
    }
    
    @GetMapping("/category/list/large/{largeId}/middle/{middleId}/small")
    public String showSmallCategoryList(@PathVariable("largeId") Long largeId, @PathVariable("middleId") Long middleId, Model model) {
    	
    	// 大カテゴリ情報の取得
        Optional<LargeCategory> largeCategory = categoryService.findLargeCategoryById(largeId);

        if (largeCategory.isPresent()) {
            model.addAttribute("largeCategory", largeCategory.get());
        } else {
            return handleError("大カテゴリ情報が見つかりませんでした", model);
        }
        
        // 中カテゴリ情報の取得
        Optional<MiddleCategory> middleCategory = categoryService.findMiddleCategoryById(middleId);

        if (middleCategory.isPresent()) {
            model.addAttribute("middleCategory", middleCategory.get());
        } else {
            return handleError("中カテゴリ情報が見つかりませんでした", model);
        }
    	
        // 小カテゴリ情報の取得
    	List<SmallCategory> smallCategories = categoryService.findSmallCategoriesByMiddleCategoryId(middleId);
 
        if (smallCategories.isEmpty()) {
            return handleError("小カテゴリ情報が見つかりませんでした", model);
        }
        
        model.addAttribute("smallCategories", smallCategories);
        model.addAttribute("largeId", largeId);
        model.addAttribute("middleId", middleId);
        
        return "category/category-small-list";
    }
    
    @GetMapping("/category/list/large/{largeId}/middle/{middleId}/small/{smallId}")
    public String showSmallCategoryDetails(@PathVariable("largeId") Long largeId, @PathVariable("middleId") Long middleId, @PathVariable("smallId") Long smallId, Model model) {
    	
    	// 大カテゴリ情報の取得
        Optional<LargeCategory> largeCategory = categoryService.findLargeCategoryById(largeId);

        if (largeCategory.isPresent()) {
            model.addAttribute("largeCategory", largeCategory.get());
        } else {
            return handleError("大カテゴリ情報が見つかりませんでした", model);
        }
        
        // 中カテゴリ情報の取得
        Optional<MiddleCategory> middleCategory = categoryService.findMiddleCategoryById(middleId);

        if (middleCategory.isPresent()) {
            model.addAttribute("middleCategory", middleCategory.get());
        } else {
            return handleError("中カテゴリ情報が見つかりませんでした", model);
        }
    	
        // 小カテゴリ情報の取得
        Optional<SmallCategory> smallCategory = categoryService.findSmallCategoryById(smallId);

        if (smallCategory.isPresent()) {
            model.addAttribute("smallCategory", smallCategory.get());
        } else {
            return handleError("小カテゴリ情報が見つかりませんでした", model);
        }
        
        // 商品情報の取得
    	List<Product> productList = productService.getProductsBySmallCategoryId(smallId);
 
        if (productList.isEmpty()) {
            return handleError("商品情報が見つかりませんでした", model);
        }
        
        model.addAttribute("productList", productList);
        
        model.addAttribute("largeId", largeId);
        model.addAttribute("middleId", middleId);
        model.addAttribute("smallId", smallId);
        
        return "category/category-small-details";
    	
    }
    
    @GetMapping("/category/list/large/{largeId}/middle/{middleId}/small/{smallId}/product/{productId}")
    public String showProductDetails(@PathVariable("largeId") Long largeId, @PathVariable("middleId") Long middleId, @PathVariable("smallId") Long smallId, @PathVariable("productId") Long productId, Model model) {
    	Optional<Product> productOptional = productService.findById(productId);

        if (productOptional.isEmpty()) {
            return handleError("商品情報が見つかりませんでした", model);
        }
        
        Product product = productOptional.get();
        model.addAttribute("product", product);
        
        // 最新の発注履歴を取得
        Optional<OrderHistory> latestOrderHistoryOptional = orderHistoryService.findLatestByProductId(productId);
        if (latestOrderHistoryOptional.isPresent()) {
            OrderHistory orderHistory = latestOrderHistoryOptional.get();
            model.addAttribute("orderHistory", orderHistory);
        } else {
            model.addAttribute("orderHistory", null);
        }
        
        model.addAttribute("storeProductInventories", product.getStoreProductInventories()); // store_product_inventory のデータ

        // ログインしているユーザーを取得
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName(); // ログイン中のユーザー名（Emailなど）

        // ユーザー名からUserエンティティを取得し、store_nameを取得
        Optional<User> optionalUser = userService.findByEmail(currentUserName); // userIdを元にユーザーを取得

        if (optionalUser.isPresent()) {
            User user = optionalUser.get(); // OptionalからUserを取得
            String storeName = user.getStore().getName(); // getStoreName()を呼び出し
            // storeNameを使用して他の処理を行う
            model.addAttribute("storeName", storeName);  // storeName をモデルに追加
        } else {
            // Userが見つからない場合の処理
        	model.addAttribute("error", "ログインしているユーザーが見つかりませんでした");
        }
        return "category/category-product-details"; // テンプレートファイルへのパス
    }
}
