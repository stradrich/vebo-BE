package com.example.inventory.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection = "inventory")
public class Part {
    @Id
    private String id;

    @JsonProperty("uuid")
    private String uuid;

    @JsonProperty("sku")
    private String sku;

    @JsonProperty("status")
    private String status;

    @JsonProperty("description")
    private String description;

    @JsonProperty("supplier")
    private String supplier;

    @JsonProperty("costPrice")
    private double costPrice;

    @JsonProperty("sellingPrice")
    private double sellingPrice;

    @JsonProperty("stockLevel")
    private int stockLevel;

    @JsonProperty("reservedStock")
    private int reservedStock;

    @JsonProperty("availableStock")
    private int availableStock;

    @JsonProperty("photoUrl")
    private String photoUrl;

    @JsonProperty("partType")
    private String partType;

    @JsonProperty("minStockLevel")
    private int minStockLevel;

    @JsonProperty("archived")
    private boolean archived;

    @JsonProperty("controlled")
    private boolean controlled;

    @JsonProperty("controlStock")
    private boolean controlStock;

    // Getters and setters omitted for brevity

    @Override
    public String toString() {
        return "Part{" +
                "id='" + id + '\'' +
                ", uuid='" + uuid + '\'' +
                ", sku='" + sku + '\'' +
                ", status='" + status + '\'' +
                ", description='" + description + '\'' +
                ", supplier='" + supplier + '\'' +
                ", costPrice=" + costPrice +
                ", sellingPrice=" + sellingPrice +
                ", stockLevel=" + stockLevel +
                ", reservedStock=" + reservedStock +
                ", availableStock=" + availableStock +
                ", photoUrl='" + photoUrl + '\'' +
                ", partType='" + partType + '\'' +
                ", minStockLevel=" + minStockLevel +
                ", archived=" + archived +
                ", controlled=" + controlled +
                ", controlStock=" + controlStock +
                '}';
    }
}
