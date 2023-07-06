package com.kh.iMMUTABLE.service;

import com.kh.iMMUTABLE.dto.CartDto;
import com.kh.iMMUTABLE.dto.CartItemDto;
import com.kh.iMMUTABLE.entity.Cart;
import com.kh.iMMUTABLE.entity.CartItem;
import com.kh.iMMUTABLE.entity.Product;
import com.kh.iMMUTABLE.entity.Users;
import com.kh.iMMUTABLE.repository.CartItemRepository;
import com.kh.iMMUTABLE.repository.CartRepository;
import com.kh.iMMUTABLE.repository.ProductRepository;
import com.kh.iMMUTABLE.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;

    // 상품 추가
    public CartItem addCartItem(String email, int productId) {
        System.out.println(" email : " + email);
        System.out.println("productId : " + productId);

        Users user = userRepository.findByUserEmail(email);
        Cart findCartId = new Cart(); //카트Id

        Product product = productRepository.findByProductId(productId);

        Cart cart = cartRepository.findByUserUserEmail(email);
        if (cart == null) { //기존에 장바구니 기능을 사용한적 없는 사용자면
//            User user = userRepository.findByUserEmail(String.valueOf(id)
            cart = new Cart();
            cart.setUser(user); //user의 id를 던져준다.

            Cart userCart = cartRepository.save(cart);
            System.out.println("생성된 카드id " + userCart.getCartId());
            System.out.println(userCart);
            if(userCart.getCartId() > 0){
               //findCartId
                findCartId.setCartId(userCart.getCartId());
            }
        } else {
            //기존에 장바구니 기능을 사용한 user라면
            if(cart.getCartId() > 0 ){ //cart_id가 있는 경우
                findCartId.setCartId(cart.getCartId());
            }
        }

        CartItem cartItem = new CartItem();
        CartItem afterSave = new CartItem();

        if(findCartId.getCartId() > 0){ //카트Id가 존재하면 해당 카트ID를 Fk로 하여 CartItem 테이블에 해당 상품에 대한 값을 insert해준다.
            CartItem findCartItem = new CartItem();
            findCartItem.setCart(findCartId);
            findCartItem.setProduct(product);

            CartItem existCartItem = cartItemRepository.findByCartCartIdAndProductProductId(findCartId.getCartId(), product.getProductId());

            if(existCartItem != null
                && existCartItem.getCartItemId() > 0
                && existCartItem.getProduct().getProductId() > 0) { //만 카 아이템 이 존재한다
                //return existCartItem;
            } else  {
                cartItem.setCart(findCartId);
//              cartItem.setCartId(findCartId.getCartId());
                cartItem.setCartPrice(product.getProductPrice());
                cartItem.setCount(1); //처음 count는 무조건 '1'로 set
                cartItem.setProduct(product);

                afterSave = cartItemRepository.save(cartItem);
            }
        }

        return afterSave;
    }


    // 상품 List 조회
    public List<CartItemDto> getCartItemList(String email) {

        // user_email로 user_id get
        Users user = userRepository.findByUserEmail(email);
        Long user_id = user.getUserId();
        // Cart 테이블에서 user_id로 cart_id get
        Cart cart = cartRepository.findByUserUserEmail(email);
        // CartItem 테이블에서 cart_id로 cartItem 내 list

        List<CartItem> cartList = cartItemRepository.findByCartCartId(cart.getCartId());
        System.out.println(cartList);
        //List<CartItem> cartItemList = cartList.getCartItemList();
        List<CartItemDto> cartItemDtoList = new ArrayList<>();

        for(CartItem cartItem : cartList) {
            CartItemDto cartItemDto = new CartItemDto();
            cartItemDto.setCartItemId((int) cartItem.getCartItemId());
            cartItemDto.setCount(cartItem.getCount());
            cartItemDto.setProductPrice(cartItem.getCartPrice());


            //상품정보에서 상품사진이랑 상품명 get
            Product product = productRepository.findByProductId(cartItem.getProduct().getProductId());
            cartItemDto.setProductName(product.getProductName());
            cartItemDto.setProductImgFst(product.getProductImgFst());

            cartItemDtoList.add(cartItemDto);
        }
        return cartItemDtoList;
    }



    // 상품 수량 업데이트


    }
