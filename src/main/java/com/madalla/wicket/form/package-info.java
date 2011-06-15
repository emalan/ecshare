/**
 * Ajax Form functionality
 * <p>
 * Includes functionality for easily enabling Ajax submit of forms as well as self-validating
 * form input fields. Common use : extends AjaxValidationForm.
 * <pre>
 * public class EmailForm extends AjaxValidationForm<Object> {
 *
 *       public EmailForm(String id) {
 *           super(id);
 *
 *           FeedbackPanel nameFeedback = new FeedbackPanel("nameFeedback");
 *           add(nameFeedback);
 *           add(new AjaxValidationRequiredTextField("name",nameFeedback);
 * </pre>
 * </p>
 *
 * @author Eugene Malan
 */
package com.madalla.wicket.form;

