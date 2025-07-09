package com.spot.exchange.order.service;

import com.spot.exchange.order.model.request.CancelOrderParam;
import com.spot.exchange.order.model.request.PlaceOrderParam;
import com.spot.exchange.order.model.response.OrderVo;
import com.spot.exchange.order.model.response.ResultVO;

public interface TradeService {

  ResultVO<OrderVo> placeOrder(PlaceOrderParam placeOrderParam);

  ResultVO<OrderVo> cancelOrder(CancelOrderParam cancelOrderParam);
}
