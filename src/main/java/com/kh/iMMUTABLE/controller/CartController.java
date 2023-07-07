package com.kh.iMMUTABLE.controller;



import com.kh.iMMUTABLE.dto.CartDto;
import com.kh.iMMUTABLE.dto.CartItemDto;
import com.kh.iMMUTABLE.entity.CartItem;
import com.kh.iMMUTABLE.repository.UserRepository;
import com.kh.iMMUTABLE.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/cart")
@RequiredArgsConstructor

public class CartController {
    private final CartService cartService;

    // 장바구니 추가
    @PostMapping("/addCartItem")
    public ResponseEntity<Boolean> addCartItem(@RequestBody Map<String, String> cartData) {

        try {
            String tempEmail = cartData.get("id"); //id로 찍히지만 실제로 넘어오는건 Email
            String tempProductId = cartData.get("productId");

            long productId = Long.parseLong(tempProductId);

            boolean result = cartService.addCartItem(tempEmail, productId);

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    // 장바구니 리스트 불러오기
    @GetMapping("/cartItemList")
    public ResponseEntity<List<CartItemDto>> getCartItemList(@RequestParam String id) {
        List<CartItemDto> result = cartService.getCartItemList(id);
        if (result == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    // 수량 업데이트
    @PostMapping("/updateCount")
    public ResponseEntity<CartItemDto> updateCount(@RequestBody Map<String, Object> cartData) {
        int count = (int) cartData.get("count");
        //int cartItemId = (int) cartData.get("cartList");
        //List<> cartitem = (List<>) cartData.get("cartList");
        int idx = (int)cartData.get("idx");
        List<Map> tempList = (List<Map>)cartData.get("cartList");
        Map<String, Object> tempMap = tempList.get(idx);

        int cartItemId = (int)tempMap.get("cartItemId");

        // int cartItemId = cartData.get("cartList").get
        //ArrayList<Cart> cartItem = (ArrayList<Cart>) cartData.get("cartList");
//        int cartItemId = (int) cartItem.get(idx).getCartItemList().get
        CartItem cart = cartService.updateCount(count, cartItemId);
        CartItemDto result = new CartItemDto();
        result.setCount(cart.getCount());
        result.setProductPrice(cart.getCartPrice());

        return new ResponseEntity<CartItemDto>(result, HttpStatus.OK);
    }
}