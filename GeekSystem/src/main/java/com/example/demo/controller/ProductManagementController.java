package com.example.demo.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.entity.LargeCategory;
import com.example.demo.entity.MiddleCategory;
import com.example.demo.entity.OrderHistory;
import com.example.demo.entity.Product;
import com.example.demo.entity.SmallCategory;
import com.example.demo.entity.User;
import com.example.demo.form.ProductSearchForm;
import com.example.demo.repository.SmallCategoryRepository;
import com.example.demo.service.CategoryService;
import com.example.demo.service.OrderHistoryService;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;

@Controller
public class ProductManagementController {
	
	@Autowired
    private UserService userService;

	@Autowired
    private ProductService productService;
	
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private OrderHistoryService orderHistoryService;
    
    @Autowired
    private SmallCategoryRepository smallCategoryRepository;
    
    // エラーハンドリングのメソッド
    private String handleError(String errorMessage, Model model) {
        model.addAttribute("errorMessage", errorMessage);
        return "error-page"; // エラーページに遷移
    }
    
 // 画面表示用
    @GetMapping("/product/list")
    public String showProductList(@RequestParam(name = "keyword", required = false) String keyword, Model model, Authentication authentication) {
        // カテゴリリストを取得
        List<LargeCategory> largeCategories = categoryService.findAllLargeCategories();
        List<MiddleCategory> middleCategories = categoryService.findAllMiddleCategories();
        List<SmallCategory> smallCategories = categoryService.findAllSmallCategories();
        
        // キーワード検索
        List<Product> productList = productService.searchProductsByKeyword(keyword);

        ProductSearchForm searchForm = new ProductSearchForm();
        model.addAttribute("searchForm", searchForm);

        // カテゴリのリストを取得してモデルに追加
        model.addAttribute("largeCategories", largeCategories);
        model.addAttribute("middleCategories", middleCategories);
        model.addAttribute("smallCategories", smallCategories);
        model.addAttribute("productList", productList);
        model.addAttribute("keyword", keyword);
        
        return "product-management/product-list";  // 商品一覧ページに遷移
    }

    // JSONでデータを返すAPIエンドポイント
    @GetMapping("/product/api/list")
    @ResponseBody
    public ResponseEntity<List<Product>> getProductListJson(
        @RequestParam(name = "largeCategoryId", required = false) Long largeCategoryId,
        @RequestParam(name = "middleCategoryId", required = false) Long middleCategoryId,
        @RequestParam(name = "smallCategoryId", required = false) Long smallCategoryId,
        @RequestParam(name = "keyword", required = false) String keyword) {
    	
        // デバッグ用のログ出力
        System.out.println("largeCategoryId: " + largeCategoryId);
        System.out.println("middleCategoryId: " + middleCategoryId);
        System.out.println("smallCategoryId: " + smallCategoryId);
        System.out.println("keyword: " + keyword);
    	
        // キーワード検索
        List<Product> productList = productService.searchProducts(largeCategoryId, middleCategoryId, smallCategoryId, keyword);
        
        if (productList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
        
        // JSONで商品リストを返す
        return ResponseEntity.ok(productList);
    }
    
    @GetMapping("/product/api/list/page")
    public ResponseEntity<Page<Product>> listProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
            // その他のパラメータ
    ) {
        Page<Product> products = productService.findAll(PageRequest.of(page, size));
        return ResponseEntity.ok(products);
    }

    @GetMapping("/product/categories/middle")
    @ResponseBody
    public List<MiddleCategory> getMiddleCategories(@RequestParam("largeCategoryId") Long largeCategoryId) {
        // largeCategoryIdに基づいて中カテゴリを取得するロジック
        return categoryService.findMiddleCategoriesByLargeCategoryId(largeCategoryId);
    }

    @GetMapping("/product/categories/small")
    @ResponseBody
    public List<SmallCategory> getSmallCategories(@RequestParam(value = "middleCategoryId", required = false) Long middleCategoryId,
                                                  @RequestParam(value = "largeCategoryId", required = false) Long largeCategoryId) {
        if (middleCategoryId != null) {
            // 中カテゴリIDが指定されている場合はそれに基づいて小カテゴリを取得
            return categoryService.findSmallCategoriesByMiddleCategoryId(middleCategoryId);
        } else if (largeCategoryId != null) {
            // 中カテゴリIDがnullで、大カテゴリIDが指定されている場合
            return categoryService.findSmallCategoriesByLargeCategoryId(largeCategoryId);
        } else {
            // 何も指定されていない場合は空リストを返す
            return Collections.emptyList();
        }
    }
    
    @GetMapping("/product/details/{id}")
    public String showProductDetails(@PathVariable("id") Long productId, Model model) {
    	Optional<Product> productOptional = productService.findById(productId);

        if (productOptional.isEmpty()) {
            return handleError("商品情報が見つかりませんでした", model);
        }
        
        Product product = productOptional.get();
        // StoreProductInventory のデータを取得
        // List<StoreProductInventory> storeProductInventories = storeProductInventoryService.findByProductId(id);

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
        return "product-management/product-details"; // テンプレートファイルへのパス
    }
    
    @GetMapping("/smallCategory/{id}/largeCategory")
    @ResponseBody
    public LargeCategory getLargeCategoryBySmallCategory(@PathVariable Long id) {
        // smallCategoryを取得
        SmallCategory smallCategory = smallCategoryRepository.findById(id).orElse(null);

        if (smallCategory != null) {
            return categoryService.findLargeCategoryBySmallCategory(smallCategory);
        }
        return null;
    }
    
 }
