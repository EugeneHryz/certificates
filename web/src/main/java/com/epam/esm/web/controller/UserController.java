package com.epam.esm.web.controller;

import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.web.model.mapper.impl.OrderModelMapper;
import com.epam.esm.service.exception.impl.InvalidRequestDataException;
import com.epam.esm.service.exception.impl.NoSuchElementException;
import com.epam.esm.service.exception.impl.ServiceException;
import com.epam.esm.web.model.OrderRequestModel;
import com.epam.esm.web.model.UserRequestModel;
import com.epam.esm.web.model.mapper.impl.UserModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    private OrderService orderService;

    private UserModelMapper userMapper;
    private OrderModelMapper orderMapper;

    @Autowired
    public UserController(UserService userService, OrderService orderService,
                          UserModelMapper userMapper, OrderModelMapper orderMapper) {
        this.userService = userService;
        this.orderService = orderService;
        this.userMapper = userMapper;
        this.orderMapper = orderMapper;
    }

    @GetMapping(value = "/{id}", produces = {"application/json"})
    public UserRequestModel getUser(@PathVariable int id) throws ServiceException, NoSuchElementException {
        return userMapper.toRequestModel(userService.getUser(id));
    }

    @GetMapping(produces = {"application/json"})
    public List<UserRequestModel> getUsers(@RequestParam(value = "page", defaultValue = "0") String page,
                                  @RequestParam(value = "size", defaultValue = "2") String size)
            throws ServiceException, InvalidRequestDataException {

        List<UserDto> usersDto = userService.getUsers(page, size);
        return userMapper.toRequestModelList(usersDto);
    }

//    @PostMapping(value = "/{userId}/orders", consumes = {"application/json"})
//    public OrderDto purchaseCertificate(@RequestBody OrderCertificateIdOnlyDto orderCertIdDto,
//                                        @PathVariable int userId, BindingResult bindingResult)
//            throws ServiceException, NoSuchElementException, InvalidRequestDataException {
//
//        if (bindingResult.hasErrors()) {
//            throw new InvalidRequestDataException(extractValidationErrorMessage(bindingResult));
//        }
//        orderCertIdDto.setUserId(userId);
//        return orderService.placeOrder(orderCertIdDto);
//    }

    @GetMapping(value = "/{userId}/orders", produces = {"application/json"})
    public List<OrderRequestModel> getUserOrders(@RequestParam(value = "page", defaultValue = "0") String page,
                                                 @RequestParam(value = "size", defaultValue = "2") String size,
                                                 @PathVariable int userId)
            throws ServiceException, InvalidRequestDataException, NoSuchElementException {

        List<OrderDto> ordersDto = orderService.getUserOrders(userId, page, size);
        return orderMapper.toRequestModelList(ordersDto);
    }

    private String extractValidationErrorMessage(BindingResult bindingResult) {
        Optional<String> message = bindingResult.getAllErrors().stream()
                .map(error -> error.getDefaultMessage()).findFirst();

        return message.orElse("No message");
    }
}
