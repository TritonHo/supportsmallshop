package com.marspotato.supportsmallshop.output;

import com.google.gson.annotations.Expose;
import com.marspotato.supportsmallshop.BO.Submission;


public class SubmissionOutput {
	@Expose
	public Submission[] createUpdateSubmissionArray;
	//TODO: add the delete one;
}
