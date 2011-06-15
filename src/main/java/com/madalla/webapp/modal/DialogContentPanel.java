package com.madalla.webapp.modal;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;

public abstract class DialogContentPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public DialogContentPanel(String id, final Component content, final boolean closeButton, final boolean saveButton) {
        super(id);

        Form<Void> form ;
        add(form = new Form<Void>("form"));

        form.add(content);

        form.add(new AjaxButton("close") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
                onCancel(target);
            }

			@Override
			public boolean isVisible() {
				return closeButton;
			}

			@Override
			protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
				replaceComponentTagBody(markupStream, openTag, getCancelButtonString());
			}


        }.setDefaultFormProcessing(false));

        form.add(new IndicatingAjaxButton("save") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onSave(target);
			}



			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.addComponent(form);
				super.onError(target, form);
			}



			@Override
			public boolean isVisible() {
				return saveButton;
			}


        });

    }

	abstract String getCancelButtonString();

    abstract void onCancel(AjaxRequestTarget target);

    abstract void onSave(AjaxRequestTarget target);

}
