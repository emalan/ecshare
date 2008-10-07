package com.madalla.webapp.images.admin;

import javax.swing.tree.TreeNode;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation;
import org.apache.wicket.extensions.markup.html.tree.table.IRenderable;
import org.apache.wicket.extensions.markup.html.tree.table.PropertyRenderableColumn;
import org.apache.wicket.model.PropertyModel;

public class PropertyEditableColumn extends PropertyRenderableColumn{
	 public PropertyEditableColumn(ColumnLocation location, String header, String propertyExpression)
	    {
	        super(location, header, propertyExpression);
	    }

	    /**
	     * @see IColumn#newCell(MarkupContainer, String, TreeNode, int)
	     */
	    public Component newCell(MarkupContainer parent, String id, TreeNode node, int level)
	    {
	        return new EditablePanel(id, new PropertyModel(node, getPropertyExpression()));
	    }

	    /**
	     * @see IColumn#newCell(TreeNode, int)
	     */
	    public IRenderable newCell(TreeNode node, int level)
	    {
	        if (getTreeTable().getTreeState().isNodeSelected(node))
	            return null;
	        else
	            return super.newCell(node, level);
	    }
}
