package com.example.demo.controller;

import java.math.BigDecimal;
import java.util.List;
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

import com.example.demo.entity.OrderHistory;
import com.example.demo.entity.Product;
import com.example.demo.entity.Store;
import com.example.demo.entity.StoreProductInventory;
import com.example.demo.entity.StoreProductPrice;
import com.example.demo.entity.User;
import com.example.demo.form.ProductOrderForm;
import com.example.demo.service.CustomUserDetails;
import com.example.demo.service.OrderHistoryService;
import com.example.demo.service.ProductService;
import com.example.demo.service.StoreProductInventoryService;
import com.example.demo.service.StoreProductPriceService;
import com.example.demo.service.StoreService;
import com.example.demo.service.UserService;

@Controller
public class OrderHistoryController {

	@Autowired
	private UserService userService;

	@Autowired
	private ProductService productService;

	@Autowired
	private OrderHistoryService orderHistoryService;

	@Autowired
	private StoreService storeService;

	@Autowired
	private StoreProductPriceService storeProductPriceService;

	@Autowired
	private StoreProductInventoryService storeProductInventoryService; // 新たに追加

	// エラーハンドリングのメソッド
	private String handleError(String errorMessage, Model model) {
		model.addAttribute("errorMessage", errorMessage);
		return "error-page"; // エラーページに遷移
	}

	@GetMapping("/product/order/{id}")
	public String ProductOrder(@PathVariable("id") Long productId, Model model) {
		Optional<Product> productOptional = productService.findById(productId);

		if (productOptional.isEmpty()) {
			return handleError("商品情報が見つかりませんでした", model);
		}

		Product product = productOptional.get();
		//	        List<Store> stores = storeService.findAll();  // 発注できる店舗情報を取得

		// ログインユーザーの情報からstoreIdを取得（セキュリティコンテキストから取得）
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		// カスタムUserDetailsクラスを使っている場合
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Long storeId = userDetails.getStoreId(); // ログインユーザーに紐付く店舗IDを取得        

		// 店舗IDと商品IDから価格情報を取得
		Optional<StoreProductPrice> storeProductPriceOptional = storeProductPriceService.findByProductIdAndStoreId(productId, storeId);
		
		// 店舗IDと商品IDから在庫情報を取得
		Optional<StoreProductInventory> storeProductInventoryOptional = storeProductInventoryService.findByProductAndStore(productId, storeId);
	    
	    System.out.println("Price: " + storeProductPriceOptional);  // デバッグログ出力
	    System.out.println("Inventory: " + storeProductInventoryOptional);  // デバッグログ出力

	    // 価格情報が存在しない場合
	    if (storeProductPriceOptional.isEmpty()) {
	        return handleError("価格情報が見つかりませんでした", model);
	    }
	    // 在庫数が存在しない場合の処理（在庫情報が存在しないケース）
	    if (storeProductInventoryOptional.isEmpty()) {
	        return handleError("在庫情報が見つかりませんでした", model);
	    }
	    
	    // 価格と在庫情報を取得
	    StoreProductPrice storeProductPrice = storeProductPriceOptional.get();
	    StoreProductInventory storeProductInventory = storeProductInventoryOptional.get();
	    
		// フォームを初期化
		ProductOrderForm productOrderForm = new ProductOrderForm();
		model.addAttribute("productOrderForm", productOrderForm);
		model.addAttribute("product", product);
		model.addAttribute("storeProductPrice", storeProductPrice);
		model.addAttribute("storeProductInventory", storeProductInventory); // 在庫情報をモデルに追加

		//	        model.addAttribute("stores", stores);

		//	        // 発注数のオプションリストを作成
		//	        List<Integer> quantities = IntStream.rangeClosed(1, 10).boxed().collect(Collectors.toList());
		//	        model.addAttribute("quantities", quantities);
		//	        
		return "product-order-history/product-order";
	}

	@PostMapping("/product/order/{id}")
	public String submitOrder(@ModelAttribute ProductOrderForm productOrderForm, @PathVariable("id") Long productId,
			Model model) {

		// ログインユーザーの情報からstoreIdを取得
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Long storeId = userDetails.getStoreId(); // ログインユーザーに紐付く店舗IDを取得
		Long userId = userDetails.getId(); // ログインユーザーのIDを取得

		// 店舗情報の取得
		Optional<Store> storeOptional = storeService.findById(storeId);
		if (storeOptional.isEmpty()) {
			return handleError("店舗情報が見つかりませんでした", model);
		}
		Store store = storeOptional.get(); // 店舗情報を取得

		// 商品情報の取得
		Optional<Product> productOptional = productService.findById(productId);
		if (productOptional.isEmpty()) {
			return handleError("商品情報が見つかりませんでした", model);
		}
		Product product = productOptional.get();

		// 店舗と商品の価格情報を取得
		Optional<StoreProductPrice> storeProductPriceOptional = storeProductPriceService.findByProductIdAndStoreId(productId, storeId);
		if (storeProductPriceOptional.isEmpty()) {
			return handleError("店舗の商品価格情報が見つかりませんでした", model);
		}
		StoreProductPrice storeProductPrice = storeProductPriceOptional.get(); // 価格情報を取得

		// ユーザー情報の取得
		Optional<User> userOptional = userService.findById(userId);
		if (userOptional.isEmpty()) {
			return handleError("ユーザー情報が見つかりませんでした", model);
		}
		User user = userOptional.get(); // ユーザー情報を取得
		
		// 店舗と商品の在庫情報を取得
		Optional<StoreProductInventory> inventoryOptional = storeProductInventoryService.findByProductAndStore(productId, storeId);
	    if (inventoryOptional.isEmpty()) {
	        return handleError("在庫情報が見つかりませんでした", model);
	    }
	    StoreProductInventory storeProductInventory = inventoryOptional.get();
	    
	    // 発注数を取得
	    int orderQuantity = productOrderForm.getQuantity(); // ユーザーからの入力または他のロジックから取得する

	    // 現在の在庫数を取得
	    int currentInventory = storeProductInventory.getProductInventory();

	    // 更新後の在庫数を計算（発注数を加算）
	    int updatedInventory = currentInventory + orderQuantity; // 発注数を加算

		// 商品の発注処理を行う
		try {
			OrderHistory order = new OrderHistory();
			order.setProduct(product); // 商品情報をセット
			order.setStore(store); // 店舗IDをセット
			order.setUser(user); // ユーザーIDを設定
			order.setQuantity(productOrderForm.getQuantity()); // 発注数をセット
			order.setTotalAmount(
				    product.getCostPrice().multiply(BigDecimal.valueOf(productOrderForm.getQuantity()))); // 仕入れ原価 × 発注数で合計金額を計算してセット
			order.setInventoryAtOrderTime(updatedInventory); // 発注時の在庫数を保存する
			order.setCostPriceAtOrderTime(product.getCostPrice()); 
			
			// 発注情報を保存する
			orderHistoryService.save(order);
			
			// 在庫数を更新する
	        storeProductInventoryService.updateInventory(productId, storeId, productOrderForm.getQuantity());

		} catch (Exception e) {
			return handleError("発注処理に失敗しました: " + e.getMessage(), model);
		}
		return "redirect:/product/order/complete/" + productId; // 成功後のリダイレクト先
	}

	@GetMapping("/product/order/complete/{id}")
	public String orderComplete(Model model, @PathVariable("id") Long id) {
		model.addAttribute("productId", id);
		
		// 商品情報を取得してモデルに追加
	    Optional<Product> productOptional = productService.findById(id);
	    if (productOptional.isPresent()) {
	        model.addAttribute("product", productOptional.get());
	    } else {
	        // 商品が見つからない場合のエラーハンドリング
	        model.addAttribute("errorMessage", "商品情報が見つかりませんでした");
	    }
		return "product-order-history/product-order-complete"; // 完了ページへのテンプレート名
	}
	
	@GetMapping("/product/order/history")
	public String showOrderHistory(Model model) {
	    // ログインユーザーの情報からstoreIdを取得
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
	    Long storeId = userDetails.getStoreId(); // ログインユーザーに紐付く店舗IDを取得
        
	    // 店舗情報の取得
	    Optional<Store> storeOptional = storeService.findById(storeId);
	    if (storeOptional.isEmpty()) {
	        return handleError("店舗情報が見つかりませんでした", model);
	    }
	    
	    Store store = storeOptional.get(); // 店舗情報を取得
	    model.addAttribute("store", store); // ここでstoreをモデルに追加
	    
	    // 店舗に関連する発注履歴を取得
	    List<OrderHistory> orderHistory = orderHistoryService.findByStoreId(storeId);
	    if (orderHistory.isEmpty()) {
	        model.addAttribute("errorMessage", "発注履歴が見つかりませんでした");
	    } else {
	    	// ログでデータを確認
	        orderHistory.forEach(order -> {
	            System.out.println(order.getId() + " - " + order.getTotalAmount());
	        });
	        
	        model.addAttribute("orderHistory", orderHistory); // 発注履歴をモデルに追加
	    }

	    return "product-order-history/product-order-history"; // 発注履歴画面のテンプレート名
	}

}
