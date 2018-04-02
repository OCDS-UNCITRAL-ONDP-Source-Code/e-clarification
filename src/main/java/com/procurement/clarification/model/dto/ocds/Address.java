package com.procurement.clarification.model.dto.ocds;

import com.fasterxml.jackson.annotation.*;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "streetAddress",
        "locality",
        "region",
        "postalCode",
        "countryName"
})
public class Address {

    @NotNull
    @JsonProperty("streetAddress")
    private final String streetAddress;

    @NotNull
    @JsonProperty("locality")
    private final String locality;

    @NotNull
    @JsonProperty("region")
    private final String region;

    @NotNull
    @JsonProperty("postalCode")
    private final String postalCode;

    @NotNull
    @JsonProperty("countryName")
    private final String countryName;

    @JsonCreator
    public Address(@JsonProperty("streetAddress") final String streetAddress,
                   @JsonProperty("locality") final String locality,
                   @JsonProperty("region") final String region,
                   @JsonProperty("postalCode") final String postalCode,
                   @JsonProperty("countryName") final String countryName) {
        this.streetAddress = streetAddress;
        this.locality = locality;
        this.region = region;
        this.postalCode = postalCode;
        this.countryName = countryName;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(streetAddress)
                .append(locality)
                .append(region)
                .append(postalCode)
                .append(countryName)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Address)) {
            return false;
        }
        final Address rhs = (Address) other;
        return new EqualsBuilder()
                .append(streetAddress, rhs.streetAddress)
                .append(locality, rhs.locality)
                .append(region, rhs.region)
                .append(postalCode, rhs.postalCode)
                .append(countryName, rhs.countryName)
                .isEquals();
    }
}