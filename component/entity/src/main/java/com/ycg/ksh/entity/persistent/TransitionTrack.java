package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;

@Table(name = "`transition_track_tab`")
public class TransitionTrack extends BaseEntity {
    
	private static final long serialVersionUID = -1955483010906872915L;

	@Id
	@Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 条码号
     */
    @Column(name = "`barcode`")
    private String barcode;

    /**
     * 司机轨迹ID
     */
    @Column(name = "`driver_track_id`")
    private Long driverTrackId;

    
    
	public TransitionTrack() {
		super();
	}

	public TransitionTrack(Long id, String barcode, Long driverTrackId) {
		super();
		this.id = id;
		this.barcode = barcode;
		this.driverTrackId = driverTrackId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Long getDriverTrackId() {
		return driverTrackId;
	}

	public void setDriverTrackId(Long driverTrackId) {
		this.driverTrackId = driverTrackId;
	}
    
}