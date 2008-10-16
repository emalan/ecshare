package com.madalla.webapp.images.admin;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.extensions.markup.html.tree.table.AbstractTreeColumn;
import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation;
import org.apache.wicket.extensions.markup.html.tree.table.IColumn;
import org.apache.wicket.extensions.markup.html.tree.table.TreeTable;
import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation.Alignment;
import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation.Unit;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.transformer.AbstractTransformerBehavior;

import com.madalla.service.cms.IRepositoryService;
import com.madalla.service.cms.IRepositoryServiceProvider;
import com.madalla.service.cms.ImageData;
import com.madalla.wicket.javascript.JavascriptBuilder;
import com.madalla.wicket.javascript.JavascriptFunction;

public class AlbumDisplayPanel extends Panel {
	
//	@Override
//	protected void onAfterRender() {
//		// TODO Auto-generated method stub
//		log.error(formDrop.getCallbackUrl());
//		super.onAfterRender();
//	}

	private static final long serialVersionUID = 1L;
	private final static Log log = LogFactory.getLog(AlbumDisplayPanel.class);
	
	private TreeTable tree;

	public AlbumDisplayPanel(String id, String album) {
		super(id);
		IColumn column = new AbstractTreeColumn(new ColumnLocation(Alignment.LEFT,20, Unit.EM),"Images"){

			@Override
			public String renderNode(TreeNode node) {
				DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node;
				Object userObject = treeNode.getUserObject();
				if (userObject instanceof String){
					return (String)userObject;
				} else if (userObject instanceof ImageData){
					return ((ImageData)userObject).getName();
				} else {
					return null;
				}
				
			}
			
		};

		IColumn columns[] = new IColumn[]{
				column
		};
		
		final AbstractDefaultAjaxBehavior onDrop = new AbstractDefaultAjaxBehavior() {
		    protected void respond(final AjaxRequestTarget target) {
		    	target.appendJavascript("console.log('some stuff dropped');");
		    }
		};

		final AbstractTransformerBehavior droppable = new AbstractTransformerBehavior(){

			@Override
			public CharSequence transform(Component component,
					CharSequence output) throws Exception {
				
				JavascriptBuilder builder = new JavascriptBuilder();
				builder.addLine("var e = $('"+component.getMarkupId()+"');");
				builder.addLine("e.addClassName('droppable');");
				
				Map map = new LinkedHashMap();
				map.put("accept", "draggable");
				map.put("hoverclass", "hover");
				
				JavascriptFunction onDropFunction = new JavascriptFunction("dragged, dropped, event");
				onDropFunction.addLine("console.log(dragged);").addLine("console.log(dropped);").
				addLine("console.log(event);"); //.addAjaxCallback(getAjaxBehavior());
				
				map.put("onDrop", onDropFunction);
				
				builder.addLine("Droppables.add(e,"+builder.formatAsJavascriptHash(map)).addLine(");");
				
				return builder.buildScriptTagString();
			}
			
			public AbstractAjaxBehavior getAjaxBehavior(){
				return onDrop;
			}
			
		};

		final Form form = new Form("albumForm"){
			private static final long serialVersionUID = 1L;

//			@Override
//			protected void onRender(MarkupStream markupStream) {
//				super.onRender(markupStream);
//				String s = "var e = $('"+getMarkupId()+"'); Droppables.add(e, {accept: 'draggable',hoverclass: 'hover',"+
//				"onDrop: function(dragged, dropped, event){"+
//				"console.log(dragged);console.log(dropped);console.log(event);"+
//				"var wcall = wicketAjaxGet('"+onDrop.getCallbackUrl()+"');"+
//    	        "}});"+
//				"e.addClassName('droppable')";
//				JavascriptUtils.writeJavascript(getResponse(), s);
//			}
		};
		form.add(droppable);
		form.add(onDrop);
		form.setOutputMarkupId(true);
		
		TreeModel treeModel = getRepositoryService().getAlbumImagesAsTree(album);
		tree = new TreeTable("albumTreeTable", treeModel, columns);
		form.add(tree);
		tree.getTreeState().expandAll();
		
		add(form);
	}
	
	private IRepositoryService getRepositoryService(){
		IRepositoryServiceProvider provider = (IRepositoryServiceProvider)getApplication();
		return provider.getRepositoryService();
	}
}
