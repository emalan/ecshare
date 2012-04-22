package com.madalla.wicket;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.WildcardListModel;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;

/**
 * Link that will stream the list to the client. When clicked this link
 * will prompt the save as dialogue in the browser.
 * 
 * @author Eugene Malan
 *
 * @param <T>
 */
public abstract class DataDownloadLink<T> extends Link<List<? extends T>>{
	private static final long serialVersionUID = 1L;
	
	private String fileName = "data.csv";
	private String seperator = ",";
	
	public DataDownloadLink(String id, List<? extends T> data) {
		super(id);
		IModel<List<? extends T>> model = new WildcardListModel<T>(data);
		setDefaultModel(model);
	}
	
	public DataDownloadLink(String id, IModel<List<? extends T>> model) {
		super(id, model);
	}

	@Override
	public void onClick() {
		StringBuffer sb = new StringBuffer();
		sb.append(getRow(getDataRowHeaders())).append(System.getProperty("line.separator"));
		for(T data : getModelObject()) {
			sb.append(getRow(getDataRowItems(data)))
			.append(System.getProperty("line.separator"));
		}
		
		IResourceStream resourceStream = new StringResourceStream(sb.toString());
		getRequestCycle().setRequestTarget(new ResourceStreamRequestTarget(resourceStream){

			@Override
			public String getFileName() {
				return fileName;
			}
			
		});
		
	}
	
	private String getRow(List<String> row) {
		StringBuffer sb = new StringBuffer();
		for (String data : row) {
			sb.append(data).append(seperator);
		}
		return StringUtils.chomp(sb.toString(), seperator);
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public abstract List<String> getDataRowItems(T data);
	
	public abstract List<String> getDataRowHeaders();

	public void setSeperator(String seperator) {
		this.seperator = seperator;
	}

}
