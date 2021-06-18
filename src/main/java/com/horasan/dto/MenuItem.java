package com.horasan.dto;

public class MenuItem {

	private String createdBy;
	private Integer createdDate;
	private String fullPath;
	private String screenId;
	private Long screenOid;
	private String description;
	private String tag;
	private String highlighted;
	private Float hitScore;
	
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Integer getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Integer createdDate) {
		this.createdDate = createdDate;
	}
	
	public String getFullPath() {
		return fullPath;
	}
	
	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}
	public String getScreenId() {
		return screenId;
	}
	
	public void setScreenId(String screenId) {
		this.screenId = screenId;
	}
	public Long getScreenOid() {
		return screenOid;
	}
	
	public void setScreenOid(Long screenOid) {
		this.screenOid = screenOid;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public String getHighlighted() {
		return highlighted;
	}
	public void setHighlighted(String highlighted) {
		this.highlighted = highlighted;
	}
	public Float getHitScore() {
		return hitScore;
	}
	public void setHitScore(Float hitScore) {
		this.hitScore = hitScore;
	}
	
}
