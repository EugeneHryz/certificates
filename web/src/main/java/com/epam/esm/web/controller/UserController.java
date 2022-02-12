package com.epam.esm.web.controller;

import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.web.model.hateoas.OrderModelAssembler;
import com.epam.esm.web.model.hateoas.UserModelAssembler;
import com.epam.esm.service.exception.impl.NoSuchElementException;
import com.epam.esm.service.exception.impl.ServiceException;
import com.epam.esm.web.model.OrderRequestModel;
import com.epam.esm.web.model.UserRequestModel;
import com.epam.esm.web.model.hateoas.pagination.impl.PagedOrderModelAssembler;
import com.epam.esm.web.model.hateoas.pagination.impl.PagedUserModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    private OrderService orderService;

    private UserModelAssembler userAssembler;
    private OrderModelAssembler orderAssembler;
    private PagedUserModelAssembler pagedUserAssembler;
    private PagedOrderModelAssembler pagedOrderAssembler;

    private ConversionService conversionService;

    @Autowired
    public UserController(UserService userService, OrderService orderService, UserModelAssembler userAssembler,
                          OrderModelAssembler orderAssembler, PagedUserModelAssembler pagedUserAssembler,
                          PagedOrderModelAssembler pagedOrderAssembler, ConversionService conversionService) {
        this.userService = userService;
        this.orderService = orderService;
        this.orderAssembler = orderAssembler;
        this.userAssembler = userAssembler;
        this.pagedUserAssembler = pagedUserAssembler;
        this.pagedOrderAssembler = pagedOrderAssembler;
        this.conversionService = conversionService;
    }

    /**
     * get user by id
     *
     * @param id user id
     * @return user representation
     * @throws ServiceException if an error occurs
     * @throws NoSuchElementException if user with specified id is not found
     */
    @GetMapping(value = "/{id}", produces = {"application/json"})
    public EntityModel<UserRequestModel> getUser(@PathVariable int id) throws ServiceException, NoSuchElementException {
        UserDto userDto = userService.getUser(id);
        UserRequestModel userRequestModel = conversionService.convert(userDto, UserRequestModel.class);

        return userAssembler.toModel(userRequestModel);
    }

    /**
     * get paged users
     *
     * @param page page number
     * @param size number of elements on one page
     * @return paged list of users with links to navigate through the pages
     * @throws ServiceException if an error occurs
     */
    @GetMapping(produces = {"application/json"})
    public PagedModel<EntityModel<UserRequestModel>> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                                              @RequestParam(value = "size", defaultValue = "2") int size) throws ServiceException {
        List<UserDto> usersDto = userService.getUsers(page, size);
        List<UserRequestModel> usersRequestModel = usersDto.stream()
                .map(u -> conversionService.convert(u, UserRequestModel.class)).collect(Collectors.toList());

        long totalElements = userService.getUserCount();
        PagedModel.PageMetadata pageMetadata = pagedUserAssembler.constructPageMetadata(totalElements, page, size);
        return pagedUserAssembler.toPagedModel(usersRequestModel, pageMetadata);
    }

    /**
     * place order on one gift certificate
     *
     * @param orderRequestModel order representation (only certificateId is enough, other values will be ignored)
     * @param userId user id
     * @return order representation with links
     * @throws ServiceException if an error occurs
     * @throws NoSuchElementException if user or certificate doesn't exist
     */
    @PostMapping(value = "/{userId}/orders", consumes = {"application/json"})
    public ResponseEntity<?> purchaseCertificate(@RequestBody OrderRequestModel orderRequestModel,
                                                 @PathVariable int userId)
            throws ServiceException, NoSuchElementException {

        orderRequestModel.setUserId(userId);
        OrderDto createdOrderDto = orderService.placeOrder(conversionService.convert(orderRequestModel, OrderDto.class));
        OrderRequestModel orderModel = conversionService.convert(createdOrderDto, OrderRequestModel.class);

        EntityModel<OrderRequestModel> entityModel = orderAssembler.toModel(orderModel);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF)
                .toUri()).body(entityModel);
    }

    /**
     * get paged orders of one user
     *
     * @param page page number
     * @param size number of elements on one page
     * @param userId user id
     * @return paged representations of user orders with links
     * @throws ServiceException if an error occurs
     * @throws NoSuchElementException if user with specified id is not found
     */
    @GetMapping(value = "/{userId}/orders", produces = {"application/json"})
    public PagedModel<EntityModel<OrderRequestModel>> getUserOrders(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                    @RequestParam(value = "size", defaultValue = "2") int size,
                                                                    @PathVariable int userId)
            throws ServiceException, NoSuchElementException {

        List<OrderDto> ordersDto = orderService.getUserOrders(userId, page, size);
        List<OrderRequestModel> ordersRequestModel = ordersDto.stream()
                .map(o -> conversionService.convert(o, OrderRequestModel.class)).collect(Collectors.toList());

        long totalElements = orderService.getUserOrderCount(userId);
        PagedModel.PageMetadata pageMetadata = pagedOrderAssembler.constructPageMetadata(totalElements, page, size);
        return pagedOrderAssembler.toPagedModel(ordersRequestModel, pageMetadata, userId);
    }

    /**
     * get one user order by id
     *
     * @param userId user id
     * @param orderId order id
     * @return user order representation with links
     * @throws ServiceException if an error occurs
     * @throws NoSuchElementException if user or order is not found
     */
    @GetMapping(value = "/{userId}/orders/{orderId}", produces = {"application/json"})
    public EntityModel<OrderRequestModel> getUserOrder(@PathVariable int userId, @PathVariable int orderId)
            throws ServiceException, NoSuchElementException {

        OrderDto orderDto = orderService.getUserOrder(userId, orderId);
        OrderRequestModel orderRequestModel = conversionService.convert(orderDto, OrderRequestModel.class);
        return orderAssembler.toModel(orderRequestModel);
    }
}
