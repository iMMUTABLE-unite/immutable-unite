package com.kh.iMMUTABLE.repository;

import com.kh.iMMUTABLE.entity.Cart;
import com.kh.iMMUTABLE.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, String> {
    Cart findByUserUserEmail(String userEmail);

    Cart deleteCartByCartId(Long cartId);
}