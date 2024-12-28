package com.example.inventory.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Min;
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
    @Min(value = 0, message = "Cost price must be greater than or equal to 0")
    private double costPrice;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("sellingPrice")
    private Double sellingPrice;

  

    // Key Requirements for photoUrl:
    // File Upload: Allow users to upload a photo, ideally using a REST API endpoint.
    // Validation: Ensure the uploaded file is a valid image (e.g., .jpg, .png).
    // Storage: Store the image either in a cloud storage solution (e.g., AWS S3, Google Cloud Storage) or locally (not recommended for scalability).
    // URL Generation: Generate a public URL pointing to the uploaded photo.
    // Assignment to photoUrl: Save the generated URL to the photoUrl field in the database.
    @JsonProperty("photoUrl") // http://example.com/placeholder.gif
    private String photoUrl = null;


    @JsonProperty("partType")
    private PartType partType;

    @JsonProperty("archived")
    private boolean archived;

    //controlledPart
    @JsonProperty("controlled")
    private ControlState controlled;

    //control stock (ENABLER!)
    @JsonProperty("controlStock")
    private ControlStock controlStock;

    // stockLevel, reservedStock, availableStock, minStockLevel DEPENDS on controlStock
    // if controlStock, enable them. 
    // int defaults to 0, which might incorrectly suggest that the field is intentionally initialized.
    // @JsonProperty("stockLevel")
    // private Integer stockLevel; // Nullable
    
    // @JsonProperty("reservedStock")
    // private Integer reservedStock; // Nullable
    
    // @JsonProperty("availableStock")
    // private Integer availableStock; // Nullable

    @JsonProperty("stockLevel")
    private Integer stockLevel = 0; // Default to 0

    @JsonProperty("reservedStock")
    private Integer reservedStock = 0; // Default to 0

    @JsonProperty("availableStock")
    private Integer availableStock = 0; // Default to 0

    
    @JsonProperty("minStockLevel")
    private Integer minStockLevel; // Nullable

    // EXTRA FIELDS
    // Create timestamp
    @CreatedDate
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;

    @JsonProperty("statusChangeReason")
    private String statusChangeReason;

    @JsonProperty("updatedBy")
    private String updatedBy;



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
        CA,
        etc;

        @JsonCreator
        public static Supplier fromValue(String value) {
            try {
                return Supplier.valueOf(value.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid supplier value: " + value);
            }
        }
    }

      public enum Status {
        ACTIVE,    // Show in all product searches 
        OBSOLETE,  // Behaves like the product is deleted, do not show anywhere
        ON_HOLD;    // Cannot be used in Sales, but allowed for backend operations

          @JsonCreator
        public static Status forValue(String value) {
        return Status.valueOf(value.toUpperCase());
    }
    }

    public enum ControlState {
        YES,
        NO
    }

    public enum ControlStock {
        YES, 
        NO
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
    // } // not needed for now
    
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
    
    // Comment: This part a little confusing!
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
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description must not be null or blank.");
        }
        if (description.length() > 40) {
            throw new IllegalArgumentException("Description must be less than or equal to 40 characters.");
        }
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
        if (costPrice <= 0) {
            throw new IllegalArgumentException("Cost price must be greater than zero");
        }
        this.costPrice = costPrice;
    }
    
    public Double getSellingPrice() {
        if (sellingPrice == null) {
            // Set a default value if sellingPrice is null
            return 0.0;
        }
        return sellingPrice;
    }
    
    
    // public void setSellingPrice(double sellingPrice) {
    //     this.sellingPrice = sellingPrice;
    // } // not needed for now
    // Method to calculate sellingPrice (e.g., based on costPrice and a profit margin)
    public void calculateSellingPrice(double costPrice, double marginPercentage) {
    if (marginPercentage < 0) {
        throw new IllegalArgumentException("Margin percentage cannot be negative");
    }
    this.sellingPrice = costPrice + (costPrice * marginPercentage / 100);
    } 

    public void calculateSellingPrice() {
        if (this.costPrice <= 0) {
            throw new IllegalArgumentException("Cost price must be greater than 0 to calculate selling price.");
        }
        this.sellingPrice = this.costPrice * 1.5; // Selling price is 1.5 times the cost price
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    // Updated Getter and Setter for photoUrl
    // The photoUrl field should only be updated after a successful image upload. Here’s how the setter could handle validation:
    public void setPhotoUrl(String photoUrl) {
        // if (photoUrl == null || !photoUrl.matches("^(http|https)://.*\\.(jpg|jpeg|png|gif)$")) {
        //     throw new IllegalArgumentException("Invalid photo URL. Must be a valid image URL (http/https and .jpg/.jpeg/.png/.gif).");
        // }
        if (!photoUrl.matches(".*\\.(jpg|jpeg|png|gif)$")) {
            throw new IllegalArgumentException("Invalid photo URL.");
        }
        
        this.photoUrl = photoUrl;
    }
    
    
    public PartType getPartType() {
        return partType;
    }
    
    public void setPartType(PartType partType) {
        this.partType = partType;
    }
        
    public boolean isArchived() {
        return archived;
    }
    
    public void setArchived(boolean archived) {
        this.archived = archived;
    }
    
    public ControlState getControlled() {
        return controlled;
    }
    
    public void setControlled(ControlState controlled) {
        this.controlled = controlled;
    }
    
    public ControlStock getControlStock() {
        return controlStock;
    } 
    public void setControlStock(ControlStock controlStock) {
        this.controlStock = controlStock;
    } 
    // Logic to enable stock fields when controlStock is YES
    public void handleStockFields() {
        // If controlStock is ControlStock.YES
        if (ControlStock.YES.equals(this.controlStock)) {
            // Ensure stock-related fields are set appropriately
            this.stockLevel = (this.stockLevel != null) ? this.stockLevel : 0;
            this.reservedStock = (this.reservedStock != null) ? this.reservedStock : 0;
    
            // Calculate availableStock based on stockLevel and reservedStock
            this.availableStock = this.stockLevel - this.reservedStock;
    
            // Set default minStockLevel if not provided
            if (this.minStockLevel == null || this.minStockLevel <= 0) {
                this.minStockLevel = 2; // Default value
            }
        } else {
            // Reset or ignore stock-related fields when controlStock is not YES
            this.stockLevel = 0;
            this.reservedStock = 0;
            this.availableStock = 0;
            this.minStockLevel = 0;
        }
    }
    
    
    

    // stockLevel, reservedStock, availableStock, minStockLevel DEPENDS on controlStock
    // if controlStock, enable them.

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
        // Ensure that Available Stock is less than or equal to Stock Level
        if (availableStock > (stockLevel - reservedStock)) {
            throw new IllegalArgumentException("Available stock cannot exceed the stock level minus reserved stock.");
        }
        this.availableStock = availableStock;
    }
    

    public int getMinStockLevel() {
        return minStockLevel;
    }
    
    public void setMinStockLevel(int minStockLevel) {
        this.minStockLevel = minStockLevel;
    }

    // EXTRA SETTER AND GETTER
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getStatusChangeReason() {
        return statusChangeReason;
    }
    
    public void setStatusChangeReason(String statusChangeReason) {
        // if (statusChangeReason == null || statusChangeReason.trim().isEmpty()) {
        //     throw new IllegalArgumentException("Status change reason cannot be null or empty.");
        // }
        this.statusChangeReason = statusChangeReason;
    }
    
    public String getUpdatedBy() {
        return updatedBy;
    }
    
    public void setUpdatedBy(String updatedBy) {
        // if (updatedBy == null || updatedBy.trim().isEmpty()) {
        //     throw new IllegalArgumentException("Updated by cannot be null or empty.");
        // }
        this.updatedBy = updatedBy;
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
