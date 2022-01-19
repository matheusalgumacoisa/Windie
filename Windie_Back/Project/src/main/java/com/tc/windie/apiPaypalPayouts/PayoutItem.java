package com.tc.windie.apiPaypalPayouts;

public class PayoutItem {
	public String value;
	public String send_id;
	public String receiver_email;
	public PayoutItem(String value, String send_id, String receiver_email) {
		super();
		this.value = value;
		this.send_id = send_id;
		this.receiver_email = receiver_email;
	}
}
