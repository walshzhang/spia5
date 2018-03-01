package tacos.controllers;

import javax.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import lombok.extern.slf4j.Slf4j;
import tacos.models.Order;
import tacos.models.User;
import tacos.repos.OrderRepository;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("order")
public class OrderController {
	
	private OrderRepository orderRepo;
	
	public OrderController(OrderRepository orderRepo) {
		this.orderRepo = orderRepo;
	}

	@GetMapping("/current")
	public String orderForm(Order order, @AuthenticationPrincipal User user) {
		order.setStreet(user.getStreet());
		order.setCity(user.getCity());
		order.setState(user.getState());
		order.setZip(user.getZip());
		return "orderForm";
	}
	
	@PostMapping
	public String processOrder(@Valid Order order,
			Errors errors, 
			SessionStatus sessionStatus, 
			@AuthenticationPrincipal User user) {
		if(errors.hasErrors()) {
			return "orderForm";
		}
		order.setUser(user);
		orderRepo.save(order);
		log.info("Order submitted: " + order);
		sessionStatus.setComplete();
		return "redirect:/";
	}
}