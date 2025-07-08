package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * JPA Entity for Service table
 * 
 * @author Admin
 */
@Entity
@Table(name = "Service")
public class Service {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ServiceID")
  private Integer serviceId;

  @Column(name = "ServiceName", nullable = false)
  private String serviceName;

  @Column(name = "ImageUrl")
  private String imageUrl;

  // Constructors
  public Service() {
  }

  public Service(String serviceName) {
    this.serviceName = serviceName;
  }

  public Service(String serviceName, String imageUrl) {
    this.serviceName = serviceName;
    this.imageUrl = imageUrl;
  }

  // Getters and Setters
  public Integer getServiceId() {
    return serviceId;
  }

  public void setServiceId(Integer serviceId) {
    this.serviceId = serviceId;
  }

  public String getServiceName() {
    return serviceName;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  // Utility methods
  @Override
  public String toString() {
    return "Service{" +
        "serviceId=" + serviceId +
        ", serviceName='" + serviceName + '\'' +
        ", imageUrl='" + imageUrl + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    Service service = (Service) o;
    return serviceId != null ? serviceId.equals(service.serviceId) : service.serviceId == null;
  }

  @Override
  public int hashCode() {
    return serviceId != null ? serviceId.hashCode() : 0;
  }
}
