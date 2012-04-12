package org.cloudsherpa.ui.client.compute.image;

import java.util.List;

import org.cloudsherpa.portal.client.Portal;
import org.openstack.model.compute.Server;
import org.openstack.model.images.Image;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;

public class ImagesView extends Composite {

	private static Binder uiBinder = GWT.create(Binder.class);

	interface Binder extends UiBinder<Widget, ImagesView> {
	}
	
	public interface Presenter {
		void onCreate();
		void onDelete();
		void onRefresh();
	}
	
	@UiField Button create;
	@UiField Button delete;
	@UiField Button refresh;
	
	@UiField ImageDetails details;
	
	@UiField(provided = true) DataGrid<Image> grid;
	
	MultiSelectionModel<Image> selectionModel;
	
	DefaultSelectionEventManager<Image> selectionEventManager = DefaultSelectionEventManager.createCheckboxManager(0);
	
	AsyncDataProvider<Image> asyncDataProvider = new AsyncDataProvider<Image>() {

		@Override
		protected void onRangeChanged(HasData<Image> display) {
			
			final Range range = display.getVisibleRange();
			
			Portal.CLOUD.listImages(range.getStart(), range.getLength(),  new AsyncCallback<List<Image>>() {

				@Override
				public void onFailure(Throwable caught) {
					Window.alert(caught.getMessage());
					
				}

				@Override
				public void onSuccess(List<Image> result) {
					updateRowData(range.getStart(), result);
					updateRowCount(range.getLength(), true);
					
				}
			});
			
		}

	};
	
	Presenter presenter;

	public ImagesView() {
		createGrid();
		initWidget(uiBinder.createAndBindUi(this));
		update();
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@UiHandler("create")
	void onCreateClick(ClickEvent event) {
		presenter.onCreate();
	}
	
	@UiHandler("delete")
	void onDeleteClick(ClickEvent event) {
		presenter.onDelete();
	}
	
	@UiHandler("refresh")
	void onRefreshClick(ClickEvent event) {
		//grid.setVisibleRangeAndClearData(grid.getVisibleRange(), true);
		RangeChangeEvent.fire(grid, grid.getVisibleRange());
		presenter.onRefresh();
	}
	
	private void update() {
		switch (selectionModel.getSelectedSet().size()) {
		case 0:
			delete.setEnabled(false);
			break;
		case 1:
			delete.setEnabled(true);
			break;
		default:
			delete.setEnabled(true);
			break;
		}
	}
	
	private void createGrid() {
		grid = new DataGrid<Image>();
		selectionModel = new MultiSelectionModel<Image>();
		Column<Image, Boolean> checkboxColumn = new Column<Image, Boolean>(new CheckboxCell()) {

			@Override
			public Boolean getValue(Image object) {
				return false;
			}
		};
		grid.setColumnWidth(checkboxColumn, "40px");
		grid.addColumn(checkboxColumn, "");
		TextColumn<Image> statusColumn = new TextColumn<Image>() {
			@Override
			public String getValue(Image object) {
				return object.getStatus();
			}
		};
		grid.setColumnWidth(statusColumn, "120px");
		grid.addColumn(statusColumn);
		/*
		Column<Image, String> logoColumn = new Column<Image, String>(new LogoCell()) {

			@Override
			public String getValue(Image object) {
				return "";
			}
		};
		grid.setColumnWidth(logoColumn, "60px");
		grid.addColumn(logoColumn);
		*/
		TextColumn<Image> nameColumn = new TextColumn<Image>() {
			@Override
			public String getValue(Image object) {
				return object.getName();
			}
		};
		grid.setColumnWidth(nameColumn, "120px");
		grid.addColumn(nameColumn);
		TextColumn<Image> minDiskColumn = new TextColumn<Image>() {
			@Override
			public String getValue(Image object) {
				return String.valueOf(object.getMinDisk());
			}
		};
		grid.setColumnWidth(minDiskColumn, "120px");
		grid.addColumn(minDiskColumn);
		ButtonCell previewButton = new ButtonCell();
		Column<Image,String> preview = new Column<Image,String>(previewButton) {
		  public String getValue(Image object) {
		    return "Launch";
		  }
		};
		preview.setFieldUpdater(new FieldUpdater<Image, String>() {
		  @Override
		  public void update(int index, Image securityGroup, String value) {
		    //presenter.onLaunchImage(securityGroup.getId());
		  }
		});
		grid.setColumnWidth(preview, "100px");
		grid.addColumn(preview);
		grid.setSelectionModel(selectionModel, selectionEventManager);
		selectionModel.addSelectionChangeHandler(new Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				update();
				
			}
		});
		asyncDataProvider.addDataDisplay(grid); 
	}
	
	private void onPreview(Server server) {
		details.id.setText(server.getId());
	}
	
}


