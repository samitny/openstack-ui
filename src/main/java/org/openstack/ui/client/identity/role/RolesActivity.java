package org.openstack.ui.client.identity.role;

import org.openstack.model.identity.Role;
import org.openstack.ui.client.compute.common.AdministrationPlace;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class RolesActivity extends AbstractActivity implements RolesView.Presenter {
	
	public static final RolesView VIEW = new RolesView();

	private AdministrationPlace place;

	public RolesActivity(AdministrationPlace place) {
		this.place = place;
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		VIEW.setPresenter(this);
		panel.setWidget(VIEW);
	}

	@Override
	public void onDelete() {
		onRefresh();
		
	}

	@Override
	public void onRefresh() {
		
	}

	@Override
	public void onRoleCreated(Role tenant) {
		VIEW.refresh();
	}

}
