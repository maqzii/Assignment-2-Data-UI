package com.amaral;

import com.amaral.interceptor.Logged;
import com.amaral.inventory.InventoryService;
import com.amaral.entity.Inventory;
import com.amaral.entity.Store;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SessionScoped // Specifies that the bean is SessionScoped
@Named // allows the bean to be available for use in the JSF
public class StoreAppBean implements Serializable {

    private Long id;
    private String name;
    private String sport;
    private int quantity;
    private double price;
    private Date dateUpdated;

    private List<Inventory> storeInventory;

    @EJB // Helps eliminate boilerplate code
    private InventoryService inventoryService;

    private Long storeId;
    private String storeName;
    private String storeLocation;

    // Creates the Store object
    public String createStore(){
        Store store = new Store(storeId, storeName, storeLocation, storeInventory);
        return "inventory";
    } // END OF createStore

    @Logged // Logs information when an @Logged method is called
    public void addInventory() {
        Inventory inventory = new Inventory(id, name, sport, quantity, price, dateUpdated);
        Optional<Inventory> inventoryExists = inventoryService.getInventoryList()
                .stream().filter(i ->
                        i.getName().equals(name) &&
                        i.getSport().equals(sport) &&
                        i.getPrice() == (price)).findFirst();

        if (inventoryExists.isPresent()) {
            inventoryService.removeFromInventory(inventory);
            inventoryService.addToInventory(inventory);
        } else {
            inventoryService.addToInventory(inventory);
        }
        clearFields();
    } // END OF addInventory

    @Logged
    public void removeInventory(Inventory inventory) {
        inventoryService.removeFromInventory(inventory);
    } // END OF removeInventory

    private void clearFields() {
        setId(null);
        setName("");
        setSport("");
        setPrice(0);
        setQuantity(0);
        setDateUpdated(null);
    } // END OF clearFields

    public List<Inventory> getInventoryList() {
        return inventoryService.getInventoryList();
    } // END OF getInventoryList

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSport() { return sport; }
    public void setSport(String sport) { this.sport = sport; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPrice() {return price; }
    public void setPrice(double price) {this.price = price; }

    public Date getDateUpdated() { return dateUpdated; }
    public void setDateUpdated(Date dateUpdated) { this.dateUpdated = dateUpdated; }

    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }

    public String getStoreLocation() { return storeLocation; }
    public void setStoreLocation(String storeLocation) { this.storeLocation = storeLocation; }

    public List<Inventory> getStoreInventory() { return storeInventory; }
    public void setStoreInventory(List<Inventory> storeInventory) { this.storeInventory = storeInventory; }
} // END OF class
