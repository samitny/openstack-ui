package org.cloudsherpa.ui.client.compute.securitygroup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class SecurityGroupDetails extends Composite {

	private static ServerDetailsUiBinder uiBinder = GWT
			.create(ServerDetailsUiBinder.class);

	interface ServerDetailsUiBinder extends UiBinder<Widget, SecurityGroupDetails> {
	}
	
	@UiField Label id;

	public SecurityGroupDetails() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
