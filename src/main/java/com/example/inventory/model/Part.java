package com.example.inventory.model;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Document(collection = "inventory")
public class Part {
    @Id
    private String id;

    @JsonProperty("uuid")
    private String uuid = UUID.randomUUID().toString();  // Auto-generated UUID

    @Indexed(unique = true)  // Ensure uniqueness at the DB level
    @JsonProperty("sku")
    private String sku;

    @JsonProperty("status")
    private Status status;

    @JsonProperty("description")
    @NotEmpty(message = "Description is mandatory")
    @Size(max = 40, message = "Description must be less than or equal to 40 characters")
    private String description;

    @JsonProperty("supplier")
    private Supplier supplier; 

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

    // Key Requirements for photoUrl:
    // File Upload: Allow users to upload a photo, ideally using a REST API endpoint.
    // Validation: Ensure the uploaded file is a valid image (e.g., .jpg, .png).
    // Storage: Store the image either in a cloud storage solution (e.g., AWS S3, Google Cloud Storage) or locally (not recommended for scalability).
    // URL Generation: Generate a public URL pointing to the uploaded photo.
    // Assignment to photoUrl: Save the generated URL to the photoUrl field in the database.
    @JsonProperty("photoUrl")
    private String photoUrl = null;


    @JsonProperty("partType")
    private PartType partType;

    @JsonProperty("minStockLevel")
    private int minStockLevel;

    @JsonProperty("archived")
    private boolean archived;

    @JsonProperty("controlled")
    private boolean controlled;

    @JsonProperty("controlStock")
    private boolean controlStock;

    // ENUMS LISTS
    public enum PartType {
        ORI,      // Original BMW
        OEM,      // OEM BMW
        CHINA,    // China
        USED,     // Used
        AFTERMKT, // Aftermarket
        PACKAGE,  // Package
        OTHERS    // Others
    }

    public enum Supplier {
        BA,
        KH,
        BY,
        CA;

        @JsonCreator
        public static Supplier fromValue(String value) {
            try {
                return Supplier.valueOf(value.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid supplier value: " + value);
            }
        }
    }

      // Status Enum
      public enum Status {
        ACTIVE,    // Show in all product searches 
        OBSOLETE,  // Behaves like the product is deleted, do not show anywhere
        ON_HOLD;    // Cannot be used in Sales, but allowed for backend operations

          @JsonCreator
        public static Status forValue(String value) {
        return Status.valueOf(value.toUpperCase());
    }
    }

    

    // Getters and setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getUuid() {
        return uuid;
    }
    
    // Since the uuid should be non-editable and hidden, you can initialize it with a generated value when a Part is created, and then make it immutable (i.e., no setters).
    // public void setUuid(String uuid) {
    //     this.uuid = uuid;
    // }
    
    public String getSku() {
        return sku;
    }
    
    public void setSku(String sku) {
        // Remove spaces, ensure alphanumeric characters, and append supplier code
        String formattedSku = sku.replaceAll("\\s", "").replaceAll("[^a-zA-Z0-9]", "");
        if (supplier != null) {
            formattedSku += supplier.name();  // Append supplier code
        }
        this.sku = formattedSku;
    }
    
    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        if (this.status == Status.OBSOLETE && status != Status.OBSOLETE) {
            throw new IllegalArgumentException("Cannot change status from OBSOLETE to another status.");
        }
        if (this.status == Status.ON_HOLD && status == Status.ACTIVE) {
            throw new IllegalArgumentException("Cannot change status from ON_HOLD to ACTIVE directly.");
        }
        // If the new status is valid, assign it
        this.status = status;
    }
    
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Supplier getSupplier() {
        return supplier;
    }
    
    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
    
    public double getCostPrice() {
        return costPrice;
    }
    
    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }
    
    public double getSellingPrice() {
        return sellingPrice;
    }
    
    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
    
    public int getStockLevel() {
        return stockLevel;
    }
    
    public void setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
    }
    
    public int getReservedStock() {
        return reservedStock;
    }
    
    public void setReservedStock(int reservedStock) {
        this.reservedStock = reservedStock;
    }
    
    public int getAvailableStock() {
        return availableStock;
    }
    
    public void setAvailableStock(int availableStock) {
        this.availableStock = availableStock;
    }
    
    public String getPhotoUrl() {
        return photoUrl;
    }
    
    // Updated Getter and Setter for photoUrl
    // The photoUrl field should only be updated after a successful image upload. Here’s how the setter could handle validation:
    public void setPhotoUrl(String photoUrl) {
        if (photoUrl == null || !photoUrl.matches("^(http|https)://.*\\.(jpg|jpeg|png|gif)$")) {
            throw new IllegalArgumentException("Invalid photo URL. Must be a valid image URL (http/https and .jpg/.jpeg/.png/.gif).");
        }
        this.photoUrl = photoUrl;
    }
    
    
    public PartType getPartType() {
        return partType;
    }
    
    public void setPartType(PartType partType) {
        this.partType = partType;
    }
    
    public int getMinStockLevel() {
        return minStockLevel;
    }
    
    public void setMinStockLevel(int minStockLevel) {
        this.minStockLevel = minStockLevel;
    }
    
    public boolean isArchived() {
        return archived;
    }
    
    public void setArchived(boolean archived) {
        this.archived = archived;
    }
    
    public boolean isControlled() {
        return controlled;
    }
    
    public void setControlled(boolean controlled) {
        this.controlled = controlled;
    }
    
    public boolean isControlStock() {
        return controlStock;
    }
    
    public void setControlStock(boolean controlStock) {
        this.controlStock = controlStock;
    }
    

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
