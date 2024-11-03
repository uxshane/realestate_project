package com.example.realestate_api.region.region_entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "region_code")
public class RegionCode {
    
    @Id
    @Column(name = "lawd_cd", length = 10)
    private String lawdCd;

    @Column(name = "region_name")
    private String regionName;

}
