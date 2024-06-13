package com.tzy.basebackend.holder;

import com.tzy.basebackend.model.DTO.UserDTO;

/**
 * @author Noel
 * Created on 2024/6/13
 * ClassName:UserHolder
 * Package:com.tzy.basebackend.holder
 */
public class UserHolder {
    private static final ThreadLocal<UserDTO> tl = new ThreadLocal<>();

    public static void save(UserDTO userDTO) {
        tl.set(userDTO);
    }

    public static UserDTO getUserDTO() {
        return tl.get();
    }

    public static void remove() {
        tl.remove();
    }
}
