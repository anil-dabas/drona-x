package com.spot.exchange.order.controller;

import com.spot.exchange.order.model.request.CancelOrderParam;
import com.spot.exchange.order.model.request.PlaceOrderParam;
import com.spot.exchange.order.model.response.OrderVo;
import com.spot.exchange.order.model.response.ResultVO;
import com.spot.exchange.order.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/trade")
public class TradeController {

  @Autowired
  private TradeService tradeService;

  @PostMapping("/order")
  ResultVO<OrderVo> placeOrder(@RequestBody PlaceOrderParam placeOrderParam) {
    return tradeService.placeOrder(placeOrderParam);
  }

  @PostMapping("/cancel-order")
  ResultVO<OrderVo> cancelOrder(@Validated @RequestBody CancelOrderParam cancelOrderParam) {
    return tradeService.cancelOrder(cancelOrderParam);
  }

}
