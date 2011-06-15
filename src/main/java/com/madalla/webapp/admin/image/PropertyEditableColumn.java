package com.madalla.webapp.admin.image;

import javax.swing.tree.TreeNode;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation;
import org.apache.wicket.extensions.markup.html.tree.table.IRenderable;
import org.apache.wicket.extensions.markup.html.tree.table.PropertyRenderableColumn;
import org.apache.wicket.model.PropertyModel;

public class PropertyEditableColumn extends PropertyRenderableColumn{
	private static final long serialVersionUID = 1L;

	public PropertyEditableColumn(ColumnLocation location, String header, String propertyExpression)
	    {
	        super(location, header, propertyExpression);
	    }

	    @Override
		public Component newCell(MarkupContainer parent, String id, TreeNode node, int level)
	    {
	        return new EditablePanel(id, new PropertyModel<String>(node, getPropertyExpression()));
	    }

	    @Override
		public IRenderable newCell(TreeNode node, int level)
	    {
	        if (getTreeTable().getTreeState().isNodeSelected(node))
	            return null;
	        else
	            return super.newCell(node, level);
	    }
}
