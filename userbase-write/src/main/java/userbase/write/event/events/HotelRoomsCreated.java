package userbase.write.event.events;


import userbase.write.event.commands.HotelRoomsCreateCommand;

import java.math.BigDecimal;


public class HotelRoomsCreated  {

    public HotelRoomsCreated() { }

    public HotelRoomsCreated(HotelRoomsCreateCommand cmd) {
        this.roomType=cmd.getRoomType();
        this.price=cmd.getPrice();
        this.stockTotal=cmd.getStockTotal();

    }
    private String roomType;

    private BigDecimal price;

    private Long stockTotal;



    public HotelRoomsCreated(String roomType, BigDecimal  price, Long stockTotal ) {
    	this.roomType = roomType;
    	this.price=price;
    	this.stockTotal=stockTotal;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getStockTotal() {
        return stockTotal;
    }

    public void setStockTotal(Long stockTotal) {
        this.stockTotal = stockTotal;
    }
}
