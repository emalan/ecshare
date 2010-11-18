/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.madalla.webapp.admin.image;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

public class EditablePanel extends Panel {

	private static final long serialVersionUID = 1L;

	/**
     * Panel constructor.
     * 
     * @param id
     *            Markup id
     * 
     * @param propertyModel
     *            Model of the text field
     */
    public EditablePanel(String id, PropertyModel<String> propertyModel)
    {
        super(id);

        TextField<String> field = new TextField<String>("textfield", propertyModel);
        add(field);

        field.add(new AjaxFormComponentUpdatingBehavior("onblur")
        {
			private static final long serialVersionUID = 1L;

			protected void onUpdate(AjaxRequestTarget target)
            {
            }
        });
    }
}
