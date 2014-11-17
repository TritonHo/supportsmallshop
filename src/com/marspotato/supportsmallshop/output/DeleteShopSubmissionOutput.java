package com.marspotato.supportsmallshop.output;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.marspotato.supportsmallshop.BO.DeleteShopSubmission;
import com.marspotato.supportsmallshop.BO.Shop;

public class DeleteShopSubmissionOutput implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Expose
	public DeleteShopSubmission s;
	@Expose
	public Shop shop;
	@Expose
	public boolean isCreator;
	@Expose
	public boolean isReviewer;
}
